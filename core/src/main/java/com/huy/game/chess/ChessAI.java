package com.huy.game.chess;

import java.util.List;

public class ChessAI {
    int evaluateScore(Spot[][] spots) {
        int score = 0;
        for(int i = 0; i <= 7; i++) {
            for(int j = 0; j <= 7; j++) {
                Piece piece = spots[i][j].getPiece();
                if(piece != null) {
                    int k = piece.isWhite() ? 1 : -1;
                    if(piece instanceof Pawn) {
                        score += 1 * k;
                    }else if(piece instanceof Rook) {
                        score += 5 * k;
                    }else if(piece instanceof Bishop) {
                        score += 3 * k;
                    }else if(piece instanceof Knight) {
                        score += 3 * k;
                    }else if (piece instanceof Queen) {
                        score += 9 * k;
                    }else {
                        score += 900 * k;
                    }
                }
            }
        }
        return score;
    }

    public int minimax(Board board ,Spot[][] spots, int depth, int alpha, int beta,boolean maximizingPlayer) {
        if(depth == 0) {
            return evaluateScore(spots);
        }

        if(maximizingPlayer) {
            int bestValue = Integer.MIN_VALUE;
            for(int i = 0; i <= 6; i++) {
                for(int j = 0; j <=6 ; j++) {
                    if(spots[i][j].getPiece().isWhite()) {
                        List<Move> list = spots[i][j].getPiece().getValidMoves(board.cloneSpots(), spots[i][j]);
                        for (Move move: list) {
                            move.makeMove(spots);
                            int value = minimax(board, board.cloneSpots(), depth - 1, alpha, beta, false);
                            move.unMove(spots);
                            bestValue = Math.max(value, bestValue);
                            alpha = Math.max(value, alpha);
                            if(beta <= alpha)
                                break;
                        }
                    }
                }
            }
            return bestValue;
        }else {
            int bestValue = Integer.MAX_VALUE;
            for(int i = 0; i <= 6; i++) {
                for(int j = 0; j <=6 ; j++) {
                    if(!spots[i][j].getPiece().isWhite()) {
                        List<Move> list = spots[i][j].getPiece().getValidMoves(board.cloneSpots(), spots[i][j]);
                        for (Move move: list) {
                            move.makeMove(spots);
                            int value = minimax(board, board.cloneSpots(), depth - 1, alpha, beta, true);
                            move.unMove(spots);
                            bestValue = Math.max(value, bestValue);
                            beta = Math.min(value, beta);
                            if(beta <= alpha)
                                break;
                        }
                    }
                }
            }
            return bestValue;
        }
    }

    public Move findBestMove(Board board, boolean isWhite) {
        int bestValue = Integer.MIN_VALUE;
        Move bestMove = null;
        Spot[][] spots = board.cloneSpots();
        for(int i = 0; i <= 6; i++) {
            for(int j = 0; j <=6 ; j++) {
                if(spots[i][j].getPiece().isWhite() == isWhite) {
                    List<Move> list = spots[i][j].getPiece().getValidMoves(board.cloneSpots(), spots[i][j]);
                    for (Move move: list) {
                        move.makeMove(spots);
                        int value = minimax(board, board.cloneSpots(), 3, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                        move.unMove(spots);
                        if(value > bestValue) {
                            bestMove = move;
                            bestValue = value;
                        }
                    }
                }
            }
        }
        return bestMove;
    }

}

