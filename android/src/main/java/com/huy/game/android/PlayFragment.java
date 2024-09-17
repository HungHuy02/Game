package com.huy.game.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.huy.game.R;
import com.huy.game.android.viewmodel.PlayFragmentViewModel;
import com.huy.game.databinding.FragmentPlayBinding;

public class PlayFragment extends Fragment {

    public PlayFragment() {

    }

    private FragmentPlayBinding fragmentPlayBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPlayBinding = FragmentPlayBinding.inflate(inflater, container, false);
        return fragmentPlayBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentPlayBinding.btnPlayTwo.setOnClickListener((v) -> {

        });

        fragmentPlayBinding.btnPlayAi.setOnClickListener((v) -> {

        });

        fragmentPlayBinding.btnTime.setOnClickListener((v) -> {
            Intent intent = new Intent(fragmentPlayBinding.getRoot().getContext(), ChangeTimeActivity.class);
            startActivity(intent);
        });

        fragmentPlayBinding.btnNew.setOnClickListener((v) -> {

        });

    }
}
