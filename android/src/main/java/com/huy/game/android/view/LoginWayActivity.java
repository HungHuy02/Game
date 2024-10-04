package com.huy.game.android.view;

import android.content.Intent;
import android.os.Bundle;

import com.huy.game.android.base.BaseActivity;
import com.huy.game.databinding.ActivityLoginWayBinding;

public class LoginWayActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.huy.game.databinding.ActivityLoginWayBinding binding = ActivityLoginWayBinding.inflate(getLayoutInflater());
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}
