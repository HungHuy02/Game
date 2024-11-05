package com.huy.game.shared.network;

import com.huy.game.chess.enums.GameResult;
import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.enums.TimeType;
import com.huy.game.chess.events.ChessGameOnlineEvent;
import com.huy.game.interfaces.SocketClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import Local.Local;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketIOClient implements SocketClient {

    private Socket socket;
    private final AuthToken authToken;

    public SocketIOClient(AuthToken authToken) {
        this.authToken = authToken;
    }

    @Override
    public void connect() {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("token", authToken.getCurrentAccessToken());
            IO.Options options = IO.Options.builder()
                .setAuth(map)
                .build();
            socket = IO.socket(Local.SocketIOUrl, options);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
        onConnect_error();
    }

    @Override
    public void guestConnect() {
        try {
            socket = IO.socket(Local.SocketIOGuestUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
    }

    private void onConnect_error() {
        on(Socket.EVENT_CONNECT_ERROR, args -> {
            if (args.length > 0 && args[0] instanceof JSONObject) {
                Exception e = (Exception) args[0];
                if (e.getMessage().startsWith("Authentication")) {
                    authToken.getNewAccessToken();
                    connect();
                }
            }
        });
    }

    @Override
    public void requestToPlayGame(String playerName, String imageUrl, int elo, TimeType timeType) {
        requestToPlay(playerName, imageUrl, elo, timeType);
        opponentFound();
    }

    private void requestToPlay(String playerName, String imageUrl, int elo, TimeType timeType) {
        try {
            socket.emit("request_to_play",
                new JSONObject().put("playerName", playerName)
                    .put("imageUrl", imageUrl)
                    .put("elo", elo)
                    .put("timeType", timeType));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void opponentFound() {
        on("opponentFound", args -> {
            if (args.length > 0 && args[0] instanceof JSONObject data) {
                try {
                    String name = data.getString("opponentName");
                    boolean isWhite = data.getBoolean("isWhite");
                    String imageUrl = null;
                    if (data.has("imageUrl")) {
                        imageUrl = data.getString("imageUrl");
                    }
                    ChessGameOnlineEvent.getInstance().notifySuccessfulMatch(name, isWhite, imageUrl);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void makeMove(String from, String to, MoveType type, int timeRemain) {
        try {
            emit("playerMove",
                new JSONObject()
                    .put("from", from)
                    .put("to", to)
                    .put("moveType", type.toString())
                    .put("timeRemain", timeRemain));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getMoveFromOpponent() {
        on("opponentMove", args -> {
            if (args.length > 0 && args[0] instanceof JSONObject data) {
                try {
                    String from = data.getString("from");
                    String to = data.getString("to");
                    MoveType type = MoveType.valueOf(data.getString("moveType"));
                    int timeRemain = data.getInt("timeRemain");
                    ChessGameOnlineEvent.getInstance().notifyPlayerMove(from, to, type, timeRemain);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void requestToDraw() {
        emit("canDraw", null);
    }

    @Override
    public void opponentWantToDraw() {
        on("opponentWantToDraw", args -> ChessGameOnlineEvent.getInstance().notifyPlayerWantToDraw());
    }

    @Override
    public void gameEnd(GameResult gameResult) {
        String result = switch (gameResult) {
            case DRAW -> "draw";
            case WHITE_WIN -> "white";
            case BLACK_WIN -> "black";
        };

        try {
            emit("gameEnd", new JSONObject().put("result", result));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void newScoreAfterGameEnd() {
        on("newScore", args -> {
            if (args.length > 0 && args[0] instanceof JSONObject data) {
                try {
                    int newScore = data.getInt("newScore");
                    ChessGameOnlineEvent.getInstance().notifyNewScore(newScore);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    private void emit(String event, JSONObject object) {
        socket.emit(event, object);
    }

    public void on(String event, Emitter.Listener func) {
        socket.on(event, func);
    }

    public void off (String event, Emitter.Listener func) {
        socket.off(event, func);
    }

    @Override
    public void disconnect() {
        socket.disconnect();
    }
}
