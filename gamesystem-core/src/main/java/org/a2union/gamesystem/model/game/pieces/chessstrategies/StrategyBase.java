package org.a2union.gamesystem.model.game.pieces.chessstrategies;

import org.a2union.gamesystem.model.game.pieces.IChessPiece;
import org.a2union.gamesystem.model.game.side.chess.ChessSide;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Iskakoff
 */
public abstract class StrategyBase implements MoveStrategy {
    protected Map<String, Integer> letters = new HashMap<String, Integer>();
    protected Map<Integer, String> lettersNumbers = new HashMap<Integer, String>();

    public StrategyBase() {
        init();
    }

    protected void init() {
        letters.put("A", 1);
        letters.put("B", 2);
        letters.put("C", 3);
        letters.put("D", 4);
        letters.put("E", 5);
        letters.put("F", 6);
        letters.put("G", 7);
        letters.put("H", 8);
        letters = Collections.unmodifiableMap(letters);
        lettersNumbers.put(1, "A");
        lettersNumbers.put(2, "B");
        lettersNumbers.put(3, "C");
        lettersNumbers.put(4, "D");
        lettersNumbers.put(5, "E");
        lettersNumbers.put(6, "F");
        lettersNumbers.put(7, "G");
        lettersNumbers.put(8, "H");
        lettersNumbers = Collections.unmodifiableMap(lettersNumbers);
    }

    protected Integer convertLetter(String letter) {
        return letters.get(letter);
    }

    protected String convertLetterNumber(Integer letter) {
        return lettersNumbers.get(letter);
    }

    public MoveStrategy isInStrategy(String currentPosition, String newPosition,
                                     Map<String, IChessPiece> piecesMap, Map<String, IChessPiece> enemyPiecesMap,
                                     boolean isInAttack, boolean isFirst) {
        Integer currentLetter = convertLetter(StringUtils.substring(currentPosition, 0, 1));
        Integer currentLine = Integer.parseInt(StringUtils.substring(currentPosition, 1, 2));
        Integer newLetter = convertLetter(StringUtils.substring(newPosition, 0, 1));
        Integer newLine = Integer.parseInt(StringUtils.substring(newPosition, 1, 2));
        return checkForFree(currentLetter, currentLine, newLetter, newLine,
                piecesMap, enemyPiecesMap, isInAttack) ?
                internalCheck(currentLetter, currentLine, newLetter, newLine, piecesMap, enemyPiecesMap, isFirst) : null;
    }

    /**
     * step tracing
     *
     * @param currentLetter  current letter number
     * @param currentLine    current line number
     * @param newLetter      new letter number
     * @param newLine        new line number
     * @param piecesMap      current game side pieces
     * @param enemyPiecesMap enemy game side pieces
     * @param isInAttack     current piece is attacking the enemy
     * @return true if path is clear
     */
    protected boolean checkForFree(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine,
                                   Map<String, IChessPiece> piecesMap, Map<String, IChessPiece> enemyPiecesMap, boolean isInAttack) {
        return checkSide(currentLetter, currentLine, newLetter, newLine, false, piecesMap) &&
                checkSide(currentLetter, currentLine, newLetter, newLine, isInAttack, enemyPiecesMap);
    }

    protected boolean checkSide(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine, boolean isInAttack, Map<String, IChessPiece> map) {
        return
                // move horizontal
                checkHorizontalMove(currentLetter, currentLine, newLetter, newLine, isInAttack, map) ||
                        // move vertical
                        checkVerticalMove(currentLetter, currentLine, newLetter, newLine, isInAttack, map) ||
                        //move diagonal
                        checkDiagonalMove(currentLetter, currentLine, newLetter, newLine, isInAttack, map) ||
                        //Knights move
                        checkKnightMove(currentLetter, currentLine, newLetter, newLine, isInAttack, map);
    }

    protected boolean checkKnightMove(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine, boolean inAttack, Map<String, IChessPiece> map) {
        if ((currentLetter.equals(newLetter) && currentLine.equals(newLine)) ||
                (!(Math.abs(currentLetter - newLetter) == 2 && Math.abs(currentLine - newLine) == 1) &&
                        !(Math.abs(currentLetter - newLetter) == 1 && Math.abs(currentLine - newLine) == 2))) {
            return false;
        }
        return (inAttack && (map.get(convertLetterNumber(newLetter) + newLine) != null)) || map.get(convertLetterNumber(newLetter) + newLine) == null;
    }

    protected boolean checkDiagonalMove(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine, boolean isInAttack, Map<String, IChessPiece> map) {
        if ((currentLetter.equals(newLetter) && currentLine.equals(newLine)) || (Math.abs(currentLetter - newLetter) != Math.abs(currentLine - newLine))) {
            return false;
        }
        boolean up = currentLine < newLine;
        boolean right = currentLetter < newLetter;
        for (int i = up ? currentLine + 1 : currentLine - 1, j = right ? currentLetter + 1 : currentLetter - 1;
             (i != (up ? (isInAttack ? newLine : newLine + 1) : (isInAttack ? newLine : newLine - 1)))
                     && (j != (right ? (isInAttack ? newLetter : newLetter + 1) : (isInAttack ? newLetter : newLetter - 1)));
             i = up ? i + 1 : i - 1, j = right ? j + 1 : j - 1) {
            if (map.get(convertLetterNumber(j) + i) != null) {
                return false;
            }
        }
        return true;
    }

    protected boolean checkVerticalMove(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine, boolean isInAttack, Map<String, IChessPiece> map) {
        if (currentLine.equals(newLine) || !currentLetter.equals(newLetter)) {
            return false;
        }
        int min = currentLine < newLine ? currentLine + 1 : isInAttack ? newLine + 1 : newLine;
        int max = currentLine > newLine ? currentLine - 1 : isInAttack ? newLine - 1 : newLine;
        for (int i = min; i <= max; i++) {
            if (map.get(convertLetterNumber(currentLetter) + i) != null) {
                return false;
            }
        }
        return true;
    }

    protected boolean checkHorizontalMove(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine, boolean isInAttack, Map<String, IChessPiece> map) {
        if (currentLetter.equals(newLetter) || !currentLine.equals(newLine)) {
            return false;
        }
        int min = currentLetter < newLetter ? currentLetter + 1 : isInAttack ? newLetter + 1 : newLetter;
        int max = currentLetter > newLetter ? currentLetter - 1 : isInAttack ? newLetter - 1 : newLetter;
        for (int i = min; i <= max; i++) {
            if (map.get(convertLetterNumber(i) + currentLine) != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * check can some of enemy pieces achieve the specified position to attack
     *
     * @param kingPosition  - position of king
     * @param pieceMap      - current side pieces
     * @param enemyPieceMap - enemy side pieces
     * @param isFirst
     * @return false if there are at least one piece that can attack the King
     */
    public boolean checkForCheck(String kingPosition, Map<String, IChessPiece> pieceMap, Map<String, IChessPiece> enemyPieceMap, boolean isFirst) {
        List<MoveStrategy> strategies = new ArrayList<MoveStrategy>();
        for (IChessPiece iPiece : pieceMap.values()) {
            boolean isInAttack = iPiece.isInAttack();
            iPiece.setInAttack(true);
            MoveStrategy moveStrategy = iPiece.getPieceType().getStrategy().isInStrategy(iPiece.getPosition(), kingPosition,
                    pieceMap, enemyPieceMap, iPiece.isInAttack(), isFirst);
            if (moveStrategy != null) {
                strategies.add(moveStrategy);
            }
            iPiece.setInAttack(isInAttack);
        }
        if (!strategies.isEmpty()) {
            // ALARM!!!! Emperor is under attack!!!!
        }
        return strategies.isEmpty();
    }

    @Override
    public String movement(String oldPosition, String newPosition, boolean isAttack, String... newType) {
        return getLetter() + oldPosition.toLowerCase() + (isAttack ? ":" : "-") + newPosition.toLowerCase();
    }

    protected String getLetter() {
        return "";
    }

    public Map<String, IChessPiece> movePiece(String newPosition, IChessPiece iPiece,ChessSide gameSide, ChessSide nextSide) {
        Map<String, IChessPiece> map = new HashMap<String, IChessPiece>();
        gameSide.getPiecesMap().remove(iPiece.getPosition());
        iPiece.move(newPosition);
        gameSide.getPiecesMap().put(newPosition, iPiece);
        if (iPiece.isInAttack()) {
            killEnemy(newPosition, gameSide, nextSide, map);
        }
        map.put(iPiece.getNumber(), iPiece);
        return map;
    }

    public Map<String, IChessPiece> movePiece(String newPosition, IChessPiece iPiece, ChessSide gameSide, ChessSide nextSide, String ptype) {
        return movePiece(newPosition, iPiece, gameSide, nextSide);
    }

    protected void killEnemy(String newPosition, ChessSide gameSide, ChessSide nextSide, Map<String, IChessPiece> map) {
        Map<String, IChessPiece> piecesMap = nextSide.getPiecesMap();
        IChessPiece piece = piecesMap.remove(newPosition);
        nextSide.getPiecesIdMap().remove(piece.getNumber());
        piece.setPosition("kill");
        map.put("kill", piece);
    }


    protected MoveStrategy internalCheck(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine,
                                         Map<String, IChessPiece> piecesMap, Map<String, IChessPiece> enemyPiecesMap, boolean isFirst) {
        return null;
    }
}
