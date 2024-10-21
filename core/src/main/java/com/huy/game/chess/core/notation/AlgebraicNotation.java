package com.huy.game.chess.core.notation;

import com.badlogic.gdx.utils.StringBuilder;
import com.huy.game.chess.core.GameHistory;
import com.huy.game.chess.core.Move;
import com.huy.game.chess.core.Spot;
import com.huy.game.chess.enums.MoveType;

public class AlgebraicNotation {

    public static String changeToFullAlgebraicNotation(Spot start, Spot end, Move move, GameHistory history) {
        MoveType moveType = move.getMoveType();
        switch (moveType) {
            case CASTLING_KING_SIDE -> {
                return "O-O";
            }
            case CASTLING_QUEEN_SIDE -> {
                return "O-O-O";
            }
        }
        Spot[][] spots = new Spot[8][8];
        StringBuilder builder = new StringBuilder();
        switch (move.getStartPiece().getType()) {
            case PAWN -> history.resetHalfmoveClock();
            case KNIGHT -> {
                builder.append('N');
            }
            case BISHOP -> {
                builder.append('B');
            }
            case QUEEN -> {
                builder.append('Q');
            }
            case ROOK -> {
                builder.append('R');

            }
            case KING -> builder.append('K');
        }
        switch (moveType) {
            case CAPTURE -> {
                builder.append('x');
                history.resetHalfmoveClock();
            }
            case EN_PASSANT -> {
                builder.append(changeColPositionToColAlgebraicNotation(start.getY()));
                builder.append('x');
            }
        }
        changePositionToSimpleAlgebraicNotation(builder, end.getX(), end.getY());
        switch (moveType) {
            case PROMOTE_TO_QUEEN -> {
                builder.append('=');
                builder.append('Q');
            }
            case PROMOTE_TO_KNIGHT -> {
                builder.append('=');
                builder.append('N');
            }
            case PROMOTE_TO_ROOK -> {
                builder.append('=');
                builder.append('R');
            }
            case PROMOTE_TO_BISHOP -> {
                builder.append('=');
                builder.append('B');
            }
        }
        if (move.isCheck()) {
            builder.append('+');
        }
        return builder.toString();
    }

    public static void changePositionToSimpleAlgebraicNotation(StringBuilder builder , int x, int y) {
        builder.append(changeColPositionToColAlgebraicNotation(y));
        builder.append(changeRowPositionToRowAlgebraicNotation(x));
    }

    public static int changeRowAlgebraicNotationToRowPosition(char c) {
        return c - '1';
    }

    public static int changeColAlgebraicNotationToColPosition(char c) {
        return c - 'a';
    }

    public static String changePositionToSimpleAlgebraicNotation(Spot spot) {
        StringBuilder builder = new StringBuilder();
        builder.append(changeColPositionToColAlgebraicNotation(spot.getY()));
        builder.append(changeRowPositionToRowAlgebraicNotation(spot.getX()));
        return builder.toString();
    }

    public static int changeRowPositionToRowAlgebraicNotation(int x) {
        return x + 1;
    }

    public static char changeColPositionToColAlgebraicNotation(int y) {
        return (char) ('a' + y);
    }
}
