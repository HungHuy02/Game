package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.huy.game.chess.core.BoardSetting;
import com.huy.game.chess.core.GameHistory;
import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.manager.ChessGameAssesManager;
import com.huy.game.chess.manager.ChessImage;

public class OptionsPopup {

    private Window optionsPopup;

    public OptionsPopup(BoardSetting setting, BitmapFont font, I18NBundle bundle, ChessGameAssesManager manager, Window check, ChessImage chessImage, Stage stage, GameHistory history, ChessMode mode) {
        setOptionsPopup(setting ,font, bundle, manager, check, chessImage, stage, history, mode);
    }

    private void setOptionsPopup(BoardSetting setting, BitmapFont font, I18NBundle bundle, ChessGameAssesManager manager, Window check, ChessImage chessImage, Stage stage, GameHistory history, ChessMode mode) {
        Skin skin = manager.getSkin();
        TextButton.TextButtonStyle style = skin.get(TextButton.TextButtonStyle.class);
        style.font = font;
        optionsPopup = new Window("", skin);
        optionsPopup.setSize(Gdx.graphics.getWidth() - 100, 300);
        optionsPopup.setPosition((Gdx.graphics.getWidth() - optionsPopup.getWidth()) / 2,
            (Gdx.graphics.getHeight() - optionsPopup.getHeight()) / 2);
        switch (mode) {
            case ONLINE -> {
                optionsPopup.setSize(Gdx.graphics.getWidth() - 100, 400);
                optionsPopup.add(drawButton(bundle.get("draw"), skin, stage)).fillX().expandX().height(100);
                optionsPopup.row();
            }
            case TWO_PERSONS -> {
                optionsPopup.setSize(Gdx.graphics.getWidth() - 100, 400);
                optionsPopup.add(autoRotateButton(setting, skin, bundle, stage)).fillX().expandX().height(100);
                optionsPopup.row();
            }
        }
        optionsPopup.add(rotateBoardButton(setting, skin, bundle.get("rotateBoard"), stage)).fillX().expandX().height(100);
        optionsPopup.row();
        optionsPopup.add(printButton(skin, bundle.get("copyPGN"), history, stage)).fillX().expandX().height(100);
        optionsPopup.row();
        optionsPopup.add(newGameButton(skin, bundle.get("newBoard"), check, chessImage, stage)).fillX().expandX().height(100);
    }

    private TextButton autoRotateButton(BoardSetting setting, Skin skin, I18NBundle bundle, Stage stage) {
        TextButton button = new TextButton(
            setting.isAutoRotate() ?
                bundle.get("turnOffAutoRotate") :
                bundle.get("turnOnAutoRotate"), skin);
        button.pad(32f);
        button.getLabel().setAlignment(Align.left);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (setting.isAutoRotate()) {
                    setting.setAutoRotate(false);
                    setting.setReverseOneSide(true);
                    button.setText(bundle.get("turnOnAutoRotate"));
                }else {
                    setting.setAutoRotate(true);
                    setting.setReverseOneSide(false);
                    button.setText(bundle.get("turnOffAutoRotate"));
                }
                optionsPopup.remove();
                stage.getActors().removeIndex(stage.getActors().size - 1);
            }
        });
        return button;
    }

    private TextButton drawButton(String name, Skin skin, Stage stage) {
        TextButton button = new TextButton(name, skin);
        button.pad(32f);
        button.getLabel().setAlignment(Align.left);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                optionsPopup.remove();
                stage.getActors().removeIndex(stage.getActors().size - 1);
            }
        });
        return button;
    }

    private TextButton rotateBoardButton(BoardSetting setting , Skin skin, String name, Stage stage) {
        TextButton button = new TextButton(name, skin);
        button.pad(32f);
        button.getLabel().setAlignment(Align.left);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setting.setRotate(!setting.isRotate());
                optionsPopup.remove();
                stage.getActors().removeIndex(stage.getActors().size - 1);
            }
        });
        return button;
    }

    private TextButton printButton(Skin skin, String name, GameHistory history, Stage stage) {
        TextButton button = new TextButton(name, skin);
        button.pad(32f);
        button.getLabel().setAlignment(Align.left);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.getClipboard().setContents(history.getPGN());
                optionsPopup.remove();
                stage.getActors().removeIndex(stage.getActors().size - 1);
            }
        });
        return button;
    }

    private TextButton newGameButton(Skin skin, String name, Window window, ChessImage chessImage, Stage stage) {
        TextButton button = new TextButton(name, skin);
        button.pad(32f);
        button.getLabel().setAlignment(Align.left);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                optionsPopup.remove();
                stage.getActors().removeIndex(stage.getActors().size - 1);
                Image overlay = chessImage.getOverlay();
                overlay.setFillParent(true);
                overlay.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        window.remove();
                        overlay.remove();
                    }
                });
                stage.addActor(overlay);
                stage.addActor(window);
            }
        });
        return button;
    }

    public Window getOptionsPopup() {
        return optionsPopup;
    }
}
