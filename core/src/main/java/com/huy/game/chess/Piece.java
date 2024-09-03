package com.huy.game.chess;

import com.badlogic.gdx.graphics.Texture;

import java.util.List;

public abstract class Piece {

    private boolean isWhite;
    private Texture texture;
    private boolean isAICalculate;

    public Piece(boolean isWhite, Texture texture) {
        this.isWhite = isWhite;
        this.texture = texture;
    }

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isAICalculate() {
        return isAICalculate;
    }

    public void setAICalculate(boolean AICalculate) {
        isAICalculate = AICalculate;
    }

    public boolean calculateOneMove(Board board, Spot[][] spots, Spot checkSpot, int x, int y) {
        Spot start = spots[checkSpot.getX()][checkSpot.getY()];
        Spot end = spots[x][y];
        Move move = new Move(start.getX(), start.getY(), x, y);
        if(canMove(board, spots,start, end)) {
            move.makeMove(spots);
            if(board.isKingSafe(checkSpot.getPiece().isWhite)) {
                move.unMove(spots);
                return true;
            }
        }
        move.unMove(spots);
        return false;
    }

    public void calculateForOnePoint(Board board, Spot[][] spots,Spot checkSpot, int x, int y) {
        Spot start = spots[checkSpot.getX()][checkSpot.getY()];
        Spot end = spots[x][y];
        Move move = new Move(start.getX(), start.getY(), x, y);
        if(canMove(board, spots, start, end)) {
            move.makeMove(spots);
            if(board.isKingSafe(checkSpot.getPiece().isWhite())) {
                board.getSpot(x, y).setShowMovePoint(true);
            }
            move.unMove(spots);
        }
    }

    public boolean isValidMove(Board board,Spot[][] spots, Spot checkSpot, int x, int y) {
        Spot start = spots[checkSpot.getX()][checkSpot.getY()];
        Spot end = spots[x][y];
        Move move = new Move(start.getX(), start.getY(), end.getX(),end.getY());
        if(canMove(board, spots,start, end)) {
            move.makeMove(spots);
            if(board.isKingSafe(checkSpot.getPiece().isWhite())) {
                board.makeTempMove(start, end);
                return board.isKingSafe(checkSpot.getPiece().isWhite());
            }
        }
        return false;
    }

    public abstract boolean canMove(Board board, Spot[][] spots,Spot start, Spot end);

    public abstract boolean calculateMove(Board board, Spot checkSpot);

    public abstract void calculateForPoint(Board board, Spot checkSpot);

    public abstract List<Move> getValidMoves(Board board ,Spot[][] spots, Spot checkSpot);
}
