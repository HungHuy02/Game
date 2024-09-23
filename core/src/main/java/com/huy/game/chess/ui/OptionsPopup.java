package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.huy.game.chess.core.BoardSetting;
import com.huy.game.chess.manager.ChessGameAssesManager;

public class OptionsPopup {

    private Window optionsPopup;

    public OptionsPopup(BoardSetting setting, BitmapFont font, I18NBundle bundle, ChessGameAssesManager manager) {
        setOptionsPopup(setting ,font, bundle, manager);
    }

    private void setOptionsPopup(BoardSetting setting, BitmapFont font, I18NBundle bundle, ChessGameAssesManager manager) {
        Skin skin = manager.getSkin();
        TextButton.TextButtonStyle style = skin.get(TextButton.TextButtonStyle.class);
        style.font = font;
        optionsPopup = new Window("", skin);
        optionsPopup.setSize(Gdx.graphics.getWidth() - 100, 300);
        optionsPopup.setPosition((Gdx.graphics.getWidth() - optionsPopup.getWidth()) / 2,
            (Gdx.graphics.getHeight() - optionsPopup.getHeight()) / 2);
        optionsPopup.add(rotateBoardButton(setting, skin, bundle.get("rotateBoard"))).fillX().expandX().height(100);
        optionsPopup.row();
        optionsPopup.add(printButton(skin, bundle.get("copyPGN"))).fillX().expandX().height(100);
        optionsPopup.row();
        optionsPopup.add(newGameButton(skin, bundle.get("newBoard"))).fillX().expandX().height(100);
    }

    private TextButton rotateBoardButton(BoardSetting setting , Skin skin, String name) {
        TextButton button = new TextButton(name, skin);
        button.pad(32f);
        button.getLabel().setAlignment(Align.left);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setting.setRotate(!setting.isRotate());
            }
        });
        return button;
    }

    private TextButton printButton(Skin skin, String name) {
        TextButton button = new TextButton(name, skin);
        button.pad(32f);
        button.getLabel().setAlignment(Align.left);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

            }
        });
        return button;
    }

    private TextButton newGameButton(Skin skin, String name) {
        TextButton button = new TextButton(name, skin);
        button.pad(32f);
        button.getLabel().setAlignment(Align.left);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

            }
        });
        return button;
    }

    public Window getOptionsPopup() {
        return optionsPopup;
    }
}
