package com.huy.game.chess;

public class ChessGameManager {

    private ChessPlayer player1;
    private ChessPlayer player2;
    private ChessPlayer currentPlayer;

    public ChessGameManager() {
        player1 = new ChessPlayer(true);
        player2 = new ChessPlayer(false);
        currentPlayer = player1;
    }

    public void switchPlayer() {
        if(currentPlayer == player1) {
            currentPlayer = player2;
        }else {
            currentPlayer = player1;
        }
        currentPlayer.increaseTurn();
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
