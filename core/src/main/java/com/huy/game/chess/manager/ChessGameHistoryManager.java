package com.huy.game.chess.manager;

import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.GameHistory;

public class ChessGameHistoryManager {

    private boolean isRePlay = false;
    private final GameHistory history;
    private Board board;
    private int index = -1;
    private boolean isTakeBack = false;

    public ChessGameHistoryManager(GameHistory history) {
        this.history = history;
    }

    public boolean isRePlay() {
        return isRePlay;
    }

    public void setRePlay(boolean rePlay) {
        isRePlay = rePlay;
    }

    public boolean isTakeBack() {
        return isTakeBack;
    }

    public void setTakeBack(boolean takeBack) {
        isTakeBack = takeBack;
    }

    public GameHistory getHistory() {
        return history;
    }

    public Board getBoard() {
        return board;
    }

    public void setNewHistory(String fen, int[] time) {
        history.setHistoryForComebackGame(fen, time);
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

    public void deleteOldSaved() {
        history.deleteOldSaved(index);
    }

    public int[] getTimeRemain() {
        return history.getTimeRemain(index);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void returnOriginIndex() {
        index = history.getNumberBoardSaved() - 1;
    }

    public void increaseIndex() {
        index++;
    }

    public void decreaseIndex() {
        index--;
    }

    public void reset() {
        history.reset();
        index = -1;
        isRePlay = false;
        isTakeBack = false;
    }
}
