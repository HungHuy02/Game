package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


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
    private float scaledPieceSize;
    private float scaledBoardWidth;
    private float scaledBoardHeight;
    private float circleRadius;
    private float capturedPieceSize;

    private Texture backArrow;
    private Texture options;
    private Texture plus;
    private Texture back;
    private Texture forwards;
    private Texture appbarBackground;
    private Texture timer;

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
        options = new Texture("chess/images/icons8-bulleted-list-100.png");
        plus = new Texture("chess/images/icons8-plus-100.png");
        back = new Texture("chess/images/icons8-back-100.png");
        forwards = new Texture("chess/images/icons8-forward-100.png");
        appbarBackground = new Texture("chess/images/appbar_background.png");
        timer = new Texture("chess/images/timer_20dp.png");

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

        float padding = spotSize / 10f;
        float pieceScale = (spotSize - (2 * padding)) / pieceSize;
        scaledPieceSize = pieceScale * pieceSize;

        circleRadius = spotSize / 5;
        capturedPieceSize = scaledPieceSize / 2;
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

    public Image getOptions() {
        return new Image(options);
    }

    public Image getPlus() {
        return new Image(plus);
    }

    public Image getBack() {
        return new Image(back);
    }

    public Image getForwards() {
        return new Image(forwards);
    }

    public Image getAppbarBackground() {
        return new Image(appbarBackground);
    }

    public Texture getTimer() {
        return timer;
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

    public float getCapturedPieceSize() {
        return capturedPieceSize;
    }

    public float getScaledPieceSize() {
        return scaledPieceSize;
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
        options.dispose();
        plus.dispose();
        back.dispose();
        forwards.dispose();
        appbarBackground.dispose();
    }
}