package com.huy.game.android.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huy.game.R;
import com.huy.game.android.models.RankInfo;
import com.huy.game.databinding.RankRowLayoutBinding;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<RankInfo> list;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RankRowLayoutBinding binding = RankRowLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RankInfo info = list.get(position);
        holder.binding.tvIndex.setText(String.format(holder.binding.getRoot().getContext().getResources().getString(R.string.ranking_text), info.getRank()));
        holder.binding.tvName.setText(info.getName());
        holder.binding.tvScore.setText(String.format(holder.binding.getRoot().getContext().getResources().getString(R.string.ranking_text), info.getElo()));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<RankInfo> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private RankRowLayoutBinding binding;

        public ViewHolder(RankRowLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
