package com.huy.game.chess.core;

import com.badlogic.gdx.graphics.Texture;
import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.enums.PieceType;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public abstract class Piece {

    private boolean isWhite;
    private Texture texture;
    private PieceType type;

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

    public void setType(PieceType type) {
        this.type = type;
    }

    public PieceType getType() {
        return type;
    }

    public boolean calculateOneMove(Board board, Spot[][] spots, Spot checkSpot, int x, int y) {
        Spot start = spots[checkSpot.getX()][checkSpot.getY()];
        Spot end = spots[x][y];
        Move move = new Move(start, end);
        MoveType moveType = canMove(board, spots,start, end);
        move.setMoveType(moveType);
        if(moveType != MoveType.CAN_NOT_MOVE) {
            move.setMoveType(moveType);
            move.makeMove(board);
            if(board.isKingSafe(end.getPiece().isWhite)) {
                move.unMove(board);
                return true;
            }
        }
        move.unMove(board);
        return false;
    }

    public void calculateForOnePoint(Board board, Board testBoard, Spot[][] spots, Spot checkSpot, int x, int y) {
        Spot start = spots[checkSpot.getX()][checkSpot.getY()];
        Spot end = spots[x][y];
        Move move = new Move(start, end);
        MoveType moveType = canMove(testBoard, spots, start, end);
        move.setMoveType(moveType);
        if(moveType != MoveType.CAN_NOT_MOVE) {
            move.makeMove(testBoard);
            if(testBoard.isKingSafe(checkSpot.getPiece().isWhite())) {
                board.getSpot(x, y).setShowMovePoint(true);
                if (moveType == MoveType.CAPTURE) {
                    board.getSpot(x, y).setCanBeCaptured(true);
                }
            }
            move.unMove(testBoard);
        }
    }

    public abstract MoveType canMove(Board board, Spot[][] spots, Spot start, Spot end);

    public abstract boolean isCheckOpponentKing(Board board, Spot[][] spots, Spot spot);

    public abstract boolean calculateMove(Board board, Spot checkSpot);

    public abstract void calculateForPoint(Board board, Spot checkSpot);

    public abstract List<Move> getValidMoves(Board board, Spot[][] spots,Spot checkSpot);

    public Map.Entry<Integer, Boolean> countSamePieceCanMoveToOneSpot(Board board, Spot[][] spots, Spot start, Spot checkSpot) {
        return new AbstractMap.SimpleEntry<>(0, true);
    }
}
