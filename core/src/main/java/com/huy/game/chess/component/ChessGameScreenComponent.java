package com.huy.game.chess.component;

import com.huy.game.chess.ChessMatchScreen;
import com.huy.game.chess.ChessScreen;
import com.huy.game.chess.WatchingHistoryScreen;
import com.huy.game.chess.module.ChessGameModule;
import com.huy.game.chess.module.ChessGameScreenModule;
import com.huy.game.chess.module.scope.Screen;

import javax.inject.Singleton;

import dagger.Subcomponent;

@Screen
@Subcomponent(modules = {
    ChessGameScreenModule.class
})
public interface ChessGameScreenComponent {

    void inject(ChessMatchScreen screen);
    void inject(ChessScreen screen);
    void inject(WatchingHistoryScreen screen);

    @Subcomponent.Factory
    interface Factory{
        ChessGameScreenComponent create();
    }
}
