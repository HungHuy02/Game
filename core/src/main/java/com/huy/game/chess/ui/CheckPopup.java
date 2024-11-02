package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.huy.game.chess.ChessScreen;
import com.huy.game.chess.manager.ChessGameAssesManager;

public class CheckPopup {

    private Window checkPopup;

    public CheckPopup(ChessGameAssesManager manager, BitmapFont font, I18NBundle bundle, Stage stage, ChessScreen chessScreen) {
        setCheckPopup(manager, font, bundle, stage, chessScreen);
    }

    public Window getCheckPopup() {
        return checkPopup;
    }

    private void setCheckPopup(ChessGameAssesManager manager, BitmapFont font, I18NBundle bundle, Stage stage, ChessScreen chessScreen) {
        Skin skin = manager.getSkin();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        TextButton.TextButtonStyle style = skin.get(TextButton.TextButtonStyle.class);
        style.font = font;
        checkPopup = new Window("", skin);
        checkPopup.setSize(Gdx.graphics.getWidth() - 100, 232);
        checkPopup.setPosition((Gdx.graphics.getWidth() - checkPopup.getWidth()) / 2,
            (Gdx.graphics.getHeight() - checkPopup.getHeight()) / 2);
        checkPopup.add(label(bundle, labelStyle)).padBottom(32).padTop(32);
        checkPopup.row();
        Table buttonTable = new Table();
        buttonTable.add(cancelButton(style, bundle, stage)).expandX().fillX().padLeft(32).padRight(16).height(100);
        buttonTable.add(okButton(style, bundle, stage, chessScreen)).expandX().fillX().padLeft(16).padRight(32).height(100);
        checkPopup.add(buttonTable).expandX().fillX().height(100).padBottom(32);
    }

    private Label label(I18NBundle bundle, Label.LabelStyle style) {
        return new Label(bundle.get("areSure"), style);
    }

    private TextButton cancelButton(TextButton.TextButtonStyle style, I18NBundle bundle, Stage stage) {
        TextButton cancelButton = new TextButton(bundle.get("cancel"), style);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                checkPopup.remove();
                stage.getActors().removeIndex(stage.getActors().size - 1);
            }
        });
        return cancelButton;
    }

    private TextButton okButton(TextButton.TextButtonStyle style, I18NBundle bundle, Stage stage, ChessScreen chessScreen) {
        TextButton okButton = new TextButton(bundle.get("yes"), style);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                checkPopup.remove();
                stage.getActors().removeIndex(stage.getActors().size - 1);
                chessScreen.newGame();
            }
        });
        return okButton;
    }
}
