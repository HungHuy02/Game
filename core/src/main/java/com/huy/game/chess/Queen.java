package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;

public class Queen extends Piece{


    public Queen(boolean isWhite, Texture texture) {
        super(isWhite, texture);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        if(end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }
        int x = Math.abs(end.getX() - start.getX());
        int y = Math.abs(end.getY() - start.getY());
        if(x == 0 || y == 0 || x == y) {
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

    @Override
    public boolean calculateMove(Board board, Spot checkSpot) {
        int[][] queenMoves =  {
            {1, -1}, {1, 0}, {1, 1},{0, -1}, {0, 1}, {-1, -1}, {-1, 0}, {-1, 1}
        };
        for (int[] move: queenMoves) {
            for(int i = 1; i <= 7; i++) {
                int x = move[0] + checkSpot.getX();
                int y = move[1] + checkSpot.getY();
                if(board.isWithinBoard(x, y)) {
                    if(calculateOneMove(board, checkSpot, x, y)) {
                        return true;
                    }
                }else {
                    break;
                }
            }
        }
        return false;
    }
}
