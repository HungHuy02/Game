package com.huy.game.chess;

public class ChessPlayer {
    private boolean isWhite;

    public ChessPlayer(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean isWhite) {
        isWhite = isWhite;
    }
}
