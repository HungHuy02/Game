package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.huy.game.chess.manager.ChessImage;

import java.util.Map;

public class Timer extends Actor {

    private final Map<String, Integer> timeList;
    private final BitmapFont font;
    private final boolean isPlayer1;
    private final ChessImage chessImage;

    public Timer(Map<String, Integer> timeList, BitmapFont font, boolean isPlayer1, ChessImage chessImage) {
        this.timeList = timeList;
        this.font = font;
        this.isPlayer1 = isPlayer1;
        this.chessImage = chessImage;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        int timeRemaining;
        if(isPlayer1) {
            if (timeList.get("play1") == 1) {
                batch.draw(chessImage.getTimer(), Gdx.graphics.getWidth() - (float) Gdx.graphics.getWidth() / 4 - (float) chessImage.getTimer().getWidth() / 2, 0);
            }
            timeRemaining = timeList.get("1");

        }else {
            if (timeList.get("play2") == 1) {
                batch.draw(chessImage.getTimer(), Gdx.graphics.getWidth() - (float) Gdx.graphics.getWidth() / 4 - (float) chessImage.getTimer().getWidth() / 2, 0);
            }
            timeRemaining = timeList.get("2");
        }
        int minutes = timeRemaining / 60;
        int remainSeconds = timeRemaining % 60;
        font.draw(batch, String.format("%d:%02d", minutes, remainSeconds), Gdx.graphics.getWidth() - (float) Gdx.graphics.getWidth() / 4 + (float) chessImage.getTimer().getWidth() / 2, 50);
    }
}
