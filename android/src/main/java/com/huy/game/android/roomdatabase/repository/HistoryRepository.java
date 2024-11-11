package com.huy.game.android.roomdatabase.repository;

import com.huy.game.android.roomdatabase.HistoryDatabase;
import com.huy.game.android.roomdatabase.dao.HistoryDAO;
import com.huy.game.android.roomdatabase.entity.HistoryEntity;
import com.huy.game.chess.interfaces.LocalDatabase;
import com.huy.game.chess.model.HistoryModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class HistoryRepository implements LocalDatabase {

    private final HistoryDAO dao = HistoryDatabase.INSTANCE.historyDAO();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void getAllHistory(Consumer<List<HistoryEntity>> consumer) {
        executorService.execute(() -> consumer.accept(dao.getAllHistory()));
    }

    @Override
    public void insert(HistoryModel model) {
        HistoryEntity entity = new HistoryEntity();
        entity.gameResult = model.gameResult();
        entity.name = model.name();
        entity.imageUrl = model.imageUrl();
        entity.timeType = model.timeType();
        entity.pieceColor = model.pieceColor();
        entity.mode = model.mode();
        entity.pgn = model.pgn();
        executorService.execute(() -> dao.insert(entity));
    }
}
