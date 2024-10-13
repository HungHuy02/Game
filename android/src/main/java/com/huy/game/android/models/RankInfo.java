package com.huy.game.android.models;

import com.google.gson.annotations.SerializedName;

public class RankInfo {

    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("score")
    private int elo;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getElo() {
        return elo;
    }
}
