//https://www.chessprogramming.org/Simplified_Evaluation_Function
package com.huy.game.chess.ai;

import com.huy.game.chess.core.Piece;
import com.huy.game.chess.core.Spot;

public class SimplifiedEvaluationFunction {

    private static final int[] KNIGHT_TABLE = {
        -50,-40,-30,-30,-30,-30,-40,-50,
        -40,-20,  0,  0,  0,  0,-20,-40,
        -30,  0, 10, 15, 15, 10,  0,-30,
        -30,  5, 15, 20, 20, 15,  5,-30,
        -30,  0, 15, 20, 20, 15,  0,-30,
        -30,  5, 10, 15, 15, 10,  5,-30,
        -40,-20,  0,  5,  5,  0,-20,-40,
        -50,-40,-30,-30,-30,-30,-40,-50};

    private static final int[] BISHOP_TABLE = {
        -20,-10,-10,-10,-10,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5, 10, 10,  5,  0,-10,
        -10,  5,  5, 10, 10,  5,  5,-10,
        -10,  0, 10, 10, 10, 10,  0,-10,
        -10, 10, 10, 10, 10, 10, 10,-10,
        -10,  5,  0,  0,  0,  0,  5,-10,
        -20,-10,-10,-10,-10,-10,-10,-20,};

    private static final int[] ROOK_TABLE =   {
        0,  0,  0,  0,  0,  0,  0,  0,
        5, 10, 10, 10, 10, 10, 10,  5,
        -5,  0,  0,  0,  0,  0,  0, -5,
        -5,  0,  0,  0,  0,  0,  0, -5,
        -5,  0,  0,  0,  0,  0,  0, -5,
        -5,  0,  0,  0,  0,  0,  0, -5,
        -5,  0,  0,  0,  0,  0,  0, -5,
        0,  0,  0,  5,  5,  0,  0,  0};

    private static final int[] QUEEN_TABLE = {
        -20,-10,-10, -5, -5,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5,  5,  5,  5,  0,-10,
        -5,  0,  5,  5,  5,  5,  0, -5,
         0,  0,  5,  5,  5,  5,  0, -5,
        -10,  5,  5,  5,  5,  5,  0,-10,
        -10,  0,  5,  0,  0,  0,  0,-10,
        -20,-10,-10, -5, -5,-10,-10,-20};

    private static final int[] PAWN_TABLE =  {
        0,  0,  0,  0,  0,  0,  0,  0,
        50, 50, 50, 50, 50, 50, 50, 50,
        10, 10, 20, 30, 30, 20, 10, 10,
        5,  5, 10, 25, 25, 10,  5,  5,
        0,  0,  0, 20, 20,  0,  0,  0,
        5, -5,-10,  0,  0,-10, -5,  5,
        5, 10, 10,-20,-20, 10, 10,  5,
        0,  0,  0,  0,  0,  0,  0,  0};

    private static final int[] KING_TABLE = {
        -50,-40,-30,-20,-20,-30,-40,-50,
        -30,-20,-10,  0,  0,-10,-20,-30,
        -30,-10, 10, 15, 15, 10,-10,-30,
        -30,-10, 15, 20, 20, 15,-10,-30,
        -30,-10, 15, 20, 20, 15,-10,-30,
        -30,-10, 10, 10, 10, 10,-10,-30,
        15, 15,  0,  0,  0,  0, 15, 15,
        15, 20, 10,  0,  0, 10, 20, 15};

    public static int score(Spot spot) {
        Piece piece = spot.getPiece();
        int position = spot.getX() * 8 + spot.getY();
        if(!piece.isWhite()) {
            position = 63 - position;
        }
        switch (piece.getType()) {
            case PAWN -> {
                return PAWN_TABLE[position];
            }
            case KNIGHT -> {
                return KNIGHT_TABLE[position];
            }
            case BISHOP -> {
                return BISHOP_TABLE[position];
            }
            case QUEEN -> {
                return QUEEN_TABLE[position];
            }
            case ROOK -> {
                return ROOK_TABLE[position];
            }
            case KING ->  {
                return KING_TABLE[position];
            }
        }
        return 0;
    }

}
