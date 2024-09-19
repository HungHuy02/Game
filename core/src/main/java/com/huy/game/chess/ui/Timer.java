package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Map;

public class Timer extends Actor {

    private Map<String, Integer> timeList;
    private BitmapFont font;
    private boolean isPlayer1;
    private ChessImage chessImage;

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
                batch.draw(chessImage.getTimer(), Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4 - chessImage.getTimer().getWidth() / 2, 0);
            }
            timeRemaining = timeList.get("1");

        }else {
            if (timeList.get("play2") == 1) {
                batch.draw(chessImage.getTimer(), Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4 - chessImage.getTimer().getWidth() / 2, 0);
            }
            timeRemaining = timeList.get("2");
        }
        int minutes = timeRemaining / 60;
        int remainSeconds = timeRemaining % 60;
        font.draw(batch, String.format(String.format("%d:%02d", minutes, remainSeconds)), Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4 + chessImage.getTimer().getWidth() / 2, 50);
    }
}
