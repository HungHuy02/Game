package com.huy.game.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.huy.game.Main;
import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.BoardSetting;
import com.huy.game.chess.core.GameHistory;
import com.huy.game.chess.core.Move;
import com.huy.game.chess.core.ZobristHashing;
import com.huy.game.chess.core.notation.AlgebraicNotation;
import com.huy.game.chess.enums.GameResult;
import com.huy.game.chess.manager.ChessGameAssesManager;
import com.huy.game.chess.manager.ChessGameHistoryManager;
import com.huy.game.chess.manager.ChessGameManager;
import com.huy.game.chess.manager.ChessImage;
import com.huy.game.chess.manager.ChessSound;
import com.huy.game.chess.manager.OpponentPlayer;
import com.huy.game.chess.manager.Player;
import com.huy.game.chess.ui.BottomAppBar;
import com.huy.game.chess.ui.Colors;
import com.huy.game.chess.ui.EndGamePopup;
import com.huy.game.chess.ui.NotationHistoryScrollPane;
import com.huy.game.chess.ui.OptionsPopup;
import com.huy.game.chess.ui.PlayerInfo;
import com.huy.game.chess.ui.TopAppBar;

import javax.inject.Inject;

public class WatchingHistoryScreen extends InputAdapter implements Screen {

    private final Main main;
    private Stage stage;

    @Inject
    SpriteBatch batch;
    @Inject
    BitmapFont bitmapFont;
    @Inject
    ChessImage chessImage;
    @Inject
    I18NBundle bundle;
    @Inject
    ChessSound chessSound;
    @Inject
    BoardSetting setting;
    @Inject
    ChessGameAssesManager manager;

    private ShapeRenderer shapeRenderer;
    private float centerX;
    private float centerY;
    private Board board;
    private ChessGameManager chessGameManager;
    private NotationHistoryScrollPane scrollPane;
    private ChessGameHistoryManager chessGameHistoryManager;
    private ZobristHashing hashing;

    public WatchingHistoryScreen(Main main) {
        this.main = main;
    }

    @Override
    public void show() {
        GameHistory gameHistory = new GameHistory();
        chessGameHistoryManager = new ChessGameHistoryManager(gameHistory);
        shapeRenderer = new ShapeRenderer();
        chessGameManager = new ChessGameManager(Player.getInstance().isWhite());
        setupUI(gameHistory);
        setupInit(gameHistory);
        setupInputMultiplexer();
        setupBoardFromPGN();
    }

    private void setupUI(GameHistory gameHistory) {
        stage = new Stage();
        PlayerInfo player1Info = new PlayerInfo(Player.getInstance().getName(), chessGameManager.getPlayer1().getCapturedPieceMap(), chessImage, chessImage.getbBishop(), Player.getInstance().isWhite(), true, bitmapFont);
        PlayerInfo player2Info = new PlayerInfo(OpponentPlayer.getInstance().getName(), chessGameManager.getPlayer2().getCapturedPieceMap(), chessImage, chessImage.getbQueen(), !Player.getInstance().isWhite(), false, bitmapFont);
        TopAppBar appBar = new TopAppBar(chessImage, bitmapFont, main, bundle);
        scrollPane = new NotationHistoryScrollPane();
        OptionsPopup optionsPopup = new OptionsPopup(setting, bitmapFont, bundle, manager, null, chessImage ,stage, gameHistory, main.getMode(), main);
        BottomAppBar bottomAppBar = new BottomAppBar(chessImage, bitmapFont, optionsPopup.getOptionsPopup(), null, stage, bundle, chessGameHistoryManager, scrollPane, manager);
        stage.addActor(appBar.getStack());
        stage.addActor(player1Info.getInfo());
        stage.addActor(player2Info.getInfo());
        stage.addActor(bottomAppBar.getStack());
        stage.addActor(scrollPane.getScrollPane());
        centerX = (Gdx.graphics.getWidth() - chessImage.getScaledBoardWidth()) / 2;
        centerY = (Gdx.graphics.getHeight() - chessImage.getScaledBoardHeight()) / 2;
    }

    private void setupInit(GameHistory gameHistory) {
        board = new Board();
        board.resetBoard(chessImage);
        gameHistory.addFEN(board, false);
        board.increaseTurn();
        hashing = new ZobristHashing(board.getSpots(), gameHistory);
    }

    private void setupInputMultiplexer() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void setupBoardFromPGN() {
        Thread thread = new Thread(() -> {
            Board newBoard = new Board();
            newBoard.resetBoard(chessImage);
            AlgebraicNotation.changePGNToBoard(
                main.pgn, newBoard, true, this);
            board = newBoard;
        });
        thread.start();
    }

    public void handleMove(Move move, Board board) {
        board.clearColor();
        chessGameHistoryManager.increaseIndex();
        String text = move.makeRealMove(board, hashing, chessGameHistoryManager.getHistory(), chessImage, chessGameManager);
        Gdx.app.postRunnable(() ->
            scrollPane.addValue(text , bitmapFont, manager, chessGameHistoryManager, chessImage)
        );
        chessGameManager.switchPlayer();
    }

    public void checkForEndGame(Move move, Board board) {
        GameResult gameResult;
        if(!board.isHaveAvailableMove(chessGameManager.getCurrentPlayer().isWhite())) {
            if(move.isCheck()) {
                gameResult = chessGameManager.getCurrentPlayer().isWhite() ? GameResult.BLACK_WIN : GameResult.WHITE_WIN;
            }else {
                gameResult = GameResult.DRAW_STALEMATE;
            }
        }else{
            if (chessGameHistoryManager.getHistory().checkThreefoldRepetition()) {
                gameResult = GameResult.DRAW_THREEFOLD;
            } else if(chessGameHistoryManager.getHistory().check50MovesRule()) {
                gameResult = GameResult.DRAW_FIFTY_MOVE;
            } else if(chessGameManager.isDrawByInsufficientPiece()) {
                gameResult = GameResult.DRAW_INSUFFICIENT;
            } else {
                gameResult = null;
            }
        }
        if (gameResult != null) {
            Gdx.app.postRunnable(() -> showEndGamePopup(gameResult));
        }
    }

    public void showEndGamePopup(GameResult gameResult) {
        EndGamePopup endGamePopup = new EndGamePopup(bitmapFont, bundle, manager, gameResult, main, null);
        stage.addActor(endGamePopup.getPopup());
        chessSound.playGameEndSound();
        chessGameHistoryManager.getHistory().handleForEndgameNotation(gameResult, scrollPane);
    }

    public void newGame() {
        main.toChessScreen();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Colors.GREY_900_94);
        batch.begin();
        batch.draw(chessImage.getChessBoard(), centerX, centerY, chessImage.getScaledBoardWidth(), chessImage.getScaledBoardHeight());
        batch.end();

        if(chessGameHistoryManager.isRePlay()) {
            renderBoard(chessGameHistoryManager.getBoard());
        }else {
            renderBoard(board);
        }
        stage.act(delta);
        stage.draw();
    }

    private void renderBoard(Board board) {
        if (setting.isReverseOneSide()) {
            if(setting.isRotate()) {
                Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                board.renderRotateColorAndPoint(shapeRenderer, chessImage.getCircleRadius(), chessImage.getPieceSize(),chessImage.getSpotSize(), centerX, centerY);
                shapeRenderer.end();
                Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);

                batch.begin();
                board.renderRotateBoardReverseOneSide(batch, chessImage.getSpotSize(), chessImage.getPieceSize(), chessImage.getScaledPieceSize(), centerX, centerY, chessImage);
                batch.end();
            }else {
                Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                board.renderColorAndPoint(shapeRenderer, chessImage.getCircleRadius(), chessImage.getPieceSize(),chessImage.getSpotSize(), centerX, centerY);
                shapeRenderer.end();
                Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);

                batch.begin();
                board.renderBoardReverseOneSide(batch, chessImage.getSpotSize(), chessImage.getPieceSize(), chessImage.getScaledPieceSize(), centerX, centerY, chessImage);
                batch.end();
            }
        }else {
            if(setting.isRotate()) {
                Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                board.renderRotateColorAndPoint(shapeRenderer, chessImage.getCircleRadius(), chessImage.getPieceSize(),chessImage.getSpotSize(), centerX, centerY);
                shapeRenderer.end();
                Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);

                batch.begin();
                board.renderRotateBoard(batch, chessImage.getSpotSize(), chessImage.getScaledPieceSize(), centerX, centerY, chessImage);
                batch.end();
            }else {
                Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                board.renderColorAndPoint(shapeRenderer, chessImage.getCircleRadius(), chessImage.getPieceSize(),chessImage.getSpotSize(), centerX, centerY);
                shapeRenderer.end();
                Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);

                batch.begin();
                board.renderBoard(batch, chessImage.getSpotSize(), chessImage.getScaledPieceSize(), centerX, centerY, chessImage);
                batch.end();
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            main.backInterface.back();
        }
        return super.keyDown(keycode);
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

    }
}
