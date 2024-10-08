package com.huy.game.chess.core;

import com.huy.game.chess.manager.ChessGameManager;

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

    public Spot getStart() {
        return start;
    }

    public Spot getEnd() {
        return end;
    }

    public String makeRealMove(Board board, ZobristHashing hashing, GameHistory history, ChessGameManager manager){
        history.addStateHash(hashing.makeAMove(start, end));
        String text = board.addMove(start, end);
        board.setSpot(end.getX(), end.getY(), start.getPiece());
        board.setSpot(start.getX(), start.getY(), null);
        history.addFEN(board, end.getPiece().isWhite(), manager);
        start.setShowColor(true);
        end.setShowColor(true);
        return text;
    }

    public void makeMove(Board board) {
        board.setSpot(end.getX(), end.getY(), new Spot(start).getPiece());
        board.setSpot(start.getX(), start.getY(), null);
    }

    public String makeAIMove(Board board) {
        Spot startSpot = board.getSpot(start.getX(), start.getY());
        String text = board.addMove(startSpot, board.getSpot(end.getX(), end.getY()));
        board.setSpot(end.getX(), end.getY(), startSpot.getPiece());
        startSpot.setShowColor(true);
        board.setSpot(start.getX(), start.getY(), null);
        return text;
    }

    public void unMove(Board board) {
        board.setSpot(start.getX(), start.getY(), startPiece);
        board.setSpot(end.getX(), end.getY(), endPiece);
    }
}
