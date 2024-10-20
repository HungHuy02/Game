package com.huy.game.chess.core;

import com.badlogic.gdx.graphics.Texture;
import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.enums.PieceType;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    private int turn;

    public Pawn(Pawn pawn) {
        super(pawn.isWhite());
        turn = pawn.turn;
        setType(PieceType.PAWN);
    }

    public Pawn(boolean isWhite, Texture texture) {
        super(isWhite, texture);
        setType(PieceType.PAWN);
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }
    private int[][] pawnMoves(Spot checkSpot) {
        int[][] pawnMoves;
        if (checkSpot.getPiece().isWhite()) {
            pawnMoves = new int[][]{
                {1, 0}, {1, -1}, {1, 1}, {2, 0}
            };
        } else {
            pawnMoves = new int[][]{
                {-1, 0}, {-1, -1}, {-1, 1}, {-2, 0}
            };
        }
        return pawnMoves;
    }

    private MoveType[] promoting() {
        return new MoveType[] {
            MoveType.PROMOTE_TO_QUEEN,
            MoveType.PROMOTE_TO_KNIGHT,
            MoveType.PROMOTE_TO_ROOK,
            MoveType.PROMOTE_TO_BISHOP
        };
    }

    @Override
    public MoveType canMove(Board board, Spot[][] spots, Spot start, Spot end) {
        if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return MoveType.CAN_NOT_MOVE;
        }
        int x = end.getX() - start.getX();
        int y = Math.abs(end.getY() - start.getY());
        if (this.isWhite()) {
            if (x < 0) {
                return MoveType.CAN_NOT_MOVE;
            }
        } else {
            if (x > 0) {
                return MoveType.CAN_NOT_MOVE;
            }
        }
        x = Math.abs(x);
        if (x == 2 && y == 0) {
            if (spots[Integer.signum(end.getX() - start.getX()) + start.getX()][start.getY()].getPiece() == null) {
                if (this.isWhite() && start.getX() == 1 && end.getPiece() == null) {
                    return MoveType.DOUBLE_STEP_PAWN;
                }
                if (!this.isWhite() && start.getX() == 6 && end.getPiece() == null) {
                    return MoveType.DOUBLE_STEP_PAWN;
                }
            }
            return MoveType.CAN_NOT_MOVE;
        } else if (x == 1 && y == 1) {
            if (end.getPiece() != null) {
                if(start.getX() == 1 && !this.isWhite()) {
                    return MoveType.PROMOTE;
                }
                if(start.getX() == 6 && this.isWhite()) {
                    return MoveType.PROMOTE;
                }
                return MoveType.CAPTURE;
            } else {
                Spot checkSpot = spots[start.getX()][end.getY()];
                if (board.getPossibleEnPassantTargetsSpot() != null) {
                    if (board.getPossibleEnPassantTargetsSpot().equals(checkSpot)) {
                        if(checkSpot.getPiece() instanceof Pawn pawn) {
                            if (board.getTurn() - pawn.turn == 1) {
                                return MoveType.EN_PASSANT;
                            }
                        }
                    }
                }
                return MoveType.CAN_NOT_MOVE;
            }
        } else if (x == 1 && y == 0) {
            if (end.getPiece() == null) {
                if(start.getX() == 1 && !this.isWhite()) {
                    return MoveType.PROMOTE;
                }
                if(start.getX() == 6 && this.isWhite()) {
                    return MoveType.PROMOTE;
                }
                return MoveType.NORMAL;
            } else {
                return MoveType.CAN_NOT_MOVE;
            }
        } else {
            return MoveType.CAN_NOT_MOVE;
        }
    }

    @Override
    public boolean isCheckOpponentKing(Board board, Spot[][] spots, Spot spot) {
        Spot king = board.getKingSpot(!isWhite());
        int x = spot.getX();
        int y = spot.getY();
        if (isWhite()) {
            x++;
        }else {
            x--;
        }
        if(x == king.getX()) {
            if (y - 1 == king.getY()) {
                return true;
            }
            if(y + 1 == king.getY()) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean calculateMove(Board board, Spot checkSpot) {
        Board testBoard = board.cloneBoard();
        Spot[][] spots = testBoard.getSpots();
        for (int[] move: pawnMoves(checkSpot)) {
            int x = move[0] + checkSpot.getX();
            int y = move[1] + checkSpot.getY();
            if(board.isWithinBoard(x, y)) {
                if(calculateOneMove(testBoard, spots,checkSpot, x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void calculateForPoint(Board board, Spot checkSpot) {
        Board testBoard = board.cloneBoard();
        Spot[][] spots = testBoard.getSpots();
        for (int[] move: pawnMoves(checkSpot)) {
            int x = move[0] + checkSpot.getX();
            int y = move[1] + checkSpot.getY();
            if(board.isWithinBoard(x, y)) {
                calculateForOnePoint(board, testBoard, spots, checkSpot, x, y);
            }
        }
    }

    @Override
    public void calculateForOnePoint(Board board, Board testBoard, Spot[][] spots, Spot checkSpot, int x, int y) {
        Spot start = spots[checkSpot.getX()][checkSpot.getY()];
        Spot end = spots[x][y];
        Move move = new Move(start, end);
        MoveType moveType = canMove(testBoard, spots, start, end);
        move.setMoveType(moveType);
        if(moveType != MoveType.CAN_NOT_MOVE) {
            move.makeMove(testBoard);
            if(testBoard.isKingSafe(checkSpot.getPiece().isWhite())) {
                board.getSpot(x, y).setShowMovePoint(true);
                if (moveType == MoveType.CAPTURE ) {
                    board.getSpot(x, y).setCanBeCaptured(true);
                }
            }
            move.unMove(testBoard);
            if (end.getPiece() != null) {
                board.getSpot(x, y).setCanBeCaptured(true);
            }
        }
    }

    @Override
    public List<Move> getValidMoves(Board board, Spot[][] spots, Spot checkSpot) {
        List<Move> list = new ArrayList<>();
        for (int[] move: pawnMoves(checkSpot)) {
            int x = move[0] + checkSpot.getX();
            int y = move[1] + checkSpot.getY();
            if(board.isWithinBoard(x, y)) {
                if(calculateOneMove(board, spots, checkSpot, x, y)) {
                    list.add(new Move(checkSpot, spots[x][y]));
                }
            }
        }
        return list;
    }
}
