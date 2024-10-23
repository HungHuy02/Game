package com.huy.game.chess.manager;

import com.badlogic.gdx.audio.Sound;

public class ChessSound {
    private final float volume = GameSetting.getInstance().isMute() ? 0f : 1f;
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
        moveSound.play(volume);
    }

    public void playIllegalSound() {
        manager.getIllegalSound().play(volume);
    }

    public void playCastleSound() {
        manager.getCastleSound().play(volume);
    }

    public void playCaptureSound() {
        captureSound.play(volume);
    }

    public void playCheckSound() {
        checkSound.play(volume);
    }

    public void playPromoteSound() {
        manager.getPromoteSound().play(volume);
    }

    public void playGameEndSound() {
        manager.getGameEndSound().play(volume);
    }

    public void dispose() {
        moveSound.dispose();
        captureSound.dispose();
        checkSound.dispose();
    }
}
