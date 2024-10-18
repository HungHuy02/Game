package com.huy.game.chess.enums;

public enum MoveType {
    CAN_NOT_MOVE,
    NORMAL,
    DOUBLE_STEP_PAWN,
    CAPTURE,
    CASTLING_KING_SIDE,
    CASTLING_QUEEN_SIDE,
    EN_PASSANT,
    PROMOTE,
    PROMOTE_TO_QUEEN,
    PROMOTE_TO_KNIGHT,
    PROMOTE_TO_ROOK,
    PROMOTE_TO_BISHOP;
}
