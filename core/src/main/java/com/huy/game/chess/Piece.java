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

    public abstract boolean canMove(Board board, Spot start, Spot end);

}
