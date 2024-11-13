package com.huy.game.android.network.retrofit.repository;

import com.huy.game.android.models.request.LoginRequest;
import com.huy.game.android.models.request.RefreshRequest;
import com.huy.game.android.models.request.RegisterRequest;
import com.huy.game.android.models.request.TokenRequest;
import com.huy.game.android.models.response.LoginResponse;
import com.huy.game.android.models.response.RefreshResponse;
import com.huy.game.android.models.response.RegisterResponse;
import com.huy.game.android.models.response.ScalarBooleanResponse;
import com.huy.game.android.network.retrofit.RetrofitClient;
import com.huy.game.android.network.retrofit.api.AuthApi;

import java.io.IOException;

import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final AuthApi authApi = RetrofitClient.getInstance().getClient().create(AuthApi.class);

    public void checkExistingUser(String email, Callback<ScalarBooleanResponse> callback) {
        authApi.checkExistingUser(email).enqueue(callback);
    }

    public void register(RegisterRequest request, Callback<RegisterResponse> callback) {
        authApi.register(request).enqueue(callback);
    }

    public void login(LoginRequest request, Callback<LoginResponse> callback) {
        authApi.login(request).enqueue(callback);
    }

    public Response<RefreshResponse> refreshToken(RefreshRequest request) {
        try {
            return authApi.refreshToken(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loginGoogle(TokenRequest request, Callback<LoginResponse> callback) {
        authApi.loginGoogle(request).enqueue(callback);
    }
}
