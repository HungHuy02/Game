package com.huy.game.chess.core;

import com.badlogic.gdx.graphics.Color;
import com.huy.game.chess.ui.Colors;

import java.util.Objects;

public class Spot {
    private Piece piece;
    private int x;
    private int y;
    private boolean showColor = false;
    private boolean isIdentificationColor = false;
    private Color spotColor = Colors.MOVE_COLOR;
    private boolean canBeCaptured = false;
    private boolean showMovePoint = false;

    public Spot(Spot spot) {
        Piece p = spot.getPiece();
        if(p != null) {
            if(p instanceof Pawn) {
                piece = new Pawn((Pawn) p);
            }else if(p instanceof Bishop) {
                piece = new Bishop(p.isWhite());
            }else if(p instanceof Rook) {
                piece = new Rook((Rook) p);
            }else if(p instanceof Queen) {
                piece = new Queen(p.isWhite());
            }else if(p instanceof Knight) {
                piece = new Knight(p.isWhite());
            }else if(p instanceof King){
                piece = new King((King) p);
            }
        }else {
            piece = null;
        }
        x = spot.getX();
        y = spot.getY();
    }

    public Spot(Piece piece, int x, int y) {
        this.piece = piece;
        this.x = x;
        this.y = y;
    }

    public Spot(Piece piece, int x, int y, boolean showColor) {
        this.piece = piece;
        this.x = x;
        this.y = y;
        this.showColor = showColor;
    }

    public Spot(Piece piece, int x, int y, boolean showColor, boolean isIdentificationColor) {
        this.piece = piece;
        this.x = x;
        this.y = y;
        this.showColor = showColor;
        this.isIdentificationColor = isIdentificationColor;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isShowColor() {
        return showColor;
    }

    public void setShowColor(boolean showColor) {
        this.showColor = showColor;
    }

    public Color getSpotColor() {
        return spotColor;
    }

    public void setSpotColor(Color spotColor) {
        this.spotColor = spotColor;
    }

    public boolean isIdentificationColor() {
        return isIdentificationColor;
    }

    public void setIdentificationColor(boolean identificationColor) {
        isIdentificationColor = identificationColor;
    }

    public boolean isCanBeCaptured() {
        return canBeCaptured;
    }

    public void setCanBeCaptured(boolean canBeCaptured) {
        this.canBeCaptured = canBeCaptured;
    }

    public boolean isShowMovePoint() {
        return showMovePoint;
    }

    public void setShowMovePoint(boolean showMovePoint) {
        this.showMovePoint = showMovePoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spot spot = (Spot) o;
        return x == spot.x && y == spot.y && Objects.equals(piece, spot.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, x, y);
    }
}
