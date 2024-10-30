package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.huy.game.Main;
import com.huy.game.chess.manager.ChessImage;
import com.huy.game.interfaces.BackInterface;

public class TopAppBar {

    private Stack stack;

    public TopAppBar(ChessImage chessImage, BitmapFont font, Main main, I18NBundle bundle) {
        setStack(chessImage, font, main, bundle);
    }

    public Stack getStack() {
        return stack;
    }

    private void setStack(ChessImage chessImage, BitmapFont font, Main main, I18NBundle bundle) {
        stack = new Stack();
        stack.setSize(Gdx.graphics.getWidth(), 100f);
        stack.setPosition(0, Gdx.graphics.getHeight() - 100f);
        stack.add(backgroundContainer(chessImage));
        stack.add(name(font, bundle));
        stack.add(backArrow(chessImage, main));
    }

    private Container<Image> backgroundContainer(ChessImage chessImage) {
        Container<Image> backgroundContainer = new Container<>(chessImage.getAppbarBackground());
        backgroundContainer.setSize(Gdx.graphics.getWidth(), 100f);
        return backgroundContainer;
    }

    private Container<Image> backArrow(ChessImage chessImage, Main main) {
        Container<Image> backArrow = new Container<>(chessImage.getBackArrow());
        backArrow.padLeft(16);
        backArrow.setFillParent(true);
        backArrow.align(Align.left);
        backArrow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                main.backInterface.back();
                main.disposeCurrentScreen();
            }
        });
        return backArrow;
    }

    private Label name(BitmapFont font, I18NBundle bundle) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        Label label = new Label(bundle.get("name"), labelStyle);
        label.setFontScale(1.5f);
        label.setFillParent(true);
        label.setAlignment(Align.center);
        return label;
    }

}
