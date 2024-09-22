package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.huy.game.chess.core.BoardSetting;

public class OptionsPopup {

    private Window optionsPopup;

    public OptionsPopup(BoardSetting setting, BitmapFont font) {
        setOptionsPopup(setting ,font);
    }

    private void setOptionsPopup(BoardSetting setting, BitmapFont font) {
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        optionsPopup = new Window("", skin);
        optionsPopup.setSize(300, 100);
        optionsPopup.setPosition((Gdx.graphics.getWidth() - optionsPopup.getWidth()) / 2,
            (Gdx.graphics.getHeight() - optionsPopup.getHeight()) / 2);

        optionsPopup.add(rotateBoardButton(setting, skin, font)).fillX().expandX();
        optionsPopup.row();
        optionsPopup.add(new TextButton("Sao chép PGN", skin)).fillX().expandX();
        optionsPopup.row();
        optionsPopup.add(new TextButton("Ván cờ mới", skin)).fillX().expandX();
        optionsPopup.setVisible(false);
    }

    private TextButton rotateBoardButton(BoardSetting setting ,Skin skin, BitmapFont font) {
        TextButton button = new TextButton("Xoay bàn cờ", skin);
        button.getLabel().getStyle().font = font;
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setting.setRotate(!setting.isRotate());
            }
        });
        return button;
    }

    private TextButton newGameButton(Skin skin, BitmapFont font) {
        TextButton button = new TextButton("Ván cờ mới", skin);
        button.getLabel().getStyle().font = font;
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
