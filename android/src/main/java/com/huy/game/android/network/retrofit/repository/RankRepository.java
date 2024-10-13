package com.huy.game.android.network.retrofit.repository;

import com.huy.game.android.models.response.GetAllRankResponse;
import com.huy.game.android.network.retrofit.RetrofitClient;
import com.huy.game.android.network.retrofit.api.RankAPI;

import retrofit2.Callback;

public class RankRepository {

    private final RankAPI rankAPI = RetrofitClient.getInstance().getClient().create(RankAPI.class);

    public void getAllRank(Callback<GetAllRankResponse> callback) {
        rankAPI.getAllRank().enqueue(callback);
    }
}
