package com.huy.game.chess.manager;

import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.GameHistory;

public class ChessGameHistoryManager {

    private boolean isRePlay = false;
    private final GameHistory history;
    private Board board;
    private int index = -1;

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

    public void setBoard(int index, ChessImage chessImage) {
        board = history.changeFENToBoard(index, chessImage);
        this.index = index;
    }

    public void setBoard(ChessImage chessImage) {
        board = history.changeFENToBoard(index, chessImage);
    }

    public void setNewBoard(ChessImage chessImage) {
        board = new Board();
        board.resetBoard(chessImage);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void increaseIndex() {
        index++;
    }

    public void decreaseIndex() {
        index--;
    }
}
