package com.huy.game.chess.core.notation;

import com.badlogic.gdx.utils.StringBuilder;
import com.huy.game.chess.ChessScreen;
import com.huy.game.chess.WatchingHistoryScreen;
import com.huy.game.chess.core.Bishop;
import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.GameHistory;
import com.huy.game.chess.core.Knight;
import com.huy.game.chess.core.Move;
import com.huy.game.chess.core.Piece;
import com.huy.game.chess.core.Queen;
import com.huy.game.chess.core.Rook;
import com.huy.game.chess.core.Spot;
import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.enums.PieceType;
import com.huy.game.chess.manager.ChessGameManager;

import java.util.Map;

public class AlgebraicNotation {

    public static String changeToFullAlgebraicNotation(Board board, Spot start, Spot end, Move move, GameHistory history, ChessGameManager chessGameManager) {
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
                int number = chessGameManager.getCurrentPlayer().getPieceNumber(PieceType.KNIGHT);
                handleSamePieceCanMoveToOneSpot(builder, board, start, end, number);
            }
            case BISHOP -> {
                builder.append('B');
                int number = chessGameManager.getCurrentPlayer().getPieceNumber(PieceType.BISHOP);
                handleSamePieceCanMoveToOneSpot(builder, board, start, end, number);
            }
            case QUEEN -> {
                builder.append('Q');
                int number = chessGameManager.getCurrentPlayer().getPieceNumber(PieceType.QUEEN);
                handleSamePieceCanMoveToOneSpot(builder, board, start, end, number);
            }
            case ROOK -> {
                builder.append('R');
                int number = chessGameManager.getCurrentPlayer().getPieceNumber(PieceType.ROOK);
                handleSamePieceCanMoveToOneSpot(builder, board, start, end, number);

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

    private static void handleSamePieceCanMoveToOneSpot(StringBuilder builder , Board board, Spot start, Spot end, int pieceNumber) {
        if (pieceNumber >= 2) {
            Map.Entry<Integer, Boolean> result = end.getPiece().countSamePieceCanMoveToOneSpot(board, board.getSpots(), start, end, pieceNumber);
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

    public static void changePGNToBoard(String pgn, Board board, boolean isWhite, WatchingHistoryScreen screen) {
        pgn = pgn.trim().replaceAll("(1-0|0-1|1/2-1/2)$", "").trim();
        String[] notations = pgn.replaceAll("\\d+\\.", "").trim().split("\\s+");
        int length = notations.length - 1;
        int i;
        for (i = 0; i < length; i++) {
            Move move = changeNotationToMove(notations[i], board, isWhite);
            screen.handleMove(move, board);
            isWhite = !isWhite;
        }
        String lastNotation = notations[i];
        if (lastNotation.endsWith("#")) {
            lastNotation = lastNotation.substring(0, lastNotation.length() - 1);
        }
        Move move = changeNotationToMove(lastNotation, board, isWhite);
        screen.handleMove(move, board);
        screen.checkForEndGame(move, board);
    }

    public static void changePGNToBoard(String pgn, Board board, boolean isWhite, ChessScreen screen) {
        pgn = pgn.trim().replaceAll("(1-0|0-1|1/2-1/2)$", "").trim();
        String[] notations = pgn.replaceAll("\\d+\\.", "").trim().split("\\s+");
        int length = notations.length - 1;
        int i;
        for (i = 0; i < length; i++) {
            Move move = changeNotationToMove(notations[i], board, isWhite);
            screen.handleMove(move);
            isWhite = !isWhite;
        }
        String lastNotation = notations[i];
        if (lastNotation.endsWith("#")) {
            lastNotation = lastNotation.substring(0, lastNotation.length() - 1);
        }
        Move move = changeNotationToMove(lastNotation, board, isWhite);
        screen.handleMove(move);
    }

    public static Move changeNotationToMove(String notation, Board board, boolean isWhite) {
        int firstChar = notation.charAt(0);
        int length = notation.length();
        if (notation.charAt(length - 1) == '+') {
            notation = notation.substring(0, notation.length() - 1);
            length--;
        }
        Move move;
        switch (firstChar) {
            case 'N' -> move = changeKnightNotaionToMove(length, notation, board, isWhite);
            case 'B' -> move = changeBishopNotationToMove(length, notation, board, isWhite);
            case 'Q' -> move = changeQueenNotationToMove(length, notation, board, isWhite);
            case 'R' -> move = changeRookNotationToMove(length, notation, board, isWhite);
            case 'K' -> {
                Spot start = board.getKingSpot(isWhite);
                Spot endSpot = board.getSpot(
                    changeRowAlgebraicNotationToRowPosition(notation.charAt(length - 1)),
                    changeColAlgebraicNotationToColPosition(notation.charAt(length - 2)));
                move = new Move(start, endSpot);
                if (notation.charAt(1) == 'x') {
                    move.setMoveType(MoveType.CAPTURE);
                }else {
                    move.setMoveType(MoveType.NORMAL);
                }
            }
            case 'O' -> {
                if (length == 3) {
                    Spot startSpot = board.getKingSpot(isWhite);
                    Spot endSpot = board.getSpot(startSpot.getX(), startSpot.getY() + 2);
                    move = new Move(startSpot, endSpot);
                    move.setMoveType(MoveType.CASTLING_KING_SIDE);
                }else {
                    Spot startSpot = board.getKingSpot(isWhite);
                    Spot endSpot = board.getSpot(startSpot.getX(), startSpot.getY() - 2);
                    move = new Move(startSpot, endSpot);
                    move.setMoveType(MoveType.CASTLING_QUEEN_SIDE);
                }
            }
            default -> move = changePawnNotationToMove(length, notation, board, isWhite);
        }
        return move;
    }

    public static Move changeKnightNotaionToMove(int length, String notation, Board board, boolean isWhite) {
        Move move = null;
        switch (length) {
            case 3 -> {
                int x = changeRowAlgebraicNotationToRowPosition(notation.charAt(2));
                int y = changeColAlgebraicNotationToColPosition(notation.charAt(1));
                for (int[] knightMove : Knight.knightMoves()) {
                    int startX = x + knightMove[0];
                    int startY = y + knightMove[1];
                    if (board.isWithinBoard(startX, startY)) {
                        Spot spot = board.getSpot(startX, startY);
                        Piece piece = spot.getPiece();
                        if (piece != null && piece.getType() == PieceType.KNIGHT && piece.isWhite() == isWhite) {
                            move = new Move(spot, board.getSpot(x, y));
                            move.setMoveType(MoveType.NORMAL);
                            return move;
                        }
                    }
                }
            }
            case 4 -> {
                int x = changeRowAlgebraicNotationToRowPosition(notation.charAt(3));
                int y = changeColAlgebraicNotationToColPosition(notation.charAt(2));
                char c = notation.charAt(1);
                if (c == 'x') {
                    for (int[] knightMove : Knight.knightMoves()) {
                        int startX = x + knightMove[0];
                        int startY = y + knightMove[1];
                        if (board.isWithinBoard(startX, startY)) {
                            Spot spot = board.getSpot(startX, startY);
                            Piece piece = spot.getPiece();
                            if (piece != null && piece.getType() == PieceType.KNIGHT && piece.isWhite() == isWhite) {
                                move = new Move(spot, board.getSpot(x, y));
                                move.setMoveType(MoveType.CAPTURE);
                                return move;
                            }
                        }
                    }
                }else {
                    int value;
                    if (c < 'a') {
                        value = changeRowAlgebraicNotationToRowPosition(c);
                        int distanceX = value - x;
                        int distanceY = 2 / distanceX;
                        int startY = y + distanceY;
                        if (board.isWithinBoard(value, startY)) {
                            Spot spot = board.getSpot(value, startY);
                            Piece piece = spot.getPiece();
                            if (piece != null && piece.getType() == PieceType.KNIGHT && piece.isWhite() == isWhite) {
                                move = new Move(spot, board.getSpot(x, y));
                                move.setMoveType(MoveType.NORMAL);
                            }else {
                                move = new Move(board.getSpot(value, y - distanceY), board.getSpot(x, y));
                                move.setMoveType(MoveType.NORMAL);
                            }
                        }else {
                            move = new Move(board.getSpot(value, y - distanceY), board.getSpot(x, y));
                            move.setMoveType(MoveType.NORMAL);
                        }
                    }else {
                        value = changeColAlgebraicNotationToColPosition(c);
                        int distanceY = value - y;
                        int distanceX = 2 / distanceY;
                        int startX = x + distanceX;
                        if (board.isWithinBoard(startX, value)) {
                            Spot spot = board.getSpot(x + distanceX, value);
                            Piece piece = spot.getPiece();
                            if (piece != null && piece.getType() == PieceType.KNIGHT && piece.isWhite() == isWhite) {
                                move = new Move(spot, board.getSpot(x, y));
                                move.setMoveType(MoveType.NORMAL);
                            } else {
                                move = new Move(board.getSpot(x - distanceX, value), board.getSpot(x, y));
                                move.setMoveType(MoveType.NORMAL);
                            }
                        } else {
                            move = new Move(board.getSpot(x - distanceX, value), board.getSpot(x, y));
                            move.setMoveType(MoveType.NORMAL);
                        }
                    }
                }
            }
            case 5 -> {
                if (notation.contains("x")) {
                    int value;
                    int x = changeRowAlgebraicNotationToRowPosition(notation.charAt(4));
                    int y = changeColAlgebraicNotationToColPosition(notation.charAt(3));
                    char c = notation.charAt(1);
                    if (c < 'a') {
                        value = changeRowAlgebraicNotationToRowPosition(c);
                        int distanceX = value - x;
                        int distanceY = 2 / distanceX;
                        int startY = y + distanceY;
                        if (board.isWithinBoard(value, startY)) {
                            Spot spot = board.getSpot(value, startY);
                            Piece piece = spot.getPiece();
                            if (piece != null && piece.getType() == PieceType.KNIGHT && piece.isWhite() == isWhite) {
                                move = new Move(spot, board.getSpot(x, y));
                                move.setMoveType(MoveType.CAPTURE);
                            }else {
                                move = new Move(board.getSpot(value, y - distanceY), board.getSpot(x, y));
                                move.setMoveType(MoveType.CAPTURE);
                            }
                        }else {
                            move = new Move(board.getSpot(value, y - distanceY), board.getSpot(x, y));
                            move.setMoveType(MoveType.CAPTURE);
                        }
                    }else {
                        value = changeColAlgebraicNotationToColPosition(c);
                        int distanceY = value - y;
                        int distanceX = 2 / distanceY;
                        int startX = x + distanceX;
                        if (board.isWithinBoard(startX, value)) {
                            Spot spot = board.getSpot(x + distanceX, value);
                            Piece piece = spot.getPiece();
                            if (piece != null && piece.getType() == PieceType.KNIGHT && piece.isWhite() == isWhite) {
                                move = new Move(spot, board.getSpot(x, y));
                                move.setMoveType(MoveType.CAPTURE);
                            } else {
                                move = new Move(board.getSpot(x - distanceX, value), board.getSpot(x, y));
                                move.setMoveType(MoveType.CAPTURE);
                            }
                        } else {
                            move = new Move(board.getSpot(x - distanceX, value), board.getSpot(x, y));
                            move.setMoveType(MoveType.CAPTURE);
                        }
                    }
                }else {
                    Spot startSpot = board.getSpot(
                        changeRowAlgebraicNotationToRowPosition(notation.charAt(2)),
                        changeColAlgebraicNotationToColPosition(notation.charAt(1)));
                    Spot endSpot = board.getSpot(
                        changeRowAlgebraicNotationToRowPosition(notation.charAt(4)),
                        changeColAlgebraicNotationToColPosition(notation.charAt(3)));
                    move = new Move(startSpot, endSpot);
                    move.setMoveType(MoveType.NORMAL);
                }
            }
            case 6 -> {
                Spot startSpot = board.getSpot(
                    changeRowAlgebraicNotationToRowPosition(notation.charAt(2)),
                    changeColAlgebraicNotationToColPosition(notation.charAt(1)));
                Spot endSpot = board.getSpot(
                    changeRowAlgebraicNotationToRowPosition(notation.charAt(5)),
                    changeColAlgebraicNotationToColPosition(notation.charAt(4)));
                move = new Move(startSpot, endSpot);
                move.setMoveType(MoveType.CAPTURE);
            }
        }
        return move;
    }

    public static Move changeBishopNotationToMove(int length, String notation, Board board, boolean isWhite) {
        Move move = null;
        switch (length) {
            case 3 -> {
                int x = changeRowAlgebraicNotationToRowPosition(notation.charAt(2));
                int y = changeColAlgebraicNotationToColPosition(notation.charAt(1));
                for (int[] bishopMove : Bishop.bishopMoves()) {
                    int checkX = x + bishopMove[0];
                    int checkY = y + bishopMove[1];
                    while(board.isWithinBoard(checkX, checkY)) {
                        Spot checkSpot = board.getSpot(checkX, checkY);
                        Piece piece = checkSpot.getPiece();
                        if(piece != null) {
                            if (piece.getType() == PieceType.BISHOP && piece.isWhite() == isWhite) {
                                move = new Move(checkSpot, board.getSpot(x, y));
                                move.setMoveType(MoveType.NORMAL);
                                return move;
                            }
                            break;
                        }
                        checkX += bishopMove[0];
                        checkY += bishopMove[1];
                    }

                }
            }
            case 4 -> {
                int x = changeRowAlgebraicNotationToRowPosition(notation.charAt(3));
                int y = changeColAlgebraicNotationToColPosition(notation.charAt(2));
                if (notation.contains("x")) {
                    for (int[] bishopMove : Bishop.bishopMoves()) {
                        int checkX = x + bishopMove[0];
                        int checkY = y + bishopMove[1];
                        while(board.isWithinBoard(checkX, checkY)) {
                            Spot checkSpot = board.getSpot(checkX, checkY);
                            Piece piece = checkSpot.getPiece();
                            if(piece != null) {
                                if (piece.getType() == PieceType.BISHOP && piece.isWhite() == isWhite) {
                                    move = new Move(checkSpot, board.getSpot(x, y));
                                    move.setMoveType(MoveType.CAPTURE);
                                    return move;
                                }
                                break;
                            }
                            checkX += bishopMove[0];
                            checkY += bishopMove[1];
                        }
                    }
                }else {
                    char c = notation.charAt(1);
                    if (c < 'a') {
                        int startX = changeRowAlgebraicNotationToRowPosition(c);
                        int directionX = Integer.signum(startX - x);
                        int distance = Math.abs(startX - x);
                        int startY = y + distance;
                        Spot start = board.getSpot(startX, startY);
                        Piece piece = start.getPiece();
                        boolean isSpot = true;
                        if (piece != null && piece.getType() == PieceType.BISHOP && piece.isWhite() == isWhite) {
                            int checkX = startX - directionX;
                            int checkY = startY - 1;
                            while (checkX != x) {
                                Spot checkSpot = board.getSpot(checkX, checkY);
                                if (checkSpot.getPiece() != null) {
                                    isSpot = false;
                                    break;
                                }
                                checkX -= directionX;
                                checkY--;
                            }
                        }else {
                            isSpot = false;
                        }
                        if (!isSpot) {
                            startY = y - distance;
                            start = board.getSpot(startX, startY);
                            piece = start.getPiece();
                            isSpot = true;
                            if (piece != null && piece.getType() == PieceType.BISHOP && piece.isWhite() == isWhite) {
                                int checkX = startX - directionX;
                                int checkY = startY + 1;
                                while (checkX != x) {
                                    Spot checkSpot = board.getSpot(checkX, checkY);
                                    if (checkSpot.getPiece() != null) {
                                        isSpot = false;
                                        break;
                                    }
                                    checkX -= directionX;
                                    checkY++;
                                }
                            } else {
                                isSpot = false;
                            }

                        }
                        if (isSpot) {
                            move = new Move(start, board.getSpot(x, y));
                            move.setMoveType(MoveType.NORMAL);
                        }
                    }else {
                        int startY = changeColAlgebraicNotationToColPosition(c);
                        int directionY = Integer.signum(startY - y);
                        int distance = Math.abs(startY - y);
                        int startX = x + distance;
                        Spot start = board.getSpot(startX, startY);
                        Piece piece = start.getPiece();
                        boolean isSpot = true;
                        if (piece != null && piece.getType() == PieceType.BISHOP && piece.isWhite() == isWhite) {
                            int checkX = startX - 1;
                            int checkY = startY - directionY;
                            while (checkX != x) {
                                Spot checkSpot = board.getSpot(checkX, checkY);
                                if (checkSpot.getPiece() != null) {
                                    isSpot = false;
                                    break;
                                }
                                checkX--;
                                checkY -= directionY;
                            }
                        }else {
                            isSpot = false;
                        }
                        if (!isSpot) {
                            startX = x - distance;
                            start = board.getSpot(startX, startY);
                            piece = start.getPiece();
                            isSpot = true;
                            if (piece != null && piece.getType() == PieceType.BISHOP && piece.isWhite() == isWhite) {
                                int checkX = startX + 1;
                                int checkY = startY - directionY;
                                while (checkX != x) {
                                    Spot checkSpot = board.getSpot(checkX, checkY);
                                    if (checkSpot.getPiece() != null) {
                                        isSpot = false;
                                        break;
                                    }
                                    checkX++;
                                    checkY -= directionY;
                                }
                            }else {
                                isSpot = false;
                            }
                        }
                        if (isSpot) {
                            move = new Move(start, board.getSpot(x, y));
                            move.setMoveType(MoveType.NORMAL);
                        }
                    }
                }
            }
            case 5 -> {
                int x = changeRowAlgebraicNotationToRowPosition(notation.charAt(4));
                int y = changeColAlgebraicNotationToColPosition(notation.charAt(3));
                if (notation.contains("x")) {
                    char c = notation.charAt(1);
                    if (c < 'a') {
                        int startX = changeRowAlgebraicNotationToRowPosition(c);
                        int directionX = Integer.signum(startX - x);
                        int distance = Math.abs(startX - x);
                        int startY = y + distance;
                        Spot start = board.getSpot(startX, startY);
                        Piece piece = start.getPiece();
                        boolean isSpot = true;
                        if (piece != null && piece.getType() == PieceType.BISHOP && piece.isWhite() == isWhite) {
                            int checkX = startX - directionX;
                            int checkY = startY - 1;
                            while (checkX != x) {
                                Spot checkSpot = board.getSpot(checkX, checkY);
                                if (checkSpot.getPiece() != null) {
                                    isSpot = false;
                                    break;
                                }
                                checkX -= directionX;
                                checkY--;
                            }
                        }else {
                            isSpot = false;
                        }
                        if (!isSpot) {
                            startY = y - distance;
                            start = board.getSpot(startX, startY);
                            piece = start.getPiece();
                            isSpot = true;
                            if (piece != null && piece.getType() == PieceType.BISHOP && piece.isWhite() == isWhite) {
                                int checkX = startX - directionX;
                                int checkY = startY + 1;
                                while (checkX != x) {
                                    Spot checkSpot = board.getSpot(checkX, checkY);
                                    if (checkSpot.getPiece() != null) {
                                        isSpot = false;
                                        break;
                                    }
                                    checkX -= directionX;
                                    checkY++;
                                }
                            } else {
                                isSpot = false;
                            }

                        }
                        if (isSpot) {
                            move = new Move(start, board.getSpot(x, y));
                            move.setMoveType(MoveType.NORMAL);
                        }
                    }else {
                        int startY = changeColAlgebraicNotationToColPosition(c);
                        int directionY = Integer.signum(startY - y);
                        int distance = Math.abs(startY - y);
                        int startX = x + distance;
                        Spot start = board.getSpot(startX, startY);
                        Piece piece = start.getPiece();
                        boolean isSpot = true;
                        if (piece != null && piece.getType() == PieceType.BISHOP && piece.isWhite() == isWhite) {
                            int checkX = startX - 1;
                            int checkY = startY - directionY;
                            while (checkX != x) {
                                Spot checkSpot = board.getSpot(checkX, checkY);
                                if (checkSpot.getPiece() != null) {
                                    isSpot = false;
                                    break;
                                }
                                checkX--;
                                checkY -= directionY;
                            }
                        }else {
                            isSpot = false;
                        }
                        if (!isSpot) {
                            startX = x - distance;
                            start = board.getSpot(startX, startY);
                            piece = start.getPiece();
                            isSpot = true;
                            if (piece != null && piece.getType() == PieceType.BISHOP && piece.isWhite() == isWhite) {
                                int checkX = startX + 1;
                                int checkY = startY - directionY;
                                while (checkX != x) {
                                    Spot checkSpot = board.getSpot(checkX, checkY);
                                    if (checkSpot.getPiece() != null) {
                                        isSpot = false;
                                        break;
                                    }
                                    checkX++;
                                    checkY -= directionY;
                                }
                            }else {
                                isSpot = false;
                            }
                        }
                        if (isSpot) {
                            move = new Move(start, board.getSpot(x, y));
                            move.setMoveType(MoveType.NORMAL);
                        }
                    }
                }else {
                    Spot startSpot = board.getSpot(
                        changeRowAlgebraicNotationToRowPosition(notation.charAt(2)),
                        changeColAlgebraicNotationToColPosition(notation.charAt(1)));
                    move = new Move(startSpot, board.getSpot(x, y));
                    move.setMoveType(MoveType.NORMAL);
                }
            }
            case 6 -> {
                Spot startSpot = board.getSpot(
                    changeRowAlgebraicNotationToRowPosition(notation.charAt(2)),
                    changeColAlgebraicNotationToColPosition(notation.charAt(1)));
                Spot endSpot = board.getSpot(
                    changeRowAlgebraicNotationToRowPosition(notation.charAt(5)),
                    changeColAlgebraicNotationToColPosition(notation.charAt(4)));
                move = new Move(startSpot, endSpot);
                move.setMoveType(MoveType.CAPTURE);
            }
        }
        return move;
    }

    public static Move changeQueenNotationToMove(int length, String notation, Board board, boolean isWhite) {
        Move move = null;
        switch (length) {
            case 3 -> {
                int x = changeRowAlgebraicNotationToRowPosition(notation.charAt(2));
                int y = changeColAlgebraicNotationToColPosition(notation.charAt(1));
                for (int[] queenMove : Queen.queenMoves()) {
                    int checkX = x + queenMove[0];
                    int checkY = y + queenMove[1];
                    while(board.isWithinBoard(checkX, checkY)) {
                        Spot checkSpot = board.getSpot(checkX, checkY);
                        Piece piece = checkSpot.getPiece();
                        if(piece != null) {
                            if (piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                move = new Move(checkSpot, board.getSpot(x, y));
                                move.setMoveType(MoveType.NORMAL);
                                return move;
                            }else {
                                break;
                            }
                        }
                        checkX += queenMove[0];
                        checkY += queenMove[1];
                    }
                }
            }
            case 4 -> {
                int x = changeRowAlgebraicNotationToRowPosition(notation.charAt(3));
                int y = changeColAlgebraicNotationToColPosition(notation.charAt(2));
                if (notation.contains("x")) {
                    for (int[] queenMove : Queen.queenMoves()) {
                        int checkX = x + queenMove[0];
                        int checkY = y + queenMove[1];
                        while(board.isWithinBoard(checkX, checkY)) {
                            Spot checkSpot = board.getSpot(checkX, checkY);
                            Piece piece = checkSpot.getPiece();
                            if(piece != null) {
                                if (piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                    move = new Move(checkSpot, board.getSpot(x, y));
                                    move.setMoveType(MoveType.CAPTURE);
                                    return move;
                                }else {
                                    break;
                                }
                            }
                            checkX += queenMove[0];
                            checkY += queenMove[1];
                        }
                    }
                }else {
                    char c = notation.charAt(1);
                    if (c < 'a') {
                        int startX = changeRowAlgebraicNotationToRowPosition(c);
                        if (startX == x) {
                            int startY = y + 1;
                            while(startY >= 0 && startY < 8) {
                                Spot spot = board.getSpot(startX, startY);
                                Piece piece = spot.getPiece();
                                if (piece != null) {
                                    if (piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                        move = new Move(spot, board.getSpot(x, y));
                                        move.setMoveType(MoveType.NORMAL);
                                    }
                                    break;
                                }
                                startY++;
                            }
                            if (move == null) {
                                startY = y - 1;
                                while(startY >= 0 && startY < 8) {
                                    Spot spot = board.getSpot(startX, startY);
                                    Piece piece = spot.getPiece();
                                    if (piece != null) {
                                        if (piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                            move = new Move(spot, board.getSpot(x, y));
                                            move.setMoveType(MoveType.NORMAL);
                                        }
                                        break;
                                    }
                                    startY--;
                                }
                            }
                        }else {
                            int directionX = Integer.signum(startX - x);
                            int distance = Math.abs(startX - x);
                            int startY = y + distance;
                            Spot start = board.getSpot(startX, startY);
                            Piece piece = start.getPiece();
                            boolean isSpot = true;
                            if (piece != null && piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                int checkX = startX - directionX;
                                int checkY = startY - 1;
                                while (checkX != x) {
                                    Spot checkSpot = board.getSpot(checkX, checkY);
                                    if (checkSpot.getPiece() != null) {
                                        isSpot = false;
                                        break;
                                    }
                                    checkX -= directionX;
                                    checkY--;
                                }
                            }else {
                                isSpot = false;
                            }
                            if (!isSpot) {
                                start = board.getSpot(startX, y);
                                piece = start.getPiece();
                                isSpot = true;
                                if (piece != null && piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                    int checkX = startX - directionX;
                                    while (checkX != x) {
                                        Spot checkSpot = board.getSpot(checkX, y);
                                        if (checkSpot.getPiece() != null) {
                                            isSpot = false;
                                            break;
                                        }
                                        checkX -= directionX;
                                    }
                                }else {
                                    isSpot = false;
                                }
                            }
                            if (!isSpot) {
                                startY = y - distance;
                                start = board.getSpot(startX, startY);
                                piece = start.getPiece();
                                isSpot = true;
                                if (piece != null && piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                    int checkX = startX - directionX;
                                    int checkY = startY + 1;
                                    while (checkX != x) {
                                        Spot checkSpot = board.getSpot(checkX, checkY);
                                        if (checkSpot.getPiece() != null) {
                                            isSpot = false;
                                            break;
                                        }
                                        checkX -= directionX;
                                        checkY++;
                                    }
                                } else {
                                    isSpot = false;
                                }

                            }
                            if (isSpot) {
                                move = new Move(start, board.getSpot(x, y));
                                move.setMoveType(MoveType.NORMAL);
                            }
                        }
                    }else {
                        int startY = changeColAlgebraicNotationToColPosition(c);
                        if (startY == y) {
                            int startX = x + 1;
                            while(startX >= 0 && startX < 8) {
                                Spot spot = board.getSpot(startX, startY);
                                Piece piece = spot.getPiece();
                                if (piece != null) {
                                    if (piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                        move = new Move(spot, board.getSpot(x, y));
                                        move.setMoveType(MoveType.NORMAL);
                                    }
                                    break;
                                }
                                startX++;
                            }
                            if (move == null) {
                                startX = x - 1;
                                while(startX >= 0 && startX < 8) {
                                    Spot spot = board.getSpot(startX, startY);
                                    Piece piece = spot.getPiece();
                                    if (piece != null) {
                                        if (piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                            move = new Move(spot, board.getSpot(x, y));
                                            move.setMoveType(MoveType.NORMAL);
                                        }
                                        break;
                                    }
                                    startX--;
                                }
                            }
                        }else {
                            int directionY = Integer.signum(startY - y);
                            int distance = Math.abs(startY - y);
                            int startX = x + distance;
                            Spot start = board.getSpot(startX, startY);
                            Piece piece = start.getPiece();
                            boolean isSpot = true;
                            if (piece != null && piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                int checkX = startX - 1;
                                int checkY = startY - directionY;
                                while (checkX != x) {
                                    Spot checkSpot = board.getSpot(checkX, checkY);
                                    if (checkSpot.getPiece() != null) {
                                        isSpot = false;
                                        break;
                                    }
                                    checkX--;
                                    checkY -= directionY;
                                }
                            }else {
                                isSpot = false;
                            }
                            if (!isSpot) {
                                start = board.getSpot(x, startY);
                                piece = start.getPiece();
                                isSpot = true;
                                if (piece != null && piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                    int checkY = startY - directionY;
                                    while (checkY != y) {
                                        Spot checkSpot = board.getSpot(x, checkY);
                                        if (checkSpot.getPiece() != null) {
                                            isSpot = false;
                                            break;
                                        }
                                        checkY -= directionY;
                                    }
                                }else {
                                    isSpot = false;
                                }
                            }
                            if (!isSpot) {
                                startX = x - distance;
                                start = board.getSpot(startX, startY);
                                piece = start.getPiece();
                                isSpot = true;
                                if (piece != null && piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                    int checkX = startX + 1;
                                    int checkY = startY - directionY;
                                    while (checkX != x) {
                                        Spot checkSpot = board.getSpot(checkX, checkY);
                                        if (checkSpot.getPiece() != null) {
                                            isSpot = false;
                                            break;
                                        }
                                        checkX++;
                                        checkY -= directionY;
                                    }
                                }else {
                                    isSpot = false;
                                }
                            }
                            if (isSpot) {
                                move = new Move(start, board.getSpot(x, y));
                                move.setMoveType(MoveType.NORMAL);
                            }
                        }
                    }
                }
            }
            case 5 -> {
                int x = changeRowAlgebraicNotationToRowPosition(notation.charAt(4));
                int y = changeColAlgebraicNotationToColPosition(notation.charAt(3));
                if (notation.contains("x")) {
                    char c = notation.charAt(1);
                    if (c < 'a') {
                        int startX = changeRowAlgebraicNotationToRowPosition(c);
                        if (startX == x) {
                            int startY = y + 1;
                            while(startY >= 0 && startY < 8) {
                                Spot spot = board.getSpot(startX, startY);
                                Piece piece = spot.getPiece();
                                if (piece != null) {
                                    if (piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                        move = new Move(spot, board.getSpot(x, y));
                                        move.setMoveType(MoveType.NORMAL);
                                    }
                                    break;
                                }
                                startY++;
                            }
                            if (move == null) {
                                startY = y - 1;
                                while(startY >= 0 && startY < 8) {
                                    Spot spot = board.getSpot(startX, startY);
                                    Piece piece = spot.getPiece();
                                    if (piece != null) {
                                        if (piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                            move = new Move(spot, board.getSpot(x, y));
                                            move.setMoveType(MoveType.NORMAL);
                                        }
                                        break;
                                    }
                                    startY--;
                                }
                            }
                        }else {
                            int directionX = Integer.signum(startX - x);
                            int distance = Math.abs(startX - x);
                            int startY = y + distance;
                            Spot start = board.getSpot(startX, startY);
                            Piece piece = start.getPiece();
                            boolean isSpot = true;
                            if (piece != null && piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                int checkX = startX - directionX;
                                int checkY = startY - 1;
                                while (checkX != x) {
                                    Spot checkSpot = board.getSpot(checkX, checkY);
                                    if (checkSpot.getPiece() != null) {
                                        isSpot = false;
                                        break;
                                    }
                                    checkX -= directionX;
                                    checkY--;
                                }
                            }else {
                                isSpot = false;
                            }
                            if (!isSpot) {
                                start = board.getSpot(startX, y);
                                piece = start.getPiece();
                                isSpot = true;
                                if (piece != null && piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                    int checkX = startX - directionX;
                                    while (checkX != x) {
                                        Spot checkSpot = board.getSpot(checkX, y);
                                        if (checkSpot.getPiece() != null) {
                                            isSpot = false;
                                            break;
                                        }
                                        checkX -= directionX;
                                    }
                                }else {
                                    isSpot = false;
                                }
                            }
                            if (!isSpot) {
                                startY = y - distance;
                                start = board.getSpot(startX, startY);
                                piece = start.getPiece();
                                isSpot = true;
                                if (piece != null && piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                    int checkX = startX - directionX;
                                    int checkY = startY + 1;
                                    while (checkX != x) {
                                        Spot checkSpot = board.getSpot(checkX, checkY);
                                        if (checkSpot.getPiece() != null) {
                                            isSpot = false;
                                            break;
                                        }
                                        checkX -= directionX;
                                        checkY++;
                                    }
                                } else {
                                    isSpot = false;
                                }

                            }
                            if (isSpot) {
                                move = new Move(start, board.getSpot(x, y));
                                move.setMoveType(MoveType.NORMAL);
                            }
                        }
                    }else {
                        int startY = changeColAlgebraicNotationToColPosition(c);
                        if (startY == y) {
                            int startX = x + 1;
                            while(startX >= 0 && startX < 8) {
                                Spot spot = board.getSpot(startX, startY);
                                Piece piece = spot.getPiece();
                                if (piece != null) {
                                    if (piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                        move = new Move(spot, board.getSpot(x, y));
                                        move.setMoveType(MoveType.NORMAL);
                                    }
                                    break;
                                }
                                startX++;
                            }
                            if (move == null) {
                                startX = x - 1;
                                while(startX >= 0 && startX < 8) {
                                    Spot spot = board.getSpot(startX, startY);
                                    Piece piece = spot.getPiece();
                                    if (piece != null) {
                                        if (piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                            move = new Move(spot, board.getSpot(x, y));
                                            move.setMoveType(MoveType.NORMAL);
                                        }
                                        break;
                                    }
                                    startX--;
                                }
                            }
                        }else {
                            int directionY = Integer.signum(startY - y);
                            int distance = Math.abs(startY - y);
                            int startX = x + distance;
                            Spot start = board.getSpot(startX, startY);
                            Piece piece = start.getPiece();
                            boolean isSpot = true;
                            if (piece != null && piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                int checkX = startX - 1;
                                int checkY = startY - directionY;
                                while (checkX != x) {
                                    Spot checkSpot = board.getSpot(checkX, checkY);
                                    if (checkSpot.getPiece() != null) {
                                        isSpot = false;
                                        break;
                                    }
                                    checkX--;
                                    checkY -= directionY;
                                }
                            }else {
                                isSpot = false;
                            }
                            if (!isSpot) {
                                start = board.getSpot(x, startY);
                                piece = start.getPiece();
                                isSpot = true;
                                if (piece != null && piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                    int checkY = startY - directionY;
                                    while (checkY != y) {
                                        Spot checkSpot = board.getSpot(x, checkY);
                                        if (checkSpot.getPiece() != null) {
                                            isSpot = false;
                                            break;
                                        }
                                        checkY -= directionY;
                                    }
                                }else {
                                    isSpot = false;
                                }
                            }
                            if (!isSpot) {
                                startX = x - distance;
                                start = board.getSpot(startX, startY);
                                piece = start.getPiece();
                                isSpot = true;
                                if (piece != null && piece.getType() == PieceType.QUEEN && piece.isWhite() == isWhite) {
                                    int checkX = startX + 1;
                                    int checkY = startY - directionY;
                                    while (checkX != x) {
                                        Spot checkSpot = board.getSpot(checkX, checkY);
                                        if (checkSpot.getPiece() != null) {
                                            isSpot = false;
                                            break;
                                        }
                                        checkX++;
                                        checkY -= directionY;
                                    }
                                }else {
                                    isSpot = false;
                                }
                            }
                            if (isSpot) {
                                move = new Move(start, board.getSpot(x, y));
                                move.setMoveType(MoveType.NORMAL);
                            }
                        }
                    }
                }else {
                    Spot startSpot = board.getSpot(
                        changeRowAlgebraicNotationToRowPosition(notation.charAt(2)),
                        changeColAlgebraicNotationToColPosition(notation.charAt(1)));
                    move = new Move(startSpot, board.getSpot(x, y));
                    move.setMoveType(MoveType.NORMAL);
                }
            }
            case 6 -> {
                Spot startSpot = board.getSpot(
                    changeRowAlgebraicNotationToRowPosition(notation.charAt(2)),
                    changeColAlgebraicNotationToColPosition(notation.charAt(1)));
                Spot endSpot = board.getSpot(
                    changeRowAlgebraicNotationToRowPosition(notation.charAt(5)),
                    changeColAlgebraicNotationToColPosition(notation.charAt(4)));
                move = new Move(startSpot, endSpot);
                move.setMoveType(MoveType.CAPTURE);
            }
        }
        return move;
    }

    public static Move changeRookNotationToMove(int length, String notation, Board board, boolean isWhite) {
        Move move = null;
        switch (length) {
            case 3 -> {
                int x = changeRowAlgebraicNotationToRowPosition(notation.charAt(2));
                int y = changeColAlgebraicNotationToColPosition(notation.charAt(1));
                for (int[] rookMove : Rook.rookMoves()) {
                    int checkX = x + rookMove[0];
                    int checkY = y + rookMove[1];
                    while(board.isWithinBoard(checkX, checkY)) {
                        Spot checkSpot = board.getSpot(checkX, checkY);
                        Piece piece = checkSpot.getPiece();
                        if(piece != null) {
                            if (piece.getType() == PieceType.ROOK && piece.isWhite() == isWhite) {
                                move = new Move(checkSpot, board.getSpot(x, y));
                                move.setMoveType(MoveType.NORMAL);
                                return move;
                            }else {
                                break;
                            }
                        }
                        checkX += rookMove[0];
                        checkY += rookMove[1];
                    }
                }
            }
            case 4 -> {
                int x = changeRowAlgebraicNotationToRowPosition(notation.charAt(3));
                int y = changeColAlgebraicNotationToColPosition(notation.charAt(2));
                if (notation.contains("x")) {
                    for (int[] rookMove : Rook.rookMoves()) {
                        int checkX = x + rookMove[0];
                        int checkY = y + rookMove[1];
                        while(board.isWithinBoard(checkX, checkY)) {
                            Spot checkSpot = board.getSpot(checkX, checkY);
                            Piece piece = checkSpot.getPiece();
                            if(piece != null) {
                                if (piece.getType() == PieceType.ROOK && piece.isWhite() == isWhite) {
                                    move = new Move(checkSpot, board.getSpot(x, y));
                                    move.setMoveType(MoveType.CAPTURE);
                                    return move;
                                }else {
                                    break;
                                }
                            }
                            checkX += rookMove[0];
                            checkY += rookMove[1];
                        }
                    }
                }else {
                    char c = notation.charAt(1);
                    if (c < 'a') {
                        int startX = changeRowAlgebraicNotationToRowPosition(c);
                        if (startX == x) {
                            int checkY = y + 1;
                            while(checkY >= 0 && checkY < 8) {
                                Spot spot = board.getSpot(x, checkY);
                                Piece piece = spot.getPiece();
                                if(piece != null) {
                                    if (piece.getType() == PieceType.ROOK && piece.isWhite() == isWhite) {
                                        move = new Move(spot, board.getSpot(x, y));
                                        move.setMoveType(MoveType.NORMAL);
                                        return move;
                                    }
                                    break;
                                }
                                checkY++;
                            }
                            checkY = y - 1;
                            while (checkY >= 0 && checkY < 8) {
                                Spot spot = board.getSpot(x, checkY);
                                Piece piece = spot.getPiece();
                                if(piece != null) {
                                    if (piece.getType() == PieceType.ROOK && piece.isWhite() == isWhite) {
                                        move = new Move(spot, board.getSpot(x, y));
                                        move.setMoveType(MoveType.NORMAL);
                                        return move;
                                    }
                                    break;
                                }
                                checkY--;
                            }
                        }else {
                            int directionX = Integer.signum(startX - x);
                            int checkX = x + directionX;
                            Spot start = board.getSpot(startX, y);
                            Piece piece = start.getPiece();
                            boolean isSpot = true;
                            if (piece != null && piece.getType() == PieceType.ROOK && piece.isWhite() == isWhite) {
                                while (checkX != startX) {
                                    Spot checkSpot = board.getSpot(checkX, y);
                                    if (checkSpot.getPiece() != null) {
                                        isSpot = false;
                                        break;
                                    }
                                    checkX += directionX;
                                }
                            }
                            if (isSpot) {
                                move = new Move(start, board.getSpot(x, y));
                                move.setMoveType(MoveType.NORMAL);
                            }
                        }
                    }else {
                        int startY = changeColAlgebraicNotationToColPosition(c);
                        if (startY == y) {
                            int checkX = x + 1;
                            while(checkX >= 0 && checkX < 8) {
                                Spot spot = board.getSpot(checkX, y);
                                Piece piece = spot.getPiece();
                                if(piece != null) {
                                    if (piece.getType() == PieceType.ROOK && piece.isWhite() == isWhite) {
                                        move = new Move(spot, board.getSpot(x, y));
                                        move.setMoveType(MoveType.NORMAL);
                                        return move;
                                    }
                                    break;
                                }
                                checkX++;
                            }
                            checkX = x - 1;
                            while (checkX >= 0 && checkX < 8) {
                                Spot spot = board.getSpot(checkX, y);
                                Piece piece = spot.getPiece();
                                if(piece != null) {
                                    if (piece.getType() == PieceType.ROOK && piece.isWhite() == isWhite) {
                                        move = new Move(spot, board.getSpot(x, y));
                                        move.setMoveType(MoveType.NORMAL);
                                        return move;
                                    }
                                    break;
                                }
                                checkX--;
                            }
                        }else {
                            int directionY = Integer.signum(startY - y);
                            int checkY = y + directionY;
                            Spot start = board.getSpot(x, startY);
                            Piece piece = start.getPiece();
                            boolean isSpot = true;
                            if (piece != null && piece.getType() == PieceType.ROOK && piece.isWhite() == isWhite) {
                                while (checkY != startY) {
                                    Spot checkSpot = board.getSpot(x, checkY);
                                    if (checkSpot.getPiece() != null) {
                                        isSpot = false;
                                        break;
                                    }
                                    checkY += directionY;
                                }
                            }
                            if (isSpot) {
                                move = new Move(start, board.getSpot(x, y));
                                move.setMoveType(MoveType.NORMAL);
                            }
                        }
                    }
                }
            }
            case 5 -> {
                int x = changeRowAlgebraicNotationToRowPosition(notation.charAt(4));
                int y = changeColAlgebraicNotationToColPosition(notation.charAt(3));
                char c = notation.charAt(2);
                if (c == 'x') {
                    char check = notation.charAt(1);
                    if (check < 'a') {
                        int startX = changeRowAlgebraicNotationToRowPosition(check);
                        if (startX == x) {
                            int checkY = y + 1;
                            while(checkY >= 0 && checkY < 8) {
                                Spot spot = board.getSpot(x, checkY);
                                Piece piece = spot.getPiece();
                                if(piece != null) {
                                    if (piece.getType() == PieceType.ROOK && piece.isWhite() == isWhite) {
                                        move = new Move(spot, board.getSpot(x, y));
                                        move.setMoveType(MoveType.CAPTURE);
                                        return move;
                                    }
                                    break;
                                }
                                checkY++;
                            }
                            checkY = y - 1;
                            while (checkY >= 0 && checkY < 8) {
                                Spot spot = board.getSpot(x, checkY);
                                Piece piece = spot.getPiece();
                                if(piece != null) {
                                    if (piece.getType() == PieceType.ROOK && piece.isWhite() == isWhite) {
                                        move = new Move(spot, board.getSpot(x, y));
                                        move.setMoveType(MoveType.CAPTURE);
                                        return move;
                                    }
                                    break;
                                }
                                checkY--;
                            }
                        }else {
                            int directionX = Integer.signum(startX - x);
                            int checkX = x + directionX;
                            Spot start = board.getSpot(startX, y);
                            Piece piece = start.getPiece();
                            boolean isSpot = true;
                            if (piece != null && piece.getType() == PieceType.ROOK && piece.isWhite() == isWhite) {
                                while (checkX != startX) {
                                    Spot checkSpot = board.getSpot(checkX, y);
                                    if (checkSpot.getPiece() != null) {
                                        isSpot = false;
                                        break;
                                    }
                                    checkX += directionX;
                                }
                            }
                            if (isSpot) {
                                move = new Move(start, board.getSpot(x, y));
                                move.setMoveType(MoveType.CAPTURE);
                            }
                        }
                    }else {
                        int startY = changeColAlgebraicNotationToColPosition(check);
                        if (startY == y) {
                            int checkX = x + 1;
                            while(checkX >= 0 && checkX < 8) {
                                Spot spot = board.getSpot(checkX, y);
                                Piece piece = spot.getPiece();
                                if(piece != null) {
                                    if (piece.getType() == PieceType.ROOK && piece.isWhite() == isWhite) {
                                        move = new Move(spot, board.getSpot(x, y));
                                        move.setMoveType(MoveType.CAPTURE);
                                        return move;
                                    }
                                    break;
                                }
                                checkX++;
                            }
                            checkX = x - 1;
                            while (checkX >= 0 && checkX < 8) {
                                Spot spot = board.getSpot(checkX, y);
                                Piece piece = spot.getPiece();
                                if(piece != null) {
                                    if (piece.getType() == PieceType.ROOK && piece.isWhite() == isWhite) {
                                        move = new Move(spot, board.getSpot(x, y));
                                        move.setMoveType(MoveType.CAPTURE);
                                        return move;
                                    }
                                    break;
                                }
                                checkX--;
                            }
                        }else {
                            int directionY = Integer.signum(startY - y);
                            int checkY = y + directionY;
                            Spot start = board.getSpot(x, startY);
                            Piece piece = start.getPiece();
                            boolean isSpot = true;
                            if (piece != null && piece.getType() == PieceType.ROOK && piece.isWhite() == isWhite) {
                                while (checkY != startY) {
                                    Spot checkSpot = board.getSpot(x, checkY);
                                    if (checkSpot.getPiece() != null) {
                                        isSpot = false;
                                        break;
                                    }
                                    checkY += directionY;
                                }
                            }
                            if (isSpot) {
                                move = new Move(start, board.getSpot(x, y));
                                move.setMoveType(MoveType.CAPTURE);
                            }
                        }
                    }
                }else {
                    Spot startSpot = board.getSpot(
                        changeRowAlgebraicNotationToRowPosition(c),
                        changeColAlgebraicNotationToColPosition(notation.charAt(1)));
                    move = new Move(startSpot, board.getSpot(x, y));
                    move.setMoveType(MoveType.NORMAL);
                }
            }
            case 6 -> {
                Spot startSpot = board.getSpot(
                    changeRowAlgebraicNotationToRowPosition(notation.charAt(2)),
                    changeColAlgebraicNotationToColPosition(notation.charAt(1)));
                Spot endSpot = board.getSpot(
                    changeRowAlgebraicNotationToRowPosition(notation.charAt(5)),
                    changeColAlgebraicNotationToColPosition(notation.charAt(4)));
                move = new Move(startSpot, endSpot);
                move.setMoveType(MoveType.CAPTURE);
            }
        }
        return move;
    }

    public static Move changePawnNotationToMove(int length, String notation, Board board, boolean isWhite) {
        Move move = null;
        switch (length) {
            case 2 -> {
                int x = changeRowAlgebraicNotationToRowPosition(notation.charAt(1));
                int y = changeColAlgebraicNotationToColPosition(notation.charAt(0));
                Spot checkSpot;
                if (isWhite) {
                    checkSpot = board.getSpot(x - 1, y);
                    Piece piece = checkSpot.getPiece();
                    if (piece != null && piece.getType() == PieceType.PAWN) {
                        move = new Move(checkSpot, board.getSpot(x, y));
                        move.setMoveType(MoveType.NORMAL);
                    } else {
                        move = new Move(board.getSpot(x - 2, y), board.getSpot(x, y));
                        move.setMoveType(MoveType.DOUBLE_STEP_PAWN);
                    }
                }else {
                    checkSpot = board.getSpot(x + 1, y);
                    Piece piece = checkSpot.getPiece();
                    if (piece != null && piece.getType() == PieceType.PAWN) {
                        move = new Move(checkSpot, board.getSpot(x, y));
                        move.setMoveType(MoveType.NORMAL);
                    } else {
                        move = new Move(board.getSpot(x + 2, y), board.getSpot(x, y));
                        move.setMoveType(MoveType.DOUBLE_STEP_PAWN);
                    }
                }
            }
            case 4 -> {
                char c = notation.charAt(1);
                if (c == 'x') {
                    int x = changeRowAlgebraicNotationToRowPosition(notation.charAt(3));
                    int y = changeColAlgebraicNotationToColPosition(notation.charAt(2));
                    int startY = changeColAlgebraicNotationToColPosition(notation.charAt(0));
                    int startX = isWhite ? x - 1 : x + 1;
                    move = new Move(board.getSpot(startX, startY), board.getSpot(x, y));
                    Spot checkSpot = board.getSpot(startX, y);
                    Piece checkPiece = checkSpot.getPiece();
                    if (checkPiece != null && checkPiece.getType() == PieceType.PAWN && checkPiece.isWhite() != isWhite) {
                        move.setMoveType(MoveType.EN_PASSANT);
                    }else {
                        move.setMoveType(MoveType.CAPTURE);
                    }
                }else {
                    int x = changeRowAlgebraicNotationToRowPosition(c);
                    int y = changeColAlgebraicNotationToColPosition(notation.charAt(0));
                    int startX = isWhite ? x - 1 : x + 1;
                    move = new Move(board.getSpot(startX, y), board.getSpot(x, y));
                    switch (notation.charAt(3)) {
                        case 'Q' -> move.setMoveType(MoveType.PROMOTE_TO_QUEEN);
                        case 'N' -> move.setMoveType(MoveType.PROMOTE_TO_KNIGHT);
                        case 'R' -> move.setMoveType(MoveType.PROMOTE_TO_ROOK);
                        case 'B' -> move.setMoveType(MoveType.PROMOTE_TO_BISHOP);
                    }
                }
            }
            case 6 -> {
                int x = changeRowAlgebraicNotationToRowPosition(notation.charAt(3));
                int y = changeColAlgebraicNotationToColPosition(notation.charAt(2));
                int startY = changeColAlgebraicNotationToColPosition(notation.charAt(0));
                int startX = isWhite ? x - 1 : x + 1;
                move = new Move(board.getSpot(startX, startY), board.getSpot(x, y));
                switch (notation.charAt(5)) {
                    case 'Q' -> move.setMoveType(MoveType.PROMOTE_TO_QUEEN);
                    case 'N' -> move.setMoveType(MoveType.PROMOTE_TO_KNIGHT);
                    case 'R' -> move.setMoveType(MoveType.PROMOTE_TO_ROOK);
                    case 'B' -> move.setMoveType(MoveType.PROMOTE_TO_BISHOP);
                }
            }
        }
        return move;
    }
}
