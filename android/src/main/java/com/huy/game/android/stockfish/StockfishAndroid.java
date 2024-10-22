package com.huy.game.android.stockfish;

import android.content.Context;

import com.huy.game.chess.interfaces.Stockfish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class StockfishAndroid implements Stockfish {

    private Process process;
    private Context context;

    public StockfishAndroid(Context context) {
        this.context = context;
        init();
    }

    @Override
    public void init() {
         try {
            process = Runtime.getRuntime().exec(context.getApplicationInfo().nativeLibraryDir+"/lib_stockfish.so");
            sendCommand("uci");
            sendCommand("setoption name Skill Level value 10");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendCommand(String command) {
        try {
            StringBuilder builder = new StringBuilder(command);
            builder.append("\n");
            Process ep = process;
            if(ep != null) {
                ep.getOutputStream().write((builder.toString()).getBytes());
                ep.getOutputStream().flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getResponse(Consumer<String> consumer) {
        new Thread(() -> {
            Process processOut = process;
            if(processOut == null){
                return;
            }
            BufferedReader out = new BufferedReader(new InputStreamReader(processOut.getInputStream()), 16384);
            String data;
            try{
                while( (data = out.readLine()) != null){
                    if(data.startsWith("bestmove")) {
                        String[] strings = data.split(" ");
                        if(strings[0].equals("bestmove"))
                        {
                            consumer.accept(strings[1]);
                            break;
                        }
                    }
                }
            } catch(IOException e){
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public void sendCommandAndGetResponse(String fen, int time, Consumer<String> consumer) {
        sendCommand(command("position fen", fen));
        sendCommand(command("go movetime", time));
        getResponse(consumer);
    }

    private String command(String startPart, Object endPart) {
        StringBuilder builder = new StringBuilder(startPart);
        builder.append(' ');
        builder.append(endPart);
        return builder.toString();
    }

    @Override
    public void destroy() {
        sendCommand("quit");
        process.destroy();
    }
}
