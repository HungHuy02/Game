package com.huy.game.android.viewmodel.apiservice;

import androidx.lifecycle.ViewModel;

import com.huy.game.android.models.User;
import com.huy.game.android.models.response.ScalarBooleanResponse;
import com.huy.game.android.network.retrofit.repository.UserRepository;

import retrofit2.Callback;

public class UserServiceViewModel extends ViewModel {

    private final UserRepository repository = new UserRepository();

    public void getCurrentUser(Callback<User> callback) {
        repository.getCurrentUser(callback);
    }

    public void updateUser(User user, Callback<ScalarBooleanResponse> callback) {
        repository.updateUser(user, callback);
    }

    public void logout(Callback<ScalarBooleanResponse> callback) {
        repository.logout(callback);
    }

    public void deleteUser(Callback<ScalarBooleanResponse> callback) {
        repository.deleteUser(callback);
    }
}
