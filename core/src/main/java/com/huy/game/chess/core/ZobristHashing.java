package com.huy.game.chess.core;

import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.enums.PieceType;

import java.util.Random;

public class ZobristHashing {

    private final long[][][] zobristArray = new long[8][8][12];
    private long hashValue = 0L;

    public ZobristHashing(Spot[][] spots, GameHistory history) {
        initialization();
        calculateInitValue(spots);
        history.addStateHash(hashValue);
    }

    public void initialization() {
        Random random = new Random();
        for(int i = 0; i <= 7; i++) {
            for(int j = 0; j <= 7; j++) {
                for (int k = 0; k <= 11; k++) {
                    zobristArray[i][j][k] = random.nextLong();
                }
            }
        }
    }

    public void calculateInitValue(Spot[][] spots) {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (spots[i][j].getPiece() != null) {
                    hashValue ^= zobristArray[i][j][indexOf(spots[i][j].getPiece().getType(), spots[i][j].getPiece().isWhite())];
                }
            }
        }
    }

    public long makeAMove(Spot start, Spot end, MoveType type) {
        int indexStart = indexOf(start.getPiece().getType(), start.getPiece().isWhite());
        hashValue ^= zobristArray[start.getX()][start.getY()][indexStart];
        hashValue ^= zobristArray[end.getX()][end.getY()][indexStart];
        handleSpecialMove(start, end, type);
        return hashValue;
    }

    public long undoAMove(Spot start, Spot end, MoveType type) {
        int indexStart = indexOf(start.getPiece().getType(), start.getPiece().isWhite());
        hashValue ^= zobristArray[start.getX()][start.getY()][indexStart];
        hashValue ^= zobristArray[end.getX()][end.getY()][indexStart];
        handleSpecialMove(start, end, type);
        return hashValue;
    }

    private void handleSpecialMove(Spot start, Spot end, MoveType type) {
        switch (type) {
            case CAPTURE -> hashValue ^= zobristArray[end.getX()][end.getY()][indexOf(end.getPiece().getType(), end.getPiece().isWhite())];
            case CASTLING_KING_SIDE -> {
                int index = indexOf(PieceType.ROOK, start.getPiece().isWhite());
                hashValue ^= zobristArray[start.getX()][7][index];
                hashValue ^= zobristArray[start.getX()][start.getY() + 1][index];
            }
            case CASTLING_QUEEN_SIDE -> {
                int index = indexOf(PieceType.ROOK , start.getPiece().isWhite());
                hashValue ^= zobristArray[start.getX()][0][index];
                hashValue ^= zobristArray[start.getX()][start.getY() - 1][index];
            }
            case EN_PASSANT -> hashValue ^= zobristArray[start.getX()][end.getY()][indexOf(PieceType.PAWN, !start.getPiece().isWhite())];
            case PROMOTE_TO_QUEEN -> {
                hashValue ^= zobristArray[end.getX()][end.getY()][indexOf(PieceType.QUEEN, start.getPiece().isWhite())];
                if(end.getPiece() != null) {
                    hashValue ^= zobristArray[end.getX()][end.getY()][indexOf(end.getPiece().getType(), end.getPiece().isWhite())];
                }
            }
            case PROMOTE_TO_KNIGHT -> {
                hashValue ^= zobristArray[end.getX()][end.getY()][indexOf(PieceType.KNIGHT, start.getPiece().isWhite())];
                if(end.getPiece() != null) {
                    hashValue ^= zobristArray[end.getX()][end.getY()][indexOf(end.getPiece().getType(), end.getPiece().isWhite())];
                }
            }
            case PROMOTE_TO_ROOK -> {
                hashValue ^= zobristArray[end.getX()][end.getY()][indexOf(PieceType.ROOK, start.getPiece().isWhite())];
                if(end.getPiece() != null) {
                    hashValue ^= zobristArray[end.getX()][end.getY()][indexOf(end.getPiece().getType(), end.getPiece().isWhite())];
                }
            }
            case PROMOTE_TO_BISHOP -> {
                hashValue ^= zobristArray[end.getX()][end.getY()][indexOf(PieceType.BISHOP, start.getPiece().isWhite())];
                if(end.getPiece() != null) {
                    hashValue ^= zobristArray[end.getX()][end.getY()][indexOf(end.getPiece().getType(), end.getPiece().isWhite())];
                }
            }
        }
    }

    private int indexOf(PieceType type, boolean isWhite) {
        if(isWhite) {
            switch (type) {
                case PAWN -> {
                    return 0;
                }
                case KNIGHT -> {
                    return 1;
                }
                case BISHOP -> {
                    return 2;
                }
                case QUEEN -> {
                    return 3;
                }
                case ROOK -> {
                    return 4;
                }
                default -> {
                    return 5;
                }
            }
        }else {
            switch (type) {
                case PAWN -> {
                    return 6;
                }
                case KNIGHT -> {
                    return 7;
                }
                case BISHOP -> {
                    return 8;
                }
                case QUEEN -> {
                    return 9;
                }
                case ROOK -> {
                    return 10;
                }
                default -> {
                    return 11;
                }
            }
        }
    }
}
