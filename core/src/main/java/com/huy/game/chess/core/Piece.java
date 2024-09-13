package com.huy.game.chess.core;

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
        Move move = new Move(start, end);
        if(canMove(board, spots,start, end)) {
            move.makeMove(board);
            if(board.isKingSafe(end.getPiece().isWhite)) {
                move.unMove(board);
                return true;
            }
        }
        move.unMove(board);
        return false;
    }

    public void calculateForOnePoint(Board board, Board testBoard, Spot[][] spots,Spot checkSpot, int x, int y) {
        Spot start = spots[checkSpot.getX()][checkSpot.getY()];
        Spot end = spots[x][y];
        Move move = new Move(start, end);
        if(canMove(board, spots, start, end)) {
            move.makeMove(testBoard);
            if(testBoard.isKingSafe(checkSpot.getPiece().isWhite())) {
                board.getSpot(x, y).setShowMovePoint(true);
            }
            move.unMove(testBoard);
        }
    }

    public abstract boolean canMove(Board board, Spot[][] spots,Spot start, Spot end);

    public abstract boolean calculateMove(Board board, Spot checkSpot);

    public abstract void calculateForPoint(Board board, Spot checkSpot);

    public abstract List<Move> getValidMoves(Board board, Spot[][] spots,Spot checkSpot);
}
