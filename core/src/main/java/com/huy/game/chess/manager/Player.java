package com.huy.game.chess.manager;

public class Player {

    private static Player Instance;
    private String name;
    private boolean isWhite;

    private Player() {

    }

    public static Player getInstance() {
        if(Instance == null) {
            Instance = new Player();
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
}
