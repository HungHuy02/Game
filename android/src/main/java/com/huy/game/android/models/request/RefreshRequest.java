package com.huy.game.android.models.request;

import com.google.gson.annotations.SerializedName;

public class RefreshRequest {

    @SerializedName("refreshToken")
    private String refreshToken;

    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
