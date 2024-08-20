package com.huy.game.chess;

public class Spot {
    private Piece piece;
    private int x;
    private int y;
    private boolean showColor = false;

    public Spot(Piece piece, int x, int y) {
        this.piece = piece;
        this.x = x;
        this.y = y;
    }

    public Spot(Piece piece, int x, int y, boolean showColor) {
        this.piece = piece;
        this.x = x;
        this.y = y;
        this.showColor = showColor;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isShowColor() {
        return showColor;
    }

    public void setShowColor(boolean showColor) {
        this.showColor = showColor;
    }
}
