package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece{

    private boolean isAICalculate = false;

    public Knight(boolean isWhite, Texture texture) {
        super(isWhite, texture);
    }

    public Knight(boolean isWhite) {
        super(isWhite);
    }

    private int[][] knightMoves() {
        return new int[][]{
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
    }

    @Override
    public boolean canMove(Board board, Spot[][] spots,Spot start, Spot end) {
        if(end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }
        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        if(x * y == 2) {
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
        Spot[][] spots = board.cloneSpots(board.getSpots());
        for (int[] move: knightMoves()) {
            int x = move[0] + checkSpot.getX();
            int y = move[1] + checkSpot.getY();
            if(board.isWithinBoard(x, y)) {
                if(calculateOneMove(board, spots,checkSpot, x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void calculateForPoint(Board board, Spot checkSpot) {
        Spot[][] spots = board.cloneSpots(board.getSpots());
        for (int[] move: knightMoves()) {
            int x = move[0] + checkSpot.getX();
            int y = move[1] + checkSpot.getY();
            if(board.isWithinBoard(x, y)) {
                calculateForOnePoint(board, spots,checkSpot, x, y);
            }
        }
    }

    @Override
    public List<Move> getValidMoves(Board board, Spot[][] spots, Spot checkSpot) {
        List<Move> list = new ArrayList<>();
        setAICalculate(true);
        for (int[] move: knightMoves()) {
            int x = move[0] + checkSpot.getX();
            int y = move[1] + checkSpot.getY();
            if(board.isWithinBoard(x, y)) {
                if(isValidMove(board, spots,checkSpot, x, y)) {
                    list.add(new Move(checkSpot.getX(), checkSpot.getY(), x, y));
                }
            }
        }
        setAICalculate(false);
        return list;
    }
}
