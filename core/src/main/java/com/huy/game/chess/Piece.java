package com.huy.game.chess;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public abstract class Piece {

    private boolean killed = false;
    private boolean isWhite;
    private Texture texture;

    public Piece(boolean isWhite, Texture texture) {
        this.isWhite = isWhite;
        this.texture = texture;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    public boolean isKilled() {
        return killed;
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean calculateOneMove(Board board, Spot checkSpot,int x, int y) {
        Spot testSpot = board.getSpot(x, y);
        if(canMove(board, checkSpot, testSpot)) {
            if(board.isKingSafe(checkSpot.getPiece().isWhite())) {
                testSpot.setShowMovePoint(true);
                return true;
            }
        }
        return false;
    }

    public abstract boolean canMove(Board board, Spot start, Spot end);

    public abstract boolean calculateMove(Board board, Spot checkSpot);
}
