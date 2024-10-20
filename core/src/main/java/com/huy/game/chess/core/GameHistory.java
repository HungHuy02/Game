package com.huy.game.chess.core;

import com.badlogic.gdx.utils.StringBuilder;
import com.huy.game.chess.core.notation.AlgebraicNotation;
import com.huy.game.chess.core.notation.FEN;
import com.huy.game.chess.manager.ChessGameManager;
import com.huy.game.chess.manager.ChessImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameHistory {

    private final List<String> movedList = new ArrayList<>();
    private final List<Long> stateHashList = new ArrayList<>();
    private final List<String> fenList = new ArrayList<>();
    private final Map<Integer, int[]> timeMap = new HashMap<>();
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

    public void addTimeRemain(int index, int player1RemainTime, int player2RemainTime) {
        timeMap.put(index, new int[] {player1RemainTime, player2RemainTime});
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

    public String addMove(Spot start, Spot end, Move move) {
        halfmoveClock++;
        String beforeMove = AlgebraicNotation.changePositionToSimpleAlgebraicNotation(start);
        String afterMove = AlgebraicNotation.changePositionToSimpleAlgebraicNotation(end);
        StringBuilder builder = new StringBuilder(beforeMove);
        builder.append(afterMove);
        movedList.add(builder.toString());
        return AlgebraicNotation.changeToFullAlgebraicNotation(start, end, move, this);
    }

    public void handleMoveColor(Board board, int index) {
        String move = getMove(index);
        board.getSpot(
            AlgebraicNotation.changeRowAlgebraicNotationToRowPosition(move.charAt(1)),
            AlgebraicNotation.changeColAlgebraicNotationToColPosition(move.charAt(0))
        ).setShowColor(true);
        board.getSpot(
            AlgebraicNotation.changeRowAlgebraicNotationToRowPosition(move.charAt(3)),
            AlgebraicNotation.changeColAlgebraicNotationToColPosition(move.charAt(2))
        ).setShowColor(true);
    }

    public Board changeFENToBoard(int index, ChessImage chessImage) {
        Board board = FEN.fenToBoard(getFEN(index), chessImage);
        handleMoveColor(board, index);
        return board;
    }

    public String getMove(int index) {
        return movedList.get(index);
    }

    public void addBoard(Board board) {
        boardList.add(board.cloneBoard());
    }

    public String getNewestFEN() {
        return fenList.get(fenList.size() - 1);
    }

    public String getFEN(int index) {
        return fenList.get(index);
    }

    public void resetHalfmoveClock() {
        halfmoveClock = 0;
    }

    public int getNumberBoardSaved() {
        return movedList.size();
    }
}
