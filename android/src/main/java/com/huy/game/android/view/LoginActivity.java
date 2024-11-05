package com.huy.game.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.huy.game.R;
import com.huy.game.android.base.BaseActivity;
import com.huy.game.android.globalstate.UserState;
import com.huy.game.android.models.User;
import com.huy.game.android.models.request.LoginRequest;
import com.huy.game.android.models.response.LoginResponse;
import com.huy.game.android.utils.Constants;
import com.huy.game.android.utils.EmailValidation;
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
            String email = Objects.requireNonNull(binding.emailTf.getText()).toString();
            String password = Objects.requireNonNull(binding.passwordTf.getText()).toString();
            if (email.isEmpty() && password.isEmpty()) {
                showError(binding.emailTil, R.string.email_can_not_blank_text);
                showError(binding.passwordTil, R.string.password_can_not_blank);
                return;
            }else {
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

            LoginRequest request = new LoginRequest(
                email,
                password
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
                    UserState.getInstance().setGuest(false);
                    UserState.getInstance().setData(user.getName(), user.getEmail(), user.getImageUrl(), user.getElo());
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    toMainActivity();
                }else if(response.code() == 401) {
                    if (response.body() != null && response.body().getMessage() != null) {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Kiểm tra lại thông tin.", Toast.LENGTH_SHORT).show();
                        showError(binding.emailTil, R.string.email_or_pass_invalid_text);
                        showError(binding.passwordTil, R.string.email_or_pass_invalid_text);
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

    private void toMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showError(TextInputLayout textInputLayout, int id) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(getString(id));
    }
}


