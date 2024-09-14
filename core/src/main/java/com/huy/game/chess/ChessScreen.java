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
import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.Move;
import com.huy.game.chess.core.Pawn;
import com.huy.game.chess.core.Spot;
import com.huy.game.chess.manager.ChessGameManager;
import com.huy.game.chess.ai.ChessAI;
import com.huy.game.chess.ui.BottomAppBar;
import com.huy.game.chess.ui.ChessImage;
import com.huy.game.chess.ui.ChessSound;
import com.huy.game.chess.ui.Colors;
import com.huy.game.chess.ui.NotationHistoryScrollPane;
import com.huy.game.chess.ui.PlayerInfo;
import com.huy.game.chess.ui.TopAppBar;

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
    private ChessGameManager chessGameManager;
    private NotationHistoryScrollPane scrollPane;

    private ChessSound chessSound;

    @Override
    public void show() {
        chessImage = new ChessImage();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        bitmapFont = new BitmapFont(Gdx.files.internal("ui/montserrat.fnt"));
        stage = new Stage();
        chessGameManager = new ChessGameManager();
        PlayerInfo player1Info = new PlayerInfo("1", chessGameManager.getPlayer1().getMap(), chessImage, chessImage.getbBishop(), true, true, bitmapFont);
        PlayerInfo player2Info = new PlayerInfo("2", chessGameManager.getPlayer2().getMap(), chessImage, chessImage.getbQueen(), false, false, bitmapFont);
        chessAI = new ChessAI();
        chessSound = new ChessSound();
        board = new Board();
        TopAppBar appBar = new TopAppBar(chessImage);
        scrollPane = new NotationHistoryScrollPane();
        BottomAppBar bottomAppBar = new BottomAppBar(chessImage, bitmapFont);
        selectedSpot = null;
        stage.addActor(appBar.getStack());
        stage.addActor(scrollPane.getScrollPane());
        stage.addActor(player1Info.getInfo());
        stage.addActor(player2Info.getInfo());
        stage.addActor(bottomAppBar.getStack());

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

        if(chessGameManager.getCurrentPlayer().isWhite()) {
            int boardX = (int) Math.floor((screenX - centerX) / chessImage.getSpotSize());
            int boardY = (int) Math.floor((Gdx.graphics.getHeight() - screenY - centerY) / chessImage.getSpotSize());

            if (board.isWithinBoard(boardX, boardY)) {
                if(board.isPromoting()) {
                    board.handlePawnPromotion(boardX, boardY, chessImage, chessSound, chessGameManager);
                }else {
                    if (selectedSpot == null) {
                        selectedSpot = board.getSpot(boardY, boardX);
                        selectedSpot.setShowColor(true);
                        if(selectedSpot.getPiece() instanceof Pawn) {
                            ((Pawn) selectedSpot.getPiece()).setTurn(chessGameManager.getCurrentTurn());
                        }
                        if(selectedSpot.getPiece() == null || selectedSpot.getPiece().isWhite() != chessGameManager.getCurrentPlayer().isWhite()) {
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
                        if(canMove && selectedSpot.getPiece().isWhite() == chessGameManager.getCurrentPlayer().isWhite()) {
                            if(selectedSpot.getPiece() instanceof Pawn ) {
                                if((selectedSpot.getX() == 6 && selectedSpot.getPiece().isWhite()) || (selectedSpot.getX() == 1 && !selectedSpot.getPiece().isWhite())) {
                                    board.setPromoting(true);
                                    board.setPromotingSpot(secondSpot);
                                }
                            }
                            board.clearColorAndPoint();
                            move.makeMove(testBoard);
                            if(testBoard.isKingSafe(chessGameManager.getCurrentPlayer().isWhite())) {
                                board.handleMoveColorAndSound(selectedSpot, secondSpot, chessSound, chessGameManager);
                                scrollPane.addValue(move.makeRealMove(board), bitmapFont);
                                selectedSpot = null;
                                chessGameManager.switchPlayer(board);
                                if(board.isCheckmate(chessGameManager.getCurrentPlayer().isWhite())) {
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
                                board.clearGuidePoint();
                            }
                        }
                    }
                }
            }
        }else {
            Thread thread = new Thread(() -> {
                board.clearColor();
                Move move = chessAI.findBestMove(board, false);
                board.handleMoveColorAndSound(board.getSpot(move.getStart().getX(), move.getStart().getY()), board.getSpot(move.getEnd().getX(), move.getEnd().getY()), chessSound, chessGameManager);
                String text = move.makeAIMove(board);
                Gdx.app.postRunnable(() -> scrollPane.addValue(text, bitmapFont));
                chessGameManager.switchPlayer(board);
            });
            thread.start();
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
