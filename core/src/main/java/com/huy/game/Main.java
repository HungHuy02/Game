package com.huy.game;

import com.badlogic.gdx.Game;
import com.huy.game.chess.ChessScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override
    public void create() {
        setScreen(new FirstScreen(this));
    }

    public void toChessScreen() {
        disposeCurrentScreen();
        setScreen(new ChessScreen());
    }

    public void disposeCurrentScreen() {
        if(getScreen() != null) {
            getScreen().dispose();
        }
    }
}
