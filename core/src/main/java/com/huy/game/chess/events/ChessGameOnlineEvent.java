package com.huy.game.chess.events;

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
        void onSuccessfulMatch(String name, boolean isWhite);
    };

    public interface PlayerMoveListener {
        void onPlayerMove(String from, String to);
    }

    private MatchListener listener;

    private PlayerMoveListener playerMoveListener;

    public void setMatchListener(MatchListener listener) {
        this.listener = listener;
    }

    public void setPlayerMoveListener(PlayerMoveListener listener) {
        this.playerMoveListener = listener;
    }

    public void notifySuccessfulMatch(String name, boolean isWhite) {
        listener.onSuccessfulMatch(name, isWhite);
    }

    public void notifyPlayerMove(String from, String to) {
        playerMoveListener.onPlayerMove(from, to);
    }
}