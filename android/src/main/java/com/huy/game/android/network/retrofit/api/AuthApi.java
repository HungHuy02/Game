package com.huy.game.android.network.retrofit.api;

import com.huy.game.android.models.request.LoginRequest;
import com.huy.game.android.models.request.RefreshRequest;
import com.huy.game.android.models.request.RegisterRequest;
import com.huy.game.android.models.response.LoginResponse;
import com.huy.game.android.models.response.RefreshResponse;
import com.huy.game.android.models.response.RegisterResponse;
import com.huy.game.android.models.response.ScalarBooleanResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthApi {

    @GET("/auth/user/check-existing-user/{email}")
    Call<ScalarBooleanResponse> checkExistingUser(@Path("email") String email);

    @POST("/auth/user/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/auth/user/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("/auth/user/refresh-token")
    Call<RefreshResponse> refreshToken(@Body RefreshRequest request);
}
