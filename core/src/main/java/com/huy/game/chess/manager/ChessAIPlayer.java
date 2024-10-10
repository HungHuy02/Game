package com.huy.game.chess.manager;

import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.Move;
import com.huy.game.chess.interfaces.Stockfish;

import java.util.function.Consumer;

public abstract class ChessAIPlayer extends ChessPlayer{

    public ChessAIPlayer(boolean isWhite) {
        super(isWhite);
    }

    public abstract Move findBestMove(Board board, String fen);
}
