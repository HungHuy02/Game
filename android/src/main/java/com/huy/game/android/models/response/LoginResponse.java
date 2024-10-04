package com.huy.game.android.models.response;

import com.google.gson.annotations.SerializedName;
import com.huy.game.android.models.User;

public class LoginResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("refreshToken")
    private String refreshToken;
    @SerializedName("userData")
    private User user;

    public LoginResponse(boolean success, String accessToken, String refreshToken, User user) {
        this.success = success;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public User getUser() {
        return user;
    }
}
