package com.huy.game.chess.core;

import com.badlogic.gdx.utils.StringBuilder;
import com.huy.game.chess.core.notation.AlgebraicNotation;
import com.huy.game.chess.core.notation.FEN;
import com.huy.game.chess.enums.GameResult;
import com.huy.game.chess.manager.ChessGameManager;
import com.huy.game.chess.manager.ChessImage;
import com.huy.game.chess.ui.NotationHistoryScrollPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameHistory {

    private final List<String> movedList = new ArrayList<>();
    private final List<Long> stateHashList = new ArrayList<>();
    private final List<String> fenList = new ArrayList<>();
    private final Map<Integer, int[]> timeMap = new HashMap<>();
    private final List<String> algebraicNotationList = new ArrayList<>();
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
        return halfmoveClock == 100;
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

    public String addMove(Board board, Spot start, Spot end, Move move, ChessGameManager chessGameManager) {
        halfmoveClock++;
        String beforeMove = AlgebraicNotation.changePositionToSimpleAlgebraicNotation(start);
        String afterMove = AlgebraicNotation.changePositionToSimpleAlgebraicNotation(end);
        StringBuilder builder = new StringBuilder(beforeMove);
        builder.append(afterMove);
        movedList.add(builder.toString());
        return AlgebraicNotation.changeToFullAlgebraicNotation(board, start, end, move, this, chessGameManager);
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

    public void setHistoryForComebackGame(int[] time) {
        fenList.clear();
        timeMap.clear();
        timeMap.put(0, time);
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

    public int[] getTimeRemain(int index) {
        return timeMap.get(index);
    }

    public void resetHalfmoveClock() {
        halfmoveClock = 0;
    }

    public int getNumberBoardSaved() {
        return movedList.size();
    }

    public void addNewAlgebraicNotation(String string) {
        algebraicNotationList.add(string);
    }

    public void handleForEndgameNotation(GameResult result, NotationHistoryScrollPane scrollPane) {
        int index = algebraicNotationList.size() - 1;
        if (index >= 0) {
            String notation = algebraicNotationList.get(index);
            if (notation.charAt(notation.length() - 1) == '+' && (result == GameResult.BLACK_WIN || result == GameResult.WHITE_WIN)) {
                algebraicNotationList.remove(index);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < notation.length() - 1; i++) {
                    builder.append(notation.charAt(i));
                }
                builder.append("#");
                algebraicNotationList.add(builder.toString());
                scrollPane.handleEndGameWithCheckMate(result, builder.toString());
            }
        }

        switch (result) {
            case WHITE_WIN -> algebraicNotationList.add("1-0");
            case BLACK_WIN -> algebraicNotationList.add("0-1");
            case DRAW_THREEFOLD, DRAW_AGREEMENT, DRAW_INSUFFICIENT, DRAW_STALEMATE, DRAW_FIFTY_MOVE -> algebraicNotationList.add("1/2-1/2");
        }

    }

    public String getPGN(){
        StringBuilder builder = new StringBuilder();
        int i;
        for(i = 0; i < algebraicNotationList.size() - 1; i++) {
            if (i % 2 == 0) {
                builder.append(i / 2 + 1);
                builder.append('.');
                builder.append(' ');
            }
            builder.append(algebraicNotationList.get(i));
            builder.append(' ');
        }
        String lastString = algebraicNotationList.get(i);
        if (lastString.startsWith("1") || lastString.startsWith("0")) {
            builder.append(lastString);
        }else {
            if (i % 2 == 0) {
                builder.append(i / 2 + 1);
                builder.append('.');
                builder.append(' ');
            }
            builder.append(algebraicNotationList.get(i));
            builder.append(' ');
        }
        return builder.toString();
    }

    public void deleteOldSaved(int index) {
        for (;index < movedList.size(); index++) {
            fenList.remove(fenList.size() - 1);
            stateHashList.remove(stateHashList.size() - 1);
            movedList.remove(movedList.size() - 1);
            algebraicNotationList.remove(algebraicNotationList.size() - 1);
        }
    }

    public void reset() {
        fenList.clear();
        stateHashList.clear();
        movedList.clear();
        algebraicNotationList.clear();
        isThreefoldRepetition = false;
        timeMap.clear();
        halfmoveClock = 0;
    }
}
