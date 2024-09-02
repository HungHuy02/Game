package com.huy.game.chess;

public class Move {
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public Move(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }

    public void makeMove(Spot[][] spots) {
        spots[endX][endY].setPiece(spots[startX][startY].getPiece());
        spots[startX][startX].setPiece(null);
    }

    public void unMove(Spot[][] spots) {
        spots[startX][startX].setPiece(spots[endX][endY].getPiece());
        spots[endX][endY].setPiece(null);

    }
}
