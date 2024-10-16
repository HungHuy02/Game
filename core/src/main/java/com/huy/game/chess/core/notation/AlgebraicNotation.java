package com.huy.game.chess.core.notation;

import com.badlogic.gdx.utils.StringBuilder;
import com.huy.game.chess.core.GameHistory;
import com.huy.game.chess.core.Piece;

public class AlgebraicNotation {

    public static String changeToFullAlgebraicNotation(int x, int y, Piece startPiece, Piece endPiece, GameHistory history) {
        StringBuilder builder = new StringBuilder();
        switch (startPiece.getType()) {
            case PAWN -> history.resetHalfmoveClock();
            case KNIGHT ->  builder.append('N');
            case BISHOP -> builder.append('B');
            case QUEEN -> builder.append('Q');
            case ROOK -> builder.append('R');
            case KING -> builder.append('K');
        }
        if(endPiece != null) {
            builder.append('x');
            history.resetHalfmoveClock();
        }
        changePositionToAlgebraicNotation(builder, x, y);
        return builder.toString();
    }

    public static void changePositionToAlgebraicNotation(StringBuilder builder ,int x, int y) {
        builder.append((char) ('a' + y));
        builder.append(x + 1);
    }

    public static int changeRowAlgebraicNotationToRowPosition(char c) {
        return c - '1';
    }

    public static int changeColAlgebraicNotationToColPosition(char c) {
        return c - 'a';
    }
}
