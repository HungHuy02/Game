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
    private final StringBuilder pgn = new StringBuilder();
    private int halfmoveClock = 0;
    private boolean isThreefoldRepetition = false;

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

    public void addFEN(Board board, boolean isWhite) {
        fenList.add(FEN.generateFEN(board, board.getSpots(), isWhite, this));
    }

    public void addTimeRemain(int index, int player1RemainTime, int player2RemainTime) {
        timeMap.put(index, new int[] {player1RemainTime, player2RemainTime});
    }

    public boolean check50MovesRule() {
        if (halfmoveClock == 100) {
            return true;
        }
        return false;
    }

    public boolean checkThreefoldRepetition() {
        return isThreefoldRepetition;
    }

    private void checkThreefoldRepetition(long newValue) {
        int count = 1;
        for (long value: stateHashList) {
            if(newValue == value) {
                count++;
                if(count == 3) {
                    isThreefoldRepetition = true;
                    break;
                }
            }
        }
    }

    public String addMove(Board board, Spot start, Spot end, Move move) {
        halfmoveClock++;
        String beforeMove = AlgebraicNotation.changePositionToSimpleAlgebraicNotation(start);
        String afterMove = AlgebraicNotation.changePositionToSimpleAlgebraicNotation(end);
        StringBuilder builder = new StringBuilder(beforeMove);
        builder.append(afterMove);
        movedList.add(builder.toString());
        return AlgebraicNotation.changeToFullAlgebraicNotation(board, start, end, move, this);
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
        Board board = FEN.fenToBoard(getFEN(index + 1), chessImage);
        handleMoveColor(board, index);
        return board;
    }

    public String getMove(int index) {
        return movedList.get(index);
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

    public void appendString(String string) {
        pgn.append(string);
        pgn.append(' ');
    }

    public String getPGN(){
        return pgn.toString();
    }

    public void deleteOldSaved(int index) {
        for (;index < movedList.size(); index++) {
            fenList.removeLast();
            stateHashList.removeLast();
            movedList.removeLast();
        }
    }
}
