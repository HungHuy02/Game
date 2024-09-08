package com.huy.game.chess;

import java.util.HashMap;
import java.util.Map;

public class ChessPlayer {
    private boolean isWhite;
    private CapturedPiecesActor capturedPiecesActor;
    private Map<String, Integer> map;

    public ChessPlayer(ChessImage chessImage ,boolean isWhite) {
        this.isWhite = isWhite;
        map = new HashMap<>();
        map.put("p", 1);
        map.put("k", 0);
        map.put("b", 0);
        map.put("r", 0);
        map.put("q", 0);
        capturedPiecesActor = new CapturedPiecesActor(map, chessImage, isWhite);
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean isWhite) {
        isWhite = isWhite;
    }
}
