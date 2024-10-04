package com.huy.game.android.view;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.huy.game.android.base.BaseActivity;
import com.huy.game.databinding.ActivityProfileBinding;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
