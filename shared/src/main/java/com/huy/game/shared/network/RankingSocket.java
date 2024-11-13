package com.huy.game.shared.network;

import java.net.URISyntaxException;

import Local.Local;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class RankingSocket {

    private Socket socket;

    public void connect() {
        try {
            socket = IO.socket(Local.SocketIORanking);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
    }

    public void newRankingAvailable(SimpleCallback callback) {
        on("newRanking", args -> {
            callback.onEvent();
        });
    }

    private void on(String event, Emitter.Listener func) {
        socket.on(event, func);
    }

    public void disconnect() {
        socket.disconnect();
    }
}
