package com.huy.game.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.Font;

public class ChessScreen extends InputAdapter implements Screen {
    private Stage stage;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont bitmapFont;
    private ChessImage chessImage;
    private ChessAI chessAI;

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
        chessImage = new ChessImage();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        bitmapFont = new BitmapFont(Gdx.files.internal("ui/montserrat.fnt"));
        stage = new Stage();
        player1 = new ChessPlayer(true);
        PlayerInfo player1Info = new PlayerInfo("1", player1.getMap(), chessImage, chessImage.getbBishop(), true, true, bitmapFont);
        player2 = new ChessPlayer(false);
        PlayerInfo player2Info = new PlayerInfo("2", player2.getMap(), chessImage, chessImage.getbQueen(), false, false, bitmapFont);
        chessAI = new ChessAI();
        currentPlayer = player1;
        chessSound = new ChessSound();
        board = new Board();
        TopAppBar appBar = new TopAppBar(chessImage);
        selectedSpot = null;
        stage.addActor(appBar.getStack());
        stage.addActor(player1Info.getInfo());
        stage.addActor(player2Info.getInfo());

        centerX = (Gdx.graphics.getWidth() - chessImage.getScaledBoardWidth()) / 2;
        centerY = (Gdx.graphics.getHeight() - chessImage.getScaledBoardHeight()) / 2;

        board.resetBoard(chessImage);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Colors.GREY_900_94);
        batch.begin();
        batch.draw(chessImage.getChessBoard(), centerX, centerY, chessImage.getScaledBoardWidth(), chessImage.getScaledBoardHeight());
        batch.end();

        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        board.renderColorAndPoint(shapeRenderer, chessImage.getCircleRadius(), chessImage.getPieceSize(),chessImage.getSpotSize(), centerX, centerY);
        shapeRenderer.end();
        Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);

        batch.begin();
        board.renderBoard(batch, chessImage.getSpotSize(), chessImage.getScaledPieceSize(), centerX, centerY);
        batch.end();

        if(board.isPromoting()) {
            board.showPromoteSelection(batch ,shapeRenderer, centerX, centerY, chessImage);
        }

        if(board.isEnd()) {
            batch.begin();
            batch.draw(chessImage.getChessBoard(), 0, 0);
            batch.end();
        }
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if(currentPlayer.isWhite()) {
            int boardX = (int) Math.floor((screenX - centerX) / chessImage.getSpotSize());
            int boardY = (int) Math.floor((Gdx.graphics.getHeight() - screenY - centerY) / chessImage.getSpotSize());

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
                        Move move = new Move(selectedSpot, secondSpot);
                        Board testBoard = board.cloneBoard();
                        boolean canMove = selectedSpot.getPiece().canMove(board, testBoard.getSpots(),selectedSpot, secondSpot);
                        if(canMove && selectedSpot.getPiece().isWhite() == currentPlayer.isWhite()) {
                            if(selectedSpot.getPiece() instanceof Pawn ) {
                                if((selectedSpot.getX() == 6 && selectedSpot.getPiece().isWhite()) || (selectedSpot.getX() == 1 && !selectedSpot.getPiece().isWhite())) {
                                    board.setPromoting(true);
                                    board.setPromotingSpot(secondSpot);
                                }
                            }
                            board.clearColorAndPoint();
                            move.makeMove(testBoard);
                            if(testBoard.isKingSafe(currentPlayer.isWhite())) {
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
                                    currentPlayer.putValue(secondSpot.getPiece());
                                    int value = player1.getValue() - player2.getValue();
                                    if(value > 0) {
                                        player1.putValue(value);
                                    }else {
                                        player2.putValue(Math.abs(value));
                                    }
                                }else {
                                    chessSound.playMoveSound();
                                }
                                move.makeRealMove(board);
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
        }else {
            Move move = chessAI.findBestMove(board, false);
            move.makeAIMove(board);
            currentPlayer = player1;
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
