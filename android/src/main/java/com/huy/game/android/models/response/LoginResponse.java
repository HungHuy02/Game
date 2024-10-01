package com.huy.game.android.models.response;

import com.google.gson.annotations.SerializedName;
import com.huy.game.android.models.User;

public class LoginResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("userData")
    private User user;

    public LoginResponse(boolean success, String message,User user) {
        this.success = success;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
