package com.huy.game.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.huy.game.android.models.RankInfo;
import com.huy.game.databinding.RankRowLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<RankInfo> list = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RankRowLayoutBinding binding = RankRowLayoutBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RankInfo info = list.get(position);
        holder.getIndexTV().setText(info.getId() + "");
        holder.getNameTV().setText(info.getName());
        holder.getEloTV().setText(info.getElo() + "");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<RankInfo> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView indexTV;
        private MaterialTextView nameTV;
        private MaterialTextView eloTV;

        public ViewHolder(RankRowLayoutBinding binding) {
            super(binding.getRoot());
            indexTV = binding.tvIndex;
            nameTV = binding.tvName;
            eloTV = binding.tvScore;
        }

        public MaterialTextView getIndexTV() {
            return indexTV;
        }

        public MaterialTextView getNameTV() {
            return nameTV;
        }

        public MaterialTextView getEloTV() {
            return eloTV;
        }
    }
}
