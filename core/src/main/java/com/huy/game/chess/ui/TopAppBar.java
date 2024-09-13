package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TopAppBar {

    private HorizontalGroup appbar;
    private Stack stack;

    public TopAppBar(ChessImage chessImage) {
        setAppbar(chessImage);
        setStack(chessImage);
    }

    public Stack getStack() {
        return stack;
    }

    private void setStack(ChessImage chessImage) {
        stack = new Stack();
        stack.setSize(Gdx.graphics.getWidth(), 100f);
        stack.setPosition(0, Gdx.graphics.getHeight() - appbar.getHeight());
        stack.add(backgroundContainer(chessImage));
        stack.add(appbar);
    }

    private void setAppbar(ChessImage chessImage) {
        appbar = new HorizontalGroup();
        appbar.setSize(Gdx.graphics.getWidth(), 100f);
        appbar.addActor(backArrow(chessImage));
    }

    private Container<Image> backgroundContainer(ChessImage chessImage) {
        Container<Image> backgroundContainer = new Container<>(chessImage.getAppbarBackground());
        backgroundContainer.setSize(Gdx.graphics.getWidth(), 100f);
        return backgroundContainer;
    }

    private Container<Image> backArrow(ChessImage chessImage) {
        Container<Image> backArrow = new Container<>(chessImage.getBackArrow());
        backArrow.padLeft(16);
        backArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });
        return backArrow;
    }


}
