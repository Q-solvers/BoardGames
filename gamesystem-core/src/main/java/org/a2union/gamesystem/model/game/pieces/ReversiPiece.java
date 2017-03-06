package org.a2union.gamesystem.model.game.pieces;

import org.a2union.gamesystem.model.game.pieces.types.reversi.IReversiPieceType;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Iskakoff
 */
public class ReversiPiece implements IReversiPiece {

    private String position;
    private String number;
    private IReversiPieceType type;

    @Override
    public IReversiStrategy checkMove(String newPosition, Map<String, ? extends IPiece> piecesMap, Map<String, ? extends IPiece> enemyPiecesMap, boolean isFirst) {
        IReversiStrategy reversiStrategy = new ReversiStrategy();
        Integer letter = CommonReversiUtils.getInstance().convertLetter(newPosition.substring(0, 1));
        Integer number = Integer.parseInt(newPosition.substring(1, 2));
        Map<String, IReversiPiece> reversiPieceMap = new HashMap<String, IReversiPiece>();
        /*
         * Horizontal part
         */
        right(piecesMap, enemyPiecesMap, letter, number, reversiPieceMap);
        left(piecesMap, enemyPiecesMap, letter, number, reversiPieceMap);
        /*
         * Vertical part
         */
        up(piecesMap, enemyPiecesMap, letter, number, reversiPieceMap);
        down(piecesMap, enemyPiecesMap, letter, number, reversiPieceMap);
        /*
        * Diagonal part
        */
        rightUp(piecesMap, enemyPiecesMap, letter, number, reversiPieceMap);
        rightDown(piecesMap, enemyPiecesMap, letter, number, reversiPieceMap);
        leftUp(piecesMap, enemyPiecesMap, letter, number, reversiPieceMap);
        leftDown(piecesMap, enemyPiecesMap, letter, number, reversiPieceMap);

        if (reversiPieceMap.isEmpty()) {
            return null;
        }
        reversiPieceMap.put(newPosition, this);
        reversiStrategy.setMap(reversiPieceMap);
        return reversiStrategy;
    }

    private void leftDown(Map<String, ? extends IPiece> piecesMap, Map<String, ? extends IPiece> enemyPiecesMap, Integer letter, Integer number, Map<String, IReversiPiece> reversiPieceMap) {
        for (int i = letter - 1, j = number - 1; (i > 0 && j > 0); i--, j--) {
            Map<String, IReversiPiece> upMap = new HashMap<String, IReversiPiece>();
            IPiece iPiece = piecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(i) + j);
            if (iPiece != null) {
                for (int i1 = letter - 1, j1 = number - 1; (i1 > i && j1 > j); i1--, j1--) {
                    IPiece piece = enemyPiecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(i1) + j1);
                    if (piece == null) {
                        upMap.clear();
                        break;
                    }
                    upMap.put(piece.getPosition(), (IReversiPiece) piece);
                }
                reversiPieceMap.putAll(upMap);
                break;
            }
        }
    }

    private void leftUp(Map<String, ? extends IPiece> piecesMap, Map<String, ? extends IPiece> enemyPiecesMap, Integer letter, Integer number, Map<String, IReversiPiece> reversiPieceMap) {
        for (int i = letter - 1, j = number + 1; (i > 0 && j <= 8); i--, j++) {
            Map<String, IReversiPiece> upMap = new HashMap<String, IReversiPiece>();
            IPiece iPiece = piecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(i) + j);
            if (iPiece != null) {
                for (int i1 = letter - 1, j1 = number + 1; (i1 > i && j1 < j); i1--, j1++) {
                    IPiece piece = enemyPiecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(i1) + j1);
                    if (piece == null) {
                        upMap.clear();
                        break;
                    }
                    upMap.put(piece.getPosition(), (IReversiPiece) piece);
                }
                reversiPieceMap.putAll(upMap);
                break;
            }
        }
    }

    private void rightDown(Map<String, ? extends IPiece> piecesMap, Map<String, ? extends IPiece> enemyPiecesMap, Integer letter, Integer number, Map<String, IReversiPiece> reversiPieceMap) {
        for (int i = letter + 1, j = number - 1; (i <= 8 && j > 0); i++, j--) {
            Map<String, IReversiPiece> upMap = new HashMap<String, IReversiPiece>();
            IPiece iPiece = piecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(i) + j);
            if (iPiece != null) {
                for (int i1 = letter + 1, j1 = number - 1; (i1 < i && j1 > j); i1++, j1--) {
                    IPiece piece = enemyPiecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(i1) + j1);
                    if (piece == null) {
                        upMap.clear();
                        break;
                    }
                    upMap.put(piece.getPosition(), (IReversiPiece) piece);
                }
                reversiPieceMap.putAll(upMap);
                break;
            }
        }
    }

    private void rightUp(Map<String, ? extends IPiece> piecesMap, Map<String, ? extends IPiece> enemyPiecesMap, Integer letter, Integer number, Map<String, IReversiPiece> reversiPieceMap) {
        for (int i = letter + 1, j = number + 1; (i <= 8 && j <= 8); i++, j++) {
            Map<String, IReversiPiece> upMap = new HashMap<String, IReversiPiece>();
            IPiece iPiece = piecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(i) + j);
            if (iPiece != null) {
                for (int i1 = letter + 1, j1 = number + 1; (i1 < i && j1 < j); i1++, j1++) {
                    IPiece piece = enemyPiecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(i1) + j1);
                    if (piece == null) {
                        upMap.clear();
                        break;
                    }
                    upMap.put(piece.getPosition(), (IReversiPiece) piece);
                }
                reversiPieceMap.putAll(upMap);
                break;
            }
        }
    }

    private void down(Map<String, ? extends IPiece> piecesMap, Map<String, ? extends IPiece> enemyPiecesMap, Integer letter, Integer number, Map<String, IReversiPiece> reversiPieceMap) {
        /*
        * down from new position
        */
        for (int i = number - 1; i > 0; i--) {
            Map<String, IReversiPiece> upMap = new HashMap<String, IReversiPiece>();
            IPiece iPiece = piecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(letter) + i);
            if (iPiece != null) {
                for (int j = number - 1; j > i; j--) {
                    IPiece piece = enemyPiecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(letter) + j);
                    if (piece == null) {
                        upMap.clear();
                        break;
                    }
                    upMap.put(piece.getPosition(), (IReversiPiece) piece);
                }
                reversiPieceMap.putAll(upMap);
                break;
            }
        }
    }

    private void up(Map<String, ? extends IPiece> piecesMap, Map<String, ? extends IPiece> enemyPiecesMap, Integer letter, Integer number, Map<String, IReversiPiece> reversiPieceMap) {
        /*
        * up from new position
        */
        for (int i = number + 1; i <= 8; i++) {
            Map<String, IReversiPiece> upMap = new HashMap<String, IReversiPiece>();
            IPiece iPiece = piecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(letter) + i);
            if (iPiece != null) {
                for (int j = number + 1; j < i; j++) {
                    IPiece piece = enemyPiecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(letter) + j);
                    if (piece == null) {
                        upMap.clear();
                        break;
                    }
                    upMap.put(piece.getPosition(), (IReversiPiece) piece);
                }
                reversiPieceMap.putAll(upMap);
                break;
            }
        }
    }

    private void left(Map<String, ? extends IPiece> piecesMap, Map<String, ? extends IPiece> enemyPiecesMap, Integer letter, Integer number, Map<String, IReversiPiece> reversiPieceMap) {
        /*
        * left from new position
        */
        for (int i = letter - 1; i > 0; i--) {
            Map<String, IReversiPiece> upMap = new HashMap<String, IReversiPiece>();
            IPiece iPiece = piecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(i) + number);
            if (iPiece != null) {
                for (int j = letter - 1; j > i; j--) {
                    IPiece piece = enemyPiecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(j) + number);
                    if (piece == null) {
                        upMap.clear();
                        break;
                    }
                    upMap.put(piece.getPosition(), (IReversiPiece) piece);
                }
                reversiPieceMap.putAll(upMap);
                break;
            }
        }
    }

    private void right(Map<String, ? extends IPiece> piecesMap, Map<String, ? extends IPiece> enemyPiecesMap, Integer letter, Integer number, Map<String, IReversiPiece> reversiPieceMap) {
        /*
        * right from new position
        */
        for (int i = letter + 1; i <= 8; i++) {
            Map<String, IReversiPiece> upMap = new HashMap<String, IReversiPiece>();
            IPiece iPiece = piecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(i) + number);
            if (iPiece != null) {
                for (int j = letter + 1; j < i; j++) {
                    IPiece piece = enemyPiecesMap.get(CommonReversiUtils.getInstance().convertLetterNumber(j) + number);
                    if (piece == null) {
                        upMap.clear();
                        break;
                    }
                    upMap.put(piece.getPosition(), (IReversiPiece) piece);
                }
                reversiPieceMap.putAll(upMap);
                break;
            }
        }
    }

    @Override
    public void move(String newPosition) {
        position = newPosition;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public IReversiPieceType getPieceType() {
        return type;
    }

    public void setPieceType(IReversiPieceType type) {
        this.type = type;
    }

    /**
     * Piece string representation format:
     * type:<type>;number:<number>;position:<position>
     * <p/>
     * types:
     * RR - reversi piece
     *
     * @return piece string representation
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("type:");
        buffer.append(getPieceType().getType());
        buffer.append(";");
        buffer.append("number:");
        buffer.append(getNumber());
        buffer.append(";");
        buffer.append("position:");
        buffer.append(getPosition());
        return buffer.toString();
    }
}
