package com.huy.game.android.models;

import com.google.gson.annotations.SerializedName;

public class RankInfo {

    @SerializedName("id")
    private long id;
    @SerializedName("ranking")
    private int rank;
    @SerializedName("name")
    private String name;
    @SerializedName("score")
    private int elo;

    public long getId() {
        return id;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public int getElo() {
        return elo;
    }
}
