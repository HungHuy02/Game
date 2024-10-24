package com.huy.game.chess.core.notation;

import com.badlogic.gdx.utils.StringBuilder;
import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.GameHistory;
import com.huy.game.chess.core.Move;
import com.huy.game.chess.core.Spot;
import com.huy.game.chess.enums.MoveType;

import java.util.Map;

public class AlgebraicNotation {

    public static String changeToFullAlgebraicNotation(Board board, Spot start, Spot end, Move move, GameHistory history) {
        MoveType moveType = move.getMoveType();
        switch (moveType) {
            case CASTLING_KING_SIDE -> {
                return "O-O";
            }
            case CASTLING_QUEEN_SIDE -> {
                return "O-O-O";
            }
        }
        StringBuilder builder = new StringBuilder();
        switch (move.getStartPiece().getType()) {
            case PAWN -> {
                history.resetHalfmoveClock();
                switch (moveType) {
                    case CAPTURE -> builder.append(changeColPositionToColAlgebraicNotation(start.getY()));
                    case PROMOTE_TO_QUEEN, PROMOTE_TO_KNIGHT, PROMOTE_TO_BISHOP, PROMOTE_TO_ROOK -> {
                        if (move.getEndPiece() != null) {
                            builder.append(changeColPositionToColAlgebraicNotation(start.getY()));
                            builder.append('x');
                        }
                    }
                }
            }
            case KNIGHT -> {
                builder.append('N');
                handleSamePieceCanMoveToOneSpot(builder, board, start, end);
            }
            case BISHOP -> {
                builder.append('B');
                handleSamePieceCanMoveToOneSpot(builder, board, start, end);
            }
            case QUEEN -> {
                builder.append('Q');
                handleSamePieceCanMoveToOneSpot(builder, board, start, end);
            }
            case ROOK -> {
                builder.append('R');
                handleSamePieceCanMoveToOneSpot(builder, board, start, end);

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

    private static void handleSamePieceCanMoveToOneSpot(StringBuilder builder , Board board, Spot start, Spot end) {
        Map.Entry<Integer, Boolean> result = end.getPiece().countSamePieceCanMoveToOneSpot(board, board.getSpots(), start, end);
        switch (result.getKey()) {
            case 1 -> {
                if (result.getValue()) {
                    builder.append(changeColPositionToColAlgebraicNotation(start.getY()));
                }else {
                    builder.append(changeRowPositionToRowAlgebraicNotation(start.getX()));
                }
            }
            case 2 -> changePositionToSimpleAlgebraicNotation(builder, start.getX(), start.getY());
        }
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
