package com.huy.game.chess;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Board {
    private Spot[][] spots = new Spot[8][8];
    private Spot wKingSpot;
    private Spot bKingSpot;

    public Spot getSpot(int x, int y) {
        return spots[x][y];
    }

    public void setSpot(int x, int y, Spot spot) {
        spots[x][y] = spot;
        if(spot.getPiece() instanceof King) {
            if(spot.getPiece().isWhite()) {
                wKingSpot = spots[x][y];
            }else {
                bKingSpot = spots[x][y];
            }
        }
    }

    public Spot getKingSpot(boolean isWhite) {
        return isWhite ? wKingSpot : bKingSpot;
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
        wKingSpot = spots[0][4];
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
        bKingSpot = spots[7][4];
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

    public void renderColor(ShapeRenderer shapeRenderer, float spotSize, float centerX, float centerY) {
        for(int i = 0; i <= 7; i++) {
            float distanceY = spotSize * i + centerY;
            for (int j = 0;j <= 7; j++) {
                float distance = spotSize * j;
                if(spots[i][j].isShowColor()) {
                    shapeRenderer.setColor(178 / 255f, 224 / 255f, 104 / 255f, 0.5f);
                    shapeRenderer.rect(centerX + distance, distanceY, spotSize, spotSize);
                }
            }
        }
    }

    public void clearColor() {
        for (int i = 0; i <= 7; i++) {
            for(int j = 0; j <= 7; j++) {
                spots[i][j].setShowColor(false);
            }
        }
    }


    public boolean isKingSafe(boolean isWhite) {
        Spot kingSpot = isWhite ? wKingSpot : bKingSpot;
        int kingX = kingSpot.getX();
        int kingY = kingSpot.getY();
        return isPositionSafe(kingX, kingY, isWhite);
    }

    public boolean isPositionSafe(int positionX, int positionY, boolean isWhite) {
        for(int i = 1; i <= 2; i++) {
            int coordinates = i == 1 ? 2 : 1;
            int temp = coordinates;
            boolean isChange = false;
            while (true) {
                Spot checkSpot = null;
                int x;
                int y;
                if(isChange) {
                    x = positionX + coordinates;
                    y = positionY + i;
                }else {
                    x = positionX + i;
                    y = positionY + coordinates;
                }
                if(x >= 0 && x <= 7 && y >=0 && y <= 7) {
                    checkSpot = getSpot(x, y);
                }
                if(checkSpot != null && checkSpot.getPiece() != null && checkSpot.getPiece().isWhite() != isWhite && checkSpot.getPiece() instanceof Knight) {
                    return false;
                }
                if(coordinates != temp) {
                    if(isChange) {
                        break;
                    }else {
                        isChange = true;
                    }
                }
                coordinates = -coordinates;
            }
        }
        for(int i = -1; i <= 1; i++) {
            int x = positionX + i;
            for(int j = -1; j <= 1; j++) {
                int y = positionY + j;
                if(x >= 0 && x <= 7 && y >=0 && y <= 7) {
                    Piece checkPiece = getSpot(x, y).getPiece();
                    if(checkPiece != null) {
                        if(checkPiece.isWhite() != isWhite) {
                            if(i != 0 && j != 0) {
                                if(checkPiece instanceof Bishop || checkPiece instanceof Queen || checkPiece instanceof Pawn || checkPiece instanceof King) {
                                    return false;
                                }
                            }else {
                                if(checkPiece instanceof Rook || checkPiece instanceof Queen) {
                                    return false;
                                }
                            }
                        }
                    }else {
                        if(i == 0 && j == 0) {
                            continue;
                        }
                        int coordinatesX = x + i;
                        int coordinatesY = y + j;
                        while(coordinatesX >= 0 && coordinatesX < 8 && coordinatesY >=0 && coordinatesY < 8) {
                            Piece testPiece = getSpot(coordinatesX, coordinatesY).getPiece();
                            if(testPiece != null) {
                                if(testPiece.isWhite() == isWhite) {
                                    break;
                                }else {
                                    if(i != 0 && j != 0) {
                                        if(testPiece instanceof Bishop || testPiece instanceof Queen){
                                            return false;
                                        }else {
                                            break;
                                        }
                                    }else {
                                        if(testPiece instanceof Rook || testPiece instanceof Queen) {
                                            return false;
                                        }else {
                                            break;
                                        }
                                    }
                                }
                            }
                            coordinatesX += i;
                            coordinatesY += j;
                        }
                    }
                }

            }
        }
        return true;
    }

}
