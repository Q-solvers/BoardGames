package org.a2union.gamesystem.model.game.pieces;

import org.a2union.gamesystem.model.game.pieces.types.chess.IChessPieceType;
import org.a2union.gamesystem.model.game.pieces.types.chess.Bishop;
import org.a2union.gamesystem.model.game.pieces.types.chess.King;
import org.a2union.gamesystem.model.game.pieces.types.chess.Knight;
import org.a2union.gamesystem.model.game.pieces.types.chess.Pawn;
import org.a2union.gamesystem.model.game.pieces.types.chess.Queen;
import org.a2union.gamesystem.model.game.pieces.types.chess.Rook;
import org.a2union.gamesystem.model.game.pieces.types.IPieceType;
import org.a2union.gamesystem.model.game.side.GameSide;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Iskakoff
 */
public abstract class CommonUtils<I extends IPieceType, J extends IPiece<I>> {

    public static Map<String, Class<? extends IPieceType>> classes = new HashMap<String, Class<? extends IPieceType>>();
    private static Map<String, Integer> letters = new HashMap<String, Integer>();
    private static Map<Integer, String> lettersNumbers = new HashMap<Integer, String>();

//    private static final CommonUtils instanse = new CommonUtils();

    private Class<? extends J> aClass;

    public CommonUtils(Class<? extends J> aClass) {
        this.aClass = aClass;
    }

    //    public static CommonUtils getInstance(Class<? extends IPiece> chessPieceClass) {
//        return instanse;
//    }

    static {
        classes.put("R", Rook.class);
        classes.put("K", Knight.class);
        classes.put("B", Bishop.class);
        classes.put("Q", Queen.class);
        classes.put("Kp", King.class);
        classes.put("p", Pawn.class);
        classes = Collections.unmodifiableMap(classes);
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

    public Integer convertLetter(String letter) {
        return letters.get(letter);
    }

    public String convertLetterNumber(Integer letter) {
        return lettersNumbers.get(letter);
    }

    /**
     * Convert string representation of piece to <code>IPiece</code> object
     *
     * @param pieceInfo - piece string represenation
     * @return piece object
     */
    public J getPiece(String pieceInfo) {
        StringTokenizer stringTokenizer = new StringTokenizer(pieceInfo, ";");
        String[] key = getValue(stringTokenizer);
        try {
            J iPiece = aClass.newInstance();
            iPiece.setPieceType(getPieceType(key[1]));
            while (stringTokenizer.hasMoreTokens()) {
                String[] value = getValue(stringTokenizer);
                Method method = ReflectionUtils.findMethod(aClass, "set" + StringUtils.capitalize(value[0]), new Class[]{String.class});
                method.invoke(iPiece, value[1]);
            }
            return iPiece;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public I getPieceType(String type) {
        Class<? extends I> aClass = (Class<? extends I>) classes.get(type);
        Assert.notNull(aClass);
        I result = null;
        try {
            result = aClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Convert board side (black or white) string representation to object representation
     *
     * @param boardSide - string representation of board side (black or white)
     * @return object representation of board side
     */
    public Map<String, J> getBoardSide(String boardSide) {
        Assert.notNull(boardSide);
        String[] piecesStr = StringUtils.split(boardSide, "\n");
        Assert.isTrue(piecesStr.length > 0);
        Map<String, J> pieces = new HashMap<String, J>(piecesStr.length);
        for (String pieceStr : piecesStr) {
            J iPiece = getPiece(pieceStr);
            pieces.put(iPiece.getPosition(), iPiece);
        }
        return pieces;
    }

    /**
     * Convert object representation of board side to string
     *
     * @param pieces - board side object representation
     * @return string representation of board side
     */
    public String getBoardSideString(Collection<J> pieces) {
        return StringUtils.join(pieces, "\n");
    }

    public String getInitialBoard(boolean white) {
        StringBuffer buffer = new StringBuffer();
        //pawns
        String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
        for (int i = 0; i < 8; i++) {
            buffer.append("type:p;number:");
            buffer.append(white ? 0 : 1);
            buffer.append(i);
            buffer.append(";position:");
            buffer.append(letters[i]);
            buffer.append(white ? 2 : 7);
            buffer.append(";initial:true");
            buffer.append("\n");
        }
        buffer.append("type:R;number:");
        buffer.append(white ? 0 : 1);
        buffer.append("11;position:A");
        buffer.append(white ? 1 : 8);
        buffer.append(";initial:true");
        buffer.append("\n");
        buffer.append("type:R;number:");
        buffer.append(white ? 0 : 1);
        buffer.append("12;position:H");
        buffer.append(white ? 1 : 8);
        buffer.append(";initial:true");
        buffer.append("\n");
        buffer.append("type:K;number:");
        buffer.append(white ? 0 : 1);
        buffer.append("13;position:B");
        buffer.append(white ? 1 : 8);
        buffer.append(";initial:true");
        buffer.append("\n");
        buffer.append("type:K;number:");
        buffer.append(white ? 0 : 1);
        buffer.append("14;position:G");
        buffer.append(white ? 1 : 8);
        buffer.append(";initial:true");
        buffer.append("\n");
        buffer.append("type:B;number:");
        buffer.append(white ? 0 : 1);
        buffer.append("15;position:C");
        buffer.append(white ? 1 : 8);
        buffer.append(";initial:true");
        buffer.append("\n");
        buffer.append("type:B;number:");
        buffer.append(white ? 0 : 1);
        buffer.append("16;position:F");
        buffer.append(white ? 1 : 8);
        buffer.append(";initial:true");
        buffer.append("\n");
        buffer.append("type:Q;number:");
        buffer.append(white ? 0 : 1);
        buffer.append("17;position:D");
        buffer.append(white ? 1 : 8);
        buffer.append(";initial:true");
        buffer.append("\n");
        buffer.append("type:Kp;number:");
        buffer.append(white ? 0 : 1);
        buffer.append("18;position:E");
        buffer.append(white ? 1 : 8);
        buffer.append(";initial:true");
        buffer.append("\n");

        return buffer.toString();
    }


    private String[] getValue(StringTokenizer stringTokenizer) {
        String[] strings = StringUtils.split(stringTokenizer.nextToken(), ":");
        Assert.isTrue(strings.length == 2);
        return strings;
    }

    public Map<String, J> getBoardIdSide(String pieces) {
        Assert.notNull(pieces);
        String[] piecesStr = StringUtils.split(pieces, "\n");
        Assert.isTrue(piecesStr.length > 0);
        Map<String, J> piecesId = new HashMap<String, J>(piecesStr.length);
        for (String pieceStr : piecesStr) {
            J iPiece = getPiece(pieceStr);
            piecesId.put(iPiece.getNumber(), iPiece);
        }
        return piecesId;
    }

    public Map<String, J> convertToPieceMap(Map<String, J> piecesIdMap) {
        HashMap<String, J> map = new HashMap<String, J>();
        for (J iPiece : piecesIdMap.values()) {
            map.put(iPiece.getPosition(), iPiece);
        }
        return map;
    }

    public Map<String, J> convertToPieceIdMap(Map<String, J> piecesMap) {
        HashMap<String, J> map = new HashMap<String, J>();
        for (J iPiece : piecesMap.values()) {
            map.put(iPiece.getNumber(), iPiece);
        }
        return map;
    }

    public void getBoard(String pieces, Map<String, J> pieceIdMap, Map<String, J> pieceMap, Map<I, List<J>> pieceTypeMap) {
        Assert.notNull(pieces);
        String[] piecesStr = StringUtils.split(pieces, "\n");
        Assert.isTrue(piecesStr.length > 0);
//        Map<String, IPiece> piecesId = new HashMap<String, IPiece>(piecesStr.length);
        for (String pieceStr : piecesStr) {
            J iPiece = getPiece(pieceStr);
            pieceIdMap.put(iPiece.getNumber(), iPiece);
            pieceMap.put(iPiece.getPosition(), iPiece);
            List<J> pieceList = pieceTypeMap.get(iPiece.getPieceType());
            if (pieceList == null) {
                pieceList = new ArrayList<J>();
                pieceTypeMap.put(iPiece.getPieceType(), pieceList);
            }
            pieceList.add(iPiece);
        }
    }

    private static final IChessPieceType[] TYPES = new IChessPieceType[]{new Bishop(), new Rook(), new Knight(), new Queen()};

    public static IChessPieceType[] getPawnExchangeTypes() {
        return TYPES;
    }

    public String getLetter(int i, GameSide side) {
        return side.getType().equals(GameSideType.FIRST) ? lettersNumbers.get(i + 1) : lettersNumbers.get(8-i);
    }

    public String convertPosition(String position, GameSide side) {
        if(GameSideType.FIRST.equals(side.getType()) || "kill".equals(position)){
            return position;
        } else {
            String letter = position.substring(0, 1);
            Integer line = Integer.parseInt(position.substring(1, 2));
            Integer integer = convertLetter(letter);
            return getLetter(integer - 1, side) + (9 - line);
        }
    }
}
