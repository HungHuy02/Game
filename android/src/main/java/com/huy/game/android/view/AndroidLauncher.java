package com.huy.game.android.view;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.huy.game.Main;
import com.huy.game.android.stockfish.StockfishAndroid;
import com.huy.game.android.utils.Constants;
import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.enums.PieceColor;
import com.huy.game.chess.enums.TimeType;
import com.huy.game.chess.manager.OpponentPlayer;
import com.huy.game.chess.manager.Player;
import com.huy.game.shared.network.SocketIOClient;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        ChessMode mode =  ChessMode.valueOf(intent.getStringExtra(Constants.BUNDLE_MODE));
        TimeType type = TimeType.valueOf(intent.getStringExtra(Constants.BUNDLE_TIME));
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true;
        switch (mode) {
            case AI, TWO_PERSONS -> {
                PieceColor player1Color = PieceColor.valueOf(intent.getStringExtra(Constants.BUNDLE_PLAYER1_COLOR));
                String player1Name = intent.getStringExtra(Constants.BUNDLE_PLAYER1_NAME);
                String player2Name = intent.getStringExtra(Constants.BUNDLE_PLAYER2_NAME);
                Player.getInstance().setData(player1Name, player1Color == PieceColor.WHITE);
                OpponentPlayer.getInstance().setData(player2Name, !(player1Color == PieceColor.WHITE));
                if(mode == ChessMode.AI) {
                    initialize(new Main(mode, new StockfishAndroid(getApplicationContext())), configuration);
                }else {
                    initialize(new Main(mode), configuration);
                }
            }
            case ONLINE -> {
                String player1Name = intent.getStringExtra(Constants.BUNDLE_PLAYER1_NAME);
                Player.getInstance().setName(player1Name);
                initialize(new Main(mode, new SocketIOClient()), configuration);
            }
        }
    }
}
