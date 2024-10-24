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
    private Spot possibleEnPassantTargetsSpot = null;

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

    public Piece getStartPiece() {
        return startPiece;
    }

    public Spot getEnd() {
        return end;
    }

    public Piece getEndPiece() {
        return endPiece;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String makeRealMove(Board board, ZobristHashing hashing, GameHistory history, ChessGameManager manager, ChessImage chessImage) {
        history.addStateHash(hashing.makeAMove(start, end, moveType));
        board.setSpot(end.getX(), end.getY(), start.getPiece());
        board.setSpot(start.getX(), start.getY(), null);
        handleSpecialMove(board, chessImage);
        handleCheck(board);
        history.addFEN(board, end.getPiece().isWhite(), manager);
        handleSpotColorAfterMove(start, end);
        board.increaseTurn();
        return history.addMove(board , start, end, this);
    }

    public void makeMove(Board board) {
        board.setSpot(end.getX(), end.getY(), new Spot(start).getPiece());
        board.setSpot(start.getX(), start.getY(), null);
        handleSpecialMove(board);
        board.increaseTurn();
    }

    public String makeAIMove(Board board, ZobristHashing hashing, GameHistory history, ChessGameManager manager) {
        history.addStateHash(hashing.makeAMove(start, end, moveType));
        Spot startSpot = board.getSpot(start.getX(), start.getY());
        Spot endSpot = board.getSpot(end.getX(), end.getY());
        board.setSpot(end.getX(), end.getY(), startSpot.getPiece());
        board.setSpot(start.getX(), start.getY(), null);
        handleSpotColorAfterMove(startSpot, endSpot);
        handleCheck(board);
        history.addFEN(board, end.getPiece().isWhite(), manager);
        board.increaseTurn();
        return history.addMove(board, startSpot, board.getSpot(end.getX(), end.getY()), this);
    }

    private void handleSpotColorAfterMove(Spot start, Spot end) {
        start.setShowColor(true);
        end.setShowColor(true);
    }

    private void handleCheck(Board board) {
        if(end.getPiece().isCheckOpponentKing(board, board.getSpots(), end)) {
            isCheck = true;
        }else {
            if (board.isIndirectCheck(start, end.getPiece().isWhite())) {
                isCheck = true;
            }
        }
    }

    public void unMove(Board board) {
        board.setSpot(start.getX(), start.getY(), startPiece);
        board.setSpot(end.getX(), end.getY(), endPiece);
        unMoveSpecialMove(board);
        board.decreaseTurn();
    }

    private void unMoveSpecialMove(Board board) {
        if (possibleEnPassantTargetsSpot != null) {
            board.setPossibleEnPassantTargetsSpot(possibleEnPassantTargetsSpot);
        }
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
                board.setSpot(possibleEnPassantTargetsSpot.getX(), possibleEnPassantTargetsSpot.getY(), possibleEnPassantTargetsSpot.getPiece());
            }
        }
    }

    private void handleSpecialMove(Board board) {
        if (board.getPossibleEnPassantTargetsSpot() != null) {
            possibleEnPassantTargetsSpot = board.getPossibleEnPassantTargetsSpot();
            board.setPossibleEnPassantTargetsSpot(null);
        }
        switch (moveType) {
            case NORMAL -> {
                switch (startPiece.getType()) {
                    case ROOK -> {
                        if(start.getY() == 7) {
                            if (board.getKingSpot(startPiece.isWhite()).getPiece() instanceof King king) {
                                king.setCanCastlingKingSide(false);
                            }
                        }
                        if (start.getY() == 0) {
                            if (board.getKingSpot(startPiece.isWhite()).getPiece() instanceof King king) {
                                king.setCanCastlingQueenSide(false);
                            }
                        }
                    }
                    case KING -> {
                        if (board.getKingSpot(startPiece.isWhite()).getPiece() instanceof King king) {
                            king.setHasMove();
                        }
                    }
                }
            }
            case DOUBLE_STEP_PAWN -> {
                Spot pawnSpot = board.getSpot(end.getX(), end.getY());
                if(pawnSpot.getPiece() instanceof Pawn pawn) {
                    pawn.setTurn(board.getTurn());
                    board.setPossibleEnPassantTargetsSpot(pawnSpot);
                }
            }
            case CASTLING_KING_SIDE -> {
                board.getSpot(start.getX(), start.getY() + 1).setPiece(board.getSpot(start.getX(), 7).getPiece());
                board.getSpot(start.getX(), 7).setPiece(null);
            }
            case CASTLING_QUEEN_SIDE -> {
                board.getSpot(start.getX(), start.getY() - 1).setPiece(board.getSpot(start.getX(), 0).getPiece());
                board.getSpot(start.getX(), 0).setPiece(null);
            }
            case EN_PASSANT -> {
                possibleEnPassantTargetsSpot.setPiece(null);
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
        if (board.getPossibleEnPassantTargetsSpot() != null) {
            possibleEnPassantTargetsSpot = board.getPossibleEnPassantTargetsSpot();
            board.setPossibleEnPassantTargetsSpot(null);
        }
        switch (moveType) {
            case NORMAL -> {
                switch (startPiece.getType()) {
                    case ROOK -> {
                        if(start.getY() == 7) {
                            if (board.getKingSpot(startPiece.isWhite()).getPiece() instanceof King king) {
                                king.setCanCastlingKingSide(false);
                            }
                        }
                        if (start.getY() == 0) {
                            if (board.getKingSpot(startPiece.isWhite()).getPiece() instanceof King king) {
                                king.setCanCastlingQueenSide(false);
                            }
                        }
                    }
                    case KING -> {
                        if (board.getKingSpot(startPiece.isWhite()).getPiece() instanceof King king) {
                            king.setHasMove();
                        }
                    }
                }
            }
            case DOUBLE_STEP_PAWN -> {
                Spot pawnSpot = board.getSpot(end.getX(), end.getY());
                if(pawnSpot.getPiece() instanceof Pawn pawn) {
                    pawn.setTurn(board.getTurn());
                    board.setPossibleEnPassantTargetsSpot(pawnSpot);
                }
            }
            case CASTLING_KING_SIDE -> {
                board.getSpot(start.getX(), start.getY() + 1).setPiece(board.getSpot(start.getX(), 7).getPiece());
                board.getSpot(start.getX(), 7).setPiece(null);
            }
            case CASTLING_QUEEN_SIDE -> {
                board.getSpot(start.getX(), start.getY() - 1).setPiece(board.getSpot(start.getX(), 0).getPiece());
                board.getSpot(start.getX(), 0).setPiece(null);
            }
            case EN_PASSANT -> {
                possibleEnPassantTargetsSpot.setPiece(null);
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
