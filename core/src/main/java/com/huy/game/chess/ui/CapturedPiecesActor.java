package com.huy.game.chess.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.huy.game.chess.manager.ChessImage;

import java.util.Map;

public class CapturedPiecesActor extends Actor {
    private final Map<String, Integer> map;
    private final ChessImage chessImage;
    private final boolean isWhite;
    private final BitmapFont font;

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
                batch.draw(chessImage.getbPawn(), position , 0, size, size);
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
                batch.draw(chessImage.getbKnight(), position , 0, size, size);
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
                batch.draw(chessImage.getbBishop(), position , 0, size, size);
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
                batch.draw(chessImage.getbRook(), position , 0, size, size);
                hasBefore = true;
            }
            if(hasBefore) {
                position += pieceSize * 2;
            }
            for (int j = 0; j < map.get("q"); j++) {
                if(j != 0) {
                    position += pieceSize;
                }
                batch.draw(chessImage.getbQueen(), position , 0, size, size);
            }
        }else {
            for (int j = 0; j < map.get("p"); j++) {
                if(j != 0) {
                    position += pieceSize;
                }
                batch.draw(chessImage.getwPawn(), position , 0, size, size);
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
                batch.draw(chessImage.getwKnight(), position , 0, size, size);
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
                batch.draw(chessImage.getwBishop(), position , 0, size, size);
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
                batch.draw(chessImage.getwRock(), position , 0, size, size);
                hasBefore = true;
            }
            if(hasBefore) {
                position += pieceSize * 2;
            }
            for (int j = 0; j < map.get("q"); j++) {
                if(j != 0) {
                    position += pieceSize;
                }
                batch.draw(chessImage.getwQueen(), position , 0, size, size);
            }
        }
        int value = map.get("value");
        if(value != 0) {
            position += pieceSize * 3;
            font.draw(batch, "+"+ value, position, 35);
        }
    }
}
