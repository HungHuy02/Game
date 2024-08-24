package com.huy.game.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class ChessScreen extends InputAdapter implements Screen {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private ChessImage chessImage;

    private float centerX;
    private float centerY;
    private Board board;
    private Spot selectedSpot;
    private ChessPlayer player1;
    private ChessPlayer player2;
    private ChessPlayer currentPlayer;

    private ChessSound chessSound;
    private int turn;

    @Override
    public void show() {
        player1 = new ChessPlayer(true);
        player2 = new ChessPlayer(false);
        currentPlayer = player1;
        chessSound = new ChessSound();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        board = new Board();
        chessImage = new ChessImage();
        selectedSpot = null;

        centerX = (Gdx.graphics.getWidth() - chessImage.getScaledBoardWidth()) / 2;
        centerY = (Gdx.graphics.getHeight() - chessImage.getScaledBoardHeight()) / 2;

        board.resetBoard(chessImage);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        batch.begin();
        batch.draw(chessImage.getChessBoard(), centerX, centerY, chessImage.getScaledBoardWidth(), chessImage.getScaledBoardHeight());
        batch.end();

        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        board.renderColor(shapeRenderer, chessImage.getSpotSize(), centerX, centerY);
        shapeRenderer.end();
        Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);

        batch.begin();
        board.renderBoard(batch, chessImage.getSpotSize(), chessImage.getPieceSize(), centerX, centerY);
        batch.end();

        if(board.isPromoting()) {
            board.showPromoteSelection(batch ,shapeRenderer, centerX, centerY, chessImage);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int boardX = (int) Math.floor((screenX - centerX) / chessImage.getSpotSize());
        int boardY = (int) Math.floor((Gdx.graphics.getHeight() - (screenY - centerY)) / chessImage.getSpotSize());

        if (boardX >= 0 && boardX < 8 && boardY >= 0 && boardY < 8) {
            if (selectedSpot == null) {
                selectedSpot = board.getSpot(boardY, boardX);
                selectedSpot.setShowColor(true);
                if(selectedSpot.getPiece() instanceof Pawn) {
                    ((Pawn) selectedSpot.getPiece()).setTurn(turn);
                }
                if(selectedSpot.getPiece() == null || selectedSpot.getPiece().isWhite() != currentPlayer.isWhite()) {
                    selectedSpot.setShowColor(false);
                    selectedSpot = null;
                }
            } else {
                Spot secondSpot = board.getSpot(boardY, boardX);
                boolean canMove = selectedSpot.getPiece().canMove(board, selectedSpot, secondSpot);
                if(canMove && selectedSpot.getPiece().isWhite() == currentPlayer.isWhite()) {
                    if(selectedSpot.getPiece() instanceof Pawn && selectedSpot.getX() == 6 && selectedSpot.getPiece().isWhite()) {
                        board.setPromoting(true);
                        board.setPromotingSpot(secondSpot);
                    }
                    board.clearColor();
                    board.setSpot(selectedSpot.getX(), selectedSpot.getY(), new Spot(null, selectedSpot.getX(), selectedSpot.getY(), true));
                    board.setSpot(boardY, boardX, new Spot(selectedSpot.getPiece(), boardY, boardX, true));
                    if(board.isKingSafe(currentPlayer.isWhite())) {
                        if(selectedSpot.getPiece() instanceof Pawn) {
                            if(((Pawn) selectedSpot.getPiece()).isMoveTwo()) {
                                ((Pawn) selectedSpot.getPiece()).setTurn(turn);
                            }
                        }
                        if(!board.isKingSafe(!currentPlayer.isWhite())) {
                            chessSound.playCheckSound();
                        }else if(selectedSpot.getPiece() instanceof King) {
                            if(((King) selectedSpot.getPiece()).isCastling()) {
                                ((King) selectedSpot.getPiece()).setCastling(false);
                                chessSound.playCastleSound();
                            }else {
                                chessSound.playMoveSound();
                            }
                        }else if(secondSpot.getPiece() != null) {
                            chessSound.playCaptureSound();
                        }else {
                            chessSound.playMoveSound();
                        }
                        selectedSpot = null;
                        if(currentPlayer == player1) {
                            currentPlayer = player2;
                        }else {
                            currentPlayer = player1;
                            turn++;
                        }
                    }else {
                        board.warnIllegalMove(selectedSpot.getPiece().isWhite());
                        selectedSpot.setShowColor(true);
                        board.setSpot(selectedSpot.getX(), selectedSpot.getY(), selectedSpot);
                        board.setSpot(boardY, boardX, secondSpot);
                        chessSound.playIllegalSound();
                    }
                }else {
                    if(secondSpot.getPiece() != null) {
                        selectedSpot.setShowColor(false);
                        selectedSpot = secondSpot;
                        selectedSpot.setShowColor(true);
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
        shapeRenderer.dispose();
        chessImage.dispose();
        chessSound.dispose();
    }
}
