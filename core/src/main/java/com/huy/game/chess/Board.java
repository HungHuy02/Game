package com.huy.game.chess;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;

public class Board {
    private Spot[][] spots = new Spot[8][8];
    private Spot wKingSpot;
    private Spot bKingSpot;
    private Spot promotingSpot;
    private boolean isPromoting = false;

    public void setPromoting(boolean isPromoting) {
        this.isPromoting = isPromoting;
    }

    public boolean isPromoting() {
        return isPromoting;
    }

    public Spot getSpot(int x, int y) {
        return spots[x][y];
    }

    public Spot getPromotingSpot() {
        return promotingSpot;
    }

    public void setPromotingSpot(Spot promotingSpot) {
        this.promotingSpot = promotingSpot;
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

    public void resetBoard(ChessImage chessImage ) {
        spots[0][0] = new Spot(new Rook(true, chessImage.getwRock()), 0, 0);
        spots[0][1] = new Spot(new Knight(true, chessImage.getwKnight()), 0, 1);
        spots[0][2] = new Spot(new Bishop(true, chessImage.getwBishop()), 0, 2);
        spots[0][3] = new Spot(new Queen(true, chessImage.getwQueen()), 0, 3);
        spots[0][4] = new Spot(new King(true, chessImage.getwKing()), 0, 4);
        wKingSpot = spots[0][4];
        spots[0][5] = new Spot(new Bishop(true, chessImage.getwBishop()), 0, 5);
        spots[0][6] = new Spot(new Knight(true, chessImage.getwKnight()), 0, 6);
        spots[0][7] = new Spot(new Rook(true, chessImage.getwRock()), 0, 7);

        for(int i = 0; i <= 7; i++) {
            spots[1][i] = new Spot(new Pawn(true, chessImage.getwPawn()), 1, i);
        }

        spots[7][0] = new Spot(new Rook(false, chessImage.getbRock()), 7, 0);
        spots[7][1] = new Spot(new Knight(false, chessImage.getbKnight()), 7, 1);
        spots[7][2] = new Spot(new Bishop(false, chessImage.getbBishop()), 7, 2);
        spots[7][3] = new Spot(new Queen(false, chessImage.getbQueen()), 7, 3);
        spots[7][4] = new Spot(new King(false, chessImage.getbKing()), 7, 4);
        bKingSpot = spots[7][4];
        spots[7][5] = new Spot(new Bishop(false, chessImage.getbBishop()), 7, 5);
        spots[7][6] = new Spot(new Knight(false, chessImage.getbKnight()), 7, 6);
        spots[7][7] = new Spot(new Rook(false, chessImage.getbRock()), 7, 7);

        for(int i = 0; i <= 7; i++) {
            spots[6][i] = new Spot(new Pawn(false, chessImage.getbPawn()), 6, i);
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
                    shapeRenderer.setColor(spots[i][j].getSpotColor());
                    shapeRenderer.rect(centerX + distance, distanceY, spotSize, spotSize);
                }
            }
        }
    }

    public void clearColor() {
        for (int i = 0; i <= 7; i++) {
            for(int j = 0; j <= 7; j++) {
                spots[i][j].setShowColor(false);
                spots[i][j].setIdentificationColor(false);
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

    public void warnIllegalMove(boolean isWhite) {
        Timer.schedule(new Timer.Task() {
            int count = 1;
            @Override
            public void run() {
                if(count++ <= 5) {
                    Spot kingSpot = getKingSpot(isWhite);
                    if(kingSpot.isShowColor()) {
                        kingSpot.setShowColor(false);
                    }else {
                        kingSpot.setShowColor(true);
                        kingSpot.setSpotColor(Colors.ILLEGAL_MOVE);
                    }
                }else {
                    this.cancel();
                }
            }
        }, 0, 0.5f);
    }

    public void showPromoteSelection(SpriteBatch batch,ShapeRenderer shapeRenderer,float centerX, float centerY, ChessImage chessImage) {
        float padding = chessImage.getSpotSize() / 10f;
        float scale = (chessImage.getSpotSize() - (2 * padding)) / chessImage.getPieceSize();
        float scaledSide = scale * chessImage.getPieceSize();
        if(promotingSpot.getX() == 7) {
            float x = centerX + chessImage.getSpotSize() * promotingSpot.getY();
            float y = centerY + chessImage.getSpotSize() * promotingSpot.getX() - 3 * chessImage.getSpotSize();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(x, y, chessImage.getSpotSize(), chessImage.getSpotSize() * 4);
            shapeRenderer.end();

            batch.begin();
            batch.draw(chessImage.getwRock(), x + padding, y + padding , scaledSide, scaledSide);
            batch.draw(chessImage.getwBishop(), x + padding, y + padding + chessImage.getSpotSize() , scaledSide, scaledSide);
            batch.draw(chessImage.getwKnight(), x + padding, y + padding + chessImage.getSpotSize() * 2, scaledSide, scaledSide);
            batch.draw(chessImage.getwQueen(), x + padding, y + padding + chessImage.getSpotSize() * 3, scaledSide, scaledSide);
            batch.end();
        }else {
            float x = centerX + chessImage.getSpotSize() * promotingSpot.getY();
            float y = centerY + chessImage.getSpotSize() * promotingSpot.getX() + 3 * chessImage.getSpotSize();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(x, y, chessImage.getSpotSize(), chessImage.getSpotSize() * 4);
            shapeRenderer.end();

            batch.begin();
            batch.draw(chessImage.getbRock(), x + padding, y + padding , scaledSide, scaledSide);
            batch.draw(chessImage.getbBishop(), x + padding, y + padding + chessImage.getSpotSize() , scaledSide, scaledSide);
            batch.draw(chessImage.getbKnight(), x + padding, y + padding + chessImage.getSpotSize() * 2, scaledSide, scaledSide);
            batch.draw(chessImage.getbQueen(), x + padding, y + padding + chessImage.getSpotSize() * 3, scaledSide, scaledSide);
            batch.end();
        }
    }
}
