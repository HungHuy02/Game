package com.huy.game.android.models.response;

import com.google.gson.annotations.SerializedName;
import com.huy.game.android.models.RankInfo;

import java.util.List;

public class GetAllRankResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("rank")
    private int rank;
    @SerializedName("list")
    private List<RankInfo> list;

    public GetAllRankResponse(boolean success, int rank, List<RankInfo> list) {
        this.success = success;
        this.rank = rank;
        this.list = list;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getRank() {
        return rank;
    }

    public List<RankInfo> getList() {
        return list;
    }
}
