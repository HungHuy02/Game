package com.huy.game.chess.manager;

import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.Move;
import com.huy.game.chess.core.Spot;
import com.huy.game.chess.core.notation.AlgebraicNotation;
import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.enums.PieceType;
import com.huy.game.chess.interfaces.Stockfish;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChessHardAIPlayer extends ChessAIPlayer{

    private final Stockfish stockfish;

    public ChessHardAIPlayer(boolean isWhite, int time,Stockfish stockfish) {
        super(isWhite, time);
        this.stockfish = stockfish;
    }

    @Override
    public Move findBestMove(Board board, String fen) {
        BlockingQueue<Move> queue = new ArrayBlockingQueue<>(1);
        stockfish.findBestMove(fen, data -> {
            Spot start =  board.getSpot(
                AlgebraicNotation.changeRowAlgebraicNotationToRowPosition(data.charAt(1)),
                AlgebraicNotation.changeColAlgebraicNotationToColPosition(data.charAt(0)));
            Spot end = board.getSpot(
                AlgebraicNotation.changeRowAlgebraicNotationToRowPosition(data.charAt(3)),
                AlgebraicNotation.changeColAlgebraicNotationToColPosition(data.charAt(2)));
            Move move = new Move(start, end);
            handleMoveType(move, start, end, data);
            queue.add(move);
        });
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleMoveType(Move move, Spot start, Spot end, String data) {
        MoveType moveType = MoveType.NORMAL;
        if (data.length() == 5) {
            moveType = switch (data.charAt(4)) {
                case 'q' -> MoveType.PROMOTE_TO_QUEEN;
                case 'n' -> MoveType.PROMOTE_TO_KNIGHT;
                case 'r' -> MoveType.PROMOTE_TO_ROOK;
                case 'b' -> MoveType.PROMOTE_TO_BISHOP;
                default -> throw new RuntimeException("error");
            };
        }else {
            if (end.getPiece() != null) {
                moveType = MoveType.CAPTURE;
            }else {
                int x = Math.abs(end.getX() - start.getX());
                int y = Math.abs(end.getY() - start.getY());
                switch (start.getPiece().getType()) {
                    case PAWN -> {
                       if (x == 2) {
                            moveType = MoveType.DOUBLE_STEP_PAWN;
                        }else if (x == 1 && y == 1) {
                            moveType = MoveType.EN_PASSANT;
                        }
                    }
                    case KING -> {
                        if (y == 2) {
                            int directionY = Integer.signum(end.getY() - start.getY());
                            if (directionY > 0) {
                                moveType = MoveType.CASTLING_KING_SIDE;
                            }else {
                                moveType = MoveType.CASTLING_QUEEN_SIDE;
                            }
                        }
                    }
                }
            }
        }
        move.setMoveType(moveType);
    }

}
