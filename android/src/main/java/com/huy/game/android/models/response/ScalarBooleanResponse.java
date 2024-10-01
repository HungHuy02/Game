package com.huy.game.android.models.response;

import com.google.gson.annotations.SerializedName;

public class ScalarBooleanResponse {

    @SerializedName("data")
    private boolean data;

    public ScalarBooleanResponse(boolean data) {
        this.data = data;
    }

    public boolean isData() {
        return data;
    }
}
