package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;

public class King extends Piece{


    public King(boolean isWhite, Texture texture) {
        super(isWhite, texture);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        if(end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }
        int x = Math.abs(end.getX() - start.getX());
        int y = Math.abs(end.getY() - start.getY());
        if(x > 1 || y > 1) {
            return false;
        }else {
            return true;
        }
    }
}
