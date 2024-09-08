package com.huy.game.chess;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Map;

public class CapturedPiecesActor extends Actor {
    private Map<String, Integer> map;
    private ChessImage chessImage;
    private boolean isWhite;

    public CapturedPiecesActor(Map<String, Integer> map,ChessImage chessImage, boolean isWhite) {
        this.chessImage = chessImage;
        this.isWhite = isWhite;
        this.map = map;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float size = chessImage.getCapturedPieceSize();
        if(isWhite) {
            for (int j = 0; j < map.get("p"); j++) {
                batch.draw(chessImage.getbPawn(), 10 + j * 40 , 10, size, size);
            }
            for (int j = 0; j < map.get("k"); j++) {
                batch.draw(chessImage.getbKnight(), 10 + j * 40 , 10, size, size);
            }
            for (int j = 0; j < map.get("b"); j++) {
                batch.draw(chessImage.getbBishop(), 10 + j * 40 , 10, size, size);
            }
            for (int j = 0; j < map.get("r"); j++) {
                batch.draw(chessImage.getbRock(), 10 + j * 40 , 10, size, size);
            }
            for (int j = 0; j < map.get("q"); j++) {
                batch.draw(chessImage.getbQueen(), 10 + j * 40 , 10, size, size);
            }
        }else {
            for (int j = 0; j < map.get("p"); j++) {
                batch.draw(chessImage.getwPawn(), 10 + j * 40 , 10, size, size);
            }
            for (int j = 0; j < map.get("k"); j++) {
                batch.draw(chessImage.getwKnight(), 10 + j * 40 , 10, size, size);
            }
            for (int j = 0; j < map.get("b"); j++) {
                batch.draw(chessImage.getwBishop(), 10 + j * 40 , 10, size, size);
            }
            for (int j = 0; j < map.get("r"); j++) {
                batch.draw(chessImage.getwRock(), 10 + j * 40 , 10, size, size);
            }
            for (int j = 0; j < map.get("q"); j++) {
                batch.draw(chessImage.getwQueen(), 10 + j * 40 , 10, size, size);
            }
        }

    }
}
