package com.huy.game;

import com.badlogic.gdx.Game;
import com.huy.game.chess.ChessMatchScreen;
import com.huy.game.chess.core.BoardSetting;
import com.huy.game.chess.ChessScreen;
import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.manager.ChessGameAssesManager;
import com.huy.game.interfaces.SocketClient;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public SocketClient socketClient;
    public ChessGameAssesManager manager;
    private ChessMode mode;
    public BoardSetting setting;

    public Main(ChessMode mode) {
        manager = new ChessGameAssesManager();
        this.mode = mode;
        setSetting();
    }

    public Main(ChessMode mode, SocketClient client) {
        manager = new ChessGameAssesManager();
        this.mode = mode;
        this.socketClient = client;
        client.connect();
        setSetting();
    }

    private void setSetting() {
        setting = new BoardSetting();
        switch (mode) {
            case AI, ONLINE:
                setting.setShowGuidePoint(false);
                break;
            case TWO_PERSONS:
                setting.setRotate(true);
                break;
        }
    }

    public ChessMode getMode() {
        return mode;
    }

    @Override
    public void create() {
        manager.loadAll();
        switch (mode) {
            case AI, TWO_PERSONS:
                setScreen(new ChessScreen(this));
                break;
            case ONLINE:
                setScreen(new ChessMatchScreen(this));
                break;
        }

    }

    public void toChessScreen() {
        setScreen(new ChessScreen(this));
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
