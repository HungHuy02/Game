package com.huy.game.android.globalstate;

public class UserState {

    private static UserState Instance;
    private String name;
    private String email;
    private String imageUrl;
    private int elo;

    private UserState() {

    }

    public static UserState getInstance() {
        if(Instance == null) {
            Instance = new UserState();
        }
        return Instance;
    }

    public void setData(String name, String email, String imageUrl, int elo) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.elo = elo;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static void setInstance(UserState instance) {
        Instance = instance;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }
}
