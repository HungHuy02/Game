package com.huy.game.chess.manager;

public class OpponentPlayer {

    private static OpponentPlayer Instance;
    private String name;
    private boolean isWhite;
    private String imageUrl;
    private boolean isGuest;

    private OpponentPlayer() {

    }

    public static OpponentPlayer getInstance() {
        if(Instance == null) {
            Instance = new OpponentPlayer();
        }
        return Instance;
    }

    public void setData(String name, boolean isWhite) {
        this.name = name;
        this.isWhite = isWhite;
    }

    public void setData(String name, boolean isWhite, String imageUrl) {
        this.name = name;
        this.isWhite = isWhite;
        this.imageUrl = imageUrl;
    }

    public void setData(String name) {
        this.name = name;
        isGuest = true;
    }

    public String getName() {
        return name;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isGuest() {
        return isGuest;
    }
}
