package com.huy.game.chess.interfaces;

import com.huy.game.chess.model.HistoryModel;

public interface LocalDatabase {

    void insert(HistoryModel model);
}
