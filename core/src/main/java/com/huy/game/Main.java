package com.huy.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.huy.game.chess.ChessMatchScreen;
import com.huy.game.chess.component.ChessGameComponent;
import com.huy.game.chess.component.DaggerChessGameComponent;
import com.huy.game.chess.core.BoardSetting;
import com.huy.game.chess.ChessScreen;
import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.interfaces.Stockfish;
import com.huy.game.chess.manager.ChessGameAssesManager;
import com.huy.game.interfaces.SocketClient;

import javax.inject.Inject;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    private ChessGameComponent component;
    public SocketClient socketClient;
    public Stockfish stockfish = null;

    @Inject
    ChessGameAssesManager manager;
    @Inject
    BoardSetting setting;
    @Inject
    BitmapFont font;
    @Inject
    SpriteBatch batch;

    private ChessMode mode;

    public Main(ChessMode mode) {
        this.mode = mode;
    }

    public Main(ChessMode mode, SocketClient client) {
        this.mode = mode;
        this.socketClient = client;
        client.connect();
    }

    public Main(ChessMode mode, Stockfish stockfish) {
        this.mode = mode;
        this.stockfish = stockfish;
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
        switch (mode) {
            case AI, TWO_PERSONS:
                toChessScreen();
                break;
            case ONLINE:
                toChessMatchScreen();
                break;
        }
    }

    private void setSetting() {
        switch (mode) {
            case AI, ONLINE:
                setting.setShowGuidePoint(false);
                break;
            case TWO_PERSONS:
                setting.setRotate(true);
                break;
        }
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

    public void disposeCurrentScreen() {
        if(getScreen() != null) {
            getScreen().dispose();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        manager.dispose();
    }
}
