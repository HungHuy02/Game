package com.huy.game.chess.manager;

import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.Move;

public abstract class ChessAIPlayer extends ChessPlayer{

    public ChessAIPlayer(boolean isWhite, int time) {
        super(isWhite, time);
    }

    public abstract Move findBestMove(Board board, String fen);
}
