package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;

public class Pawn extends Piece{

    private int turn;
    private boolean isMoveTwo = false;
    private boolean isCalculate = false;

    public Pawn(Pawn pawn) {
        super(pawn.isWhite());
        turn = pawn.turn;
        isMoveTwo = pawn.isMoveTwo;
    }

    public Pawn(boolean isWhite, Texture texture) {
        super(isWhite, texture);
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public boolean isMoveTwo() {
        return isMoveTwo;
    }

    private int[][] pawnMoves(Spot checkSpot) {
        int[][] pawnMoves;
        if(checkSpot.getPiece().isWhite()) {
            pawnMoves = new int[][]{
                {1, 0}, {1, -1}, {1, 1}, {2, 0}
            };
        }else {
            pawnMoves = new int[][]{
                {-1, 0}, {-1, -1}, {-1, 1}, {-2, 0}
            };
        }
        return pawnMoves;
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        if(end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }
        int x = end.getX() - start.getX();
        int y = Math.abs(end.getY() - start.getY());
        if(this.isWhite()) {
            if(x < 0) {
                return false;
            }
        }else {
            if(x > 0) {
                return false;
            }
        }
        x = Math.abs(x);
        if(x > 2 || y >= 2) {
            return false;
        } else if(x > 1 && y == 0){
            if(board.getTempSpot(Integer.signum(end.getX() - start.getX()) + start.getX(), start.getY()).getPiece() == null) {
                if(this.isWhite() && start.getX() == 1 && end.getPiece() == null) {
                    isMoveTwo = true;
                    return true;
                }
                if(!this.isWhite() && start.getX() == 6 && end.getPiece() == null) {
                    isMoveTwo = true;
                    return true;
                }
            }
            return false;
        }else if(x == 1 && y == 1){
            if(end.getPiece() != null) {
                isMoveTwo = false;
                board.getSpot(end.getX(), end.getY()).setCanBeCaptured(true);
                return true;
            }else {
                Piece checkPiece = board.getTempSpot(start.getX(), start.getY() + (end.getY() - start.getY())).getPiece();
                if(checkPiece instanceof Pawn && checkPiece.isWhite() != this.isWhite()) {
                    Pawn pawn = (Pawn) checkPiece;
                    if(pawn.isMoveTwo) {
                        if(pawn.isWhite()) {
                            if(pawn.turn == this.turn) {
                                if(!isCalculate) {
                                    board.setSpot(start.getX(), start.getY() + (end.getY() - start.getY()), null);
                                }
                                return true;
                            }
                        }else {
                            if(this.turn - pawn.turn == 1) {
                                if(!isCalculate) {
                                    board.setSpot(start.getX(), start.getY() + (end.getY() - start.getY()), null);
                                }
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        }else if(x == 0 && y != 0) {
            return false;
        }else if(y == 1) {
            return false;
        }else {
            if(end.getPiece() == null) {
                isMoveTwo = false;
                return true;
            }else {
                return false;
            }
        }
    }

    @Override
    public boolean calculateMove(Board board, Spot checkSpot) {
        isCalculate = true;
        for (int[] move: pawnMoves(checkSpot)) {
            int x = move[0] + checkSpot.getX();
            int y = move[1] + checkSpot.getY();
            if(board.isWithinBoard(x, y)) {
                if(calculateOneMove(board, checkSpot, x, y)) {
                    isCalculate = false;
                    return true;
                }
            }
        }
        isCalculate = false;
        return false;
    }

    @Override
    public void calculateForPoint(Board board, Spot checkSpot) {
        isCalculate = true;
        for (int[] move: pawnMoves(checkSpot)) {
            int x = move[0] + checkSpot.getX();
            int y = move[1] + checkSpot.getY();
            if(board.isWithinBoard(x, y)) {
                calculateForOnePoint(board, checkSpot, x, y);
            }
        }
        isCalculate = false;
    }
}
