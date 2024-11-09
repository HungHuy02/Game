package com.huy.game.chess.core;

import com.badlogic.gdx.graphics.Texture;
import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.enums.PieceType;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Queen extends Piece {

    public Queen(boolean isWhite, Texture texture) {
        super(isWhite, texture);
        setType(PieceType.QUEEN);
    }

    public Queen(boolean isWhite) {
        super(isWhite);
        setType(PieceType.QUEEN);
    }

    public static int[][] queenMoves() {
        return new int[][]{
            {1, -1}, {1, 0}, {1, 1},{0, -1}, {0, 1}, {-1, -1}, {-1, 0}, {-1, 1}
        };
    }

    @Override
    public MoveType canMove(Board board, Spot[][] spots, Spot start, Spot end) {
        if(end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return MoveType.CAN_NOT_MOVE;
        }
        int x = Math.abs(end.getX() - start.getX());
        int y = Math.abs(end.getY() - start.getY());
        if(x == 0 || y == 0 || x == y) {
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
        if(distanceX == 0 || distanceY == 0 || distanceX == distanceY) {
            int directionX = Integer.signum(king.getX() - spot.getX());
            int directionY = Integer.signum(king.getY() - spot.getY());
            int currentX = spot.getX() + directionX;
            int currentY = spot.getY() + directionY;
            while(currentX != king.getX() || currentY != king.getY()) {
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
        for (int[] move: queenMoves()) {
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
        for (int[] move: queenMoves()) {
            for(int i = 1; i <= 7; i++) {
                int x = move[0] * i + checkSpot.getX();
                int y = move[1] * i + checkSpot.getY();
                if(board.isWithinBoard(x, y)) {
                    calculateForOnePoint(board, testBoard, spots,checkSpot, x, y);
                }else {
                    break;
                }
            }
        }
    }

    @Override
    public List<Move> getValidMoves(Board board, Spot[][] spots, Spot checkSpot) {
        List<Move> list = new ArrayList<>();
        for (int[] move: queenMoves()) {
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

    @Override
    public Map.Entry<Integer, Boolean> countSamePieceCanMoveToOneSpot(Board board, Spot[][] spots, Spot start, Spot checkSpot, int number) {
        boolean row = true;
        int directionX = Integer.signum(start.getX() - checkSpot.getX());
        int directionY = Integer.signum( start.getY() - checkSpot.getY());
        if (number == 2) {
            for (int[] move: queenMoves()) {
                if (move[0] != directionX || move[1] != directionY) {
                    int x = move[0] + checkSpot.getX();
                    int y = move[1] + checkSpot.getY();
                    while (board.isWithinBoard(x, y)) {
                        Piece piece = spots[x][y].getPiece();
                        if (piece != null) {
                            if (piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite()) {
                                if (y == start.getY()) {
                                    row = false;
                                }
                                return new AbstractMap.SimpleEntry<>(1, row);
                            }
                            break ;
                        }
                        x += move[0];
                        y += move[1];
                    }
                }
            }
        }else {
            int count = 0;
            row = false;
            boolean col = false;
            for (int[] move: queenMoves()) {
                if (move[0] != directionX || move[1] != directionY) {
                    int x = move[0] + checkSpot.getX();
                    int y = move[1] + checkSpot.getY();
                    while (board.isWithinBoard(x, y)) {
                        Piece piece = spots[x][y].getPiece();
                        if (piece != null) {
                            if (piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite()) {
                                count++;
                                if (y == start.getY()) {
                                    col = true;
                                }
                                if (x == start.getX()) {
                                    row = true;
                                }
                                if (col && row) {
                                    return new AbstractMap.SimpleEntry<>(2, true);
                                }
                            }
                            break ;
                        }
                        x += move[0];
                        y += move[1];
                    }
                }
            }
            if (col) {
                return new AbstractMap.SimpleEntry<>(1, false);
            }
            if (row || count != 0) {
                return new AbstractMap.SimpleEntry<>(1, true);
            }
        }
        return new AbstractMap.SimpleEntry<>(0, row);
    }
}
