package com.huy.game.android.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.huy.game.android.adapter.HistoryRecyclerViewAdapter;
import com.huy.game.android.roomdatabase.entity.HistoryEntity;
import com.huy.game.android.roomdatabase.repository.HistoryRepository;
import com.huy.game.databinding.FragementHistoryBinding;

import java.util.List;

public class HistoryFragment extends Fragment {

    private FragementHistoryBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragementHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllHistory();
    }

    private void getAllHistory() {
        HistoryRepository repository = new HistoryRepository();
        repository.getAllHistory(this::setupRecyclerView);
    }

    private void setupRecyclerView(List<HistoryEntity> list) {
        requireActivity().runOnUiThread(() -> {
            HistoryRecyclerViewAdapter adapter = new HistoryRecyclerViewAdapter();
            adapter.setData(list, getContext());
            binding.rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvHistory.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            binding.rvHistory.setAdapter(adapter);
        });
    }
}
