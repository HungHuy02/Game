package com.huy.game.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

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
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    if(response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();
                        StorageUtils storageUtils = StorageUtils.getInstance(LoginActivity.this);
                        assert loginResponse != null;
                        storageUtils.setStringValue(Constants.DATASTORE_ACCESS_TOKEN, loginResponse.getAccessToken());
                        storageUtils.setStringValue(Constants.DATASTORE_REFRESH_TOKEN, loginResponse.getRefreshToken());
                        User user = loginResponse.getUser();
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
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable throwable) {
                    Log.e("error", throwable.toString());
                }
            });
        });
    }
}
