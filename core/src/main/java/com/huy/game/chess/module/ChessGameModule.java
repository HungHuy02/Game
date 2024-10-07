package com.huy.game.chess.module;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.huy.game.chess.core.BoardSetting;
import com.huy.game.chess.manager.ChessGameAssesManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ChessGameModule {

    @Singleton
    @Provides
    public SpriteBatch getSpriteBatch() {
        return new SpriteBatch();
    }

    @Singleton
    @Provides
    public ChessGameAssesManager provideChessGameAssesManager() {
        ChessGameAssesManager manager = new ChessGameAssesManager();
        manager.loadAll();
        return manager;
    }

    @Singleton
    @Provides
    public BoardSetting provideBoardSetting() {
        return new BoardSetting();
    }

    @Singleton
    @Provides
    public BitmapFont getBitmapFont(ChessGameAssesManager manager) {
        return manager.getFont();
    }
}
