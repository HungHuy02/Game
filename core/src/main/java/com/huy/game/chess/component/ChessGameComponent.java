package com.huy.game.chess.component;

import com.huy.game.Main;
import com.huy.game.chess.module.ChessGameModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    ChessGameModule.class
})
public interface ChessGameComponent {

    void inject(Main main);

    ChessGameScreenComponent.Factory chessGameScreenComponentFactory();
}
