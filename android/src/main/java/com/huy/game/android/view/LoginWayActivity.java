package com.huy.game.android.view;

import android.content.Intent;
import android.os.Bundle;

import com.huy.game.android.base.BaseActivity;
import com.huy.game.android.globalstate.UserState;
import com.huy.game.databinding.ActivityLoginWayBinding;

public class LoginWayActivity extends BaseActivity {

    private ActivityLoginWayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginWayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loginButton();
        emailButton();
        guestButton();
    }

    private void loginButton() {
        binding.loginBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void emailButton() {
        binding.emailBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void guestButton() {
        binding.guestBtn.setOnClickListener((v) -> {
            UserState.getInstance().setGuestAccount();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}
