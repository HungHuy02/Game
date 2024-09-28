package com.huy.game.chess.core;

import com.badlogic.gdx.graphics.Texture;
import com.huy.game.chess.enums.PieceType;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(boolean isWhite, Texture texture) {
        super(isWhite, texture);
        setType(PieceType.KNIGHT);
    }

    public Knight(boolean isWhite) {
        super(isWhite);
        setType(PieceType.KNIGHT);
    }

    private int[][] knightMoves() {
        return new int[][]{
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
    }

    @Override
    public boolean canMove(Board board, Spot[][] spots, Spot start, Spot end) {
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
        Board testBoard = board.cloneBoard();
        Spot[][] spots = testBoard.getSpots();
        for (int[] move: knightMoves()) {
            int x = move[0] + checkSpot.getX();
            int y = move[1] + checkSpot.getY();
            if(board.isWithinBoard(x, y)) {
                if(calculateOneMove(testBoard, spots,checkSpot, x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void calculateForPoint(Board board, Spot checkSpot) {
        Board testBoard = board.cloneBoard();
        Spot[][] spots = testBoard.getSpots();
        for (int[] move: knightMoves()) {
            int x = move[0] + checkSpot.getX();
            int y = move[1] + checkSpot.getY();
            if(board.isWithinBoard(x, y)) {
                calculateForOnePoint(board, testBoard,spots,checkSpot, x, y);
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
                if(calculateOneMove(board, spots,checkSpot, x, y)) {
                    list.add(new Move(checkSpot, spots[x][y]));
                }
            }
        }
        setAICalculate(false);
        return list;
    }
}
