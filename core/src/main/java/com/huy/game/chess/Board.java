package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Board {
    private Spot[][] spots = new Spot[8][8];

    public Spot getSpot(int x, int y) throws Exception {
        if(x < 0 || x > 7 || y < 0 || y > 7) {
            throw new Exception("Index out of bound");
        }
        return spots[x][y];
    }

    public void resetBoard(Texture wRook, Texture wKnight, Texture wBishop,
                           Texture wQueen, Texture wKing, Texture wPawn,
                           Texture bRook, Texture bKnight, Texture bBishop,
                           Texture bQueen, Texture bKing, Texture bPawn
    ) {
        spots[0][0] = new Spot(new Rook(true, wRook), 0, 0);
        spots[0][1] = new Spot(new Knight(true, wKnight), 0, 1);
        spots[0][2] = new Spot(new Bishop(true, wBishop), 0, 2);
        spots[0][3] = new Spot(new Queen(true, wQueen), 0, 3);
        spots[0][4] = new Spot(new King(true, wKing), 0, 4);
        spots[0][5] = new Spot(new Bishop(true, wBishop), 0, 5);
        spots[0][6] = new Spot(new Knight(true, wKnight), 0, 6);
        spots[0][7] = new Spot(new Rook(true, wRook), 0, 7);

        for(int i = 0; i <= 7; i++) {
            spots[1][i] = new Spot(new Pawn(true, wPawn), 1, i);
        }

        spots[7][0] = new Spot(new Rook(false, bRook), 7, 0);
        spots[7][1] = new Spot(new Knight(false, bKnight), 7, 1);
        spots[7][2] = new Spot(new Bishop(false, bBishop), 7, 2);
        spots[7][3] = new Spot(new Queen(false, bQueen), 7, 3);
        spots[7][4] = new Spot(new King(false, bKing), 7, 4);
        spots[7][5] = new Spot(new Bishop(false, bBishop), 7, 5);
        spots[7][6] = new Spot(new Knight(false, bKnight), 7, 6);
        spots[7][7] = new Spot(new Rook(false, bRook), 7, 7);

        for(int i = 0; i <= 7; i++) {
            spots[6][i] = new Spot(new Pawn(false, bPawn), 6, i);
        }

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                spots[i][j] = new Spot(null,i, j);
            }
        }
    }

    public void renderBoard(SpriteBatch batch, float spotSize, float pieceSide, float centerX, float centerY) {
        float padding = spotSize / 10f;
        float scale = (spotSize - (2 * padding)) / pieceSide;
        float x = centerX + padding;
        float y = centerY + padding;
        float scaledSide = scale * pieceSide;
        for(int i = 0; i <= 7; i++) {
            float distanceY = spotSize * i + y;
            for (int j = 0;j <= 7; j++) {
                float distance = spotSize * j;
                if(spots[i][j].getPiece() != null) {
                    batch.draw(spots[i][j].getPiece().getTexture(), x + distance, distanceY, scaledSide, scaledSide);
                }
            }
        }
    }

}
