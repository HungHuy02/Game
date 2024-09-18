package com.huy.game.chess.manager;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.Piece;
import com.huy.game.chess.core.Spot;
import com.huy.game.chess.ui.Colors;

import java.util.HashMap;
import java.util.Map;

public class ChessGameManager {

    private ChessPlayer player1;
    private ChessPlayer player2;
    private ChessPlayer currentPlayer;
    private Timer timer;
    private Label player1Timer;
    private Label player2Timer;

    public ChessGameManager() {
        player1 = new ChessPlayer(true);
        player2 = new ChessPlayer(false);
        currentPlayer = player1;
        timer = new Timer();
    }

    public void setLabel(Label player1Timer, Label player2Timer) {
        this.player1Timer = player1Timer;
        this.player2Timer = player2Timer;
    }


    public void switchPlayer(Board board) {
        if(!board.isPromoting()) {
            cancelTimer();
            if(currentPlayer == player1) {
                currentPlayer = player2;
                handleTimer(player2Timer);
            }else {
                currentPlayer = player1;
                handleTimer(player1Timer);
            }
            currentPlayer.increaseTurn();
        }
    }

    public void putValue(Piece piece) {
        currentPlayer.putValue(piece);
        int value = player1.getValue() - player2.getValue();
        if(value > 0) {
            player1.putValue(value);
        }else {
            player2.putValue(Math.abs(value));
        }
    }

    public void handleTimer(Label label) {
        Timer.Task task = new Timer.Task() {
            int remainingTime = currentPlayer.getTimeRemain();

            @Override
            public void run() {
                if (remainingTime > 0) {
                    int minutes = remainingTime / 60;
                    int remainSeconds = remainingTime % 60;
                    label.setText(String.format("%d:%02d", minutes, remainSeconds));
                    remainingTime--;
                    currentPlayer.setTimeRemain(remainingTime);
                } else {
                    this.cancel();
                    label.setText("0:00");
                }
            }
        };
        timer.scheduleTask(task, 0, 1);
    }

    public void cancelTimer() {
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
