package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;

public abstract class Piece {

    private boolean isWhite;
    private Texture texture;

    public Piece(boolean isWhite, Texture texture) {
        this.isWhite = isWhite;
        this.texture = texture;
    }

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean calculateOneMove(Board board, Spot checkSpot,int x, int y) {
        board.setTempSpots();
        Spot start = board.getTempSpot(checkSpot.getX(), checkSpot.getY());
        Spot end = board.getTempSpot(x, y);
        if(canMove(board, start, end)) {
            board.makeTempMove(start, end);
            return board.isKingSafe(checkSpot.getPiece().isWhite());
        }
        return false;
    }

    public void calculateForOnePoint(Board board, Spot checkSpot, int x, int y) {
        board.setTempSpots();
        Spot start = board.getTempSpot(checkSpot.getX(), checkSpot.getY());
        Spot end = board.getTempSpot(x, y);
        if(canMove(board, start, end)) {
            board.makeTempMove(start, end);
            if(board.isKingSafe(checkSpot.getPiece().isWhite())) {
                board.getSpot(x, y).setShowMovePoint(true);
            }
        }
    }

    public abstract boolean canMove(Board board, Spot start, Spot end);

    public abstract boolean calculateMove(Board board, Spot checkSpot);

    public abstract void calculateForPoint(Board board, Spot checkSpot);
}
