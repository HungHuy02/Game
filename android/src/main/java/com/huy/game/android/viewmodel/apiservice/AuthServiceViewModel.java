package com.huy.game.android.viewmodel.apiservice;

import androidx.lifecycle.ViewModel;

import com.huy.game.android.models.request.LoginRequest;
import com.huy.game.android.models.request.RegisterRequest;
import com.huy.game.android.models.response.LoginResponse;
import com.huy.game.android.models.response.RefreshResponse;
import com.huy.game.android.models.response.RegisterResponse;
import com.huy.game.android.models.response.ScalarBooleanResponse;
import com.huy.game.android.network.retrofit.repository.AuthRepository;

import retrofit2.Callback;

public class AuthServiceViewModel extends ViewModel {

    private final AuthRepository repository = new AuthRepository();

    public void checkExistingUser(String email, Callback<ScalarBooleanResponse> callback) {
        repository.checkExistingUser(email, callback);
    }

    public void register(RegisterRequest request, Callback<RegisterResponse> callback) {
        repository.register(request, callback);
    }

    public void login(LoginRequest request, Callback<LoginResponse> callback) {
        repository.login(request, callback);
    }
}
