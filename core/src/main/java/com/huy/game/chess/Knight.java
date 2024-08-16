package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;

public class Knight extends Piece{


    public Knight(boolean isWhite, Texture texture) {
        super(isWhite, texture);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        return false;
    }
}
