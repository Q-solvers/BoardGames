/*
 * $Id: ChessBoard.java 186 2010-03-10 20:59:04Z iskakoff $
 */
package org.a2union.gamesystem.web.components.chess;

import org.a2union.gamesystem.model.game.GameBase;
import org.a2union.gamesystem.model.game.GameStatus;
import org.a2union.gamesystem.model.game.chess.IChessGameService;
import org.a2union.gamesystem.model.game.pieces.IChessPiece;
import org.a2union.gamesystem.model.game.pieces.IPiece;
import org.a2union.gamesystem.model.game.pieces.CommonChessUtils;
import org.a2union.gamesystem.model.game.pieces.types.chess.King;
import org.a2union.gamesystem.model.game.side.GameSide;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.game.step.Step;
import org.a2union.gamesystem.model.user.IUserService;
import org.a2union.gamesystem.model.user.User;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class for component that represent chess board
 *
 * @author Iskakoff
 */
@IncludeStylesheet(value = "context:css/board.css")
@SupportsInformalParameters
public class ChessBoard {

    private static final String[] BOARD_LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H"};

    private static Logger log = Logger.getLogger(ChessBoard.class);
    @Parameter(required = true, allowNull = false)
    private GameBase game;

    @Environmental
    private RenderSupport renderSupport;

    @Inject
    private ComponentResources resources;

    @Inject
    private Messages messages;

    @Inject
    private IChessGameService chessGameService;

    @Inject
    private IUserService userService;

    @Inject
    @Path("context:js/chessboard.js")
    private Asset chessboardScript;

    @Inject
    @Path("context:js/board.js")
    private Asset boardScript;

    @Inject
    @Path("${tapestry.scriptaculous}/effects.js")
    private Asset scriptaculosEffects;

    @Inject
    @Path("${tapestry.scriptaculous}/prototype.js")
    private Asset prototype;

    @Inject
    @Path(value = "context:images/blackrook.gif")
    private Asset blackrook;
    @Inject
    @Path(value = "context:images/blackpawn.gif")
    private Asset blackpawn;
    @Inject
    @Path(value = "context:images/blackbishop.gif")
    private Asset blackbishop;
    @Inject
    @Path(value = "context:images/blackknight.gif")
    private Asset blackknight;
    @Inject
    @Path(value = "context:images/blackking.gif")
    private Asset blackking;
    @Inject
    @Path(value = "context:images/blackqueen.gif")
    private Asset blackqueen;
    @Inject
    @Path(value = "context:images/whiterook.gif")
    private Asset whiterook;
    @Inject
    @Path(value = "context:images/whitepawn.gif")
    private Asset whitepawn;
    @Inject
    @Path(value = "context:images/whitebishop.gif")
    private Asset whitebishop;
    @Inject
    @Path(value = "context:images/whiteknight.gif")
    private Asset whiteknight;
    @Inject
    @Path(value = "context:images/whiteking.gif")
    private Asset whiteking;
    @Inject
    @Path(value = "context:images/whitequeen.gif")
    private Asset whitequeen;
    private GameSide currentGameSide;

    public Asset getBlackrook() {
        return blackrook;
    }

    public Asset getBlackpawn() {
        return blackpawn;
    }

    public Asset getBlackbishop() {
        return blackbishop;
    }

    public Asset getBlackknight() {
        return blackknight;
    }

    public Asset getBlackking() {
        return blackking;
    }

    public Asset getBlackqueen() {
        return blackqueen;
    }

    public Asset getWhiterook() {
        return whiterook;
    }

    public Asset getWhitepawn() {
        return whitepawn;
    }

    public Asset getWhitebishop() {
        return whitebishop;
    }

    public Asset getWhiteknight() {
        return whiteknight;
    }

    public Asset getWhiteking() {
        return whiteking;
    }

    public Asset getWhitequeen() {
        return whitequeen;
    }

    public Asset getChessboardScript() {
        return chessboardScript;
    }

    public Asset getBoardScript() {
        return boardScript;
    }

    public Asset getScriptaculosEffects() {
        return scriptaculosEffects;
    }

    public GameBase getGame() {
        return game;
    }

    public void setGame(GameBase game) {
        this.game = game;
    }

    @SetupRender
    void setupRender() {
        renderSupport.addScriptLink(scriptaculosEffects);
        renderSupport.addScriptLink(boardScript);
        renderSupport.addScriptLink(chessboardScript);
    }

    /**
     * Render chess board
     *
     * @param writer Document writer
     */
    @BeginRender
    void begin(MarkupWriter writer) {
        // some optimizaton :-)
        writeFigureImagesMap(writer);
        if (GameStatus.ACTIVE.equals(game.getStatus())) {
            writer.element("script");
            writer.write("gameId=\"" + game.getUUID() + "\";");
            writer.write("canMove=" + getCurrentGameSide().isActive() + ";");
            Link eventLink = resources.createEventLink("echo");
            writer.write("setInterval(\"echo('" + eventLink.toAbsoluteURI() + "');\", 2000);"); //updateHistory();
            writer.end();
        }
        // movement indicator
//        writer.element("div", "id", "cm", "style", "position: relative; margin-left:-30px; margin-top: 100px; float: left; width: 32px; height: 32px; text-align: center;");
//        writer.end();
        if (GameStatus.COMPLETED.equals(game.getStatus())) {
            writer.element("div", "id", "result", "style", "position: relative; width: 300px; height: 32px; text-align: center;");
            writer.write(messages.format("gameover", messages.format(game.getResult().toString())));
            writer.end();
        }
        writer.element("div", "style", "position: relative; float: left; width: 310px;");
        //create board
        for (int i = 1; i <= 8; i++) {
            writer.element("div", "style", "position: relative;");
            writer.element("div", "style", "position: relative; float: left; width: 32px; height: 32px; text-align: center;");
            writer.write(getLineNumber(i));
            writer.end();
            writeLine(writer, i);
            writer.end();
        }
        //write letters
        writer.element("div", "style", "position: relative;");
        writer.element("div", "style", "position: relative; float:left; width: 32px; height: 32px;");
        writer.end();
        for (int i = 0; i < 8; i++) {
            writer.element("div", "style", "position: relative; float:left; width: 32px; height: 32px; text-align: center;");
            writer.write(getLetter(i));
            writer.end();
        }
        writer.end();
        if (GameStatus.ACTIVE.equals(game.getStatus())) {
            Link retreatLink = resources.createEventLink("retreat");
            Link drawSuggestLink = resources.createEventLink("suggestDraw");
            Link drawAcceptLink = resources.createEventLink("acceptDraw");
            writer.element("div", "style", "position: relative;");
            writer.element("div", "id", "retreat", "class", "btn");
            writer.element("a", "href", retreatLink.toAbsoluteURI(), "onclick", "if(!confirm('" + messages.get("retreat") + "?')) return false;");
            writer.write(messages.get("retreat"));
            writer.end();
            writer.end();
            boolean vis = getCurrentGameSide().isDrawProposed() || getCurrentGameSide().getNext().isDrawProposed();
            writer.element("div", "id", "suggest", "class", "btn", "style", "visibility:" + (!vis ? "visible" : "hidden"));
            writer.element("a", "href", drawSuggestLink.toAbsoluteURI(), "onclick", "if(!confirm('" + messages.get("suggest") + "?')) return false;");
            writer.write(messages.get("suggest"));
            writer.end();
            writer.end();
            writer.element("div", "id", "draw", "class", "btn", "style", "visibility:"
                    + (getCurrentGameSide().getNext().isDrawProposed() ? "visible" : "hidden"));
            writer.element("a", "href", drawAcceptLink.toAbsoluteURI(), "onclick", "if(!confirm('" + messages.get("acceptdraw") + "?')) return false;");
            writer.write(messages.get("acceptdraw"));
            writer.end();
            writer.end();
            writer.end();
        }

        renderPieces(writer);
        writer.end(); // board div

        //TODO extract game-history to component
        writer.element("div", "id", "div-gamehistory");
        writer.element("table", "style", "width:100%;");
        writer.element("tbody", "id", "gameshistory", "style", "width:100%;");
        writer.element("tr");
        //header
        writer.element("th");
        writer.writeRaw("");
        writer.end();
        writer.element("th");
        writer.writeRaw(messages.get(GameSideType.FIRST.getType()));
        writer.end();
        writer.element("th");
        writer.writeRaw(messages.get(GameSideType.SECOND.getType()));
        writer.end();
        //game history
        writeHistory(writer);
        writer.end();
        writer.end();
        writer.end();
        writer.end();
        writer.element("script");
        writer.writeRaw("var hist = document.getElementById(\"div-gamehistory\");hist.scrollTop = hist.scrollHeight;");
        writer.writeRaw("");
        writer.end();
    }

    private void writeFigureImagesMap(MarkupWriter writer) {
        writer.element("script");
        writer.write("var figureTypes = new Array(12);");
        writer.write("figureTypes['B0'] = \"" + whitebishop.toClientURL() + "\";");
        writer.write("figureTypes['Kp0'] = \"" + whiteking.toClientURL() + "\";");
        writer.write("figureTypes['p0'] = \"" + whitepawn.toClientURL() + "\";");
        writer.write("figureTypes['Q0'] = \"" + whitequeen.toClientURL() + "\";");
        writer.write("figureTypes['R0'] = \"" + whiterook.toClientURL() + "\";");
        writer.write("figureTypes['K0'] = \"" + whiteknight.toClientURL() + "\";");


        writer.write("figureTypes['B1'] = \"" + blackbishop.toClientURL() + "\";");
        writer.write("figureTypes['Kp1'] = \"" + blackking.toClientURL() + "\";");
        writer.write("figureTypes['p1'] = \"" + blackpawn.toClientURL() + "\";");
        writer.write("figureTypes['Q1'] = \"" + blackqueen.toClientURL() + "\";");
        writer.write("figureTypes['R1'] = \"" + blackrook.toClientURL() + "\";");
        writer.write("figureTypes['K1'] = \"" + blackknight.toClientURL() + "\";");
        writer.end();
    }

    private void writeHistory(MarkupWriter writer) {
        List<Step> gameHistory = chessGameService.getGameHistory(game);
        int num = 0;
        for (Step step : gameHistory) {
            if (step.getNumber() > num) {
                if (num != 0)
                    writer.end();
                num = step.getNumber();
                writer.element("tr", "id", "move" + num);
                writer.element("td");
                writer.writeRaw(num + ".");
                writer.end();
            }
            writer.element("td");
            writer.writeRaw(step.getStepInfo());
            writer.end();
        }
        if (num != 0)
            writer.end();
    }

    private String getColumnOffset(int columnNumber) {
        return columnNumber * 32 + "px";
    }

    private String getLineOffset(int lineNumber) {
        return (lineNumber - 1) * 32 + "px";
    }

    /**
     * Line for current player. TODO  For white-side must inverse order
     *
     * @param i - render line number
     * @return line number for current player
     */
    private String getLineNumber(int i) {
        return getCurrentGameSide().getType().equals(GameSideType.FIRST) ? String.valueOf(9 - i) : String.valueOf(i);
    }

    /**
     * TODO For black player column name must be inversed
     *
     * @param i column number
     * @return column name
     */
    private String getLetter(int i) {
        return getCurrentGameSide().getType().equals(GameSideType.FIRST) ? BOARD_LETTERS[i] : BOARD_LETTERS[7 - i];
    }

    private IChessPiece getPiece(String pieceId) {
        IChessPiece iPiece = (IChessPiece) getCurrentGameSide().getISide().getPiecesIdMap().get(pieceId);
        if (iPiece != null) {
            return iPiece;
        }
        throw new IllegalArgumentException("incorrect piece id");
    }

    private void writeLine(MarkupWriter writer, int lineNumber) {
        for (int j = 0; j < 8; j++) {
            String id = getLetter(j) + getLineNumber(lineNumber);
            Link eventLink = resources.createEventLink("move", id);
            writer.element("div",
                    "id",
                    id,
                    "style",
                    "position: relative; float:left; width: 32px; height: 32px;background-color: "
                            + ((((lineNumber + j) % 2) == 0) ? oddEvenColor() : samePieceColor()) + ";",
                    "onmousedown",
                    "checkForCorrectness('" + eventLink.toAbsoluteURI() + "');");
            writer.end();
        }
    }


    private void renderPieces(MarkupWriter writer) {
        Set<GameSide> gameSides = game.getGameSides();
        User user = userService.getCurrentUser();
        //mySide
        for (GameSide gameSide : gameSides) {
            boolean isCurrentUserSide = gameSide.getUser().equals(user);
            boolean white = GameSideType.FIRST.equals(gameSide.getType());
            Map<String, IPiece> map = gameSide.getISide().getPiecesMap();
            for (String position : map.keySet()) {
                IPiece iPiece = map.get(position);
                renderPiece(writer, iPiece, white, isCurrentUserSide);
            }

        }
    }

    private void renderPiece(MarkupWriter writer, IPiece iPiece, boolean white, boolean isCurrentUserSide) {
        Link eventLink = isCurrentUserSide ? null : resources.createEventLink("kill", iPiece.getNumber());
        String position = convertPosition(iPiece.getPosition());
        writer.element("div",
                "id", iPiece.getNumber(),
                "style",
                "position:absolute; width: 32px; height: 32px;",
                "class",
                position.substring(0, 1) + " l" + position.substring(1, 2),
                "onmousedown",
                getPieceMouseEvent(isCurrentUserSide, eventLink));
        writer.element("img", "src", getPieceImg(iPiece.getPieceType().getType(), white).toClientURL());
        writer.end();
        writer.end();
    }

    private String getPieceMouseEvent(boolean isCurrentUserSide, Link eventLink) {
        if (GameStatus.ACTIVE.equals(game.getStatus())) {
            return isCurrentUserSide ? "pieceSelect(this);" : "checkForCorrectness('" + eventLink.toAbsoluteURI() + "');";
        }
        return "";
    }

    private Asset getPieceImg(String type, boolean white) {
        if ("R".equals(type)) {
            return white ? whiterook : blackrook;
        } else if ("K".equals(type)) {
            return white ? whiteknight : blackknight;
        } else if ("B".equals(type)) {
            return white ? whitebishop : blackbishop;
        } else if ("Q".equals(type)) {
            return white ? whitequeen : blackqueen;
        } else if ("Kp".equals(type)) {
            return white ? whiteking : blackking;
        } else if ("p".equals(type)) {
            return white ? whitepawn : blackpawn;
        }
        throw new IllegalArgumentException("incorrect piece type");
    }

    private String samePieceColor() {
        return "rgb(255, 255, 255)";
    }

    private String oddEvenColor() {
        return " rgb(136, 0, 0)";
    }


    //Common Event
    public void onRetreat() {
        chessGameService.retreat(game);
    }

    public void onSuggestDraw() {
        chessGameService.suggestDraw(game);
    }

    public void onAcceptDraw() {
        chessGameService.acceptDraw(game);
    }

    // AJAX EVENT HANDLERS

    /**
     * result codes:
     * 999 - game completed
     * 0 - nothing happens
     * 1 - enemy has done movement
     * 2 - enemy has proposed draw
     *
     * @param canMove - can current user move
     * @return result array
     */
    public JSONArray onEcho(boolean canMove) {
        JSONArray result = new JSONArray();
        StringBuffer buffer = new StringBuffer();
        GameSide currentGameSide = getCurrentGameSide();
        GameSide nextGameSide = currentGameSide.getNext();
        if (!GameStatus.ACTIVE.equals(game.getStatus())) {
            buffer.append(999);
        } else if (nextGameSide.isDrawProposed() || currentGameSide.isDrawProposed()) {
            buffer.append(2).append(",").append(nextGameSide.isDrawProposed() ? 1 : 0).append(",").append(currentGameSide.isDrawProposed() ? 1 : 0);
        } else if (currentGameSide.isActive() && !canMove) {
            buffer.append(1);
            Step lastStep = chessGameService.getLastStep(getGame());
            if (lastStep != null) {
                buffer.append(",");
                buffer.append(lastStep.getNumber()).append(",").append(lastStep.getStepInfo());
            }
            String nextGameSideLastStep = nextGameSide.getLastStep();
            String postfix = nextGameSide.isWhite() ? "0" : "1";
//            result = new JSONArray();
            if (StringUtils.isNotEmpty(nextGameSideLastStep)) {
                Map<String, IChessPiece> moveMap = CommonChessUtils.getInstance().getBoardSide(nextGameSideLastStep);
                for (IChessPiece iPiece : moveMap.values()) {
                    String pieceType = iPiece.getPieceType().getType() + postfix;
                    buffer.append(",").append(convertPosition(iPiece.getPosition())).append(",").
                            append(iPiece.getNumber()).append(",").append(pieceType);
                }
                System.out.println(buffer.toString());
            }
//            buffer.append(nextGameSide.getLastStep());
        } else {
            buffer.append(0);
        }
        result.put(buffer.toString());
        return result;
    }

    /**
     * @param newPosition - new position
     * @param pieceId     - identifier of piece to move
     * @return changed pieces array in JSON notation
     */
    public JSONArray onMove(String newPosition, String pieceId) {
        IChessPiece piece = (IChessPiece) getPiece(pieceId);
        if (piece == null || newPosition == null) {
            return null;
        }
        JSONArray result = new JSONArray();
        String postfix = isWhite() ? "0" : "1";
        if (chessGameService.isPawnExchange(game, piece, newPosition)) {
            result.put(new JSONObject().put("pawnExchange", true));
            String pieceType = piece.getPieceType().getType() + postfix;
            result.put(new JSONObject().put("position", newPosition).put("id", pieceId).put("pieceType", pieceType).put("kill", false));
            return result;
        }
        Map<String, IChessPiece> moveMap = chessGameService.doMove(game, piece, newPosition);
        if (moveMap != null && moveMap.size() != 0) {
            for (IChessPiece iPiece : moveMap.values()) {
                String pieceType = iPiece.getPieceType().getType() + postfix;
                result.put(new JSONObject().put("position", convertPosition(iPiece.getPosition())).
                        put("id", iPiece.getNumber()).put("pieceType", pieceType));
            }
        }
        return result;
    }

    private boolean isWhite() {
        return getCurrentGameSide().isWhite();
    }

    public GameSide getCurrentGameSide() {
        if (currentGameSide == null) {
            // some optimizaton :-)
            currentGameSide = chessGameService.getCurrentGameSide(game);
        }
        return currentGameSide;
    }

    public JSONArray onKill(String pieceToKillId, String pieceId) {
        IChessPiece piece = (IChessPiece) getPiece(pieceId);
        Map<String, IChessPiece> map = getCurrentGameSide().getNext().getISide().getPiecesIdMap();
        IPiece pieceToKill = map.get(pieceToKillId);
        // 1. we can't move nothing
        // 2. we can't kill nobody
        // 3. Emperor is unbreakable!
        if (piece == null || pieceToKill == null || new King().equals(pieceToKill.getPieceType())) {
            return null;
        }
        piece.setInAttack(true);
        String postfix = isWhite() ? "0" : "1";
        if (chessGameService.isPawnExchange(game, piece, pieceToKill.getPosition())) {
            JSONArray result = new JSONArray();
            result.put(new JSONObject().put("pawnExchange", true));
            String pieceType = piece.getPieceType().getType() + postfix;
            result.put(new JSONObject().put("position", pieceToKill.getPosition()).put("id", pieceId).put("pieceType", pieceType).put("kill", true));
            return result;
        }
        Map<String, IChessPiece> moveMap = chessGameService.doMove(game, piece, pieceToKill.getPosition());
        JSONArray result = new JSONArray();
        if (moveMap != null && moveMap.size() != 0) {
            for (IChessPiece iPiece : moveMap.values()) {
                String pieceType = iPiece.getPieceType().getType() + postfix;
                result.put(new JSONObject().put("position", convertPosition(iPiece.getPosition())).
                        put("id", iPiece.getNumber()).put("pieceType", pieceType));
            }
        }
        return result;
    }

    public JSONObject onUpHistory() {
//        <!-- Codes by Quackit.com -->
//        <a href="javascript:location.reload(true)">Refresh this page</a>
        if (GameStatus.COMPLETED.equals(getGame().getStatus())) {
            return new JSONObject().put("finish", "true");
        }
        Step lastStep = chessGameService.getLastStep(getGame());
        if (lastStep != null)
            return new JSONObject().put("number", String.valueOf(lastStep.getNumber())).put("moveText", lastStep.getStepInfo());
        return null;
    }

    private String convertPosition(String position) {
        if (GameSideType.FIRST.equals(getCurrentGameSide().getType()) || "kill".equals(position)) {
            return position;
        } else {
            String letter = position.substring(0, 1);
            Integer line = Integer.parseInt(position.substring(1, 2));
            Integer integer = CommonChessUtils.getInstance().convertLetter(letter);
            return getLetter(integer - 1) + (9 - line);
        }
    }
}