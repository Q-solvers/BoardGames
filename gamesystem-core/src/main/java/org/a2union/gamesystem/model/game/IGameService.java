/*
 * $Id: $
 */
package org.a2union.gamesystem.model.game;

import org.a2union.gamesystem.model.game.pieces.IPiece;
import org.a2union.gamesystem.model.game.pieces.types.IPieceType;
import org.a2union.gamesystem.model.game.side.GameSide;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.game.step.Step;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.game.zone.GameZoneType;
import org.a2union.gamesystem.model.user.User;

import java.util.List;
import java.util.Map;

/**
 * @author Iskakoff
 */
public interface IGameService<T extends GameBase, I extends IPieceType, J extends IPiece<I>> {

    GameZone createGameZone(GameZoneType type);

    GameZone getGameZone(String zoneId);

    void createGame(T game, GameSideType type, User owner);

    /**
     * Choose side of gamer for current game
     * Throws IllegalStateException if game state is not NOT_ACTIVE
     *
     * @param game     - current game
     * @param gamer    - gamer to choose side
     * @param sideType - gamer side
     */
    void chooseSide(GameBase game, User gamer, GameSideType sideType);

    /**
     * Delete specified game
     *
     * @param gameId identifier of game to delete
     */
    void deleteGame(String gameId);

    T getGame(String gameId);

    /**
     * @return main game zone of server
     */
    GameZone getMainZone();

    /**
     * @param gameZoneType type of zone to select
     * @return game zone by specified type
     */
    GameZone obtainGameZoneByType(GameZoneType gameZoneType);

    void acceptGame(String gameId, User user);


    int countGamesByGameZoneAndOwner(String gameZoneId, User owner);

    List<T> getGameZoneGamesOwnerPage(final String gameZoneId, final User owner, final int startIndex, final int size);

    int countOpenGames(String gameZoneId, User user);

    List<T> getOpenGames(String gameZoneId, User user);

    List<T> getOpenGamesPage(String gameZoneId, User user, int startIndex, int size);

    int countActiveGames(String gameZoneId, User user);

    List<T> getActiveGames(String gameZoneId, User user);

    List<T> getActiveGamesPage(String gameZoneId, User user, int startIndex, int size);


    /**
     * Move piece on the board
     *
     * @param game        - current game
     * @param piece       - piece to move
     * @param newPosition - new position
     * @return new position
     */
    Map<String, J> doMove(T game, J piece, String newPosition);

//    Map<String, IChessPiece> doMove(T game, IChessPiece piece, String newPosition, boolean exch, String ptype);

    /**
     * Obtain current user game side
     *
     * @param game current game
     * @return game side of current user
     */
    GameSide getCurrentGameSide(T game);

    Step getLastStep(T game);

    List<Step> getGameHistory(T game);

    void completeGame(T base);

    void retreat(T game);

    void drawGame(T game);

    void suggestDraw(T game);

    void acceptDraw(T game);

    //for test propose only
    boolean canMoveMap(Map<String, J> pieceMap, Map<String, J> enemyPieceMap, String kingPosition, boolean first, boolean nextFirst);
}
