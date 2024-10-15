package com.huy.game.chess.core;

import com.badlogic.gdx.utils.StringBuilder;
import com.huy.game.chess.manager.ChessGameManager;

import java.util.ArrayList;
import java.util.List;

public class GameHistory {

    private final List<String> movedList = new ArrayList<>();
    private final List<Long> stateHashList = new ArrayList<>();
    private final List<String> fenList = new ArrayList<>();
    private final List<Board> boardList = new ArrayList<>();
    private int halfmoveClock = 0;

    public GameHistory() {

    }

    public int getHalfmoveClock() {
        return halfmoveClock;
    }

    public void addStateHash(long hashValue) {
        checkThreefoldRepetition(hashValue);
        check50MovesRule();
        stateHashList.add(hashValue);
    }

    public void addFEN(Board board, boolean isWhite, ChessGameManager manager) {
        fenList.add(FEN.generateFEN(board, board.getSpots(), isWhite, this, manager));
    }

    private void check50MovesRule() {
        if (halfmoveClock == 100) {

        }
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
        halfmoveClock++;
        String beforeMove = changeToFullAlgebraicNotation(start.getX(), start.getY(), start.getPiece(),null);
        String afterMove = changeToFullAlgebraicNotation(end.getX(), end.getY(), start.getPiece(), end.getPiece());
        movedList.add(beforeMove + " " + afterMove);
        return afterMove;
    }

    public String changeToFullAlgebraicNotation(int x, int y, Piece startPiece,Piece endPiece) {
        StringBuilder builder = new StringBuilder();
        switch (startPiece.getType()) {
            case PAWN -> halfmoveClock = 0;
            case KNIGHT ->  builder.append('N');
            case BISHOP -> builder.append('B');
            case QUEEN -> builder.append('Q');
            case ROOK -> builder.append('R');
            case KING -> builder.append('K');
        }
        if(endPiece != null) {
            builder.append('x');
            halfmoveClock = 0;
        }
        changePositionToAlgebraicNotation(builder, x, y);
        return builder.toString();
    }

    public void changePositionToAlgebraicNotation(StringBuilder builder ,int x, int y) {
        builder.append((char) ('a' + y));
        builder.append(x + 1);
    }

    public String getMove(int index) {
        return movedList.get(index - index / 3 - 1);
    }

    public void addBoard(Board board) {
        boardList.add(board.cloneBoard());
    }

    public String getNewestFEN() {
        return fenList.get(fenList.size() - 1);
    }

    public String getFEN(int index) {
        return fenList.get(index - index / 3 - 1);
    }
}
