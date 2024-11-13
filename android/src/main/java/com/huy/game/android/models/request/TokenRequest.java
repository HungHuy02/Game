package com.huy.game.android.models.request;

import com.google.gson.annotations.SerializedName;

public class TokenRequest {

    @SerializedName("token")
    private String token;

    public TokenRequest(String token) {
        this.token = token;
    }
}
