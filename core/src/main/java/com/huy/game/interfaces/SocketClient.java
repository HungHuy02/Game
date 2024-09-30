package com.huy.game.interfaces;

public interface SocketClient {
    void connect();
    void requestToPlay();
    void makeMove(String from, String to);
    void getMoveFromOpponent();
    void disconnect();
}
