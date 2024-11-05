package com.huy.game.android.globalstate;

import com.huy.game.chess.manager.GameSetting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserState {

    private static UserState Instance;
    private String name = GameSetting.getInstance().getLanguage().equals("vi-VN") ? "Bạn" : "You";
    private String email;
    private String imageUrl;
    private int elo;
    private boolean isGuest = false;

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

    public void setGuestAccount() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedTime = now.format(formatter);
        isGuest = true;
        name = "guest" + formattedTime;
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

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    public boolean isGuest() {
        return isGuest;
    }
}
