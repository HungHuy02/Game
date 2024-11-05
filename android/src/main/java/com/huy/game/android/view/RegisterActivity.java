package com.huy.game.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.huy.game.R;
import com.huy.game.android.base.BaseActivity;
import com.huy.game.android.models.request.RegisterRequest;
import com.huy.game.android.models.response.RegisterResponse;
import com.huy.game.android.utils.EmailValidation;
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
            String name = Objects.requireNonNull(binding.nameTf.getText()).toString();
            String email = Objects.requireNonNull(binding.emailTf.getText()).toString();
            String password = Objects.requireNonNull(binding.passwordTf.getText()).toString();

            if (name.isEmpty() && email.isEmpty() && password.isEmpty()) {
                showError(binding.nameTil, R.string.name_can_not_blank_text);
                showError(binding.emailTil, R.string.email_can_not_blank_text);
                showError(binding.passwordTil, R.string.password_can_not_blank);
                return;
            }else {
                if (name.isEmpty()) {
                    showError(binding.nameTil, R.string.name_can_not_blank_text);
                    return;
                }else {
                    binding.nameTil.setErrorEnabled(false);
                }

                if (email.isEmpty()) {
                    showError(binding.emailTil, R.string.email_can_not_blank_text);
                    return;
                }else {
                    binding.emailTil.setErrorEnabled(false);
                }

                if (password.isEmpty()) {
                    showError(binding.passwordTil, R.string.password_can_not_blank);
                    return;
                }else {
                    binding.passwordTil.setErrorEnabled(false);
                }
            }

            if (!EmailValidation.patternMatches(email)) {
                showError(binding.emailTil, R.string.email_not_in_correct_format_text);
                return;
            }else {
                binding.emailTil.setErrorEnabled(false);
            }

            if (password.length() <= 6) {
                showError(binding.passwordTil, R.string.pass_must_at_least_7_char_text);
                return;
            }else {
                binding.passwordTil.setErrorEnabled(false);
            }

            if (!binding.checkBox.isChecked()) {
                binding.checkBox.setErrorShown(true);
                return;
            }else {
                binding.checkBox.setErrorShown(false);
            }

            RegisterRequest request = new RegisterRequest(
                name,
                email,
                password
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
                    finish();
                }else if(response.code() == 400) {
                    showError(binding.emailTil, R.string.account_already_exists_text);
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

    private void showError(TextInputLayout textInputLayout, int id) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(getString(id));
    }
}
