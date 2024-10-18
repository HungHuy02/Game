package com.huy.game.chess.core.notation;

import com.badlogic.gdx.utils.StringBuilder;
import com.huy.game.chess.core.Bishop;
import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.GameHistory;
import com.huy.game.chess.core.King;
import com.huy.game.chess.core.Knight;
import com.huy.game.chess.core.Pawn;
import com.huy.game.chess.core.Piece;
import com.huy.game.chess.core.Queen;
import com.huy.game.chess.core.Rook;
import com.huy.game.chess.core.Spot;
import com.huy.game.chess.manager.ChessGameManager;
import com.huy.game.chess.manager.ChessImage;

public class FEN {

    public static String generateFEN(Board board , Spot[][] spots, boolean isWhite, GameHistory history, ChessGameManager manager) {
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

    public static Board fenToBoard(String fen, ChessImage chessImage) {
        Board board = new Board();
        String[] parts = fen.split(" ");
        board.setSpots(positionToSpots(parts[0], chessImage, board));
        handleMoveNext(parts[1]);
        handleCastlingRights(parts[2], board, board.getSpots());
        handlePossibleEnPassantTargets(board, board.getSpots(), parts[3]);
        handleHalfmoveClock(parts[4]);
        handleFullMoveNumber(parts[5]);
        return board;
    }

    private static Spot[][] positionToSpots(String position, ChessImage chessImage, Board board) {
        Spot[][] spots = new Spot[8][8];
        String[] rows = position.split("/");
        for(int i = 0; i <= 7; i++) {
            int index = 0;
            for(int j = 0; j < rows[i].length(); j++) {
                char c = rows[i].charAt(j);
                int value = checkIndex(c);
                int x = 7 - i;
                if(value == 0) {
                    spots[x][index] = checkChar(c, x, index, chessImage, board);
                    index++;
                }else {
                    for(int k = index; k < index + value; k++) {
                        spots[x][k] = new Spot(null, x, k);
                    }
                    index += value;
                }
            }
        }
        return spots;
    }

    private static int checkIndex(char c) {
        switch (c) {
            case '1' -> {
                return 1;
            }
            case '2' -> {
                return 2;
            }
            case '3' -> {
                return 3;
            }
            case '4' -> {
                return 4;
            }
            case '5' -> {
                return 5;
            }
            case '6' -> {
                return 6;
            }
            case '7' -> {
                return 7;
            }
            case '8' -> {
                return 8;
            }
        }
        return 0;
    }

    private static Spot checkChar(char c, int x, int y, ChessImage chessImage, Board board) {
        boolean isKingSpot = false;
        Piece piece = switch (c) {
            case 'b' -> new Bishop(false, chessImage.getbBishop());
            case 'k' -> {
                isKingSpot = true;
                yield  new King(false, chessImage.getbKing());
            }
            case 'n' -> new Knight(false, chessImage.getbKnight());
            case 'r' -> new Rook(false, chessImage.getbRook());
            case 'p' -> new Pawn(false, chessImage.getbPawn());
            case 'q' -> new Queen(false, chessImage.getbQueen());
            case 'B' -> new Bishop(true, chessImage.getwBishop());
            case 'K' -> {
                isKingSpot = true;
                yield  new King(true, chessImage.getwKing());
            }
            case 'N' -> new Knight(true, chessImage.getwKnight());
            case 'R' -> new Rook(true, chessImage.getwRock());
            case 'P' -> new Pawn(true, chessImage.getwPawn());
            case 'Q' -> new Queen(true, chessImage.getwQueen());
            default -> null;
        };
        Spot spot = new Spot(piece, x, y);
        if(isKingSpot) {
            if (piece.isWhite()) {
                board.setwKingSpot(spot);
            }else {
                board.setbKingSpot(spot);
            }
        }
        return spot;
    }

    private static boolean handleMoveNext(String moveNext) {
        return moveNext.equals("w");
    }

    private static void handleCastlingRights(String castlingRights, Board board, Spot[][] spots) {
        if(castlingRights.equals("-")) {

        }
    }

    private static void handlePossibleEnPassantTargets(Board board, Spot[][] spots, String possibleEnPassantTargets) {
        if (!possibleEnPassantTargets.equals("-")) {
            int x = AlgebraicNotation.changeRowAlgebraicNotationToRowPosition(possibleEnPassantTargets.charAt(1));
            int y = AlgebraicNotation.changeColAlgebraicNotationToColPosition(possibleEnPassantTargets.charAt(0));
            if(spots[x][y].getPiece() instanceof Pawn) {
                board.setPossibleEnPassantTargetsSpot(spots[x][y]);
            }
        }

    }

    private static int handleHalfmoveClock(String halfmoveClock) {
        return Integer.parseInt(halfmoveClock);
    }

    private static int handleFullMoveNumber(String fullMoveNumber) {
        return Integer.parseInt(fullMoveNumber);
    }

}
