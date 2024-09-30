package com.huy.game.shared.network;

import com.huy.game.chess.events.ChessGameOnlineEvent;
import com.huy.game.interfaces.SocketClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import Local.Local;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketIOClient implements SocketClient {

    private Socket socket;

    @Override
    public void connect() {
        try {
            socket = IO.socket(Local.SocketIOUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();

    }

    @Override
    public void requestToPlay() {
        requestToPlay("HUY");
        opponentFound();
    }

    private void requestToPlay(String playerName) {
        try {
            socket.emit("request_to_play", new JSONObject().put("playerName", playerName));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void opponentFound() {
        on("opponentFound", args -> {
            if (args.length > 0 && args[0] instanceof JSONObject) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String name = data.getString("opponentName");
                    boolean isWhite = data.getBoolean("isWhite");
                    ChessGameOnlineEvent.getInstance().notifySuccessfulMatch(name, isWhite);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void makeMove(String from, String to) {
        try {
            emit("playerMove", new JSONObject().put("from", from).put("to", to));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getMoveFromOpponent() {
        on("opponentMove", args -> {
            if (args.length > 0 && args[0] instanceof JSONObject) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String from = data.getString("from");
                    String to = data.getString("to");
                    ChessGameOnlineEvent.getInstance().notifyPlayerMove(from, to);
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
