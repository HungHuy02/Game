package com.huy.game.chess.manager;

import com.huy.game.chess.ai.ChessAI;
import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.Move;

public class ChessEasyAIPlayer extends ChessAIPlayer{

    public ChessEasyAIPlayer(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public Move findBestMove(Board board, String fen) {
        return ChessAI.findBestMove(board, this.isWhite());
    }
}
