package com.huy.game.chess.manager;

import com.badlogic.gdx.utils.Timer;
import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.BoardSetting;
import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.enums.PieceType;
import com.huy.game.chess.enums.TimeType;
import com.huy.game.chess.interfaces.Stockfish;

import java.util.HashMap;
import java.util.Map;

public class ChessGameManager {

    private ChessPlayer player1;
    private ChessPlayer player2;
    private ChessPlayer currentPlayer;
    private Timer timer;
    private Map<String, Integer> timeList;
    private int plusTime;

    public ChessGameManager(ChessMode mode, boolean isWhite, TimeType timeType,Stockfish stockfish) {
        int time = handleTime(timeType);
        setupPlayer(mode,isWhite, stockfish, time);
        setupTime(time);
    }

    private int handleTime(TimeType timeType) {
        return switch (timeType) {
            case ONE_MINUTE -> 60;
            case ONE_MINUTE_PLUS_ONE -> {
                plusTime = 1;
                yield 60;
            }
            case TWO_MINUTE_PLUS_ONE -> {
                plusTime = 1;
                yield 120;
            }
            case THREE_MINUTE -> 180;
            case THREE_MINUTE_PLUS_TWO -> {
                plusTime = 2;
                yield  180;
            }
            case FIVE_MINUTE -> 300;
            case FIVE_MINUTE_PLUS_FIVE -> {
                plusTime = 5;
                yield 300;
            }
            case TEN_MINUTE -> 600;
            case FIFTEEN_MINUTE_PLUS_TEN -> {
                plusTime = 10;
                yield  900;
            }
            case THIRTY_MINUTE -> 1800;
            case NO_TIME -> 0;
        };
    }

    private void setupPlayer(ChessMode mode, boolean isWhite, Stockfish stockfish, int time) {
        player1 = new ChessPlayer(isWhite, time);
        switch (mode) {
            case TWO_PERSONS -> player2 = new ChessPlayer(!isWhite, time);
            case AI -> {
                switch (mode.getDifficulty()) {
                    case EASY -> player2 = new ChessEasyAIPlayer(!isWhite, time);
                    case HARD -> player2 = new ChessHardAIPlayer(!isWhite, time, stockfish);
                }
            }
            case ONLINE -> player2 = new ChessOnlinePlayer(!isWhite, time);
        }
        currentPlayer = isWhite ? player1 : player2;
    }


    private void setupTime(int time) {
        timer = new Timer();
        timeList = new HashMap<>();
        timeList.put("play1", 0);
        timeList.put("play2", 0);
        timeList.put("1", time);
        timeList.put("2", time);
        handleTimer("1");
    }

    public void switchPlayer(BoardSetting setting) {
        cancelTimer();
        int timeRemain = currentPlayer.getTimeRemain() + plusTime;
        if(currentPlayer == player1) {
            timeList.put("1",timeRemain );
            currentPlayer.setTimeRemain(timeRemain);
            currentPlayer = player2;
            handleTimer("2");
        }else {
            timeList.put("2", timeRemain);
            currentPlayer.setTimeRemain(timeRemain);
            currentPlayer = player1;
            handleTimer("1");
        }
        if (setting.isAutoRotate()) {
            setting.setRotate(!setting.isRotate());
        }
    }

    public void putValue(PieceType type) {
        currentPlayer.addCapturedPiece(type);
        int value = player1.getValue() - player2.getValue();
        if(value > 0) {
            player1.addCapturedPiece(value);
            player2.addCapturedPiece(0);
            player2.decreasePieceNumber(type);
        }else {
            player1.addCapturedPiece(0);
            player1.decreasePieceNumber(type);
            player2.addCapturedPiece(Math.abs(value));
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
