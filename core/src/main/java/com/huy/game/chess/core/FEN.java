package com.huy.game.chess.core;

import com.badlogic.gdx.utils.StringBuilder;
import com.huy.game.chess.manager.ChessGameManager;

public class FEN {

    public static String generateFEN(Board board ,Spot[][] spots, boolean isWhite, GameHistory history, ChessGameManager manager) {
        StringBuilder builder = new StringBuilder();
        piecePosition(spots, builder);
        moveNext(builder, isWhite);
        castlingRights(board, builder);
        possibleEnPassantTargets(builder, board);
        halfmoveClock(builder, history);
        fullMoveNumber(builder, manager);
        return builder.toString();
    }

    private static void piecePosition(Spot[][] spots, StringBuilder builder) {
        for(int i = 7; i >= 0; i--) {
            int count = 0;
            for (int j = 0; j <= 7; j++) {
                Piece piece = spots[i][j].getPiece();
                if(piece != null) {
                    if(count != 0) {
                        builder.append(count);
                        count = 0;
                    }
                    char notation = switch (piece.getType()) {
                        case PAWN -> 'p';
                        case KNIGHT -> 'n';
                        case BISHOP -> 'b';
                        case QUEEN -> 'q';
                        case ROOK -> 'r';
                        case KING -> 'k';
                    };
                    notation = piece.isWhite() ? Character.toUpperCase(notation) : notation;
                    builder.append(notation);
                }else {
                    count++;
                }
            }
            if (count != 0) {
                builder.append(count);
            }
            if(i != 0) {
                builder.append('/');
            }
        }
    }

    private static void moveNext(StringBuilder builder,boolean isWhite) {
        builder.append(' ');
        builder.append(isWhite ? 'b' : 'w');
    }

    public static void castlingRights(Board board, StringBuilder builder) {
        builder.append(' ');
        checkCastlingRights(board, builder, true, 'K', 'Q');
        checkCastlingRights(board, builder, false, 'k', 'q');
        if (builder.isEmpty()) {
            builder.append('-');
        }
    }

    private static void checkCastlingRights(Board board, StringBuilder builder, boolean isWhite, char kingSide, char queenSide) {
        Spot kingSpot = board.getKingSpot(isWhite);
        Piece kingPiece = kingSpot.getPiece();
        if (kingPiece instanceof King && !((King) kingPiece).isHasMove()) {
            if (canCastle(board, kingSpot, 7)) {
                builder.append(kingSide);
            }
            if (canCastle(board, kingSpot, 0)) {
                builder.append(queenSide);
            }
        }
    }

    private static boolean canCastle(Board board, Spot kingSpot, int rookFile) {
        Piece rook = board.getSpot(kingSpot.getX(), rookFile).getPiece();
        return (rook instanceof Rook) && !((Rook) rook).isHasMove();
    }

    private static void possibleEnPassantTargets(StringBuilder builder, Board board) {
        builder.append(' ');
        Spot spot = board.getPossibleEnPassantTargetsSpot();
        if(spot == null) {
            builder.append('-');
        }else {
            builder.append((char) ('a' + spot.getY()));
            builder.append(spot.getX() + 1);
        }
    }

    private static void halfmoveClock(StringBuilder builder, GameHistory history) {
        builder.append(' ');
        builder.append(history.getHalfmoveClock());
    }

    private static void fullMoveNumber(StringBuilder builder, ChessGameManager manager) {
        builder.append(' ');
        builder.append(manager.getCurrentPlayer().getTurn());
    }

}
