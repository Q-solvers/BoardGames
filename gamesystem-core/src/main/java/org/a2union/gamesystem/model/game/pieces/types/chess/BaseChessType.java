package org.a2union.gamesystem.model.game.pieces.types.chess;

/**
 * @author Iskakoff
 */
public abstract class BaseChessType implements IChessPieceType {
    protected boolean inAttack;
    private boolean pawnExchange;
    private boolean initialStep;

    public boolean isInAttack() {
        return inAttack;
    }

    public boolean isInitialStep() {
        return initialStep;
    }

    public void setInitialStep(boolean initialStep) {
        this.initialStep = initialStep;
    }

    public void setInAttack(boolean inAttack) {
        this.inAttack = inAttack;
    }

    public boolean isPawnExchange() {
        return pawnExchange;
    }

    public void setPawnExchange(boolean pawnExchange) {
        this.pawnExchange = pawnExchange;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IChessPieceType) {
            IChessPieceType type = (IChessPieceType) obj;
            return getType().equals(type.getType());
        }
        return false;
    }


    @Override
    public int hashCode() {
        return getType().hashCode();
    }
}
