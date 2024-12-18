package com.huy.game.android.view;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.huy.game.Main;
import com.huy.game.android.globalstate.UserState;
import com.huy.game.android.network.socketio.AuthToken;
import com.huy.game.android.roomdatabase.repository.HistoryRepository;
import com.huy.game.android.stockfish.StockfishAndroid;
import com.huy.game.android.utils.Constants;
import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.enums.PieceColor;
import com.huy.game.chess.enums.TimeType;
import com.huy.game.chess.manager.OpponentPlayer;
import com.huy.game.chess.manager.Player;
import com.huy.game.interfaces.BackInterface;
import com.huy.game.shared.network.SocketIOClient;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication implements BackInterface {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Main main = null;
        Intent intent = getIntent();
        ChessMode mode =  ChessMode.valueOf(intent.getStringExtra(Constants.BUNDLE_MODE));
        TimeType type = TimeType.valueOf(intent.getStringExtra(Constants.BUNDLE_TIME));
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true;
        boolean isWatching = intent.getBooleanExtra(Constants.BUNDLE_WATCHING_HISTORY, false);
        switch (mode) {
            case AI, TWO_PERSONS -> {
                PieceColor player1Color = PieceColor.valueOf(intent.getStringExtra(Constants.BUNDLE_PLAYER1_COLOR));
                String player1Name = intent.getStringExtra(Constants.BUNDLE_PLAYER1_NAME);
                String player2Name = intent.getStringExtra(Constants.BUNDLE_PLAYER2_NAME);
                boolean isRotateBoard = intent.getBooleanExtra(Constants.BUNDLE_ROTATE_BOARD, false);
                Player.getInstance().setData(player1Name, player1Color == PieceColor.WHITE, UserState.getInstance().getImageUrl(), UserState.getInstance().getElo());
                OpponentPlayer.getInstance().setData(player2Name, !(player1Color == PieceColor.WHITE));
                if(mode == ChessMode.AI) {
                    boolean enableSuggesting = intent.getBooleanExtra(Constants.BUNDLE_ENABLE_SUGGEST, false);
                    boolean enableTakeback = intent.getBooleanExtra(Constants.BUNDLE_ENABLE_TAKEBACK, false);
                    int level = intent.getIntExtra(Constants.BUNDLE_AI_LEVEL, 1);
                    main = new Main(mode, enableSuggesting, enableTakeback, type, new StockfishAndroid(getApplicationContext(), level, enableSuggesting), this, new HistoryRepository(), isWatching);
                }else {
                    main = new Main(mode, type, isRotateBoard, this, new HistoryRepository(), isWatching);
                }
            }
            case ONLINE -> {
                String player1Name = intent.getStringExtra(Constants.BUNDLE_PLAYER1_NAME);
                if (UserState.getInstance().isGuest()) {
                    Player.getInstance().setData(UserState.getInstance().getName());
                }else {
                    Player.getInstance().setData(player1Name, UserState.getInstance().getImageUrl(), UserState.getInstance().getElo());
                }
                main = new Main(mode, type, new SocketIOClient(new AuthToken(getApplicationContext())), this, UserState.getInstance().isGuest(), new HistoryRepository(), isWatching);
            }
        }
        String pgn = intent.getStringExtra(Constants.BUNDLE_PGN);
        main.setPGN(pgn);
        initialize(main, configuration);
    }

    @Override
    public void back() {
        finish();
    }

    @Override
    public void newGame(ChessMode mode) {
        switch (mode) {
            case AI -> {
                Intent intent = new Intent(this, PlayWithAISetupActivity.class);
                startActivity(intent);
            }
            case TWO_PERSONS -> {
                Intent intent = new Intent(this, TwoPersonsPlaySetupActivity.class);
                startActivity(intent);
            }
        }
        finish();
    }
}
