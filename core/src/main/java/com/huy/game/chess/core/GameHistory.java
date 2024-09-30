package com.huy.game.chess.core;

import java.util.ArrayList;
import java.util.List;

public class GameHistory {

    private final List<String> movedList = new ArrayList<>();
    private final List<Long> stateHashList = new ArrayList<>();
    private final List<Board> boardList = new ArrayList<>();

    public GameHistory() {

    }

    public void addStateHash(long hashValue) {
        checkThreefoldRepetition(hashValue);
        stateHashList.add(hashValue);
    }

    private void checkThreefoldRepetition(long newValue) {
        int count = 1;
        for (long value: stateHashList) {
            if(newValue == value) {
                count++;
                if(count == 3) {
                    break;
                }
            }
        }
    }

    public String addMove(Spot start, Spot end) {
        String beforeMove = changeToFullAlgebraicNotation(start.getX(), start.getY(), start.getPiece(),null);
        String afterMove = changeToFullAlgebraicNotation(end.getX(), end.getY(), start.getPiece(), end.getPiece());
        movedList.add(beforeMove + " " + afterMove);
        return afterMove;
    }

    public String changeToFullAlgebraicNotation(int x, int y, Piece startPiece,Piece endPiece) {
        String move = endPiece != null ? "x" + changePositionToAlgebraicNotation(x, y) : changePositionToAlgebraicNotation(x, y);
        switch (startPiece.getType()) {
            case KNIGHT ->  move = "N" + move;
            case BISHOP -> move = "B" + move;
            case QUEEN -> move = "Q" + move;
            case ROOK -> move = "R" + move;
            case KING -> move = "K" + move;
        }
        return move;
    }

    public String changePositionToAlgebraicNotation(int x, int y) {
        char col = (char) ('a' + y);
        int row = x + 1;
        return "" + col + row;
    }

    public String getMove(int index) {
        if(index % 3 == 1) {
            return movedList.get(index + index / 3);
        }else {
            return movedList.get(index + index / 3 + 1);
        }
    }

    public void addBoard(Board board) {
        boardList.add(board.cloneBoard());
    }
}
