package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class TopAppBar {

    private Stack stack;

    public TopAppBar(ChessImage chessImage, BitmapFont font) {
        setStack(chessImage, font);
    }

    public Stack getStack() {
        return stack;
    }

    private void setStack(ChessImage chessImage, BitmapFont font) {
        stack = new Stack();
        stack.setSize(Gdx.graphics.getWidth(), 100f);
        stack.setPosition(0, Gdx.graphics.getHeight() - 100f);
        stack.add(backgroundContainer(chessImage));
        stack.add(name(font));
        stack.add(backArrow(chessImage));
    }

    private Container<Image> backgroundContainer(ChessImage chessImage) {
        Container<Image> backgroundContainer = new Container<>(chessImage.getAppbarBackground());
        backgroundContainer.setSize(Gdx.graphics.getWidth(), 100f);
        return backgroundContainer;
    }

    private Container<Image> backArrow(ChessImage chessImage) {
        Container<Image> backArrow = new Container<>(chessImage.getBackArrow());
        backArrow.padLeft(16);
        backArrow.setFillParent(true);
        backArrow.align(Align.left);
        backArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.input.setInputProcessor(new InputAdapter() {
                    @Override
                    public boolean keyDown(int keycode) {
                        return keycode == Input.Keys.BACK;
                    }
                });
            }
        });
        return backArrow;
    }

    private Label name(BitmapFont font) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        Label label = new Label("Chess", labelStyle);
        label.setFontScale(1.5f);
        label.setFillParent(true);
        label.setAlignment(Align.center);
        return label;
    }

}
