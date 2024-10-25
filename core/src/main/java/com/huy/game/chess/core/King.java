package com.huy.game.chess.core;

import com.badlogic.gdx.graphics.Texture;
import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.enums.PieceType;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    private boolean hasMove = false;
    private boolean isSafe = true;
    private boolean canCastlingKingSide = true;
    private boolean canCastlingQueenSide = true;

    public boolean isHasMove() {
        return hasMove;
    }

    public void setHasMove() {
        hasMove = true;
    }

    public void setSafe(boolean isSafe) {
        this.isSafe = isSafe;
    }

    public King(boolean isWhite, Texture texture) {
        super(isWhite, texture);
        setType(PieceType.KING);
    }

    public boolean isCanCastlingKingSide() {
        return canCastlingKingSide;
    }

    public void setCanCastlingKingSide(boolean canCastlingKingSide) {
        this.canCastlingKingSide = canCastlingKingSide;
    }

    public boolean isCanCastlingQueenSide() {
        return canCastlingQueenSide;
    }

    public void setCanCastlingQueenSide(boolean canCastlingQueenSide) {
        this.canCastlingQueenSide = canCastlingQueenSide;
    }

    public King(King king) {
        super(king.isWhite());
        hasMove = king.hasMove;
        isSafe = king.isSafe;
        canCastlingKingSide = king.canCastlingKingSide;
        canCastlingQueenSide = king.canCastlingQueenSide;
        setType(PieceType.KING);
    }

    @Override
    public MoveType canMove(Board board, Spot[][] spots, Spot start, Spot end) {
        if(end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return MoveType.CAN_NOT_MOVE;
        }
        int x = Math.abs(end.getX() - start.getX());
        int y = Math.abs(end.getY() - start.getY());
        if(x == 0 && y == 2) {
            if(this.isSafe && !this.hasMove) {
                int directionY = Integer.signum(end.getY() - start.getY());
                if(directionY > 0) {
                    if (canCastlingKingSide) {
                        for(int i = 1; i <= 2; i++) {
                            Piece checkPiece = spots[start.getX()][start.getY() + directionY * i].getPiece();
                            if(checkPiece != null) {
                                return MoveType.CAN_NOT_MOVE;
                            }else {
                                if(!board.isPositionSafe(start.getX(), start.getY() + directionY * i, this.isWhite())) {
                                    return MoveType.CAN_NOT_MOVE;
                                }
                            }
                        }
                        return MoveType.CASTLING_KING_SIDE;
                    }
                }else {
                    if (canCastlingQueenSide) {
                        for(int i = 1; i <= 2; i++) {
                            Piece checkPiece = spots[start.getX()][start.getY() + directionY * i].getPiece();
                            if(checkPiece != null) {
                                return MoveType.CAN_NOT_MOVE;
                            }else {
                                if(!board.isPositionSafe(start.getX(), start.getY() + directionY * i, this.isWhite())) {
                                    return MoveType.CAN_NOT_MOVE;
                                }
                            }
                        }
                        return MoveType.CASTLING_QUEEN_SIDE;
                    }
                }
            }
            return MoveType.CAN_NOT_MOVE;
        }else if (x <= 1 && y <= 1) {
            if(end.getPiece()!= null) {
                return MoveType.CAPTURE;
            }
            return MoveType.NORMAL;
        }else {
            return MoveType.CAN_NOT_MOVE;
        }
    }

    @Override
    public boolean isCheckOpponentKing(Board board, Spot[][] spots, Spot spot) {
        return false;
    }

    @Override
    public boolean calculateMove(Board board, Spot checkSpot) {
        Board testBoard = board.cloneBoard();
        Spot[][] spots = testBoard.getSpots();
        for(int i = -1; i <= 1; i++) {
            int x = checkSpot.getX() + i;
            for(int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                int y = checkSpot.getY() + j;
                if(board.isWithinBoard(x, y)) {
                    if(calculateOneMove(testBoard, spots,checkSpot, x, y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void calculateForPoint(Board board, Spot checkSpot) {
        Board testBoard = board.cloneBoard();
        Spot[][] spots = testBoard.getSpots();
        for(int i = -1; i <= 1; i++) {
            int x = checkSpot.getX() + i;
            for(int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                int y = checkSpot.getY() + j;
                if(board.isWithinBoard(x, y)) {
                    calculateForOnePoint(board,testBoard, spots, checkSpot, x, y);
                }
            }
        }
        if(board.isWithinBoard(checkSpot.getX(), checkSpot.getY() + 2)){
            calculateForOnePoint(board, testBoard,spots,checkSpot, checkSpot.getX(), checkSpot.getY() + 2);
        }
        if(board.isWithinBoard(checkSpot.getX(), checkSpot.getY() - 2)){
            calculateForOnePoint(board, testBoard,spots,checkSpot, checkSpot.getX(), checkSpot.getY() - 2);
        }
    }

    @Override
    public boolean calculateOneMove(Board board, Spot[][] spots, Spot checkSpot, int x, int y) {
        Spot start = spots[checkSpot.getX()][checkSpot.getY()];
        Spot end = spots[x][y];
        Move move = new Move(start, end);
        MoveType moveType = canMove(board, spots,start, end);
        move.setMoveType(moveType);
        if(moveType != MoveType.CAN_NOT_MOVE) {
            move.makeMove(board);
            if(board.isPositionSafe(x, y, end.getPiece().isWhite())) {
                move.unMove(board);
                return true;
            }
        }
        move.unMove(board);
        return false;
    }

    @Override
    public void calculateForOnePoint(Board board, Board testBoard, Spot[][] spots, Spot checkSpot, int x, int y) {
        Spot start = spots[checkSpot.getX()][checkSpot.getY()];
        Spot end = spots[x][y];
        Move move = new Move(start, end);
        MoveType moveType = canMove(testBoard, spots, start, end);
        move.setMoveType(moveType);
        if(moveType != MoveType.CAN_NOT_MOVE) {
            move.makeMove(testBoard);
            if(testBoard.isPositionSafe(x, y, checkSpot.getPiece().isWhite())) {
                board.getSpot(x, y).setShowMovePoint(true);
                if (moveType == MoveType.CAPTURE) {
                    board.getSpot(x, y).setCanBeCaptured(true);
                }
            }
            move.unMove(testBoard);
        }
    }

    @Override
    public List<Move> getValidMoves(Board board, Spot[][] spots, Spot checkSpot) {
        List<Move> list = new ArrayList<>();
        for(int i = -1; i <= 1; i++) {
            int x = checkSpot.getX() + i;
            for(int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                int y = checkSpot.getY() + j;
                if(board.isWithinBoard(x, y)) {
                    if(calculateOneMove(board, spots,checkSpot, x, y)) {
                        list.add(new Move(checkSpot, spots[x][y]));
                    }
                }
            }
        }
        if(board.isWithinBoard(checkSpot.getX(), checkSpot.getY() + 2)){
            if(calculateOneMove(board, spots,checkSpot, checkSpot.getX(), checkSpot.getY() + 2)) {
                list.add(new Move(checkSpot, spots[checkSpot.getX()][checkSpot.getY() + 2]));
            }
        }
        if(board.isWithinBoard(checkSpot.getX(), checkSpot.getY() - 2)){
            if(calculateOneMove(board, spots,checkSpot, checkSpot.getX(), checkSpot.getY() - 2)) {
                list.add(new Move(checkSpot, spots[checkSpot.getX()][checkSpot.getY() - 2]));
            }
        }
        return list;
    }
}
