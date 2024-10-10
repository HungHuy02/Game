package com.huy.game.chess.interfaces;

import java.util.function.Consumer;

public interface Stockfish {

    void init();
    void sendCommand(String command);
    void sendCommandAndGetResponse(String fen, int time, Consumer<String> consumer);
    void destroy();
}
