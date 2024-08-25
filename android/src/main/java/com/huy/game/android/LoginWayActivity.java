package com.huy.game.android;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.huy.game.databinding.ActivityLoginWayBinding;

public class LoginWayActivity extends AppCompatActivity {

    private ActivityLoginWayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginWayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        binding.emailBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.guestBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(this, AndroidLauncher.class);
            startActivity(intent);
        });
    }
}
