package com.huy.game.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

public class TopAppBar {

    private Stage stage;
    private HorizontalGroup appbar;
    private Stack stack;

    public TopAppBar(ChessImage chessImage) {
        setAppbar(chessImage);
        setStack(chessImage);
        setStage();
    }

    public void setStage() {
        stage = new Stage();
        stage.addActor(stack);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStack(ChessImage chessImage) {
        stack = new Stack();
        stack.setSize(Gdx.graphics.getWidth(), 100f);
        stack.setPosition(0, Gdx.graphics.getHeight() - appbar.getHeight());
        stack.add(backgroundContainer(chessImage));
        stack.add(appbar);
    }

    public void setAppbar(ChessImage chessImage) {
        appbar = new HorizontalGroup();
        appbar.setSize(Gdx.graphics.getWidth(), 100f);
        appbar.addActor(backArrow(chessImage));
    }

    public Container<Image> backgroundContainer(ChessImage chessImage) {
        Container<Image> backgroundContainer = new Container<>(chessImage.getAppbarBackground());
        backgroundContainer.setSize(Gdx.graphics.getWidth(), 100f);
        return backgroundContainer;
    }

    public Container<Image> backArrow(ChessImage chessImage) {
        Container<Image> backArrow = new Container<>(chessImage.getBackArrow());
        backArrow.padLeft(16);
        return backArrow;
    }
}
