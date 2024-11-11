package com.huy.game.android.roomdatabase.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.enums.GameResult;
import com.huy.game.chess.enums.TimeType;

@Entity(tableName = "history")
public class HistoryEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "time_type")
    public TimeType timeType;

    @ColumnInfo(name = "image_url")
    public String imageUrl;

    @ColumnInfo(name = "piece_color")
    public boolean pieceColor;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "mode")
    public ChessMode mode;

    @ColumnInfo(name = "result")
    public GameResult gameResult;

    @ColumnInfo(name = "pgn")
    public String pgn;
}
