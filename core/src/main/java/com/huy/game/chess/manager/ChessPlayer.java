package com.huy.game.chess.manager;

import com.huy.game.chess.core.Bishop;
import com.huy.game.chess.core.Pawn;
import com.huy.game.chess.core.Piece;
import com.huy.game.chess.core.Queen;
import com.huy.game.chess.core.Rook;

import java.util.HashMap;
import java.util.Map;

public class ChessPlayer {
    private boolean isWhite;
    private Map<String, Integer> map;
    private int value = 0;
    private int turn = 1;
    private int timeRemain = 500;

    public ChessPlayer(boolean isWhite) {
        this.isWhite = isWhite;
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
        isWhite = isWhite;
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

    public void putValue(Piece piece) {
        if(piece instanceof Pawn) {
            map.put("p", map.get("p") + 1);
            value += 1;
        }else if(piece instanceof Bishop) {
            map.put("b", map.get("b") + 1);
            value += 3;
        }else if(piece instanceof Rook) {
            map.put("r", map.get("r") + 1);
            value += 5;
        }else if(piece instanceof Queen) {
            map.put("q", map.get("q") + 1);
            value += 9;
        }else {
            map.put("k", map.get("k") + 1);
            value += 3;
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
