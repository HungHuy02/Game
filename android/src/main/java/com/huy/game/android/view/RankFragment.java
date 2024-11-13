package com.huy.game.android.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huy.game.R;
import com.huy.game.android.adapter.RecyclerViewAdapter;
import com.huy.game.android.globalstate.UserState;
import com.huy.game.android.models.RankInfo;
import com.huy.game.android.models.response.GetAllRankResponse;
import com.huy.game.android.viewmodel.apiservice.RankServiceViewModel;
import com.huy.game.databinding.FragmentRankBinding;
import com.huy.game.shared.network.RankingSocket;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankFragment extends Fragment {

    private FragmentRankBinding fragmentRankBinding;
    private RankServiceViewModel viewModel;
    private RankingSocket socket;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentRankBinding = FragmentRankBinding.inflate(inflater, container, false);
        return fragmentRankBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        getAllRank();
        setupSocket();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(RankServiceViewModel.class);
    }

    private void getAllRank() {
        viewModel.getAllRank(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetAllRankResponse> call, @NonNull Response<GetAllRankResponse> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    List<RankInfo> list = response.body().getList();
                    setupRecyclerView(list);
                    setupCurrentRank(response.body().getRank());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetAllRankResponse> call, @NonNull Throwable throwable) {
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSocket() {
        socket = new RankingSocket();
        socket.connect();
        socket.newRankingAvailable(this::getAllRank);
    }

    private void setupRecyclerView(List<RankInfo> list) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter();
        adapter.setList(list);
        RecyclerView recyclerView = fragmentRankBinding.listView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupCurrentRank(int rank) {
        fragmentRankBinding.tvIndex.setText(String.format(getResources().getString(R.string.ranking_text), rank));
        fragmentRankBinding.tvName.setText(UserState.getInstance().getName());
        fragmentRankBinding.tvScore.setText(String.format(getResources().getString(R.string.elo_text), UserState.getInstance().getElo()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        socket.disconnect();
    }
}
