package com.huy.game.chess.core;

import com.badlogic.gdx.graphics.Texture;
import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.enums.PieceType;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Knight extends Piece {

    public Knight(boolean isWhite, Texture texture) {
        super(isWhite, texture);
        setType(PieceType.KNIGHT);
    }

    public Knight(boolean isWhite) {
        super(isWhite);
        setType(PieceType.KNIGHT);
    }

    public static int[][] knightMoves() {
        return new int[][]{
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
    }

    @Override
    public MoveType canMove(Board board, Spot[][] spots, Spot start, Spot end) {
        if(end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return MoveType.CAN_NOT_MOVE;
        }
        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        if(x * y == 2) {
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
        int x = Math.abs(king.getX() - spot.getX());
        int y = Math.abs(king.getY() - spot.getY());
        if(x * y == 2) {
            return true;
        }
        return false;
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
        for (int[] move: knightMoves()) {
            int x = move[0] + checkSpot.getX();
            int y = move[1] + checkSpot.getY();
            if(board.isWithinBoard(x, y)) {
                if(calculateOneMove(board, spots,checkSpot, x, y)) {
                    list.add(new Move(checkSpot, spots[x][y]));
                }
            }
        }
        return list;
    }

    @Override
    public Map.Entry<Integer, Boolean> countSamePieceCanMoveToOneSpot(Board board, Spot[][] spots, Spot start, Spot checkSpot, int number) {
        boolean row = true;
        if (number == 2) {
            for (int[] move: knightMoves()) {
                int x = move[0] + checkSpot.getX();
                int y = move[1] + checkSpot.getY();
                if (board.isWithinBoard(x, y)) {
                    if (x != start.getX() || y != start.getY()) {
                        Piece piece = spots[x][y].getPiece();
                        if(piece != null) {
                            if (piece.getType() == PieceType.KNIGHT && piece.isWhite() == isWhite()) {
                                if (y == start.getY()) {
                                    row = false;
                                }
                                return new AbstractMap.SimpleEntry<>(1, row);
                            }
                        }

                    }
                }
            }
        }else {
            int count = 0;
            row = false;
            boolean col = false;
            for (int[] move: knightMoves()) {
                int x = move[0] + checkSpot.getX();
                int y = move[1] + checkSpot.getY();
                if (board.isWithinBoard(x, y)) {
                    if (x != start.getX() || y != start.getY()) {
                        Piece piece = spots[x][y].getPiece();
                        if(piece != null) {
                            if (piece.getType() == PieceType.KNIGHT && piece.isWhite() == isWhite()) {
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
                        }

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
