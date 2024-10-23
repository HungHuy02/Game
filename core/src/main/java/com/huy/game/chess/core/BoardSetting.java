package com.huy.game.chess.core;

public class BoardSetting {
    private boolean isRotate = false;
    private boolean isShowGuidePoint = true;
    private boolean isAutoRotate = false;
    private boolean isReverseOneSide = false;

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

    public boolean isAutoRotate() {
        return isAutoRotate;
    }

    public void setAutoRotate(boolean autoRotate) {
        isAutoRotate = autoRotate;
    }

    public boolean isReverseOneSide() {
        return isReverseOneSide;
    }

    public void setReverseOneSide(boolean reverseOneSide) {
        isReverseOneSide = reverseOneSide;
    }
}
