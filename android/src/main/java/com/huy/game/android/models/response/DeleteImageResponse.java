package com.huy.game.android.models.response;

import com.google.gson.annotations.SerializedName;

public class DeleteImageResponse {
    @SerializedName("id")
    private Long id;
    @SerializedName("success")
    private String success;

    public DeleteImageResponse(Long id, String success) {
        this.id = id;
        this.success = success;
    }

    public Long getId() {
        return id;
    }

    public String getSuccess() {
        return success;
    }
}
