package com.huy.game.chess.core;

import com.badlogic.gdx.graphics.Texture;
import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.enums.PieceType;

import java.util.ArrayList;
import java.util.List;

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
        setType(PieceType.ROOK);
    }

    public Rook(boolean isWhite, Texture texture) {
        super(isWhite, texture);
        setType(PieceType.ROOK);
    }

    @Override
    public MoveType canMove(Board board, Spot[][] spots, Spot start, Spot end) {
        if(end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return MoveType.CAN_NOT_MOVE;
        }
        int x = Math.abs(end.getX() - start.getX());
        int y = Math.abs(end.getY() - start.getY());
        if(x == 0 || y == 0) {
            int directionX = Integer.signum(end.getX() - start.getX());
            int directionY = Integer.signum(end.getY() - start.getY());
            int currentX = start.getX() + directionX;
            int currentY = start.getY() + directionY;
            while(currentX != end.getX() || currentY != end.getY()) {
                if(spots[currentX][currentY].getPiece() != null) {
                    return MoveType.CAN_NOT_MOVE;
                }
                currentX += directionX;
                currentY += directionY;
            }
            hasMove = true;
            if(end.getPiece() != null) {
                board.getSpot(end.getX(), end.getY()).setCanBeCaptured(true);
                return MoveType.CAPTURE;
            }
            return MoveType.NORMAL;
        }else {
            return MoveType.CAN_NOT_MOVE;
        }
    }

    @Override
    public boolean calculateMove(Board board, Spot checkSpot) {
        Board testBoard = board.cloneBoard();
        Spot[][] spots = testBoard.getSpots();
        for (int[] move: rookMoves()) {
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
        for (int[] move: rookMoves()) {
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
        List<Move> list = new ArrayList<>();
        setAICalculate(true);
        for (int[] move: rookMoves()) {
            for(int i = 1; i <= 7; i++) {
                int x = move[0] * i + checkSpot.getX();
                int y = move[1] * i + checkSpot.getY();
                if(board.isWithinBoard(x, y)) {
                    if(calculateOneMove(board, spots, checkSpot, x, y)) {
                        list.add(new Move(checkSpot, spots[x][y]));
                    }
                }else {
                    break;
                }
            }
        }
        setAICalculate(false);
        return list;
    }
}
