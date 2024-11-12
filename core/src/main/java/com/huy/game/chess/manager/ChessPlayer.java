package com.huy.game.chess.manager;

import com.huy.game.chess.enums.PieceType;

import java.util.HashMap;
import java.util.Map;

public class ChessPlayer {
    private boolean isWhite;
    private Map<String, Integer> capturedPieceMap;
    private Map<PieceType, Integer> pieceNumberMap;
    private int value = 0;
    private int timeRemain;

    public ChessPlayer(boolean isWhite, int timeRemain) {
        this.isWhite = isWhite;
        this.timeRemain = timeRemain;
        setupCapturedPieceMap();
        setupPieceMap();
    }

    private void setupCapturedPieceMap() {
        capturedPieceMap = new HashMap<>();
        capturedPieceMap.put("p", 0);
        capturedPieceMap.put("k", 0);
        capturedPieceMap.put("b", 0);
        capturedPieceMap.put("r", 0);
        capturedPieceMap.put("q", 0);
        capturedPieceMap.put("value", 0);
    }

    private void setupPieceMap() {
        pieceNumberMap = new HashMap<>();
        pieceNumberMap.put(PieceType.PAWN, 8);
        pieceNumberMap.put(PieceType.QUEEN, 1);
        pieceNumberMap.put(PieceType.KNIGHT, 2);
        pieceNumberMap.put(PieceType.BISHOP, 2);
        pieceNumberMap.put(PieceType.ROOK, 2);
    }

    public void reset() {
        value = 0;
        capturedPieceMap.put("p", 0);
        capturedPieceMap.put("k", 0);
        capturedPieceMap.put("b", 0);
        capturedPieceMap.put("r", 0);
        capturedPieceMap.put("q", 0);
        capturedPieceMap.put("value", 0);
        pieceNumberMap.put(PieceType.PAWN, 8);
        pieceNumberMap.put(PieceType.QUEEN, 1);
        pieceNumberMap.put(PieceType.KNIGHT, 2);
        pieceNumberMap.put(PieceType.BISHOP, 2);
        pieceNumberMap.put(PieceType.ROOK, 2);
    }

    public Map<String, Integer> getCapturedPieceMap() {
        return capturedPieceMap;
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

    public void addCapturedPiece(PieceType type) {
        switch (type) {
            case PAWN -> {
                capturedPieceMap.put("p", capturedPieceMap.get("p") + 1);
                value += 1;
            }
            case BISHOP -> {
                capturedPieceMap.put("b", capturedPieceMap.get("b") + 1);
                value += 3;
            }
            case KNIGHT -> {
                capturedPieceMap.put("k", capturedPieceMap.get("k") + 1);
                value += 3;
            }
            case QUEEN -> {
                capturedPieceMap.put("q", capturedPieceMap.get("q") + 1);
                value += 9;
            }
            case ROOK -> {
                capturedPieceMap.put("r", capturedPieceMap.get("r") + 1);
                value += 5;
            }
        }
    }

    public void addCapturedPiece(int value) {
        capturedPieceMap.put("value", value);
    }

    public void decreasePieceNumber(PieceType type) {
        pieceNumberMap.put(type, pieceNumberMap.get(type) - 1);
    }

    public void increasePieceNumber(PieceType type) {
        pieceNumberMap.put(type, pieceNumberMap.get(type) + 1);
    }

    public void setPieceNumber(PieceType type, int number) {
        pieceNumberMap.put(type, number);
    }

    public int getPieceNumber(PieceType type) {
        return pieceNumberMap.get(type);
    }

    public void setTimeRemain(int timeRemain) {
        this.timeRemain = timeRemain;
    }

    public int getTimeRemain() {
        return timeRemain;
    }
}
