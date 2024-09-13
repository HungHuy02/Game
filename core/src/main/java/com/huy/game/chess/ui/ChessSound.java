package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class ChessSound {
    private Sound moveSound;
    private Sound illegalSound;
    private Sound castleSound;
    private Sound captureSound;
    private Sound checkSound;
    private Sound promoteSound;
    private Sound gameEndSound;

    public ChessSound() {
        moveSound = Gdx.audio.newSound(Gdx.files.internal("chess/sounds/move-self.mp3"));
        illegalSound = Gdx.audio.newSound(Gdx.files.internal("chess/sounds/illegal.mp3"));
        castleSound = Gdx.audio.newSound(Gdx.files.internal("chess/sounds/castle.mp3"));
        captureSound = Gdx.audio.newSound(Gdx.files.internal("chess/sounds/capture.mp3"));
        checkSound = Gdx.audio.newSound(Gdx.files.internal("chess/sounds/move-check.mp3"));
        promoteSound = Gdx.audio.newSound(Gdx.files.internal("chess/sounds/promote.mp3"));
        gameEndSound = Gdx.audio.newSound(Gdx.files.internal("chess/sounds/game-end.mp3"));
    }

    public void playMoveSound() {
        moveSound.play();
    }

    public void playIllegalSound() {
        illegalSound.play();
    }

    public void playCastleSound() {
        castleSound.play();
    }

    public void playCaptureSound() {
        captureSound.play();
    }

    public void playCheckSound() {
        checkSound.play();
    }

    public void playPromoteSound() {
        promoteSound.play();
    }

    public void playGameEndSound() {
        gameEndSound.play();
    }

    public void dispose() {
        moveSound.dispose();
        illegalSound.dispose();
        castleSound.dispose();
        captureSound.dispose();
        checkSound.dispose();
        promoteSound.dispose();
        gameEndSound.dispose();
    }
}
