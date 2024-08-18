package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;

public class Bishop extends Piece{


    public Bishop(boolean isWhite, Texture texture) {
        super(isWhite, texture);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        if(end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }
        int x = Math.abs(end.getX() - start.getX());
        int y = Math.abs(end.getY() - start.getY());
        if(x == y) {
            int directionX = Integer.signum(end.getX() - start.getX());
            int directionY = Integer.signum(end.getY() - start.getY());
            int currentX = start.getX() + directionX;
            int currentY = start.getY() + directionY;
            while(currentX != end.getX() || currentY != end.getY()) {
                if(board.getSpot(currentX, currentY).getPiece() != null) {
                    return false;
                }
                currentX += directionX;
                currentY += directionY;
            }
            return true;
        }else {
            return false;
        }
    }
}