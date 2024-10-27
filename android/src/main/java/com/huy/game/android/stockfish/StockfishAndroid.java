package com.huy.game.android.stockfish;

import android.content.Context;
import android.util.Log;

import com.huy.game.chess.interfaces.Stockfish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class StockfishAndroid implements Stockfish {

    private Process process;
    private Context context;
    private int depth;
    private int time;

    public StockfishAndroid(Context context) {
        this.context = context;
        init();
    }

    public StockfishAndroid(Context context, int level) {
        this.context = context;
        init();
        setupStockfish(level);
    }

    @Override
    public void init() {
         try {
            process = Runtime.getRuntime().exec(context.getApplicationInfo().nativeLibraryDir+"/lib_stockfish.so");
            sendCommand("uci");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupStockfish(int level) {
        int skillLevel = switch (level) {
            case 1 -> {
                depth = 5;
                time = 50;
                yield  -9;
            }
            case 2 -> {
                depth = 5;
                time = 100;
                yield  -5;
            }
            case 3 -> {
                depth = 5;
                time = 150;
                yield  -1;
            }
            case 4 -> {
                depth = 5;
                time = 200;
                yield  3;
            }
            case 5 -> {
                depth = 5;
                time = 300;
                yield 7;
            }
            case 6 -> {
                depth = 8;
                time = 400;
                yield 11;
            }
            case 7 -> {
                depth = 13;
                time = 500;
                yield 16;
            }
            case 8 -> {
                depth = 22;
                time = 1000;
                yield 20;
            }
            default -> throw new IllegalStateException("Unexpected value: " + level);
        };
        StringBuilder builder = new StringBuilder();
        builder.append("setoption name Skill Level value ");
        builder.append(skillLevel);
        sendCommand(builder.toString());
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

    @Override
    public void findBestMove(String fen, Consumer<String> consumer) {
        sendCommand(command("position fen", fen));
        StringBuilder builder = new StringBuilder();
        builder.append("go depth ");
        builder.append(depth);
        builder.append(" movetime ");
        builder.append(time);
        sendCommand(builder.toString());
        getResponse(consumer);
    }


    public void getResponse(Consumer<String> consumer) {
        Process processOut = process;
        if(processOut == null){
            return;
        }
        BufferedReader out = new BufferedReader(new InputStreamReader(processOut.getInputStream()), 16384);
        String data;
        try{
            long lastReceivedTime = System.currentTimeMillis();
            loop:
            while(true) {
                while( (data = out.readLine()) != null){
                    Log.e("test", data);
                    if(data.startsWith("bestmove")) {
                        String[] strings = data.split(" ");
                        if(strings[0].equals("bestmove"))
                        {
                            consumer.accept(strings[1]);
                            break loop;
                        }
                    }
                    lastReceivedTime = System.currentTimeMillis();
                }
                if (System.currentTimeMillis() - lastReceivedTime > 2000) {
                    break;
                }
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }
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
