package com.huy.game.android.stockfish;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StockfishAndroid {

    private Process process;

    public StockfishAndroid(Context context) {
        init(context);
    }

    public void init(Context context) {
         try {
            process = Runtime.getRuntime().exec(context.getApplicationInfo().nativeLibraryDir+"/lib_stockfish.so");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCommand(String command) {
        try {
            Process ep = process;
            if(ep != null) {
                ep.getOutputStream().write((command + "\n").getBytes());
                ep.getOutputStream().flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getResponse() {
        Thread outThread =  new Thread(() -> {
            Process processOut = process;
            if(processOut == null){
                return;
            }


            BufferedReader out = new BufferedReader(new InputStreamReader(processOut.getInputStream()));

            String data;
            try{
                while( (data = out.readLine()) != null ){
                    Log.e("test", data);
                }


            } catch(IOException e){

            }

        });

        outThread.start();
    }

    public void close() {
        process.destroy();
    }
}
