package com.huy.game.android.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("elo")
    private int elo;

    public User(String name, String email, String imageUrl, int elo) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.elo = elo;
    }

    public User() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }
}
