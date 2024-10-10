package com.huy.game.chess.manager;

public class GameSetting {

    private static GameSetting Instance;

    private GameSetting() {

    }

    public static GameSetting getInstance() {
        if(Instance == null) {
            Instance = new GameSetting();
        }
        return Instance;
    }

    private String language;
    private boolean mute;

    public void setSetting(String language, boolean mute) {
        this.language = language;
        this.mute = mute;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }
}
