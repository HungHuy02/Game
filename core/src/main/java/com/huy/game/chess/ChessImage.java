package com.huy.game.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;


public class ChessImage {

    private Texture chessBoard;
    private Texture wBishop;
    private Texture wKing;
    private Texture wKnight;
    private Texture wPawn;
    private Texture wQueen;
    private Texture wRock;

    private Texture bBishop;
    private Texture bKing;
    private Texture bKnight;
    private Texture bPawn;
    private Texture bQueen;
    private Texture bRock;

    private float spotSize;
    private float pieceSize;
    private float scaledBoardWidth;
    private float scaledBoardHeight;
    private float circleRadius;

    private Texture backArrow;
    private Texture appbarBackground;

    public ChessImage() {
        chessBoard = new Texture("chess/images/chess_board.png");
        wBishop = new Texture("chess/images/wbishop.png");
        wKing = new Texture("chess/images/wking.png");
        wKnight = new Texture("chess/images/wknight.png");
        wPawn = new Texture("chess/images/wpawn.png");
        wQueen = new Texture("chess/images/wqueen.png");
        wRock = new Texture("chess/images/wrook.png");

        bBishop = new Texture("chess/images/bbishop.png");
        bKing = new Texture("chess/images/bking.png");
        bKnight = new Texture("chess/images/bknight.png");
        bPawn = new Texture("chess/images/bpawn.png");
        bQueen = new Texture("chess/images/bqueen.png");
        bRock = new Texture("chess/images/brook.png");

        backArrow = new Texture("chess/images/icons8-left-100.png");
        appbarBackground = new Texture("chess/images/appbar_background.png");

        float boardWidth = chessBoard.getWidth();
        float boardHeight = chessBoard.getHeight();
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        float widthRatio = (float) screenWidth / boardWidth;
        float heightRatio = (float) screenHeight / boardHeight;
        float scale = Math.min(widthRatio, heightRatio);

        scaledBoardWidth = boardWidth * scale;
        scaledBoardHeight = boardHeight * scale;

        spotSize = scaledBoardWidth / 8;
        pieceSize = bBishop.getHeight();

        circleRadius = spotSize / 5;
    }

    public Texture getChessBoard() {
        return chessBoard;
    }

    public Texture getwBishop() {
        return wBishop;
    }

    public Texture getwKing() {
        return wKing;
    }

    public Texture getwKnight() {
        return wKnight;
    }

    public Texture getwPawn() {
        return wPawn;
    }

    public Texture getwQueen() {
        return wQueen;
    }

    public Texture getwRock() {
        return wRock;
    }

    public Texture getbBishop() {
        return bBishop;
    }

    public Texture getbKing() {
        return bKing;
    }

    public Texture getbKnight() {
        return bKnight;
    }

    public Texture getbPawn() {
        return bPawn;
    }

    public Texture getbQueen() {
        return bQueen;
    }

    public Texture getbRock() {
        return bRock;
    }

    public Image getBackArrow() {
        return new Image(backArrow);
    }

    public Image getAppbarBackground() {
        return new Image(appbarBackground);
    }

    public float getSpotSize() {
        return spotSize;
    }

    public float getPieceSize() {
        return pieceSize;
    }

    public float getScaledBoardWidth() {
        return scaledBoardWidth;
    }

    public float getScaledBoardHeight() {
        return scaledBoardHeight;
    }

    public float getCircleRadius() {
        return circleRadius;
    }


    public void dispose() {
        chessBoard.dispose();
        wBishop.dispose();
        wKing.dispose();
        wKnight.dispose();
        wPawn.dispose();
        wQueen.dispose();
        wRock.dispose();

        bBishop.dispose();
        bKing.dispose();
        bKnight.dispose();
        bPawn.dispose();
        bQueen.dispose();
        bRock.dispose();

        backArrow.dispose();
        appbarBackground.dispose();
    }
}
