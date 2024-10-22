package com.huy.game.interfaces;

import com.huy.game.chess.enums.MoveType;

public interface SocketClient {
    void connect();
    void requestToPlayGame(String playerName);
    void makeMove(String from, String to, MoveType type);
    void getMoveFromOpponent();
    void disconnect();
}
