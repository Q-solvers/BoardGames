/*
 * $Id: ChessBoard.java 181 2010-02-16 19:38:28Z iskakoff $
 */
package org.a2union.gamesystem.web.components.reversi;

import org.a2union.gamesystem.model.game.GameBase;
import org.a2union.gamesystem.model.game.GameStatus;
import org.a2union.gamesystem.model.game.pieces.CommonReversiUtils;
import org.a2union.gamesystem.model.game.pieces.IPiece;
import org.a2union.gamesystem.model.game.pieces.IReversiPiece;
import org.a2union.gamesystem.model.game.pieces.ReversiPiece;
import org.a2union.gamesystem.model.game.pieces.IChessPiece;
import org.a2union.gamesystem.model.game.pieces.CommonChessUtils;
import org.a2union.gamesystem.model.game.pieces.types.reversi.ReversiPieceType;
import org.a2union.gamesystem.model.game.reversi.IReversiGameService;
import org.a2union.gamesystem.model.game.side.GameSide;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.game.step.Step;
import org.a2union.gamesystem.model.user.IUserService;
import org.a2union.gamesystem.model.user.User;
import org.apache.log4j.Logger;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.commons.lang.StringUtils;

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
public class ReversiBoard {

    private static final String[] BOARD_LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H"};

    private static Logger log = Logger.getLogger(ReversiBoard.class);
    @Parameter(required = true, allowNull = false)
    private GameBase game;

    @Environmental
    private RenderSupport renderSupport;

    @Inject
    private ComponentResources resources;

    @Inject
    private Messages messages;

    @Inject
    private IReversiGameService reversiGameService;

    @Inject
    private IUserService userService;

    @Inject
    @Path(value = "context:images/reversi/rblack.gif")
    private Asset rblack;
    @Inject
    @Path(value = "context:images/reversi/rwhite.gif")
    private Asset rwhite;

    @Inject
    @Path("context:js/reversiboard.js")
    private Asset reversiboardScript;

    @Inject
    @Path("context:js/board.js")
    private Asset boardScript;

    @Inject
    @Path("${tapestry.scriptaculous}/effects.js")
    private Asset scriptaculosEffects;

    @Inject
    @Path("${tapestry.scriptaculous}/prototype.js")
    private Asset prototype;

    private GameSide currentGameSide;

    public Asset getScriptaculosEffects() {
        return scriptaculosEffects;
    }

    public Asset getRblack() {
        return rblack;
    }

    public Asset getRwhite() {
        return rwhite;
    }

    public Asset getReversiboardScript() {
        return reversiboardScript;
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
        renderSupport.addScriptLink(reversiboardScript);
    }

    /**
     * Render chess board
     *
     * @param writer Document writer
     */
    @BeginRender
    void begin(MarkupWriter writer) {
        // some optimizaton :-)
        if (GameStatus.ACTIVE.equals(game.getStatus())) {
            writer.element("script");
            writer.write("gameId=\"" + game.getUUID() + "\";");
            writer.write("canMove=" + getCurrentGameSide().isActive() + ";");
            writer.write("stepNum=" + getCurrentGameSide().getLastStepNumber() + ";");
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
        writer.element("input", "id", "hidden_move", "type","hidden", "value", currentGameSide.isActive() ? 1 : 0);
        writer.end();
        writer.element("input", "id", "hidden_step", "type","hidden", "value", currentGameSide.getLastStepNumber());
        writer.end();
    }

    private void renderPieces(MarkupWriter writer) {
        Set<GameSide> gameSides = game.getGameSides();
        User user = userService.getCurrentUser();
        //mySide
        for (GameSide gameSide : gameSides) {
            boolean isCurrentUserSide = gameSide.getUser().equals(user);
            boolean white = GameSideType.FIRST.equals(gameSide.getType());
            Map<String, IReversiPiece> map = gameSide.getISide().getPiecesMap();
            for (String position : map.keySet()) {
                IReversiPiece iPiece = map.get(position);
                renderPiece(writer, iPiece, white, isCurrentUserSide);
            }

        }
    }

    private void renderPiece(MarkupWriter writer, IPiece iPiece, boolean white, boolean isCurrentUserSide) {
        String position = convertPosition(iPiece.getPosition());
        writer.element("div",
                "id", iPiece.getNumber(),
                "style",
                "position:absolute; width: 32px; height: 32px;",
                "class",
                position.substring(0, 1) + " l" + position.substring(1, 2));
        writer.element("img", "src", getPieceImg(white).toClientURL());
        writer.end();
        writer.end();
    }

    private Asset getPieceImg(boolean white) {
        return white ? rwhite : rblack;
    }

    private String convertPosition(String position) {
        if (GameSideType.FIRST.equals(getCurrentGameSide().getType()) || "kill".equals(position)) {
            return position;
        } else {
            String letter = position.substring(0, 1);
            Integer line = Integer.parseInt(position.substring(1, 2));
            Integer integer = CommonReversiUtils.getInstance().convertLetter(letter);
            return getLetter(integer - 1) + (9 - line);
        }
    }


    private void writeHistory(MarkupWriter writer) {
        List<Step> gameHistory = reversiGameService.getGameHistory(game);
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
     * Line for current player.
     *
     * @param i - render line number
     * @return line number for current player
     */
    private String getLineNumber(int i) {
        return getCurrentGameSide().getType().equals(GameSideType.FIRST) ? String.valueOf(9 - i) : String.valueOf(i);
    }

    /**
     *
     * @param i column number
     * @return column name
     */
    private String getLetter(int i) {
        return getCurrentGameSide().getType().equals(GameSideType.FIRST) ? BOARD_LETTERS[i] : BOARD_LETTERS[7 - i];
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

    private String samePieceColor() {
        return "rgb(255, 255, 255)";
    }

    private String oddEvenColor() {
        return " rgb(136, 0, 0)";
    }


    //Common Event
    public void onRetreat() {
        reversiGameService.retreat(game);
    }

    public void onSuggestDraw() {
        reversiGameService.suggestDraw(game);
    }

    public void onAcceptDraw() {
        reversiGameService.acceptDraw(game);
    }

    // AJAX EVENT HANDLERS

    public Object onMove(String newPosition) {
        ReversiPiece reversiPiece = new ReversiPiece();
        reversiPiece.setPieceType(new ReversiPieceType());
        GameSide gameSide = getCurrentGameSide();
        reversiPiece.setNumber((gameSide.isFirst()?"0" : "1") + (gameSide.getLastStepNumber()+1));
        reversiPiece.setPosition(newPosition);
        if(reversiGameService.doMove(game, reversiPiece, newPosition) != null)
            return this;
        else {
            return null;
        }
    }

    /**
     * result codes:
     * 999 - game completed
     * 0 - nothing happens
     * 2 - enemy has proposed draw
     *
     * @param canMove - can current user move
     * @param stepNum - last updated step number
     * @return result array
     */
    public Object onEcho(boolean canMove, int stepNum) {
        JSONArray result = new JSONArray();
        StringBuffer buffer = new StringBuffer();
        GameSide currentGameSide = getCurrentGameSide();
        GameSide nextGameSide = currentGameSide.getNext();
        if (!GameStatus.ACTIVE.equals(game.getStatus())) {
            buffer.append(999);
        } else if (nextGameSide.isDrawProposed() || currentGameSide.isDrawProposed()) {
            buffer.append(2).append(",").append(nextGameSide.isDrawProposed() ? 1 : 0).append(",").append(currentGameSide.isDrawProposed() ? 1 : 0);
        } else if ((currentGameSide.isActive() && !canMove) ||
                (currentGameSide.getLastStepNumber()!= stepNum)) {
            return this;
//            buffer.append(nextGameSide.getLastStep());
        } else if (currentGameSide.isActive()) {
            buffer.append(3);
        } else {
            buffer.append(0);
            buffer.append(",");
            buffer.append(currentGameSide.isActive()? 1 : 0);
            buffer.append(",");
            buffer.append(currentGameSide.getLastStepNumber());
        }
        result.put(buffer.toString());
        return result;
    }

    // END OF AJAX EVENT HANDLERS

    public GameSide getCurrentGameSide() {
        if (currentGameSide == null) {
            // some optimizaton :-)
            currentGameSide = reversiGameService.getCurrentGameSide(game);
        }
        return currentGameSide;
    }

}