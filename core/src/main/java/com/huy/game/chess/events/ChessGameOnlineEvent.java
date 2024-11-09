package com.huy.game.chess.events;

import com.huy.game.chess.enums.MoveType;

public class ChessGameOnlineEvent {

    private static ChessGameOnlineEvent Instance;

    private ChessGameOnlineEvent() {

    }

    public static ChessGameOnlineEvent getInstance() {
        if(Instance == null) {
            Instance = new ChessGameOnlineEvent();
        }
        return Instance;
    }

    public interface MatchListener {
        void onSuccessfulMatch(String name, boolean isWhite, String imageUrl);
        void onArePlaying();
    }

    public interface PlayerActionListener {
        void onPlayerMove(String from, String to, MoveType type, int timeRemain);
        void onPlayerWantToDraw();
        void onNewScore(int newScore);
        void onOpponentLeftMatch();
        void onOpponentComeback();
        void currentGameState(String fen, int elo, String move, int playerTime, int opponentTime);
    }

    private MatchListener listener;

    private PlayerActionListener playerActionListener;

    public void setMatchListener(MatchListener listener) {
        this.listener = listener;
    }

    public void setPlayerMoveListener(PlayerActionListener listener) {
        this.playerActionListener = listener;
    }

    public void notifySuccessfulMatch(String name, boolean isWhite, String imageUrl) {
        listener.onSuccessfulMatch(name, isWhite, imageUrl);
    }


    public void notifyArePlaying() {
        listener.onArePlaying();
    }

    public void notifyPlayerMove(String from, String to, MoveType type, int timeRemain) {
        playerActionListener.onPlayerMove(from, to, type, timeRemain);
    }

    public void notifyPlayerWantToDraw() {
        playerActionListener.onPlayerWantToDraw();
    }

    public void notifyNewScore(int newScore) {
        playerActionListener.onNewScore(newScore);
    }

    public void notifyOpponentLeftMatch() {
        playerActionListener.onOpponentLeftMatch();
    }

    public void notifyOpponentComeback() {
        playerActionListener.onOpponentComeback();
    }

    public void notifyCurrentGameState(String fen, int elo, String move, int playerTime, int opponentTime) {
        playerActionListener.currentGameState(fen, elo, move, playerTime, opponentTime);
    }
}
