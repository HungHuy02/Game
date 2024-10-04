package com.huy.game.android.models.response;

import com.google.gson.annotations.SerializedName;

public class RefreshResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("accessToken")
    private String accessToken;

    public RefreshResponse(boolean success, String accessToken) {
        this.success = success;
        this.accessToken = accessToken;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
