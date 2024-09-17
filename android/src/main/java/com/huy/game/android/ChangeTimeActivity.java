package com.huy.game.android;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huy.game.databinding.ActivityChangeTimeBinding;

public class ChangeTimeActivity extends AppCompatActivity {

    private ActivityChangeTimeBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChangeTimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btn1m.setOnClickListener((v) -> {

        });

        binding.btn1mp1.setOnClickListener((v) -> {

        });

        binding.btn2mp1.setOnClickListener((v) -> {

        });

        binding.btn3m.setOnClickListener((v) -> {

        });

        binding.btn3mp2.setOnClickListener((v) -> {

        });

        binding.btn5m.setOnClickListener((v) -> {

        });

        binding.btn10m.setOnClickListener((v) -> {

        });

        binding.btn15mp10.setOnClickListener((v) -> {

        });

        binding.btn30m.setOnClickListener((v) -> {

        });
    }
}
