package com.huy.game.android.network.retrofit.repository;

import com.huy.game.android.models.User;
import com.huy.game.android.models.response.DeleteImageResponse;
import com.huy.game.android.models.response.ScalarBooleanResponse;
import com.huy.game.android.network.retrofit.RetrofitClient;
import com.huy.game.android.network.retrofit.api.UserApi;

import retrofit2.Call;
import retrofit2.Callback;

public class UserRepository {

    private final UserApi userApi = RetrofitClient.getInstance().getClient().create(UserApi.class);

    public void getCurrentUser(Callback<User> callback) {
        userApi.getCurrentUser().enqueue(callback);
    }

    public void updateUser(User user, Callback<ScalarBooleanResponse> callback) {
        userApi.updateUser(user).enqueue(callback);
    }

    public void logout(Callback<ScalarBooleanResponse> callback) {
        userApi.logout().enqueue(callback);
    }

    public void deleteUser(Callback<ScalarBooleanResponse> callback) {
        userApi.delete().enqueue(callback);
    }

    public void deleteImage(Callback<DeleteImageResponse> callback) {
        userApi.deleteImage().enqueue(callback);
    }
}
