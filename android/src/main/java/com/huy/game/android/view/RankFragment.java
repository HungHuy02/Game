package com.huy.game.android.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.huy.game.databinding.FragmentRankBinding;

public class RankFragment extends Fragment {

    private FragmentRankBinding fragmentRankBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentRankBinding = FragmentRankBinding.inflate(inflater, container, false);
        return fragmentRankBinding.getRoot();
    }
}
