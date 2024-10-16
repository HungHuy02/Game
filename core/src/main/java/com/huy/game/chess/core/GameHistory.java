package com.huy.game.chess.core;

import com.badlogic.gdx.utils.StringBuilder;
import com.huy.game.chess.core.notation.AlgebraicNotation;
import com.huy.game.chess.core.notation.FEN;
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
        String beforeMove = AlgebraicNotation.changeToFullAlgebraicNotation(start.getX(), start.getY(), start.getPiece(),null, this);
        String afterMove = AlgebraicNotation.changeToFullAlgebraicNotation(end.getX(), end.getY(), start.getPiece(), end.getPiece(), this);
        StringBuilder builder = new StringBuilder(beforeMove);
        builder.append(' ');
        builder.append(afterMove);
        movedList.add(builder.toString());
        return afterMove;
    }

    public void handleMoveColor(Board board, int index) {
        String[] positions = getMove(index).split(" ");
        board.getSpot(
            AlgebraicNotation.changeRowAlgebraicNotationToRowPosition(positions[0].charAt(positions[0].length() - 1)),
            AlgebraicNotation.changeColAlgebraicNotationToColPosition(positions[0].charAt(positions[0].length() - 2))
        ).setShowColor(true);
        board.getSpot(
            AlgebraicNotation.changeRowAlgebraicNotationToRowPosition(positions[1].charAt(positions[1].length() - 1)),
            AlgebraicNotation.changeColAlgebraicNotationToColPosition(positions[1].charAt(positions[1].length() - 2))
        ).setShowColor(true);
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

    public void resetHalfmoveClock() {
        halfmoveClock = 0;
    }
}
