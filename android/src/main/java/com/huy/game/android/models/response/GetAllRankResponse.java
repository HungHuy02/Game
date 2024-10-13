package com.huy.game.android.models.response;

import com.google.gson.annotations.SerializedName;
import com.huy.game.android.models.RankInfo;

import java.util.List;

public class GetAllRankResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("list")
    private List<RankInfo> list;

    public GetAllRankResponse(boolean success, List<RankInfo> list) {
        this.success = success;
        this.list = list;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<RankInfo> getList() {
        return list;
    }
}
