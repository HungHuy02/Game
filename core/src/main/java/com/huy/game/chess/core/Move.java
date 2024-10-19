package com.huy.game.chess.core;

import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.manager.ChessGameManager;
import com.huy.game.chess.manager.ChessImage;

public class Move {
    private final Spot start;
    private final Spot end;
    private final Piece startPiece;
    private final Piece endPiece;
    private MoveType moveType;
    private boolean isCheck = false;

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

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String makeRealMove(Board board, ZobristHashing hashing, GameHistory history, ChessGameManager manager, ChessImage chessImage){
        history.addStateHash(hashing.makeAMove(start, end, moveType));
        String text = history.addMove(start, end, this);
        board.setSpot(end.getX(), end.getY(), start.getPiece());
        board.setSpot(start.getX(), start.getY(), null);
        handleSpecialMove(board, chessImage);
        history.addFEN(board, end.getPiece().isWhite(), manager);
        start.setShowColor(true);
        end.setShowColor(true);
        return text;
    }

    public void makeMove(Board board) {
        board.setSpot(end.getX(), end.getY(), new Spot(start).getPiece());
        board.setSpot(start.getX(), start.getY(), null);
        handleSpecialMove(board);
    }

    public String makeAIMove(Board board, ZobristHashing hashing, GameHistory history, ChessGameManager manager) {
        history.addStateHash(hashing.makeAMove(start, end, moveType));
        Spot startSpot = board.getSpot(start.getX(), start.getY());
        String text = history.addMove(startSpot, board.getSpot(end.getX(), end.getY()), this);
        board.setSpot(end.getX(), end.getY(), startSpot.getPiece());
        startSpot.setShowColor(true);
        board.setSpot(start.getX(), start.getY(), null);
        history.addFEN(board, end.getPiece().isWhite(), manager);
        return text;
    }

    public void unMove(Board board) {
        board.setSpot(start.getX(), start.getY(), startPiece);
        board.setSpot(end.getX(), end.getY(), endPiece);
        unMoveSpecialMove(board);
    }

    private void unMoveSpecialMove(Board board) {
        switch (moveType) {
            case CASTLING_KING_SIDE -> {
                board.getSpot(start.getX(), 7).setPiece(board.getSpot(start.getX(), start.getY() + 1).getPiece());
                board.getSpot(start.getX(), start.getY() + 1).setPiece(null);
            }
            case CASTLING_QUEEN_SIDE -> {
                board.getSpot(start.getX(), 0).setPiece(board.getSpot(start.getX(), start.getY() - 1).getPiece());
                board.getSpot(start.getX(), start.getY() - 1).setPiece(null);
            }
            case EN_PASSANT -> {
                board.getPossibleEnPassantTargetsSpot().setPiece(null);
                board.setPossibleEnPassantTargetsSpot(null);
            }
        }
    }

    private void handleSpecialMove(Board board) {
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
            case PROMOTE -> {
                board.setPromoting(true);
                board.setPromotingMove(this);
            }
            case PROMOTE_TO_QUEEN -> {
                Queen queen = new Queen(endPiece.isWhite());
                board.getSpot(end.getX(),end.getY()).setPiece(queen);
            }
            case PROMOTE_TO_KNIGHT -> {
                Knight knight = new Knight(endPiece.isWhite());
                board.getSpot(end.getX(),end.getY()).setPiece(knight);
            }
            case PROMOTE_TO_ROOK -> {
                Rook rook = new Rook(endPiece.isWhite());
                board.getSpot(end.getX(),end.getY()).setPiece(rook);
            }
            case PROMOTE_TO_BISHOP -> {
                Bishop bishop = new Bishop(endPiece.isWhite());
                board.getSpot(end.getX(),end.getY()).setPiece(bishop);
            }
        }
    }

    public void handleSpecialMove(Board board, ChessImage chessImage) {
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
            case PROMOTE -> {
                board.setPromoting(true);
                board.setPromotingMove(this);
            }
            case PROMOTE_TO_QUEEN -> {
                Queen queen = new Queen(startPiece.isWhite(), startPiece.isWhite() ? chessImage.getwQueen() : chessImage.getbQueen());
                board.getSpot(end.getX(),end.getY()).setPiece(queen);
            }
            case PROMOTE_TO_KNIGHT -> {
                Knight knight = new Knight(startPiece.isWhite(), startPiece.isWhite() ? chessImage.getwKnight() : chessImage.getbKnight());
                board.getSpot(end.getX(),end.getY()).setPiece(knight);
            }
            case PROMOTE_TO_ROOK -> {
                Rook rook = new Rook(startPiece.isWhite(), startPiece.isWhite() ? chessImage.getwRock() : chessImage.getbRook());
                board.getSpot(end.getX(),end.getY()).setPiece(rook);
            }
            case PROMOTE_TO_BISHOP -> {
                Bishop bishop = new Bishop(startPiece.isWhite(), startPiece.isWhite() ? chessImage.getwBishop() : chessImage.getbBishop());
                board.getSpot(end.getX(),end.getY()).setPiece(bishop);
            }
        }
    }
}
