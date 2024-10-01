package com.huy.game.android.network.retrofit.repository;

import com.huy.game.android.models.request.LoginRequest;
import com.huy.game.android.models.request.RegisterRequest;
import com.huy.game.android.models.response.LoginResponse;
import com.huy.game.android.models.response.RegisterResponse;
import com.huy.game.android.models.response.ScalarBooleanResponse;
import com.huy.game.android.network.retrofit.RetrofitClient;
import com.huy.game.android.network.retrofit.api.AuthApi;

import retrofit2.Callback;

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
}
