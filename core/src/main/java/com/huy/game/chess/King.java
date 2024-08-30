package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;

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
    public boolean canMove(Board board, Spot start, Spot end) {
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
                        Piece checkPiece = board.getSpot(start.getX(), start.getY() + directionY * i).getPiece();
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
        for(int i = -1; i <= 1; i++) {
            int x = checkSpot.getX() + i;
            for(int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                int y = checkSpot.getY() + j;
                if(board.isWithinBoard(x, y)) {
                    if(calculateOneMove(board, checkSpot, x, y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void calculateForPoint(Board board, Spot checkSpot) {
        for(int i = -1; i <= 1; i++) {
            int x = checkSpot.getX() + i;
            for(int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                int y = checkSpot.getY() + j;
                if(board.isWithinBoard(x, y)) {
                    calculateForOnePoint(board, checkSpot, x, y);
                }
            }
        }
        isCalculate = true;
        calculateForOnePoint(board, checkSpot, checkSpot.getX(), checkSpot.getY() + 2);
        calculateForOnePoint(board, checkSpot, checkSpot.getX(), checkSpot.getY() - 2);
        isCalculate = false;
    }

    @Override
    public boolean calculateOneMove(Board board, Spot checkSpot, int x, int y) {
        Spot testSpot = board.getTempSpot(x, y);
        if(canMove(board, checkSpot, testSpot)) {
            if(board.isPositionSafe(x, y, checkSpot.getPiece().isWhite())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void calculateForOnePoint(Board board, Spot checkSpot, int x, int y) {
        Spot testSpot = board.getTempSpot(x, y);
        if(canMove(board, checkSpot, testSpot)) {
            if(board.isPositionSafe(x, y, checkSpot.getPiece().isWhite())) {
                board.getSpot(x, y).setShowMovePoint(true);
            }
        }
    }
}
