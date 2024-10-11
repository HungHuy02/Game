package com.huy.game.android.network.retrofit.api;

import com.huy.game.android.models.User;
import com.huy.game.android.models.response.DeleteImageResponse;
import com.huy.game.android.models.response.ScalarBooleanResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {

    @GET("/user/get-current")
    Call<User> getCurrentUser();

    @POST("/user/update-user")
    Call<ScalarBooleanResponse> updateUser(@Body User user);

    @POST("/user/logout")
    Call<ScalarBooleanResponse> logout();

    @POST("/user/delete-user")
    Call<ScalarBooleanResponse> delete();

    @POST("/user/delete-image")
    Call<DeleteImageResponse> deleteImage();
}
