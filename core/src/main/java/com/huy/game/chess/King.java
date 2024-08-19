package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;

public class King extends Piece{

    private boolean hasMove = false;
    private boolean isSafe = true;

    public void setHasMove() {
        hasMove = true;
    }

    public void setSafe(boolean isSafe) {
        this.isSafe = isSafe;
    }

    public King(boolean isWhite, Texture texture) {
        super(isWhite, texture);
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
                    this.setHasMove();
                    if(directionY > 0) {
                        board.setSpot(start.getX(), 7, new Spot(null, start.getX(), 7));
                        board.setSpot(start.getX(), start.getY() + 1, new Spot(rookPiece, start.getX(), start.getY() + 1));
                    }else {
                        board.setSpot(start.getX(), 0, new Spot(null, start.getX(), 0));
                        board.setSpot(start.getX(), start.getY() - 1, new Spot(rookPiece, start.getX(), start.getY() - 1));
                    }
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }else return x <= 1 && y <= 1;
    }
}
