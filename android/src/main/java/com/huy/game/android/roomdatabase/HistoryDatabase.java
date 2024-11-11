package com.huy.game.android.roomdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.huy.game.android.roomdatabase.dao.HistoryDAO;
import com.huy.game.android.roomdatabase.entity.HistoryEntity;
import com.huy.game.android.local.Local;

@Database(entities = {HistoryEntity.class}, version = 1, exportSchema = false)
public abstract class HistoryDatabase extends RoomDatabase {

    public abstract HistoryDAO historyDAO();

    public static volatile HistoryDatabase INSTANCE;

    public static HistoryDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (HistoryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        HistoryDatabase.class, Local.LOCAL_DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
