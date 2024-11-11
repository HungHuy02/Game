package com.huy.game.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.huy.game.R;
import com.huy.game.android.globalstate.UserState;
import com.huy.game.android.roomdatabase.entity.HistoryEntity;
import com.huy.game.android.utils.Constants;
import com.huy.game.android.view.AndroidLauncher;
import com.huy.game.chess.enums.Difficulty;
import com.huy.game.chess.enums.PieceColor;
import com.huy.game.databinding.HistoryRowLayoutBinding;

import java.util.List;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.HistoryViewHolder>{

    private List<HistoryEntity> historyList;
    private Context context;

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HistoryRowLayoutBinding binding = HistoryRowLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryEntity history = historyList.get(position);
        switch (history.timeType) {
            case ONE_MINUTE, ONE_MINUTE_PLUS_ONE, TWO_MINUTE_PLUS_ONE
                -> holder.binding.timeIcon.setImageDrawable(AppCompatResources.getDrawable( context, R.drawable.icons8_bullet_39));
            case THREE_MINUTE, THREE_MINUTE_PLUS_TWO, FIVE_MINUTE, FIVE_MINUTE_PLUS_FIVE
                -> holder.binding.timeIcon.setImageDrawable(AppCompatResources.getDrawable( context, R.drawable.flash_on_24px));
            case TEN_MINUTE, FIFTEEN_MINUTE_PLUS_TEN, THIRTY_MINUTE
                -> holder.binding.timeIcon.setImageDrawable(AppCompatResources.getDrawable( context, R.drawable.timer_24px));
        }
        String imageUrl = history.imageUrl;
        if (("").equals(imageUrl) && imageUrl != null) {
            Glide.with(context)
                .load(imageUrl)
                .into(holder.binding.opponentImage);
        }

        holder.binding.tvOpponentName.setText(history.name);
        String text = switch (history.gameResult) {
            case WHITE_WIN -> "1-0";
            case BLACK_WIN -> "0-1";
            case DRAW_STALEMATE, DRAW_THREEFOLD, DRAW_FIFTY_MOVE, DRAW_INSUFFICIENT, DRAW_AGREEMENT -> "1/2-1/2";
        };
        boolean pieceColor = history.pieceColor;
        holder.binding.tvResult.setText(text);
        holder.binding.rowHistory.setOnClickListener((v) -> {
            Intent intent = new Intent(context, AndroidLauncher.class);
            intent.putExtra(Constants.BUNDLE_PGN, history.pgn);
            history.mode.setDifficulty(Difficulty.HARD);
            intent.putExtra(Constants.BUNDLE_MODE, history.mode.toString());
            intent.putExtra(Constants.BUNDLE_TIME, history.timeType.toString());
            intent.putExtra(Constants.BUNDLE_PLAYER1_NAME, UserState.getInstance().getName());
            intent.putExtra(Constants.BUNDLE_PLAYER2_NAME, history.name);
            intent.putExtra(Constants.BUNDLE_WATCHING_HISTORY, true);
            intent.putExtra(Constants.BUNDLE_PLAYER1_COLOR, pieceColor ? PieceColor.WHITE.toString() : PieceColor.BLACK.toString());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return historyList == null ? 0 : historyList.size();
    }

    public void setData(List<HistoryEntity> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        private final HistoryRowLayoutBinding binding;

        public HistoryViewHolder(HistoryRowLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
