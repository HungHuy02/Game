package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;
import com.huy.game.chess.ChessScreen;
import com.huy.game.chess.enums.GameResult;
import com.huy.game.chess.manager.Player;

public class WaitTime {

    private HorizontalGroup horizontalGroup;
    private Label countdownTime;
    private Timer timer;

    public WaitTime(BitmapFont font, I18NBundle bundle, Timer timer) {
        this.timer = timer;
        horizontalGroup(font, bundle);
    }

    public HorizontalGroup getHorizontalGroup() {
        return horizontalGroup;
    }

    private void horizontalGroup(BitmapFont font, I18NBundle bundle) {
        horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(text(font, bundle));
        horizontalGroup.space(16f);
        countdownTime(font);
        horizontalGroup.addActor(countdownTime);
        horizontalGroup.setPosition(32f, Gdx.graphics.getHeight() - 230f);
    }

    private Label text(BitmapFont font, I18NBundle bundle) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        return  new Label(bundle.get("lostConnect"), style);
    }

    private void countdownTime(BitmapFont font) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        countdownTime = new Label("", style);
    }

    public void setupTimer(ChessScreen chessScreen) {
        timer.scheduleTask(new Timer.Task() {
            int timeRemaining = 120;
            @Override
            public void run() {
                timeRemaining--;
                int minutes = timeRemaining / 60;
                int remainSeconds = timeRemaining % 60;
                countdownTime.setText(String.format("%d:%02d", minutes, remainSeconds));
                if (timeRemaining == 0) {
                    timer.clear();
                    horizontalGroup.remove();
                    chessScreen.showEndGamePopup(Player.getInstance().isWhite() ? GameResult.WHITE_WIN : GameResult.BLACK_WIN);
                }
            }
        }, 0, 1);
    }

}
