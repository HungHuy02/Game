package com.huy.game.android.globalstate;

public class UserState {

    private static UserState Instance;
    private String name;
    private String email;

    private UserState() {

    }

    public static UserState getInstance() {
        if(Instance == null) {
            Instance = new UserState();
        }
        return Instance;
    }

    public void setData(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
