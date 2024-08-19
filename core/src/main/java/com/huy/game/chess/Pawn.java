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
        int x = end.getX() - start.getX();
        int y = Math.abs(end.getY() - start.getY());
        if(this.isWhite()) {
            if(x < 0) {
                return false;
            }
        }else {
            if(x > 0) {
                return false;
            }
        }
        x = Math.abs(x);
        if(x > 2 || y > 2) {
            return false;
        } else if(x > 1 || y > 1){
            if(this.isWhite() && start.getX() == 1 && end.getPiece() == null) {
                return true;
            }
            if(!this.isWhite() && start.getX() == 6 && end.getPiece() == null) {
                return true;
            }
            return false;
        }else if(x == 1 && y == 1){
            return end.getPiece() != null;
        }else if(x == 0 && y != 0) {
            return false;
        }else {
            return end.getPiece() == null;
        }
    }
}
