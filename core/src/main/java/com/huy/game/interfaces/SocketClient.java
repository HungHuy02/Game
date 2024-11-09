package com.huy.game.interfaces;

import com.huy.game.chess.enums.GameResult;
import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.enums.TimeType;

public interface SocketClient {
    void connect();
    void guestConnect();
    void requestToPlayGame(String playerName, String imageUrl, int elo, TimeType timeType);
    void makeMove(String from, String to, MoveType type, int timeRemain);
    void getMoveFromOpponent();
    void requestToDraw();
    void opponentWantToDraw();
    void gameEnd(GameResult gameResult);
    void newScoreAfterGameEnd();
    void opponentLeftMatch();
    void opponentComeback();
    void sendCurrentGameState(String fen, int elo, String move, int playerTime, int opponentTime);
    void currentGameState();
    void arePlaying();
    void disconnect();
}
