package com.huy.game.chess;

public class ChessPlayer {
    private boolean isWhile;

    public ChessPlayer(boolean isWhile) {
        this.isWhile = isWhile;
    }

    public boolean isWhile() {
        return isWhile;
    }

    public void setWhile(boolean aWhile) {
        isWhile = aWhile;
    }
}
