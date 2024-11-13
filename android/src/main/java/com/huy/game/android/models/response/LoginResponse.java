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
    @SerializedName("elo")
    private int elo;

    public LoginResponse(boolean success, String message, String accessToken, String refreshToken, User user, int elo) {
        this.success = success;
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
        this.elo = elo;
    }

    public int getElo() {
        return elo;
    }

    public User getUser() {
        return user;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
