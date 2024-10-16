package com.huy.game.chess.manager;

import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.Move;
import com.huy.game.chess.core.notation.AlgebraicNotation;
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
        stockfish.sendCommandAndGetResponse(fen, this.getTimeRemain(), data -> {
            Move move = new Move(
                board.getSpot(
                    AlgebraicNotation.changeRowAlgebraicNotationToRowPosition(data.charAt(1)),
                    AlgebraicNotation.changeColAlgebraicNotationToColPosition(data.charAt(0))),
                board.getSpot(
                    AlgebraicNotation.changeRowAlgebraicNotationToRowPosition(data.charAt(3)),
                    AlgebraicNotation.changeColAlgebraicNotationToColPosition(data.charAt(2))));
            queue.add(move);
        });
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
