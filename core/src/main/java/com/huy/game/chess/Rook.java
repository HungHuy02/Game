package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;

public class Rook extends Piece {

    private boolean hasMove = false;

    public boolean isHasMove() {
        return hasMove;
    }

    public void setHasMove() {
        this.hasMove = true;
    }

    private int[][] rookMoves() {
        return new int[][]{
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };
    }

    public Rook(Rook rook) {
        super(rook.isWhite());
        hasMove = rook.hasMove;
    }

    public Rook(boolean isWhite, Texture texture) {
        super(isWhite, texture);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        if(end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }
        int x = Math.abs(end.getX() - start.getX());
        int y = Math.abs(end.getY() - start.getY());
        if(x == 0 || y == 0) {
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
            if(end.getPiece() != null) {
                board.getSpot(end.getX(), end.getY()).setCanBeCaptured(true);
            }
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean calculateMove(Board board, Spot checkSpot) {
        for (int[] move: rookMoves()) {
            for(int i = 1; i <= 7; i++) {
                int x = move[0] * i + checkSpot.getX();
                int y = move[1] * i + checkSpot.getY();
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

    @Override
    public void calculateForPoint(Board board, Spot checkSpot) {
        for (int[] move: rookMoves()) {
            for(int i = 1; i <= 7; i++) {
                int x = move[0] * i + checkSpot.getX();
                int y = move[1] * i + checkSpot.getY();
                if(board.isWithinBoard(x, y)) {
                    calculateForOnePoint(board, checkSpot, x, y);
                }else {
                    break;
                }
            }
        }
    }
}
