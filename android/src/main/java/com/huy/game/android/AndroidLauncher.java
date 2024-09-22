package com.huy.game.android;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.huy.game.Main;
import com.huy.game.android.utils.Constants;
import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.enums.PieceColor;
import com.huy.game.chess.enums.TimeType;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        ChessMode mode =  ChessMode.valueOf(intent.getStringExtra(Constants.BUNDLE_MODE));
        TimeType type = TimeType.valueOf(intent.getStringExtra(Constants.BUNDLE_TIME));
        switch (mode) {
            case AI, TWO_PERSONS -> {
                PieceColor player1Color = PieceColor.valueOf(intent.getStringExtra(Constants.BUNDLE_PLAYER1_COLOR));
                String player1Name = intent.getStringExtra(Constants.BUNDLE_PLAYER1_NAME);
                String player2Name = intent.getStringExtra(Constants.BUNDLE_PLAYER2_NAME);
            }
            case ONLINE -> {
                String player1Name = intent.getStringExtra(Constants.BUNDLE_PLAYER1_NAME);
            }
        }

        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.
        initialize(new Main(mode), configuration);
    }
}
