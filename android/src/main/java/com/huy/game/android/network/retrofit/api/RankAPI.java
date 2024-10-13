package com.huy.game.android.network.retrofit.api;

import com.huy.game.android.models.response.GetAllRankResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RankAPI {

    @GET("/rank/get-rank")
    Call<GetAllRankResponse> getAllRank();
}
