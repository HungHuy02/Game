package com.huy.game.chess.manager;

import com.huy.game.chess.enums.PieceType;

import java.util.HashMap;
import java.util.Map;

public class ChessPlayer {
    private boolean isWhite;
    private Map<String, Integer> map;
    private int value = 0;
    private int turn = 1;
    private int timeRemain;

    public ChessPlayer(boolean isWhite, int timeRemain) {
        this.isWhite = isWhite;
        this.timeRemain = timeRemain;
        setupMap();
    }

    private void setupMap() {
        map = new HashMap<>();
        map.put("p", 0);
        map.put("k", 0);
        map.put("b", 0);
        map.put("r", 0);
        map.put("q", 0);
        map.put("value", 0);
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public int getValue() {
        return value;
    }

    public void increaseTurn() {
        turn++;
    }

    public int getTurn() {
        return turn;
    }

    public void putValue(PieceType type) {
        switch (type) {
            case PAWN -> {
                map.put("p", map.get("p") + 1);
                value += 1;
            }
            case BISHOP -> {
                map.put("b", map.get("b") + 1);
                value += 3;
            }
            case KNIGHT -> {
                map.put("k", map.get("k") + 1);
                value += 3;
            }
            case QUEEN -> {
                map.put("q", map.get("q") + 1);
                value += 9;
            }
            case ROOK -> {
                map.put("r", map.get("r") + 1);
                value += 5;
            }
        }
    }

    public void putValue(int value) {
        map.put("value", value);
    }

    public void setTimeRemain(int timeRemain) {
        this.timeRemain = timeRemain;
    }

    public int getTimeRemain() {
        return timeRemain;
    }
}
