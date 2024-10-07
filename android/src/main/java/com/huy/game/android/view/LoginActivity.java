package com.huy.game.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.huy.game.android.base.BaseActivity;
import com.huy.game.android.globalstate.UserState;
import com.huy.game.android.models.User;
import com.huy.game.android.models.request.LoginRequest;
import com.huy.game.android.models.response.LoginResponse;
import com.huy.game.android.utils.Constants;
import com.huy.game.android.utils.StorageUtils;
import com.huy.game.android.viewmodel.apiservice.AuthServiceViewModel;
import com.huy.game.databinding.ActivityLoginBinding;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private AuthServiceViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        backButton();
        loginButton();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AuthServiceViewModel.class);
    }

    private void backButton() {
        binding.backBtn.setOnClickListener((v) -> finish());
    }

    private void loginButton() {
        binding.loginBtn.setOnClickListener((v) -> {
            LoginRequest request = new LoginRequest(
                Objects.requireNonNull(binding.emailTf.getText()).toString(),
                Objects.requireNonNull(binding.passwordTf.getText()).toString()
            );
            login(request);
        });
    }

    private void login(LoginRequest request) {
        viewModel.login(request, new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    StorageUtils storageUtils = StorageUtils.getInstance(LoginActivity.this);
                    assert loginResponse != null;
                    storageUtils.setStringValue(Constants.DATASTORE_ACCESS_TOKEN, loginResponse.getAccessToken());
                    storageUtils.setStringValue(Constants.DATASTORE_REFRESH_TOKEN, loginResponse.getRefreshToken());
                    User user = loginResponse.getUser();
                    UserState.getInstance().setData(user.getName(), user.getEmail(), user.getImageUrl());
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else if(response.code() == 400) {
                    if (response.body() != null && response.body().getMessage() != null) {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Kiểm tra lại thông tin.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Lỗi không xác định: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable throwable) {
                Log.e("error", throwable.toString());
            }
        });
    }
}
