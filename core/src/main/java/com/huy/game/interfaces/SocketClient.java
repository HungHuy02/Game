package com.huy.game.interfaces;

import com.huy.game.chess.enums.GameResult;
import com.huy.game.chess.enums.MoveType;

public interface SocketClient {
    void connect();
    void requestToPlayGame(String playerName, String imageUrl,int elo);
    void makeMove(String from, String to, MoveType type);
    void getMoveFromOpponent();
    void requestToDraw();
    void opponentWantToDraw();
    void gameEnd(GameResult gameResult);
    void newScoreAfterGameEnd();
    void disconnect();
}
