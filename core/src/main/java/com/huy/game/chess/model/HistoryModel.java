package com.huy.game.chess.model;

import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.enums.GameResult;
import com.huy.game.chess.enums.TimeType;

public record HistoryModel(
    TimeType timeType,
    String imageUrl,
    boolean pieceColor,
    String name,
    GameResult gameResult,
    ChessMode mode, String pgn
) {

}
