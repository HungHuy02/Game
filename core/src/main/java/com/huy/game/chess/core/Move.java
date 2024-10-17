package com.huy.game.chess.core;

import com.huy.game.chess.core.notation.AlgebraicNotation;
import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.manager.ChessGameManager;

public class Move {
    private Spot start;
    private Spot end;
    private Piece startPiece;
    private Piece endPiece;
    private MoveType moveType;

    public Move(Spot start, Spot end) {
        this.start = start;
        this.end = end;
        startPiece = start.getPiece();
        endPiece = end.getPiece();
    }

    public void setMoveType(MoveType moveType) {
        this.moveType = moveType;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public Spot getStart() {
        return start;
    }

    public Spot getEnd() {
        return end;
    }

    public String makeRealMove(Board board, ZobristHashing hashing, GameHistory history, ChessGameManager manager){
        history.addStateHash(hashing.makeAMove(start, end));
        String text = history.addMove(start, end);
        board.setSpot(end.getX(), end.getY(), start.getPiece());
        board.setSpot(start.getX(), start.getY(), null);
        handleMove(board);
        history.addFEN(board, end.getPiece().isWhite(), manager);
        start.setShowColor(true);
        end.setShowColor(true);
        return text;
    }

    public void makeMove(Board board) {
        board.setSpot(end.getX(), end.getY(), new Spot(start).getPiece());
        board.setSpot(start.getX(), start.getY(), null);
        handleMove(board);
    }

    public String makeAIMove(Board board, ZobristHashing hashing, GameHistory history, ChessGameManager manager) {
        history.addStateHash(hashing.makeAMove(start, end));
        Spot startSpot = board.getSpot(start.getX(), start.getY());
        String text = history.addMove(startSpot, board.getSpot(end.getX(), end.getY()));
        board.setSpot(end.getX(), end.getY(), startSpot.getPiece());
        startSpot.setShowColor(true);
        board.setSpot(start.getX(), start.getY(), null);
        history.addFEN(board, end.getPiece().isWhite(), manager);
        return text;
    }

    public void unMove(Board board) {
        board.setSpot(start.getX(), start.getY(), startPiece);
        board.setSpot(end.getX(), end.getY(), endPiece);
    }

    private void handleMove(Board board) {
        switch (moveType) {
            case CASTLING_KING_SIDE -> {
                board.getSpot(start.getX(), start.getY() + 1).setPiece(board.getSpot(start.getX(), 7).getPiece());
                board.getSpot(start.getX(), 7).setPiece(null);
            }
            case CASTLING_QUEEN_SIDE -> {
                board.getSpot(start.getX(), start.getY() - 1).setPiece(board.getSpot(start.getX(), 0).getPiece());
                board.getSpot(start.getX(), 0).setPiece(null);
            }
            case EN_PASSANT -> {
                board.getPossibleEnPassantTargetsSpot().setPiece(null);
                board.setPossibleEnPassantTargetsSpot(null);
            }
            case PROMOTE_TO_QUEEN -> {

            }
            case PROMOTE_TO_KNIGHT -> {

            }
            case PROMOTE_TO_ROOK -> {

            }
            case PROMOTE_TO_BISHOP -> {

            }
        }
    }
}
