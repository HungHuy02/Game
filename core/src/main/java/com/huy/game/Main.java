package com.huy.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.huy.game.chess.ChessMatchScreen;
import com.huy.game.chess.WatchingHistoryScreen;
import com.huy.game.chess.component.ChessGameComponent;
import com.huy.game.chess.component.DaggerChessGameComponent;
import com.huy.game.chess.core.BoardSetting;
import com.huy.game.chess.ChessScreen;
import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.enums.TimeType;
import com.huy.game.chess.interfaces.LocalDatabase;
import com.huy.game.chess.interfaces.Stockfish;
import com.huy.game.chess.manager.ChessGameAssesManager;
import com.huy.game.chess.manager.Player;
import com.huy.game.interfaces.BackInterface;
import com.huy.game.interfaces.SocketClient;

import javax.inject.Inject;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    private ChessGameComponent component;
    public SocketClient socketClient;
    public Stockfish stockfish = null;
    public BackInterface backInterface;
    public LocalDatabase localDatabase;
    public String pgn = null;
    public boolean isWatching;

    @Inject
    ChessGameAssesManager manager;
    @Inject
    BoardSetting setting;
    @Inject
    BitmapFont font;
    @Inject
    SpriteBatch batch;

    private final ChessMode mode;
    private boolean isRotateBoard;
    public TimeType timeType;
    public boolean enableSuggesting = false;
    private boolean enableTakeback = false;

    public Main(ChessMode mode,
                TimeType timeType,
                boolean isRotateBoard,
                BackInterface backInterface,
                LocalDatabase localDatabase,
                boolean isWatching) {
        this.mode = mode;
        this.timeType = timeType;
        this.isRotateBoard = isRotateBoard;
        this.backInterface = backInterface;
        this.localDatabase = localDatabase;
        this.isWatching = isWatching;
    }

    public Main(ChessMode mode,
                TimeType timeType,
                SocketClient client,
                BackInterface backInterface,
                boolean isGuest,
                LocalDatabase localDatabase,
                boolean isWatching) {
        this.mode = mode;
        this.timeType = timeType;
        this.socketClient = client;
        this.backInterface = backInterface;
        this.localDatabase = localDatabase;
        this.isWatching = isWatching;
        if (isGuest) {
            client.guestConnect();
        }else {
            client.connect();
        }
    }

    public Main(ChessMode mode,
                boolean enableSuggesting,
                boolean enableTakeback,
                TimeType timeType,
                Stockfish stockfish,
                BackInterface backInterface,
                LocalDatabase localDatabase,
                boolean isWatching) {
        this.mode = mode;
        this.timeType = timeType;
        this.stockfish = stockfish;
        this.enableSuggesting = enableSuggesting;
        this.enableTakeback = enableTakeback;
        this.backInterface = backInterface;
        this.localDatabase = localDatabase;
        this.isWatching = isWatching;
    }

    public ChessMode getMode() {
        return mode;
    }

    @Override
    public void create() {
        component = DaggerChessGameComponent.create();
        component.inject(this);
        manager.loadAll();
        setSetting();
        if (isWatching) {
            if (Player.getInstance().isWhite()) {
                setting.setRotate(true);
            }
            toWatchingHistoryScreen();
        }else {
            switch (mode) {
                case AI, TWO_PERSONS -> toChessScreen();
                case ONLINE -> toChessMatchScreen();
            }
        }
    }

    private void setSetting() {
        switch (mode) {
            case ONLINE ->
                setting.setShowGuidePoint(false);
            case AI -> {
                manager.loadAIImage();
                setting.setSuggestMove(enableSuggesting);
                setting.setCanBack(enableTakeback);
            }
            case TWO_PERSONS -> {
                if (isRotateBoard) {
                    setting.setAutoRotate(true);
                }else {
                    setting.setReverseOneSide(true);
                }
            }
        }
    }

    public void setPGN(String pgn) {
        this.pgn = pgn;
    }

    public void toChessScreen() {
        ChessScreen chessScreen = new ChessScreen(this);
        component.chessGameScreenComponentFactory().create().inject(chessScreen);
        setScreen(chessScreen);
    }

    public void toChessMatchScreen() {
        ChessMatchScreen chessMatchScreen = new ChessMatchScreen(this);
        component.chessGameScreenComponentFactory().create().inject(chessMatchScreen);
        setScreen(chessMatchScreen);
    }

    public void toWatchingHistoryScreen() {
        WatchingHistoryScreen watchingHistoryScreen = new WatchingHistoryScreen(this);
        component.chessGameScreenComponentFactory().create().inject(watchingHistoryScreen);
        setScreen(watchingHistoryScreen);
    }

    @Override
    public void dispose() {
        super.dispose();
        manager.dispose();
        switch (mode) {
            case AI -> stockfish.destroy();
            case ONLINE -> socketClient.disconnect();
        }
    }
}
