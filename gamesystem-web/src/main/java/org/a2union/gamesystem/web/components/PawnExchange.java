package org.a2union.gamesystem.web.components;

import org.a2union.gamesystem.model.game.GameBase;
import org.a2union.gamesystem.model.game.chess.IChessGameService;
import org.a2union.gamesystem.model.game.pieces.CommonChessUtils;
import org.a2union.gamesystem.model.game.pieces.CommonUtils;
import org.a2union.gamesystem.model.game.pieces.IChessPiece;
import org.a2union.gamesystem.model.game.pieces.types.chess.IChessPieceType;
import org.a2union.gamesystem.model.game.side.GameSide;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.user.IUserService;
import org.a2union.gamesystem.model.user.User;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import java.util.Map;
import java.util.Set;


@IncludeStylesheet(value = "context:css/board.css")
@SupportsInformalParameters
public class PawnExchange {
    @Parameter(required = true, allowNull = false)
    private GameBase game;
    @Inject
    private IUserService userService;

    @Inject
    private IChessGameService chessGameService;

    @Inject
    private ComponentResources resources;
    @Inject
    @Path(value = "context:images/blackrook.gif")
    private Asset blackrook;
    @Inject
    @Path(value = "context:images/blackbishop.gif")
    private Asset blackbishop;
    @Inject
    @Path(value = "context:images/blackknight.gif")
    private Asset blackknight;

    @Inject
    @Path(value = "context:images/blackqueen.gif")
    private Asset blackqueen;
    @Inject
    @Path(value = "context:images/whiterook.gif")
    private Asset whiterook;

    @Inject
    @Path(value = "context:images/whitebishop.gif")
    private Asset whitebishop;
    @Inject
    @Path(value = "context:images/whiteknight.gif")
    private Asset whiteknight;

    @Inject
    @Path(value = "context:images/whitequeen.gif")
    private Asset whitequeen;

    @Inject
    private Messages messages;

    @BeginRender
    void begin(MarkupWriter writer) {
        writer.writeRaw(messages.get("chooseFigure"));
        writer.element("br");
        writer.end();
        renderPieces(writer, isWhite());
    }

    @SetupRender
    void setupRender() {

    }

    public JSONArray onExchangePawn(String figureTypeId, String pawnId, String pawnPosition, boolean kill) {
        IChessPiece pawn = getPiece(pawnId);
        if (figureTypeId == null || pawn == null || pawnPosition == null)
            return null;
        pawn.setInAttack(kill);

        Map<String, IChessPiece> moveMap = chessGameService.doMove(game, pawn, pawnPosition, true, FIGURE_TYPES[Integer.valueOf(figureTypeId) - 1]);
        JSONArray result = new JSONArray();
        if (moveMap != null && moveMap.size() != 0) {
            String postfix = isWhite() ? "0" : "1";
            for (IChessPiece iPiece : moveMap.values()) {

                String pieceType = iPiece.getPieceType().getType();
                result.put(new JSONObject().put("position", CommonChessUtils.getInstance().convertPosition(iPiece.getPosition(), chessGameService.getCurrentGameSide(game))).
                        put("id", iPiece.getNumber()).put("pieceType", pieceType + postfix));
            }
        }
        return result;
    }


    private IChessPiece getPiece(String pieceId) {
        IChessPiece iPiece = (IChessPiece) chessGameService.getCurrentGameSide(game).getISide().getPiecesIdMap().get(pieceId);
        if (iPiece != null) {
            return iPiece;
        }
        throw new IllegalArgumentException("incorrect piece id");
    }


    private boolean isWhite() {
        Set<GameSide> gameSides = game.getGameSides();
        User user = userService.getCurrentUser();
        for (GameSide gameSide : gameSides) {
            boolean currentUserSide = gameSide.getUser().equals(user);
            boolean white = GameSideType.FIRST.equals(gameSide.getType());
            if (currentUserSide && white)
                return true;
        }
        return false;
    }

    private String[] LETTERS = new String[]{"A8", "B8", "C8", "D8"};
    private String[] FIGURE_TYPES = new String[]{"B", "R", "K", "Q"};

    private void renderPieces(MarkupWriter writer, boolean white) {
        int i = 1;
        for (IChessPieceType type : CommonUtils.getPawnExchangeTypes()) {
            Link eventLink = resources.createEventLink("exchangePawn", i);
            writer.element("img", "id", i, "style", "position:relaitive;width: 32px; height: 32px;", "class",
                    LETTERS[i - 1].substring(0, 1) + " l" + LETTERS[i - 1].substring(1, 2) + ";", "src", getPieceImg(type.getType(), white).toClientURL(), "onmousedown", "chooseExchangeFigure('" + eventLink.toAbsoluteURI() + "');");
            writer.end();
            i++;
        }
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
        }
        throw new IllegalArgumentException("incorrect piece type");
    }
}
