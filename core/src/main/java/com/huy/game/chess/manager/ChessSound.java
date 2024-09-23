package com.huy.game.chess.manager;

import com.badlogic.gdx.audio.Sound;

public class ChessSound {
    private final Sound moveSound;
    private final Sound captureSound;
    private final Sound checkSound;
    private final ChessGameAssesManager manager;

    public ChessSound(ChessGameAssesManager manager) {
        this.manager = manager;
        moveSound = manager.getMoveSound();
        captureSound = manager.getCaptureSound();
        checkSound = manager.getMoveCheckSound();
    }

    public void playMoveSound() {
        moveSound.play();
    }

    public void playIllegalSound() {
        manager.getIllegalSound().play();
    }

    public void playCastleSound() {
        manager.getCastleSound().play();
    }

    public void playCaptureSound() {
        captureSound.play();
    }

    public void playCheckSound() {
        checkSound.play();
    }

    public void playPromoteSound() {
        manager.getPromoteSound().play();
    }

    public void playGameEndSound() {
        manager.getGameEndSound().play();
    }

    public void dispose() {
        moveSound.dispose();
        captureSound.dispose();
        checkSound.dispose();
    }
}
