package com.huy.game.android.viewmodel.apiservice;

import androidx.lifecycle.ViewModel;

import com.huy.game.android.models.response.GetAllRankResponse;
import com.huy.game.android.network.retrofit.repository.RankRepository;

import retrofit2.Callback;

public class RankServiceViewModel extends ViewModel {

    private final RankRepository repository = new RankRepository();

    public void getAllRank(Callback<GetAllRankResponse> callback) {
        repository.getAllRank(callback);
    }
}
