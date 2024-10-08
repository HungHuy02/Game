package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.huy.game.chess.manager.ChessGameAssesManager;

public class EndGamePopup {

    private Window endGamePopup;

    public EndGamePopup(BitmapFont font, I18NBundle bundle, ChessGameAssesManager manager, boolean isWhite) {
        setEndGamePopup(font, bundle, manager, isWhite);
    }

    public Window getPopup() {
        return endGamePopup;
    }

    private void setEndGamePopup(BitmapFont font, I18NBundle bundle, ChessGameAssesManager manager, boolean isWhite) {
        Skin skin = manager.getSkin();
        TextButton.TextButtonStyle style = skin.get(TextButton.TextButtonStyle.class);
        style.font = font;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        endGamePopup = new Window("", skin);
        endGamePopup.setSize(Gdx.graphics.getWidth() - 100, 364);
        endGamePopup.setPosition((Gdx.graphics.getWidth() - endGamePopup.getWidth()) / 2,
            (Gdx.graphics.getHeight() - endGamePopup.getHeight()) / 2);
        endGamePopup.add(stack(manager, labelStyle,bundle)).fillX().expandX().height(100).padTop(32);
        endGamePopup.row();
        endGamePopup.add(winText(labelStyle, bundle, isWhite)).center().height(100);
        endGamePopup.row();
        Table buttonTable = new Table();
        buttonTable.add(rePlayButton(style, bundle)).expandX().fillX().padLeft(32).padRight(16).height(100);
        buttonTable.add(newGameButton(style, bundle)).expandX().fillX().padLeft(16).padRight(32).height(100);
        endGamePopup.add(buttonTable).expandX().fillX().height(100).padBottom(32);
    }

    private Stack stack(ChessGameAssesManager chessGameAssesManager, Label.LabelStyle style, I18NBundle bundle) {
        Image image = new Image(chessGameAssesManager.getClose());
        Container<Image> container = new Container<>(image);
        container.padLeft(32);
        container.setFillParent(true);
        container.align(Align.left);
        container.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                endGamePopup.remove();
            }
        });
        Label label = new Label(bundle.get("gameEnd"), style);
        label.setFontScale(1.5f);
        label.setFillParent(true);
        label.setAlignment(Align.center);
        Stack stack = new Stack();
        stack.addActor(label);
        stack.addActor(container);
        return stack;
    }

    private Label winText(Label.LabelStyle style, I18NBundle bundle,boolean isWhite) {
        return new Label(isWhite ? bundle.get("whiteWin") : bundle.get("blackWin"), style);
    }

    private TextButton newGameButton(TextButton.TextButtonStyle style, I18NBundle bundle) {
        TextButton textButton = new TextButton(bundle.get("newBoard"), style);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });
        return textButton;
    }

    private TextButton rePlayButton(TextButton.TextButtonStyle style, I18NBundle bundle) {
        TextButton textButton = new TextButton(bundle.get("rePlay"), style);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });
        return textButton;
    }
}
