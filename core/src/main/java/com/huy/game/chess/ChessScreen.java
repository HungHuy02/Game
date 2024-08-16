package com.huy.game.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class ChessScreen implements Screen {

    private SpriteBatch batch;
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

    private float centerX;
    private float centerY;
    private float scaledBoardWidth;
    private float scaledBoardHeight;
    private float spotSide;
    private Board board;
    private float pieceSide;

    @Override
    public void show() {
        batch = new SpriteBatch();
        board = new Board();
        chessBoard = new Texture("chess/images/chess_board.png");

        float boardWidth = chessBoard.getWidth();
        float boardHeight = chessBoard.getHeight();
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        float widthRatio = (float) screenWidth / boardWidth;
        float heightRatio = (float) screenHeight / boardHeight;
        float scale = Math.min(widthRatio, heightRatio);

        scaledBoardWidth = boardWidth * scale;
        scaledBoardHeight = boardHeight * scale;
        centerX = (screenWidth - scaledBoardWidth) / 2;
        centerY = (screenHeight - scaledBoardHeight) / 2;

        spotSide = scaledBoardWidth / 8;

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
        pieceSide = bRock.getHeight();
        board.resetBoard(wRock, wKnight,wBishop, wQueen, wKing, wPawn, bRock, bKnight, bBishop, bQueen, bKing, bPawn);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        batch.begin();
        batch.draw(chessBoard, centerX, centerY, scaledBoardWidth, scaledBoardHeight);
        board.renderBoard(batch, spotSide, pieceSide, centerX, centerY);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
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
    }
}
