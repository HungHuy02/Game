package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;

import java.util.List;

public class Bishop extends Piece{

    private int[][] bishopMoves() {
        return new int[][]{
            {1, -1}, {1, 1}, {-1, 1}, {-1, -1}
        };
    }

    public Bishop(boolean isWhite, Texture texture) {
        super(isWhite, texture);
    }

    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean canMove(Board board, Spot[][] spots,Spot start, Spot end) {
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
                if(spots[currentX][currentY].getPiece() != null) {
                    return false;
                }
                currentX += directionX;
                currentY += directionY;
            }
            if(!isAICalculate()) {
                if(end.getPiece() != null) {
                    board.getSpot(end.getX(), end.getY()).setCanBeCaptured(true);
                }
            }
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean calculateMove(Board board, Spot checkSpot) {
        Board testBoard = board.cloneBoard();
        Spot[][] spots = testBoard.getSpots();
        for (int[] move: bishopMoves()) {
            for(int i = 1; i <= 7; i++) {
                int x = move[0] * i + checkSpot.getX();
                int y = move[1] * i + checkSpot.getY();
                if(board.isWithinBoard(x, y)) {
                    if(calculateOneMove(testBoard, spots,checkSpot, x, y)) {
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
        Board testBoard = board.cloneBoard();
        Spot[][] spots = testBoard.getSpots();
        for (int[] move: bishopMoves()) {
            for(int i = 1; i <= 7; i++) {
                int x = move[0] * i + checkSpot.getX();
                int y = move[1] * i + checkSpot.getY();
                if(board.isWithinBoard(x, y)) {
                   calculateForOnePoint(board, testBoard,spots,checkSpot, x, y);
                }else {
                    break;
                }
            }
        }
    }

    @Override
    public List<Move> getValidMoves(Board board, Spot[][] spots, Spot checkSpot) {
        return List.of();
    }
}
