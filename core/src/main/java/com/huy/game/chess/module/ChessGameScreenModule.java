package com.huy.game.chess.module;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.I18NBundle;
import com.huy.game.chess.manager.ChessGameAssesManager;
import com.huy.game.chess.manager.ChessImage;
import com.huy.game.chess.manager.ChessSound;
import com.huy.game.chess.module.scope.Screen;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ChessGameScreenModule {

    @Screen
    @Provides
    public I18NBundle getI18NBundle(ChessGameAssesManager manager) {
        return  manager.getBundle("");
    }

    @Screen
    @Provides
    public ChessImage getChessImage(ChessGameAssesManager manager) {
        return new ChessImage(manager);
    }

    @Screen
    @Provides
    public ChessSound getChessSound(ChessGameAssesManager manager) {
        return new ChessSound(manager);
    }

}
