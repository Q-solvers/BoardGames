package org.a2union.gamesystem.model.game.pieces.chessstrategies;

/**
 * @author Iskakoff
 */
public class IPStartegy extends ComplexStrategy {
    public IPStartegy() {
        super(new InitialPawnStrategy(), new PawnStrategy());
    }
}
