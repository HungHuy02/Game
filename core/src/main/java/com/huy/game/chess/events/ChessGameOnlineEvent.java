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
    }

    public interface PlayerActionListener {
        void onPlayerMove(String from, String to, MoveType type, int timeRemain);
        void onPlayerWantToDraw();
        void onNewScore(int newScore);
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

    public void notifyPlayerMove(String from, String to, MoveType type, int timeRemain) {
        playerActionListener.onPlayerMove(from, to, type, timeRemain);
    }

    public void notifyPlayerWantToDraw() {
        playerActionListener.onPlayerWantToDraw();
    }

    public void notifyNewScore(int newScore) {
        playerActionListener.onNewScore(newScore);
    }
}
