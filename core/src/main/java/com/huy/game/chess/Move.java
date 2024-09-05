package com.huy.game.chess;

public class Move {
    private Spot start;
    private Spot end;
    private Piece startPiece;
    private Piece endPiece;

    public Move(Spot start, Spot end) {
        this.start = start;
        this.end = end;
        startPiece = start.getPiece();
        endPiece = end.getPiece();
    }

    public void makeRealMove(Board board) {
        board.setSpot(end.getX(), end.getY(), start.getPiece());
        board.setSpot(start.getX(), start.getY(), null);
    }

    public void makeMove(Board board) {
        board.setSpot(end.getX(), end.getY(), new Spot(start).getPiece());
        board.setSpot(start.getX(), start.getY(), null);
    }

    public void makeAIMove(Board board) {
        board.setSpot(end.getX(), end.getY(), board.getSpot(start.getX(), start.getY()).getPiece());
        board.setSpot(start.getX(), start.getY(), null);
    }

    public void unMove(Board board) {
        board.setSpot(start.getX(), start.getY(), startPiece);
        board.setSpot(end.getX(), end.getY(), null);
    }
}
