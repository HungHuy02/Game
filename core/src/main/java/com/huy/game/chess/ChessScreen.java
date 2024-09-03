package com.huy.game.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
        board.renderColorAndPoint(shapeRenderer, chessImage.getCircleRadius(), chessImage.getPieceSize(),chessImage.getSpotSize(), centerX, centerY);
        shapeRenderer.end();
        Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);

        batch.begin();
        board.renderBoard(batch, chessImage.getSpotSize(), chessImage.getPieceSize(), centerX, centerY);
        batch.end();

        if(board.isPromoting()) {
            board.showPromoteSelection(batch ,shapeRenderer, centerX, centerY, chessImage);
        }

        if(board.isEnd()) {
            batch.begin();
            batch.draw(chessImage.getChessBoard(), 0, 0);
            batch.end();
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int boardX = (int) Math.floor((screenX - centerX) / chessImage.getSpotSize());
        int boardY = (int) Math.floor((Gdx.graphics.getHeight() - (screenY - centerY)) / chessImage.getSpotSize());

        if (board.isWithinBoard(boardX, boardY)) {
            if(board.isPromoting()) {
                if(boardX != board.getPromotingSpot().getY()) {
                    board.setPromoting(false);
                    board.setPromotingSpot(null);
                }else {
                    board.setPromoting(false);
                    Piece piece = null;
                    if(board.getPromotingSpot().getPiece().isWhite()) {
                        switch (boardY) {
                            case 4:
                                piece = new Rook(true, chessImage.getwRock());
                                break;
                            case 5:
                                piece = new Bishop(true, chessImage.getwBishop());
                                break;
                            case 6:
                                piece = new Knight(true, chessImage.getwKnight());
                                break;
                            case 7:
                                piece = new Queen(true, chessImage.getwQueen());
                                break;
                        }
                    }else {
                        switch (boardY) {
                            case 0:
                                piece = new Queen(false, chessImage.getbQueen());
                                break;
                            case 1:
                                piece = new Knight(false, chessImage.getbKnight());
                                break;
                            case 2:
                                piece = new Bishop(false, chessImage.getbBishop());
                                break;
                            case 3:
                                piece = new Rook(false, chessImage.getbRock());
                                break;
                            case 4:
                                piece = new Rook(true, chessImage.getwRock());
                                break;
                            case 5:
                                piece = new Bishop(true, chessImage.getwBishop());
                                break;
                            case 6:
                                piece = new Knight(true, chessImage.getwKnight());
                                break;
                            case 7:
                                piece = new Queen(true, chessImage.getwQueen());
                                break;
                        }
                    }
                    board.setSpot(board.getPromotingSpot().getX(), board.getPromotingSpot().getY(), piece);
                    chessSound.playPromoteSound();
                }
            }else {
                if (selectedSpot == null) {
                    selectedSpot = board.getSpot(boardY, boardX);
                    selectedSpot.setShowColor(true);
                    if(selectedSpot.getPiece() instanceof Pawn) {
                        ((Pawn) selectedSpot.getPiece()).setTurn(turn);
                    }
                    if(selectedSpot.getPiece() == null || selectedSpot.getPiece().isWhite() != currentPlayer.isWhite()) {
                        if(!selectedSpot.isIdentificationColor()) {
                            selectedSpot.setShowColor(false);
                        }
                        selectedSpot = null;
                    }else {
                        selectedSpot.getPiece().calculateForPoint(board, selectedSpot);
                    }
                } else {
                    Spot secondSpot = board.getSpot(boardY, boardX);
                    boolean canMove = selectedSpot.getPiece().canMove(board, board.cloneSpots(board.getSpots()),selectedSpot, secondSpot);
                    if(canMove && selectedSpot.getPiece().isWhite() == currentPlayer.isWhite()) {
                        if(selectedSpot.getPiece() instanceof Pawn ) {
                            if((selectedSpot.getX() == 6 && selectedSpot.getPiece().isWhite()) || (selectedSpot.getX() == 1 && !selectedSpot.getPiece().isWhite())) {
                                board.setPromoting(true);
                                board.setPromotingSpot(secondSpot);
                            }
                        }
                        board.clearColorAndPoint();
                        board.makeTempMove(selectedSpot, secondSpot);
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
                            board.makeMove(selectedSpot, secondSpot);
                            selectedSpot = null;
                            if(currentPlayer == player1) {
                                currentPlayer = player2;
                            }else {
                                currentPlayer = player1;
                                turn++;
                            }
                            if(board.isCheckmate(currentPlayer.isWhite())) {
                                chessSound.playGameEndSound();
                                board.setEnd();
                            }
                        }else {
                            board.warnIllegalMove(selectedSpot.getPiece().isWhite());
                            selectedSpot.setShowColor(true);
                            chessSound.playIllegalSound();
                        }
                    }else {
                        if(secondSpot.getPiece() != null) {
                            selectedSpot.setShowColor(false);
                            selectedSpot = secondSpot;
                            board.clearGuidePoint();
                            selectedSpot.setShowColor(true);
                            selectedSpot.getPiece().calculateForPoint(board, selectedSpot);
                        }else {
                            selectedSpot = null;
                        }
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
