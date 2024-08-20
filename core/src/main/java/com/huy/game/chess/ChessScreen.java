package com.huy.game.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class ChessScreen extends InputAdapter implements Screen {

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
    private Spot selectedSpot;
    private ChessPlayer player1;
    private ChessPlayer player2;
    private ChessPlayer currentPlayer;

    private Sound moveSound;

    @Override
    public void show() {
        player1 = new ChessPlayer(true);
        player2 = new ChessPlayer(false);
        currentPlayer = player1;
        moveSound = Gdx.audio.newSound(Gdx.files.internal("chess/sounds/move-self.mp3"));
        batch = new SpriteBatch();
        board = new Board();
        chessBoard = new Texture("chess/images/chess_board.png");
        selectedSpot = null;

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
        Gdx.input.setInputProcessor(this);
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int boardX = (int) Math.floor((screenX - centerX) / spotSide);
        int boardY = (int) Math.floor((Gdx.graphics.getHeight() - (screenY - centerY)) / spotSide);

        if (boardX >= 0 && boardX < 8 && boardY >= 0 && boardY < 8) {
            if (selectedSpot == null) {
                selectedSpot = board.getSpot(boardY, boardX);
                if(selectedSpot.getPiece() == null || selectedSpot.getPiece().isWhite() != currentPlayer.isWhile()) {
                    selectedSpot = null;
                }
            } else {
                Spot secondSpot = board.getSpot(boardY, boardX);
                boolean canMove = selectedSpot.getPiece().canMove(board, selectedSpot, secondSpot);
                if(canMove) {
                    board.setSpot(selectedSpot.getX(), selectedSpot.getY(), new Spot(null, selectedSpot.getX(), selectedSpot.getY()));
                    board.setSpot(boardY, boardX, new Spot(selectedSpot.getPiece(), boardY, boardX));
                    if(board.isKingSafe(currentPlayer.isWhile())) {
                        selectedSpot = null;
                        batch.begin();
                        batch.draw(chessBoard, centerX, centerY, scaledBoardWidth, scaledBoardHeight);
                        board.renderBoard(batch, spotSide, pieceSide, centerX, centerY);
                        batch.end();
                        if(currentPlayer == player1) {
                            currentPlayer = player2;
                        }else {
                            currentPlayer = player1;
                        }
                        moveSound.play();
                    }else {
                        board.setSpot(selectedSpot.getX(), selectedSpot.getY(), selectedSpot);
                        board.setSpot(boardY, boardX, secondSpot);
                    }
                }else {
                    if(secondSpot.getPiece() != null) {
                        selectedSpot = secondSpot;
                    }else {
                        selectedSpot = null;
                    }
                }
            }
        }
        return super.touchDown(screenX, screenY, pointer, button);
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
