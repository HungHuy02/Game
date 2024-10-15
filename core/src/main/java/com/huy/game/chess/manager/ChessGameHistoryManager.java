package com.huy.game.chess.manager;

import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.GameHistory;

public class ChessGameHistoryManager {

    private boolean isRePlay = false;
    private GameHistory history;
    private Board board;

    public ChessGameHistoryManager(GameHistory history) {
        this.history = history;
    }

    public boolean isRePlay() {
        return isRePlay;
    }

    public void setRePlay(boolean rePlay) {
        isRePlay = rePlay;
    }

    public GameHistory getHistory() {
        return history;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
