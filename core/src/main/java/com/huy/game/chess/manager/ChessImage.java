package com.huy.game.chess.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class ChessImage {

    private final Texture chessBoard;
    private final Texture position;
    private final Texture rotate_position;

    private final float spotSize;
    private final float pieceSize;
    private final float scaledPieceSize;
    private final float scaledBoardWidth;
    private final float scaledBoardHeight;
    private final float circleRadius;
    private final float capturedPieceSize;

    private final ChessGameAssesManager manager;

    public ChessImage(ChessGameAssesManager manager) {
        this.manager = manager;
        chessBoard = manager.getChessBoard();
        position = manager.getPositionNotation();
        rotate_position = manager.getRotatePositionNotion();

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
        Texture bBishop = manager.getBlackBishop();
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

    public Texture getPosition() {
        return position;
    }

    public Texture getRotate_position() {
        return rotate_position;
    }

    public Texture getwBishop() {
        return manager.getWhiteBishop();
    }

    public Texture getwKing() {
        return manager.getWhiteKing();
    }

    public Texture getwKnight() {
        return manager.getWhiteKnight();
    }

    public Texture getwPawn() {
        return manager.getWhitePawn();
    }

    public Texture getwQueen() {
        return manager.getWhiteQueen();
    }

    public Texture getwRock() {
        return manager.getWhiteRook();
    }

    public Texture getbBishop() {
        return manager.getBlackBishop();
    }

    public Texture getbKing() {
        return manager.getBlackKing();
    }

    public Texture getbKnight() {
        return manager.getBlackKnight();
    }

    public Texture getbPawn() {
        return manager.getBlackPawn();
    }

    public Texture getbQueen() {
        return manager.getBlackQueen();
    }

    public Texture getbRook() {
        return manager.getBlackRook();
    }

    public Image getBackArrow() {
        return new Image(manager.getBackArrow());
    }

    public Image getOptions() {
        return new Image(manager.getOptionsButton());
    }

    public Image getPlus() {
        return new Image(manager.getNewButton());
    }

    public Image getBack() {
        return new Image(manager.getBackButton());
    }

    public Image getForwards() {
        return new Image(manager.getForwardsButton());
    }

    public Image getAppbarBackground() {
        return new Image(manager.getAppbarBackground());
    }

    public Image getOverlay() {
        return new Image(manager.getOverlay());
    }

    public Image getCancer() { return new Image(manager.getClose());}

    public Texture getCheck() {
        return manager.getCheckButton();
    }

    public Texture getTimer() {
        return manager.getTimer();
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
        position.dispose();
        rotate_position.dispose();
    }
}
