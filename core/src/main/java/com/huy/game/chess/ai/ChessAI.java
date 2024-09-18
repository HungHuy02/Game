package com.huy.game.chess.ai;

import com.huy.game.chess.core.Bishop;
import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.Knight;
import com.huy.game.chess.core.Move;
import com.huy.game.chess.core.Pawn;
import com.huy.game.chess.core.Piece;
import com.huy.game.chess.core.Queen;
import com.huy.game.chess.core.Rook;
import com.huy.game.chess.core.Spot;

import java.util.List;

public class ChessAI {
    int evaluateScore(Spot[][] spots, boolean isWhite, Board board) {
        int score = 0;
        for(int i = 0; i <= 7; i++) {
            for(int j = 0; j <= 7; j++) {
                Piece piece = spots[i][j].getPiece();
                if(piece != null) {
                    int k = piece.isWhite() == isWhite ? 1 : -1;
                    if(piece instanceof Pawn) {
                        score += k;
                    }else if(piece instanceof Rook) {
                        score += 5 * k;
                    }else if(piece instanceof Bishop) {
                        score += 3 * k;
                    }else if(piece instanceof Knight) {
                        score += 3 * k;
                    }else if (piece instanceof Queen) {
                        score += 9 * k;
                    }else {
                        if(board.isKingSafe(piece.isWhite())) {
                            score += 900 * k;
                        }else {
                            score -= 900 * k;
                        }
                    }
                }
            }
        }
        return score;
    }

    public int minimax(Board board , Spot[][] spots, boolean isWhite, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if(depth == 0 || board.isEnd()) {
            return evaluateScore(spots, isWhite, board);
        }

        if(maximizingPlayer) {
            int maxValue = Integer.MIN_VALUE;
            for(int i = 0; i <= 7; i++) {
                for(int j = 0; j <= 7 ; j++) {
                    if(spots[i][j].getPiece() != null) {
                        if(spots[i][j].getPiece().isWhite() == isWhite) {
                            List<Move> list = spots[i][j].getPiece().getValidMoves(board, spots, spots[i][j]);
                            for (Move move: list) {
                                move.makeMove(board);
                                int value = minimax(board, spots, isWhite,depth - 1, alpha, beta, false);
                                move.unMove(board);
                                maxValue = Math.max(value, maxValue);
                                alpha = Math.max(value, alpha);
                                if(beta <= alpha)
                                    break;
                            }
                        }
                    }
                }
            }
            return maxValue;
        }else {
            int minValue = Integer.MAX_VALUE;
            for(int i = 0; i <= 7; i++) {
                for(int j = 0; j <= 7 ; j++) {
                    if(spots[i][j].getPiece() != null) {
                        if(spots[i][j].getPiece().isWhite() != isWhite) {
                            List<Move> list = spots[i][j].getPiece().getValidMoves(board, spots, spots[i][j]);
                            for (Move move: list) {
                                move.makeMove(board);
                                int value = minimax(board, spots, isWhite,depth - 1, alpha, beta, true);
                                move.unMove(board);
                                minValue = Math.min(value, minValue);
                                beta = Math.min(value, beta);
                                if(beta <= alpha)
                                    break;
                            }
                        }
                    }
                }
            }
            return minValue;
        }
    }

    public Move findBestMove(Board board, boolean isWhite) {
        int bestValue = Integer.MIN_VALUE;
        Move bestMove = null;
        Board testBoard = board.cloneBoard();
        Spot[][] spots = testBoard.getSpots();
        for(int i = 0; i <= 7; i++) {
            for(int j = 0; j <= 7 ; j++) {
                if(spots[i][j].getPiece() != null) {
                    if(spots[i][j].getPiece().isWhite() == isWhite) {
                        List<Move> moves = spots[i][j].getPiece().getValidMoves(testBoard, spots, spots[i][j]);
                        for (Move move: moves) {
                            move.makeMove(testBoard);
                            int value = minimax(testBoard, spots, isWhite,3, Integer.MIN_VALUE, Integer.MAX_VALUE, !isWhite);
                            move.unMove(testBoard);
                            if(value > bestValue) {
                                bestMove = move;
                                bestValue = value;
                            }
                        }
                    }
                }
            }
        }
        return bestMove;
    }
}

