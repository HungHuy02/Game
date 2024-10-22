package com.huy.game.chess.core;

import com.badlogic.gdx.graphics.Texture;
import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.enums.PieceType;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    private int[][] bishopMoves() {
        return new int[][]{
            {1, -1}, {1, 1}, {-1, 1}, {-1, -1}
        };
    }

    public Bishop(boolean isWhite, Texture texture) {
        super(isWhite, texture);
        setType(PieceType.BISHOP);
    }

    public Bishop(boolean isWhite) {
        super(isWhite);
        setType(PieceType.BISHOP);
    }

    @Override
    public MoveType canMove(Board board, Spot[][] spots, Spot start, Spot end) {
        if(end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return MoveType.CAN_NOT_MOVE;
        }
        int x = Math.abs(end.getX() - start.getX());
        int y = Math.abs(end.getY() - start.getY());
        if(x == y) {
            int directionX = Integer.signum(end.getX() - start.getX());
            int directionY = Integer.signum(end.getY() - start.getY());
            int currentX = start.getX() + directionX;
            int currentY = start.getY() + directionY;
            while(currentX != end.getX() && currentY != end.getY()) {
                if(spots[currentX][currentY].getPiece() != null) {
                    return MoveType.CAN_NOT_MOVE;
                }
                currentX += directionX;
                currentY += directionY;
            }
            if(end.getPiece() != null) {
                return MoveType.CAPTURE;
            }
            return MoveType.NORMAL;
        }else {
            return MoveType.CAN_NOT_MOVE;
        }
    }

    @Override
    public boolean isCheckOpponentKing(Board board, Spot[][] spots, Spot spot) {
        Spot king = board.getKingSpot(!isWhite());
        int distanceX = Math.abs(king.getX() - spot.getX());
        int distanceY = Math.abs(king.getY() - spot.getY());
        if(distanceX == distanceY) {
            int directionX = Integer.signum(king.getX() - spot.getX());
            int directionY = Integer.signum(king.getY() - spot.getY());
            int currentX = spot.getX() + directionX;
            int currentY = spot.getY() + directionY;
            while(currentX != king.getX() && currentY != king.getY()) {
                if(spots[currentX][currentY].getPiece() != null) {
                    return false;
                }
                currentX += directionX;
                currentY += directionY;
            }
            return true;
        }
        return false;
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
        List<Move> list = new ArrayList<>();
        for (int[] move: bishopMoves()) {
            for(int i = 1; i <= 7; i++) {
                int x = move[0] * i + checkSpot.getX();
                int y = move[1] * i + checkSpot.getY();
                if(board.isWithinBoard(x, y)) {
                    if(calculateOneMove(board, spots,checkSpot, x, y)) {
                        list.add(new Move(checkSpot, spots[x][y]));
                    }
                }else {
                    break;
                }
            }
        }
        return list;
    }
}
