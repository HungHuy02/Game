package com.huy.game;

import com.badlogic.gdx.Game;
import com.huy.game.chess.core.BoardSetting;
import com.huy.game.chess.ChessScreen;
import com.huy.game.chess.enums.ChessMode;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    private ChessMode mode;
    private BoardSetting setting;

    public Main(ChessMode mode) {
        this.mode = mode;
        setSetting();
    }

    private void setSetting() {
        setting = new BoardSetting();
        switch (mode) {
            case AI:
                setting.setShowGuidePoint(false);
                break;
            case ONLINE:
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
        setScreen(new FirstScreen(this));
    }

    public void toChessScreen() {
        disposeCurrentScreen();
        setScreen(new ChessScreen(this));
    }

    public void disposeCurrentScreen() {
        if(getScreen() != null) {
            getScreen().dispose();
        }
    }
}
