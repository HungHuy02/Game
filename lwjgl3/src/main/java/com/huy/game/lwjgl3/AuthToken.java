package com.huy.game.lwjgl3;

public class AuthToken implements com.huy.game.shared.network.AuthToken {


    @Override
    public String getNewAccessToken() {
        return "";
    }

    @Override
    public String getCurrentAccessToken() {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NSwibmFtZSI6IkjDoCIsImlhdCI6MTczMDgxOTc3MCwiZXhwIjoxNzMwODIxNTcwfQ.LSwl7A2bfmP1DhRe1mrfsBisM_OYVEa4-OQTeAEO9t4";
    }
}
