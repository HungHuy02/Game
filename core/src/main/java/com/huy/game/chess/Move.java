package com.huy.game.chess;

public class Move {
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private Spot start;
    private Spot end;

    public Move(int startX, int startY,int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public Move(Spot start, Spot end) {
        this.start = start;
        this.end = end;
    }

    public void makeMove(Spot[][] spots) {
        spots[endX][endY].setPiece(spots[startX][startY].getPiece());
        spots[startX][startX].setPiece(null);
    }

    public void unMove(Spot[][] spots) {
        spots[startX][startX].setPiece(spots[endX][endY].getPiece());
        spots[endX][endY].setPiece(null);

    }

    public void makeMove(Board board) {
        board.setSpot(end.getX(), end.getY(), new Spot(start).getPiece());
        board.setSpot(start.getX(), start.getY(), null);
    }

    public void unMove(Board board) {
        board.setSpot(start.getX(), start.getY(), start.getPiece());
        board.setSpot(end.getX(), end.getY(), null);
    }
}
