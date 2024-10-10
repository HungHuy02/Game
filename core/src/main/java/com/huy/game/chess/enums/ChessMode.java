package com.huy.game.chess.enums;

public enum ChessMode {
    ONLINE,
    AI,
    TWO_PERSONS;

    private Difficulty difficulty;

    public void setDifficulty(Difficulty difficulty) {
        if (this == AI) {
            this.difficulty = difficulty;
        } else {
            throw new UnsupportedOperationException("Difficulty can only be set for AI mode.");
        }
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
