package com.huy.game.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.huy.game.android.globalstate.UserState;
import com.huy.game.android.models.User;
import com.huy.game.android.models.request.LoginRequest;
import com.huy.game.android.models.response.LoginResponse;
import com.huy.game.android.viewmodel.apiservice.AuthServiceViewModel;
import com.huy.game.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthServiceViewModel viewModel = new ViewModelProvider(this).get(AuthServiceViewModel.class);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener((v) -> finish());

        binding.loginBtn.setOnClickListener((v) -> {
            LoginRequest request = new LoginRequest(
                binding.emailTf.getText().toString(),
                binding.passwordTf.getText().toString()
            );

            viewModel.login(request, new Callback<>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if(response.isSuccessful()) {
                        User user = response.body().getUser();
                        UserState.getInstance().setData(user.getName(), user.getEmail());
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
                public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                    Log.e("error", throwable.toString());
                }
            });
        });
    }
}
