package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;

import java.util.List;

public class King extends Piece{

    private boolean hasMove = false;
    private boolean isSafe = true;
    private boolean isCastling = false;
    private boolean isCalculate = false;

    public void setHasMove() {
        hasMove = true;
    }

    public void setSafe(boolean isSafe) {
        this.isSafe = isSafe;
    }

    public void setCastling(boolean castling) {
        isCastling = castling;
    }

    public boolean isCastling() {
        return isCastling;
    }

    public King(boolean isWhite, Texture texture) {
        super(isWhite, texture);
    }

    public King(King king) {
        super(king.isWhite());
        hasMove = king.hasMove;
        isSafe = king.isSafe;
        isCastling = king.isCastling;
    }

    @Override
    public boolean canMove(Board board, Spot[][] spots,Spot start, Spot end) {
        if(end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }
        int x = Math.abs(end.getX() - start.getX());
        int y = Math.abs(end.getY() - start.getY());
        if(x == 0 && y == 2) {
            if(this.isSafe && !this.hasMove) {
                int directionY = Integer.signum(end.getY() - start.getY());
                Piece rookPiece;
                if(directionY > 0) {
                    rookPiece = board.getSpot(start.getX(), 7).getPiece();
                }else {
                    rookPiece = board.getSpot(start.getX(), 0).getPiece();
                }
                if(rookPiece !=null && rookPiece.isWhite() == this.isWhite() && rookPiece instanceof Rook) {
                    if (((Rook) rookPiece).isHasMove()) {
                        return false;
                    }
                    for(int i = 1; i <= 2; i++) {
                        Piece checkPiece = board.getTempSpot(start.getX(), start.getY() + directionY * i).getPiece();
                        if(checkPiece != null) {
                            return false;
                        }else {
                            if(!board.isPositionSafe(start.getX(), start.getY() + directionY * i, this.isWhite())) {
                                return false;
                            }
                        }
                    }
                    if(!isCalculate) {
                        this.setHasMove();
                        if(directionY > 0) {
                            board.setSpot(start.getX(), 7, null);
                            board.setSpot(start.getX(), start.getY() + 1, rookPiece);
                        }else {
                            board.setSpot(start.getX(), 0, null);
                            board.setSpot(start.getX(), start.getY() - 1, rookPiece);
                        }
                        isCastling = true;
                    }
                    board.getSpot(start.getX(), start.getY() + directionY * 2).setShowMovePoint(true);
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }else {
            if (x <= 1 && y <= 1) {
                end.setShowMovePoint(true);
                return true;
            }else {
                return false;
            }
        }
    }

    @Override
    public boolean calculateMove(Board board, Spot checkSpot) {
        Spot[][] spots = board.cloneSpots(board.getSpots());
        for(int i = -1; i <= 1; i++) {
            int x = checkSpot.getX() + i;
            for(int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                int y = checkSpot.getY() + j;
                if(board.isWithinBoard(x, y)) {
                    if(calculateOneMove(board, spots,checkSpot, x, y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void calculateForPoint(Board board, Spot checkSpot) {
        Spot[][] spots = board.cloneSpots(board.getSpots());
        for(int i = -1; i <= 1; i++) {
            int x = checkSpot.getX() + i;
            for(int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                int y = checkSpot.getY() + j;
                if(board.isWithinBoard(x, y)) {
                    calculateForOnePoint(board, spots,checkSpot, x, y);
                }
            }
        }
        isCalculate = true;
        if(board.isWithinBoard(checkSpot.getX(), checkSpot.getY() + 2)){
            calculateForOnePoint(board, spots,checkSpot, checkSpot.getX(), checkSpot.getY() + 2);
        }
        if(board.isWithinBoard(checkSpot.getX(), checkSpot.getY() - 2)){
            calculateForOnePoint(board, spots,checkSpot, checkSpot.getX(), checkSpot.getY() - 2);
        }
        isCalculate = false;
    }

    @Override
    public List<Move> getValidMoves(Board board, Spot[][] spots, Spot checkSpot) {
        return List.of();
    }

    @Override
    public boolean calculateOneMove(Board board, Spot[][] spots,Spot checkSpot, int x, int y) {
        Spot start = spots[checkSpot.getX()][checkSpot.getY()];
        Spot end = spots[x][y];
        Move move = new Move(start.getX(), start.getY(), x, y);
        if(canMove(board, spots,start, end)) {
            move.makeMove(spots);
            if(board.isPositionSafe(x, y, checkSpot.getPiece().isWhite())) {
                move.unMove(spots);
                return true;
            }
        }
        move.unMove(spots);
        return false;
    }

    @Override
    public void calculateForOnePoint(Board board, Spot[][] spots,Spot checkSpot, int x, int y) {
        Spot start = spots[checkSpot.getX()][checkSpot.getY()];
        Spot end = spots[x][y];
        Move move = new Move(start.getX(), start.getY(), x, y);
        if(canMove(board, spots,start, end)) {
            move.makeMove(spots);
            if(board.isPositionSafe(x, y, checkSpot.getPiece().isWhite())) {
                board.getSpot(x, y).setShowMovePoint(true);
            }
            move.unMove(spots);
        }
    }
}
