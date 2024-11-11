package com.huy.game.android.roomdatabase.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.huy.game.android.roomdatabase.entity.HistoryEntity;

import java.util.List;

@Dao
public interface HistoryDAO {
    @Insert
    void insert(HistoryEntity history);

    @Update
    void update(HistoryEntity history);

    @Query("SELECT * FROM history ORDER BY id DESC")
    List<HistoryEntity> getAllHistory();
}
