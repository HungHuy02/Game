package com.huy.game.chess.manager;

public class Player {

    private static Player Instance;
    private String name;
    private boolean isWhite;
    private String imageUrl;
    private int elo;
    private boolean isGuest;

    private Player() {

    }

    public static Player getInstance() {
        if(Instance == null) {
            Instance = new Player();
        }
        return Instance;
    }

    public void setData(String name, boolean isWhite, String imageUrl, int elo) {
        this.name = name;
        this.isWhite = isWhite;
        this.imageUrl = imageUrl;
        this.elo = elo;
    }

    public void setData(String name, String imageUrl, int elo) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.elo = elo;
    }

    public void setData(String name) {
        this.name = name;
        isGuest = true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWhite(boolean isWhite) {
        this.isWhite = isWhite;
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

    public int getElo() {
        return elo;
    }

    public boolean isGuest() {
        return isGuest;
    }
}
