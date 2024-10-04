package com.huy.game.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.huy.game.android.base.BaseActivity;
import com.huy.game.android.models.request.RegisterRequest;
import com.huy.game.android.models.response.RegisterResponse;
import com.huy.game.android.viewmodel.apiservice.AuthServiceViewModel;
import com.huy.game.databinding.ActivityRegisterBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthServiceViewModel authServiceViewModel = new ViewModelProvider(this).get(AuthServiceViewModel.class);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener((v) -> finish());

        binding.registerBtn.setOnClickListener((v) -> {
            RegisterRequest request = new RegisterRequest(
                binding.nameTf.getText().toString(),
                binding.emailTf.getText().toString(),
                binding.passwordTf.getText().toString()
            );

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
        });
    }
}
