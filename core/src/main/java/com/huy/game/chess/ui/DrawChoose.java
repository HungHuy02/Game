package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;
import com.huy.game.Main;
import com.huy.game.chess.enums.GameResult;
import com.huy.game.chess.manager.ChessImage;

public class DrawChoose {

    private HorizontalGroup horizontalGroup;
    private Timer timer;

    public DrawChoose(BitmapFont font, I18NBundle bundle, ChessImage chessImage, Main main) {
        setupTimer();
        horizontalGroup(font, bundle, chessImage, main);
    }

    public HorizontalGroup getHorizontalGroup() {
        return horizontalGroup;
    }

    private void setupTimer() {
        timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                horizontalGroup.remove();
            }
        }, 60);
        timer.start();
    }

    private void horizontalGroup(BitmapFont font, I18NBundle bundle, ChessImage chessImage, Main main) {
        horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(text(font, bundle));
        horizontalGroup.space(16f);
        horizontalGroup.addActor(acceptButton(chessImage.getCheck(), main));
        horizontalGroup.addActor(cancelButton(chessImage.getCancer()));
        horizontalGroup.setPosition(32f, Gdx.graphics.getHeight() - 200f);
    }

    private Label text(BitmapFont font, I18NBundle bundle) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        return new Label(bundle.get("wantDraw"), labelStyle);
    }

    private Image acceptButton(Texture imageTexture, Main main) {
        Image image = new Image(imageTexture);
        image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                main.socketClient.gameEnd(GameResult.DRAW);
                timer.clear();
                horizontalGroup.remove();
            }
        });
        return image;
    }

    private Image cancelButton(Image image) {
        image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                timer.clear();
                horizontalGroup.remove();
            }
        });
        return image;
    }
}
