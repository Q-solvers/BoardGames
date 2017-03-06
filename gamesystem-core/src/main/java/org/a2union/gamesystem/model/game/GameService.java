/*
 * $Id: $
 */
package org.a2union.gamesystem.model.game;

import org.a2union.gamesystem.commons.INotificationResolver;
import org.a2union.gamesystem.model.game.pieces.CommonUtils;
import org.a2union.gamesystem.model.game.pieces.IPiece;
import org.a2union.gamesystem.model.game.pieces.types.IPieceType;
import org.a2union.gamesystem.model.game.side.GameSide;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.game.step.Step;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.game.zone.GameZoneType;
import org.a2union.gamesystem.model.game.zone.IGameZoneDAO;
import org.a2union.gamesystem.model.user.IUserService;
import org.a2union.gamesystem.model.user.User;
import org.a2union.gamesystem.model.user.rate.Rate;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Iskakoff
 */
@Transactional(propagation = Propagation.REQUIRED)
public class GameService<T extends GameBase, I extends IPieceType, J extends IPiece<I>> implements IGameService<T, I, J> {
    private IGameZoneDAO gameZoneDAO;
    private IGameDAO<T> gameDAO;
    private IUserService userService;
    private INotificationResolver notificationResolver;


    @Transactional
    public GameZone createGameZone(GameZoneType type) {
        GameZone gameZone = new GameZone();
        gameZone.setType(type);
        gameZoneDAO.save(gameZone);
        return gameZone;
    }

    @Transactional
    public GameZone getGameZone(String zoneId) {
        return gameZoneDAO.getById(zoneId);
    }

    @Transactional
    public void createGame(T game, GameSideType type, User owner) {
        game.setStatus(GameStatus.NOT_ACTIVE);
        game.setCreationTime(new Date());
        game.setOwner(owner);
        chooseSide(game, owner, type);
        gameDAO.save(game);
    }

    @Transactional
    public void chooseSide(GameBase game, User gamer, GameSideType sideType) {
        GameSide side = new GameSide();
        side.setType(sideType);
        side.setUser(gamer);
        side.setLastStepNumber(1);
        //initialize pieces of current side
        side.setPieces(game.getZone().getType().getUtils().getInitialBoard(GameSideType.FIRST.equals(sideType)));
        if (game.getStatus().equals(GameStatus.NOT_ACTIVE))
            game.addSide(side);
        else
            throw new IllegalStateException("couldn't accept game because it has incorrect state. State = " + game.getStatus() + "but must be " + GameStatus.NOT_ACTIVE);
    }

    @Transactional
    public void deleteGame(String gameId) {
        gameDAO.delete(gameDAO.getById(gameId));
    }


    public T getGame(String gameId) {
        return gameDAO.getById(gameId);
    }

    public GameZone getMainZone() {
        return gameZoneDAO.getMainZone();
    }

    public GameZone obtainGameZoneByType(GameZoneType gameZoneType) {
        return gameZoneDAO.getZoneByType(gameZoneType);
    }

    @Transactional
    public void acceptGame(String gameId, User user) {
        GameBase game = gameDAO.getById(gameId);
        GameSideType gameSideType = game.getFreeType();
        if (gameSideType == null) {
            throw new IllegalStateException("couldn't accept game because it has no free side type. Game id = " + game.getUUID());
        }
        chooseSide(game, user, gameSideType);
        game.setStatus(GameStatus.ACTIVE);
        Set<GameSide> gameSides = game.getGameSides();
        for (GameSide gameSide : gameSides) {
            if (GameSideType.FIRST.equals(gameSide.getType())) {
                gameSide.setActive(true);
                break;
            }
        }
        game.setStartTime(new Date());
    }

    private static final String GAMES_BY_GAMEZONE_AND_OWNER = "from GameBase g where g.zone.UUID = :zoneId and g.owner = :owner";

    public int countGamesByGameZoneAndOwner(String gameZoneId, User owner) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("zoneId", gameZoneId);
        parameters.put("owner", owner);
        return gameDAO.countItems(parameters, GAMES_BY_GAMEZONE_AND_OWNER);
    }

    public List<T> getGameZoneGamesOwnerPage(String gameZoneId, User owner, int startIndex, int size) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("zoneId", gameZoneId);
        parameters.put("owner", owner);
        return gameDAO.getItemsPage(parameters, GAMES_BY_GAMEZONE_AND_OWNER, startIndex, size);
    }

    private static final String OPEN_GAMES = "from GameBase g where g.status = :status and g.zone.UUID = :zoneId and g.owner != :user";

    public int countOpenGames(String gameZoneId, User user) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("zoneId", gameZoneId);
        parameters.put("user", user);
        parameters.put("status", GameStatus.NOT_ACTIVE);
        return gameDAO.countItems(parameters, OPEN_GAMES);
    }

    public List<T> getOpenGames(String gameZoneId, User user) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("zoneId", gameZoneId);
        parameters.put("user", user);
        parameters.put("status", GameStatus.NOT_ACTIVE);
        return gameDAO.getItems(parameters, OPEN_GAMES);
    }

    public List<T> getOpenGamesPage(String gameZoneId, User user, int startIndex, int size) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("zoneId", gameZoneId);
        parameters.put("user", user);
        parameters.put("status", GameStatus.NOT_ACTIVE);
        return gameDAO.getItemsPage(parameters, OPEN_GAMES, startIndex, size);
    }

    private static final String ACTIVE_GAMES = "from GameBase g join g.gameSides as side where g.status = :status and g.zone.UUID = :zoneId and side.user = :user";

    public int countActiveGames(String gameZoneId, User user) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("zoneId", gameZoneId);
        parameters.put("user", user);
        parameters.put("status", GameStatus.ACTIVE);
        return gameDAO.countItems(parameters, ACTIVE_GAMES);
    }

    public List<T> getActiveGames(String gameZoneId, User user) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("zoneId", gameZoneId);
        parameters.put("user", user);
        parameters.put("status", GameStatus.ACTIVE);
        return gameDAO.getItems(parameters, "select g " + ACTIVE_GAMES);
    }

    public List<T> getActiveGamesPage(String gameZoneId, User user, int startIndex, int size) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("zoneId", gameZoneId);
        parameters.put("user", user);
        parameters.put("status", GameStatus.ACTIVE);
        return gameDAO.getItemsPage(parameters, "select g " + ACTIVE_GAMES, startIndex, size);
    }

    public GameSide getCurrentGameSide(T game) {
        Set<GameSide> gameSides = game.getGameSides();
        User user = userService.getCurrentUser();
        for (GameSide gameSide : gameSides) {
            if (gameSide.getUser().equals(user)) {
                return gameSide;
            }
        }
        throw new IllegalArgumentException("you try to play game you have not accepted");
    }

    public Step getLastStep(T game) {
        return gameDAO.getLastStepByGame(game);
    }

    @Override
    public List<Step> getGameHistory(T game) {
        return gameDAO.getAllSteps(game);
    }

    @Override
    @Transactional
    public void completeGame(T base) {
        GameResult result = base.getResult();
        Set<GameSide> gameSides = base.getGameSides();
        Integer integer = 0;
        for (GameSide gameSide : gameSides) {
            integer += userService.getUserRateValue(gameSide.getUser(), base.getZone());
        }
        for (GameSide gameSide : gameSides) {
            Rate userRate = userService.getUserRate(gameSide.getUser(), base.getZone());
            Integer value = null;
            if (userRate != null)
                value = userRate.getValue();
            if (value == null)
                value = 0;
            double v = integer != 0 ? 1 - (((double) value) / integer) : 0;
            if (gameSide.getType().equals(result.getSideType())) {
                v *= 100;
            } else if (result.getSideType() == null) {
                v *= 30;
            } else {
                v = value != 0 ? -100 * v : 0;
            }
            value += (int) v;
            if (userRate != null)
                userRate.setValue(value);
        }
        base.setEndTime(new Date());
        base.setStatus(GameStatus.COMPLETED);
    }

    @Override
    @Transactional
    public void retreat(T game) {
        GameSide side = getCurrentGameSide(game);
        game.setResult(side.isFirst() ? GameResult.BLACK_WON : GameResult.WHITE_WON);
        side.setActive(false);
        side.getNext().setActive(true);
        completeGame(game);
    }

    @Override
    @Transactional
    public void drawGame(T game) {
        if (!game.isActive()) {
            return;
        }
        // check for game owning
        getCurrentGameSide(game);
        game.setResult(GameResult.DRAW);
        completeGame(game);
    }

    @Override
    @Transactional
    public void suggestDraw(T game) {
        if (!game.isActive()) {
            return;
        }
        GameSide side = getCurrentGameSide(game);
        GameSide next = side.getNext();
        if (next.isDrawProposed()) {
            return;
        }
        side.setDrawProposed(true);
    }

    @Override
    @Transactional
    public void acceptDraw(T game) {
        if (!game.isActive()) {
            return;
        }
        GameSide side = getCurrentGameSide(game);
        GameSide next = side.getNext();
        if (next.isDrawProposed()) {
            drawGame(game);
        }
    }

    @Override
    public boolean canMoveMap(Map<String, J> pieceMap, Map<String, J> enemyPieceMap, String kingPosition, boolean first, boolean nextFirst) {
        return false;
    }

    @Override
    public Map<String, J> doMove(T game, J piece, String newPosition) {
        return null;
    }


    protected void notifyEnemy(T game, GameSide gameSide, String result) {
        User user = gameSide.getNext().getUser();
        if (!userService.isUserOnline(user)) {
            String type = game.getZone().getType().getValue();
            notificationResolver.sendMovementNotification(user, result, game.getUUID(), type + "/" + type + "game");
        }
    }
    @NotTransactional
    public void setGameZoneDAO(IGameZoneDAO gameZoneDAO) {
        this.gameZoneDAO = gameZoneDAO;
    }

    @NotTransactional
    public void setGameDAO(IGameDAO<T> gameDAO) {
        this.gameDAO = gameDAO;
    }
    @NotTransactional
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @NotTransactional
    public void setNotificationResolver(INotificationResolver notificationResolver) {
        this.notificationResolver = notificationResolver;
    }
    public CommonUtils<I, J> getUtils() {
        return null;
    }
}
