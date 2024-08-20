package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;

public class Pawn extends Piece{

    private int turn;
    private boolean isMoveTwo = false;

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
            if(this.isWhite() && start.getX() == 1 && end.getPiece() == null) {
                isMoveTwo = true;
                return true;
            }
            if(!this.isWhite() && start.getX() == 6 && end.getPiece() == null) {
                isMoveTwo = true;
                return true;
            }
            return false;
        }else if(x == 1 && y == 1){
            if(end.getPiece() != null) {
                return true;
            }else {
                Piece checkPiece = board.getSpot(start.getX(), start.getY() + (end.getY() - start.getY())).getPiece();
                if(checkPiece instanceof Pawn) {
                    Pawn pawn = (Pawn) checkPiece;
                    if(pawn.isMoveTwo) {
                        if(pawn.isWhite()) {
                            if(pawn.turn == this.turn) {
                                board.setSpot(start.getX(), start.getY() + (end.getY() - start.getY()), new Spot(null, start.getX(), start.getY() + (end.getY() - start.getY())));
                                return true;
                            }
                        }else {
                            if(this.turn - pawn.turn == 1) {
                                board.setSpot(start.getX(), start.getY() + (end.getY() - start.getY()), new Spot(null, start.getX(), start.getY() + (end.getY() - start.getY())));
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
            return end.getPiece() == null;
        }
    }
}
