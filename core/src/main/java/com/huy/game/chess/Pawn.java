package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;

public class Pawn extends Piece{


    public Pawn(boolean isWhite, Texture texture) {
        super(isWhite, texture);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        if(end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }
        int x = Math.abs(end.getX() - start.getX());
        int y = end.getY() - start.getY();
        if(this.isWhite()) {
            if(y < 0) {
                return false;
            }
        }else {
            if(y > 0) {
                return false;
            }
        }
        if(x > 1 || y > 1) {
            return false;
        }else if(x == 1 && y == 1){
            return end.getPiece() != null;
        }else {
            return end.getPiece() == null;
        }
    }
}
