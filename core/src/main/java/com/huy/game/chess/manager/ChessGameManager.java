package com.huy.game.chess.manager;

import com.badlogic.gdx.utils.Timer;
import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.Piece;
import com.huy.game.chess.enums.Difficulty;
import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.interfaces.Stockfish;

import java.util.HashMap;
import java.util.Map;

public class ChessGameManager {

    private ChessPlayer player1;
    private ChessPlayer player2;
    private ChessPlayer currentPlayer;
    private Timer timer;
    private Map<String, Integer> timeList;

    public ChessGameManager(ChessMode mode, boolean isWhite, Stockfish stockfish) {
        setupPlayer(mode,isWhite, stockfish);
        setupTime();
    }

    private void setupPlayer(ChessMode mode, boolean isWhite, Stockfish stockfish) {
        player1 = new ChessPlayer(isWhite);
        switch (mode) {
            case TWO_PERSONS -> player2 = new ChessPlayer(!isWhite);
            case AI -> {
                switch (mode.getDifficulty()) {
                    case EASY -> player2 = new ChessEasyAIPlayer(!isWhite);
                    case HARD -> player2 = new ChessHardAIPlayer(!isWhite, stockfish);
                }
            }
            case ONLINE -> player2 = new ChessOnlinePlayer(!isWhite);
        }
        currentPlayer = isWhite ? player1 : player2;
    }


    private void setupTime() {
        timer = new Timer();
        timeList = new HashMap<>();
        timeList.put("play1", 0);
        timeList.put("play2", 0);
        timeList.put("1", 500);
        timeList.put("2", 500);
        handleTimer("1");
    }


    public void switchPlayer(Board board) {
        if(!board.isPromoting()) {
            cancelTimer();
            if(currentPlayer == player1) {
                currentPlayer = player2;
                handleTimer("2");
            }else {
                currentPlayer = player1;
                handleTimer("1");
            }
            currentPlayer.increaseTurn();
        }
    }

    public void putValue(Piece piece) {
        currentPlayer.putValue(piece);
        int value = player1.getValue() - player2.getValue();
        if(value > 0) {
            player1.putValue(value);
            player2.putValue(0);
        }else {
            player1.putValue(0);
            player2.putValue(Math.abs(value));
        }
    }

    public void handleTimer(String key) {
        timeList.put("play"+ key, 1);
        Timer.Task task = new Timer.Task() {
            int remainingTime = currentPlayer.getTimeRemain();

            @Override
            public void run() {
                if (remainingTime > 0) {
                    remainingTime--;
                    currentPlayer.setTimeRemain(remainingTime);
                    timeList.put(key, remainingTime);
                } else {
                    this.cancel();
                    timeList.put(key, 0);
                }
            }
        };
        timer.scheduleTask(task, 0, 1);
    }

    public Map<String, Integer> getTimeList() {
        return timeList;
    }

    public void cancelTimer() {
        timeList.put("play1", 0);
        timeList.put("play2", 0);
        timer.clear();
    }

    public int getCurrentTurn() {
        return currentPlayer.getTurn();
    }

    public ChessPlayer getPlayer1() {
        return player1;
    }

    public ChessPlayer getPlayer2() {
        return player2;
    }

    public ChessPlayer getCurrentPlayer() {
        return currentPlayer;
    }
}
