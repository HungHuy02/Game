package com.huy.game.chess.manager;

public class OpponentPlayer {

    private static OpponentPlayer Instance;
    private String name;
    private boolean isWhite;
    private String imageUrl;

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

    public String getName() {
        return name;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
