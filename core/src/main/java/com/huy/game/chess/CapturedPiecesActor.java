package com.huy.game.chess;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Map;

public class CapturedPiecesActor extends Actor {
    private Map<String, Integer> map;
    private ChessImage chessImage;
    private boolean isWhite;
    private int value = 0;
    private BitmapFont font;

    public CapturedPiecesActor(Map<String, Integer> map,ChessImage chessImage, boolean isWhite, BitmapFont font) {
        this.chessImage = chessImage;
        this.isWhite = isWhite;
        this.map = map;
        this.font = font;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float size = chessImage.getCapturedPieceSize();
        float pieceSize = size / 3;
        float position = 0;
        boolean hasBefore = false;
        if(isWhite) {
            for (int j = 0; j < map.get("p"); j++) {
                if(j != 0) {
                    position += pieceSize;
                }
                batch.draw(chessImage.getbPawn(), position , 10, size, size);
                hasBefore = true;
            }
            if(hasBefore) {
                position += pieceSize * 2;
                hasBefore = false;
            }
            for (int j = 0; j < map.get("k"); j++) {
                if(j != 0) {
                    position += pieceSize;
                }
                batch.draw(chessImage.getbKnight(), position , 10, size, size);
                hasBefore = true;
            }
            if(hasBefore) {
                position += pieceSize * 2;
                hasBefore = false;
            }
            for (int j = 0; j < map.get("b"); j++) {
                if(j != 0) {
                    position += pieceSize;
                }
                batch.draw(chessImage.getbBishop(), position , 10, size, size);
                hasBefore = true;
            }
            if(hasBefore) {
                position += pieceSize * 2;
                hasBefore = false;
            }
            for (int j = 0; j < map.get("r"); j++) {
                if(j != 0) {
                    position += pieceSize;
                }
                batch.draw(chessImage.getbRock(), position , 10, size, size);
                hasBefore = true;
            }
            if(hasBefore) {
                position += pieceSize * 2;
            }
            for (int j = 0; j < map.get("q"); j++) {
                if(j != 0) {
                    position += pieceSize;
                }
                batch.draw(chessImage.getbQueen(), position , 10, size, size);
            }
        }else {
            for (int j = 0; j < map.get("p"); j++) {
                if(j != 0) {
                    position += pieceSize;
                }
                batch.draw(chessImage.getwPawn(), position , 10, size, size);
                hasBefore = true;
            }
            if(hasBefore) {
                position += pieceSize * 2;
                hasBefore = false;
            }
            for (int j = 0; j < map.get("k"); j++) {
                if(j != 0) {
                    position += pieceSize;
                }
                batch.draw(chessImage.getwKnight(), position , 10, size, size);
                hasBefore = true;
            }
            if(hasBefore) {
                position += pieceSize * 2;
                hasBefore = false;
            }
            for (int j = 0; j < map.get("b"); j++) {
                if(j != 0) {
                    position += pieceSize;
                }
                batch.draw(chessImage.getwBishop(), position , 10, size, size);
                hasBefore = true;
            }
            if(hasBefore) {
                position += pieceSize * 2;
                hasBefore = false;
            }
            for (int j = 0; j < map.get("r"); j++) {
                if(j != 0) {
                    position += pieceSize;
                }
                batch.draw(chessImage.getwRock(), position , 10, size, size);
                hasBefore = true;
            }
            if(hasBefore) {
                position += pieceSize * 2;
            }
            for (int j = 0; j < map.get("q"); j++) {
                if(j != 0) {
                    position += pieceSize;
                }
                batch.draw(chessImage.getwQueen(), position , 10, size, size);
            }
        }
        position += pieceSize * 3;
        font.draw(batch, "+"+ value, position, 45);
    }
}
