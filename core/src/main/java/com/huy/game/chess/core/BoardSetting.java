package com.huy.game.chess.core;

public class BoardSetting {
    private boolean isRotate = false;
    private boolean isShowGuidePoint = true;

    public BoardSetting() {

    }

    public boolean isRotate() {
        return isRotate;
    }

    public void setRotate(boolean rotate) {
        isRotate = rotate;
    }

    public boolean isShowGuidePoint() {
        return isShowGuidePoint;
    }

    public void setShowGuidePoint(boolean showGuidePoint) {
        isShowGuidePoint = showGuidePoint;
    }
}
