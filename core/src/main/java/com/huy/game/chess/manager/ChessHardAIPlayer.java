package com.huy.game.chess.manager;

import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.Move;
import com.huy.game.chess.interfaces.Stockfish;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChessHardAIPlayer extends ChessAIPlayer{

    private final Stockfish stockfish;

    public ChessHardAIPlayer(boolean isWhite, Stockfish stockfish) {
        super(isWhite);
        this.stockfish = stockfish;
    }

    @Override
    public Move findBestMove(Board board, String fen) {
        BlockingQueue<Move> queue = new ArrayBlockingQueue<>(1);
        stockfish.sendCommandAndGetResponse(fen, this.getTimeRemain(), data -> {
            Move move = new Move(
                board.getSpot(data.charAt(1) - '1', data.charAt(0) - 'a'),
                board.getSpot(data.charAt(3) - '1', data.charAt(2) - 'a'));
            queue.add(move);
        });
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
