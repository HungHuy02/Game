package com.huy.game.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.huy.game.android.base.BaseActivity;
import com.huy.game.android.models.request.RegisterRequest;
import com.huy.game.android.models.response.RegisterResponse;
import com.huy.game.android.viewmodel.apiservice.AuthServiceViewModel;
import com.huy.game.databinding.ActivityRegisterBinding;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    private ActivityRegisterBinding binding;
    private AuthServiceViewModel authServiceViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupViewModel();
        backButton();
        registerButton();
    }

    private void setupViewModel() {
        authServiceViewModel = new ViewModelProvider(this).get(AuthServiceViewModel.class);
    }

    private void backButton() {
        binding.backBtn.setOnClickListener((v) -> finish());
    }

    private void registerButton() {
        binding.registerBtn.setOnClickListener((v) -> {
            RegisterRequest request = new RegisterRequest(
                Objects.requireNonNull(binding.nameTf.getText()).toString(),
                Objects.requireNonNull(binding.emailTf.getText()).toString(),
                Objects.requireNonNull(binding.passwordTf.getText()).toString()
            );
            register(request);
        });
    }

    private void register(RegisterRequest request) {
        authServiceViewModel.register(request, new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                if(response.isSuccessful()) {
                    Toast.makeText( RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginWayActivity.class);
                    startActivity(intent);
                }else if(response.code() == 400) {
                    if (response.body() != null && response.body().getMessage() != null) {
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại. Kiểm tra lại thông tin.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RegisterActivity.this, "Lỗi không xác định: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable throwable) {
                Log.e("error", throwable.toString());
            }
        });
    }
}
