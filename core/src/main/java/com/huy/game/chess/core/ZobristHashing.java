package com.huy.game.chess.core;

import java.util.Random;

public class ZobristHashing {

    private long[][][] zobristArray = new long[8][8][12];
    private long hashValue = 0l;

    public ZobristHashing(Spot[][] spots) {
        initialization();
        calculateInitValue(spots);
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
                    hashValue ^= zobristArray[i][j][indexOf(spots[i][j])];
                }
            }
        }
    }

    public long makeAMove(Spot start, Spot end) {
        int indexStart = indexOf(start);
        hashValue ^= zobristArray[start.getX()][start.getY()][indexStart];
        hashValue ^= zobristArray[end.getX()][end.getY()][indexStart];
        if(end.getPiece() != null) {
            hashValue ^= zobristArray[end.getX()][end.getY()][indexOf(end)];
        }
        return hashValue;
    }

    public long undoAMove(Spot start, Spot end) {
        int indexStart = indexOf(start);
        hashValue ^= zobristArray[start.getX()][start.getY()][indexStart];
        hashValue ^= zobristArray[end.getX()][end.getY()][indexStart];
        if(end.getPiece() != null) {
            hashValue ^= zobristArray[end.getX()][end.getY()][indexOf(end)];
        }
        return hashValue;
    }

    private int indexOf(Spot spot) {
        Piece piece = spot.getPiece();
        if(piece.isWhite()) {
            if(piece instanceof Pawn) {
                return 0;
            }else if(piece instanceof Knight) {
                return 1;
            }else if(piece instanceof Bishop) {
                return 2;
            }else if(piece instanceof Rook) {
                return 3;
            }else if(piece instanceof Queen) {
                return 4;
            }else {
                return 5;
            }
        }else {
            if(piece instanceof Pawn) {
                return 6;
            }else if(piece instanceof Knight) {
                return 7;
            }else if(piece instanceof Bishop) {
                return 8;
            }else if(piece instanceof Rook) {
                return 9;
            }else if(piece instanceof Queen) {
                return 10;
            }else {
                return 11;
            }
        }
    }
}
