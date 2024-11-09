package com.huy.game.chess.manager;

import com.badlogic.gdx.utils.Timer;
import com.huy.game.chess.ChessScreen;
import com.huy.game.chess.core.BoardSetting;
import com.huy.game.chess.core.GameHistory;
import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.enums.GameResult;
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
    private final TimeType timeType;

    public ChessGameManager(ChessMode mode, boolean isWhite, TimeType timeType,Stockfish stockfish, GameHistory history, ChessScreen screen) {
        this.timeType = timeType;
        int time = handleTime();
        setupPlayer(mode,isWhite, stockfish, time);
        setupTime(time, history, screen);
    }

    private int handleTime() {
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


    private void setupTime(int time, GameHistory history, ChessScreen screen) {
        if (timeType != TimeType.NO_TIME) {
            timer = new Timer();
            timeList = new HashMap<>();
            timeList.put("play1", 0);
            timeList.put("play2", 0);
            timeList.put("1", time);
            timeList.put("2", time);
            handleTimer("1", screen);
            history.addTimeRemain(0, time, time);
        }
    }

    public void setTimeRemain(int[] timeRemain) {
        player1.setTimeRemain(timeRemain[0]);
        player2.setTimeRemain(timeRemain[1]);
        timeList.put("1", timeRemain[0]);
        timeList.put("2", timeRemain[1]);
    }

    public void switchPlayer(BoardSetting setting, ChessGameHistoryManager manager, ChessScreen screen) {
        if (timeType != TimeType.NO_TIME) {
            cancelTimer();
            manager.getHistory().addTimeRemain(manager.getIndex(), player1.getTimeRemain(), player2.getTimeRemain());
            int timeRemain = currentPlayer.getTimeRemain() + plusTime;
            if(currentPlayer == player1) {
                timeList.put("1",timeRemain );
                currentPlayer.setTimeRemain(timeRemain);
                currentPlayer = player2;
                handleTimer("2", screen);
            }else {
                timeList.put("2", timeRemain);
                currentPlayer.setTimeRemain(timeRemain);
                currentPlayer = player1;
                handleTimer("1", screen);
            }
        }else {
            if(currentPlayer == player1) {
                currentPlayer = player2;
            }else {
                currentPlayer = player1;
            }
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
        }else {
            player1.addCapturedPiece(0);
            player2.addCapturedPiece(Math.abs(value));
        }
        if (currentPlayer.isWhite() == player1.isWhite()) {
            player2.decreasePieceNumber(type);
        }else {
            player1.decreasePieceNumber(type);
        }
    }

    public void putValueForPromote(PieceType type) {
        currentPlayer.addCapturedPiece(type);
        currentPlayer.increasePieceNumber(type);
        int value = player1.getValue() - player2.getValue();
        if(value > 0) {
            player1.addCapturedPiece(value);
            player2.addCapturedPiece(0);
        }else {
            player1.addCapturedPiece(0);
            player2.addCapturedPiece(Math.abs(value));
        }
    }

    public void handleTimer(String key, ChessScreen screen) {
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
                    GameResult result;
                    if (isDrawByInsufficientPieceAfterTimeout()) {
                        result = GameResult.DRAW_INSUFFICIENT;
                    }else {
                        result = currentPlayer.isWhite() ? GameResult.BLACK_WIN : GameResult.WHITE_WIN;
                    }
                    screen.showEndGamePopup(result);
                    screen.handleAfterShowEndGamePopup(result);
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

    public void setTimeRemainInOnlineMode(int timeRemain) {
        cancelTimer();
        currentPlayer.setTimeRemain(timeRemain);
        if (currentPlayer.isWhite() == player1.isWhite()) {
            timeList.put("1", timeRemain);
        }else {
            timeList.put("2", timeRemain);
        }
    }

    public int getPlayerTime(boolean isWhite) {
        if (player1.isWhite() == isWhite) {
            return player1.getTimeRemain();
        }else  {
            return player2.getTimeRemain();
        }
    }

    public int getTimeRemainForOnlineMode() {
        return currentPlayer.isWhite() == player1.isWhite() ? player2.getTimeRemain() : player1.getTimeRemain();
    }

    public void reset(GameHistory history, ChessScreen screen) {
        int time = handleTime();
        if (timeType != TimeType.NO_TIME) {
            timer.clear();
            timeList.put("play1", 0);
            timeList.put("play2", 0);
            timeList.put("1", time);
            timeList.put("2", time);
            player1.setTimeRemain(time);
            player2.setTimeRemain(time);
            handleTimer("1", screen);
            history.addTimeRemain(0, time, time);
        }
        player1.reset();
        player2.reset();
        currentPlayer = player1.isWhite() ? player1 : player2;
    }

    public void finish() {
        if (timeType != TimeType.NO_TIME)
            timer.clear();
    }

    public boolean isDrawByInsufficientPieceAfterTimeout() {
        ChessPlayer chessPlayer = player1;
        if (currentPlayer.isWhite() == player1.isWhite()) {
            chessPlayer = player2;
        }
        if (chessPlayer.getPieceNumber(PieceType.QUEEN) == 0
            && chessPlayer.getPieceNumber(PieceType.ROOK) == 0) {
            int knightQuantity = chessPlayer.getPieceNumber(PieceType.KNIGHT);
            int bishopQuantity = chessPlayer.getPieceNumber(PieceType.BISHOP);
            return knightQuantity + bishopQuantity == 0 || knightQuantity + bishopQuantity == 1;
        }
        return false;
    }

    public boolean isDrawByInsufficientPiece() {
        if (player1.getPieceNumber(PieceType.QUEEN) == 0
            && player1.getPieceNumber(PieceType.ROOK) == 0
            && player2.getPieceNumber(PieceType.QUEEN) == 0
            && player2.getPieceNumber(PieceType.ROOK) == 0
            && player1.getPieceNumber(PieceType.PAWN) == 0
            && player2.getPieceNumber(PieceType.PAWN) == 0) {
            int bishopQuantity1 = player1.getPieceNumber(PieceType.BISHOP);
            int bishopQuantity2 = player2.getPieceNumber(PieceType.BISHOP);
            int knightQuantity1 = player1.getPieceNumber(PieceType.KNIGHT);
            int knightQuantity2 = player2.getPieceNumber(PieceType.KNIGHT);
            if ((knightQuantity1 + knightQuantity2 == 1 && bishopQuantity1 + bishopQuantity2 == 0)
                || (bishopQuantity1 + bishopQuantity2 == 1 && knightQuantity1 + knightQuantity2 == 0)) {
                return true;
            }else {
                return knightQuantity1 == 0 && knightQuantity2 == 0 && bishopQuantity1 == 0 && bishopQuantity2 == 0;
            }

        }
        return false;
    }

}
