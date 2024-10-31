package com.huy.game.chess.interfaces;

import java.util.function.Consumer;

public interface Stockfish {

    void setupNewGame();
    void sendCommand(String command);
    void findBestMove(String fen, Consumer<String> consumer);
    void suggestiveMove(String fen, Consumer<String> consumer);
    void destroy();
}
