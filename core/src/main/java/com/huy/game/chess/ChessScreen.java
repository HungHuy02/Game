package com.huy.game.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.huy.game.Main;
import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.BoardSetting;
import com.huy.game.chess.core.GameHistory;
import com.huy.game.chess.core.Move;
import com.huy.game.chess.core.Spot;
import com.huy.game.chess.core.ZobristHashing;
import com.huy.game.chess.core.notation.AlgebraicNotation;
import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.enums.GameResult;
import com.huy.game.chess.enums.MoveType;
import com.huy.game.chess.enums.TimeType;
import com.huy.game.chess.events.ChessGameOnlineEvent;
import com.huy.game.chess.manager.ChessAIPlayer;
import com.huy.game.chess.manager.ChessGameAssesManager;
import com.huy.game.chess.manager.ChessGameHistoryManager;
import com.huy.game.chess.manager.ChessGameManager;
import com.huy.game.chess.manager.ChessImage;
import com.huy.game.chess.manager.ChessOnlinePlayer;
import com.huy.game.chess.manager.ChessSound;
import com.huy.game.chess.manager.OpponentPlayer;
import com.huy.game.chess.manager.Player;
import com.huy.game.chess.model.HistoryModel;
import com.huy.game.chess.ui.BottomAppBar;
import com.huy.game.chess.ui.CheckPopup;
import com.huy.game.chess.ui.Colors;
import com.huy.game.chess.ui.DrawChoose;
import com.huy.game.chess.ui.EndGamePopup;
import com.huy.game.chess.ui.NotationHistoryScrollPane;
import com.huy.game.chess.ui.OptionsPopup;
import com.huy.game.chess.ui.PlayerInfo;
import com.huy.game.chess.ui.TopAppBar;
import com.huy.game.chess.ui.WaitTime;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

public class ChessScreen extends InputAdapter implements Screen {

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
    private Spot selectedSpot;
    private ChessGameManager chessGameManager;
    private NotationHistoryScrollPane scrollPane;
    private ExecutorService executorService;
    private PolygonSpriteBatch polygonSpriteBatch;

    private ChessGameHistoryManager chessGameHistoryManager;
    private ZobristHashing hashing;
    private InputMultiplexer multiplexer;
    private EndGamePopup endGamePopup;

    public ChessScreen(Main main) {
        this.main = main;
    }

    @Override
    public void show() {
        polygonSpriteBatch = new PolygonSpriteBatch();
        executorService = Executors.newSingleThreadExecutor();
        GameHistory gameHistory = new GameHistory();
        chessGameHistoryManager = new ChessGameHistoryManager(gameHistory);
        shapeRenderer = new ShapeRenderer();
        chessGameManager = new ChessGameManager(main.getMode(), Player.getInstance().isWhite(), main.timeType, main.stockfish, gameHistory, this);
        selectedSpot = null;
        setupUI(gameHistory);
        setupInputMultiplexer();
        setupBoard(gameHistory);
        handleSetupWithSpecificMode(gameHistory);
    }

    private void setupUI(GameHistory gameHistory) {
        stage = new Stage();
        Texture image = chessImage.getUserImage();
        PlayerInfo player1Info = new PlayerInfo(Player.getInstance().getName(), chessGameManager.getPlayer1().getCapturedPieceMap(), chessImage, image, Player.getInstance().isWhite(), true, bitmapFont, chessGameManager.getTimeList(), main.timeType);
        PlayerInfo player2Info = new PlayerInfo(OpponentPlayer.getInstance().getName(), chessGameManager.getPlayer2().getCapturedPieceMap(), chessImage, main.getMode() == ChessMode.AI ? manager.getAIImage() : image, !Player.getInstance().isWhite(), false, bitmapFont, chessGameManager.getTimeList(), main.timeType);
        TopAppBar appBar = new TopAppBar(chessImage, bitmapFont, main, bundle);
        scrollPane = new NotationHistoryScrollPane();
        CheckPopup checkPopup = new CheckPopup(manager, bitmapFont, bundle, stage, this, true, null);
        OptionsPopup optionsPopup = new OptionsPopup(setting, bitmapFont, bundle, manager, checkPopup.getCheckPopup(), chessImage ,stage, gameHistory, main.getMode(), main);
        BottomAppBar bottomAppBar = new BottomAppBar(chessImage, bitmapFont, optionsPopup.getOptionsPopup(), checkPopup.getCheckPopup(), stage, bundle, chessGameHistoryManager, scrollPane, manager);
        stage.addActor(appBar.getStack());
        stage.addActor(scrollPane.getScrollPane());
        stage.addActor(player1Info.getInfo());
        stage.addActor(player2Info.getInfo());
        stage.addActor(bottomAppBar.getStack());
        centerX = (Gdx.graphics.getWidth() - chessImage.getScaledBoardWidth()) / 2;
        centerY = (Gdx.graphics.getHeight() - chessImage.getScaledBoardHeight()) / 2;
    }

    public void setupBoard(GameHistory gameHistory) {
        board = new Board();
        board.resetBoard(chessImage);
        gameHistory.addFEN(board, false);
        board.increaseTurn();
        hashing = new ZobristHashing(board.getSpots(), gameHistory);
    }

    public void setupInputMultiplexer() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void handleSetupWithSpecificMode(GameHistory gameHistory) {
        switch (main.getMode()) {
            case ONLINE -> setupPlayOnline();
            case AI -> {
                if(chessGameManager.getCurrentPlayer() instanceof ChessAIPlayer) {
                    setting.setRotate(true);
                    handleAIMove();
                }else {
                    if (setting.canGenerateSuggestMove()) {
                        handleSuggestMove(gameHistory);
                    }
                }
            }
        }
    }

    private void setupPlayOnline() {
        Timer timer = new Timer();
        WaitTime waitTime = new WaitTime(bitmapFont, bundle, timer);
        ChessGameOnlineEvent.getInstance().setPlayerMoveListener(new ChessGameOnlineEvent.PlayerActionListener() {
            @Override
            public void onPlayerMove(String from, String to, MoveType type, int timeRemain) {
                if(chessGameHistoryManager.isRePlay()) {
                    chessGameHistoryManager.setRePlay(false);
                    chessGameHistoryManager.returnOriginIndex();
                }
                Spot start = board.getSpot(from.charAt(0) - '0', from.charAt(1) - '0');
                Spot end = board.getSpot(to.charAt(0) - '0', to.charAt(1) - '0');
                Move move = new Move(start,end);
                move.setMoveType(type);
                chessGameManager.setTimeRemainInOnlineMode(timeRemain);
                handleMove(move);
            }

            @Override
            public void onPlayerWantToDraw() {
                DrawChoose drawChoose = new DrawChoose(bitmapFont, bundle, chessImage, main);
                stage.addActor(drawChoose.getHorizontalGroup());
            }

            @Override
            public void onNewScore(int newScore) {
                if (endGamePopup == null) {
                    showEndGamePopup(GameResult.DRAW_AGREEMENT);
                }
                endGamePopup.setNewScore(Player.getInstance().getElo(), newScore);
            }

            @Override
            public void onOpponentLeftMatch() {
                if (Player.getInstance().isGuest()) {
                    if (endGamePopup == null) {
                        showEndGamePopup(Player.getInstance().isWhite() ? GameResult.WHITE_WIN : GameResult.BLACK_WIN);
                    }
                }else {
                    waitTime.setupTimer(ChessScreen.this);
                    stage.addActor(waitTime.getHorizontalGroup());
                }
            }

            @Override
            public void onOpponentComeback() {
                waitTime.getHorizontalGroup().remove();
                timer.clear();
                Timer delay = new Timer();
                delay.scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        main.socketClient.sendCurrentGameState(
                            Player.getInstance().isWhite(),
                            Player.getInstance().getElo(),
                            chessGameHistoryManager.getHistory().getPGN(),
                            chessGameManager.getPlayerTime(Player.getInstance().isWhite()),
                            chessGameManager.getPlayerTime(OpponentPlayer.getInstance().isWhite()));
                    }
                }, 1);
            }

            @Override
            public void currentGameState(boolean isWhite, int elo, String pgn, int playerTime, int opponentTime) {
                setting.setRotate(isWhite);
                chessGameManager.setTimeRemain(new int[] { playerTime, opponentTime});
                chessGameHistoryManager.setNewHistory(new int[] { playerTime, opponentTime});
                main.pgn = pgn;
                setupBoardFromPGN();
            }
        });
        main.socketClient.getMoveFromOpponent();
        main.socketClient.opponentWantToDraw();
        main.socketClient.newScoreAfterGameEnd();
        main.socketClient.opponentLeftMatch();
        main.socketClient.opponentComeback();
    }

    private void setupBoardFromPGN() {
        AlgebraicNotation.changePGNToBoard(
            main.pgn, board, true, ChessScreen.this);
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
            if(board.isPromoting()) {
                board.showPromoteSelection(batch ,shapeRenderer, centerX, centerY, chessImage, setting);
            }
        }

        if (setting.canGenerateSuggestMove()) {
            if (setting.isRotate()) {
                board.renderSuggestiveMoveForRotateBoard(polygonSpriteBatch, centerX, centerY, chessImage.getSpotSize());
            }else {
                board.renderSuggestiveMove(polygonSpriteBatch, centerX, centerY, chessImage.getSpotSize());
            }
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch (main.getMode()) {
            case TWO_PERSONS:
                handleClick(board, screenX, screenY);
                break;
            case AI:
                handleClickWhenPlayWithAI(screenX, screenY);
                break;
            case ONLINE:
                handleClickWhenPlayOnline(screenX, screenY);
                break;
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    private void handleClickWhenPlayWithAI(int screenX, int screenY) {
        if(!(chessGameManager.getCurrentPlayer() instanceof ChessAIPlayer)) {
            handleClick(chessGameHistoryManager.isTakeBack() ? chessGameHistoryManager.getBoard() : board, screenX, screenY);
        }
    }

    private void handleClickWhenPlayOnline(int screenX, int screenY) {
        if(!(chessGameManager.getCurrentPlayer() instanceof ChessOnlinePlayer)) {
            handleClick(board, screenX, screenY);
        }
    }

    private void handleClick(Board board, int screenX, int screenY) {
        int boardX = (int) Math.floor((screenX - centerX) / chessImage.getSpotSize());
        int boardY = (int) Math.floor((Gdx.graphics.getHeight() - screenY - centerY) / chessImage.getSpotSize());

        if (board.isWithinBoard(boardX, boardY)) {
            if (main.getMode() == ChessMode.AI && setting.isCanBack() && chessGameHistoryManager.isRePlay() && !chessGameHistoryManager.isTakeBack()) {
                chessGameHistoryManager.setTakeBack(true);
                handleClick(chessGameHistoryManager.getBoard(), screenX, screenY);
            }else {
                if(board.isPromoting()) {
                    handlePawnPromotion(board, boardX, boardY);
                }else {
                    if(setting.isRotate()) {
                        boardX = 7 - boardX;
                        boardY = 7 - boardY;
                    }
                    if (selectedSpot == null) {
                        selectedSpot = board.getSpot(boardY, boardX);
                        selectedSpot.setShowColor(true);
                        if(selectedSpot.getPiece() == null || selectedSpot.getPiece().isWhite() != chessGameManager.getCurrentPlayer().isWhite()) {
                            if(!selectedSpot.isIdentificationColor()) {
                                selectedSpot.setShowColor(false);
                            }
                            selectedSpot = null;
                        }else {
                            selectedSpot.getPiece().calculateForPoint(board, selectedSpot);
                        }
                    } else {
                        checkAndHandleMove(board, boardX, boardY);
                    }
                }
            }
        }
    }

    private void checkAndHandleMove(Board board, int boardX, int boardY) {
        Spot secondSpot = board.getSpot(boardY, boardX);
        Move move = new Move(selectedSpot, secondSpot);
        Board testBoard = board.cloneBoard();
        MoveType moveType = selectedSpot.getPiece().canMove(testBoard, testBoard.getSpots(),selectedSpot, secondSpot);
        if(moveType != MoveType.CAN_NOT_MOVE && selectedSpot.getPiece().isWhite() == chessGameManager.getCurrentPlayer().isWhite()) {
            move.setMoveType(moveType);
            board.clearColorAndPoint();
            move.makeMove(testBoard);
            if(testBoard.isKingSafe(chessGameManager.getCurrentPlayer().isWhite())) {
                if (moveType != MoveType.PROMOTE) {
                    handleTakeBack();
                    handleMove(move);
                    handleMoveWithOtherMode(move);
                }else {
                    handleTakeBack();
                    move.handleSpecialMove(this.board, chessImage, chessGameManager);
                }
                selectedSpot = null;
            }else {
                handleWhenPerformIllegalMove();
            }
        }else {
            handleWhenClickAnotherSpotCanNotMoveTo(secondSpot);
        }
    }

    private void handlePawnPromotion(Board board, int boardX, int boardY) {
        board.handlePawnPromotion(boardX, boardY, setting);
        Move move = board.getPromotingMove();
        if(move != null) {
            handleMove(move);
        }
        handleMoveWithOtherMode(move);
    }

    private void handleTakeBack() {
        if (chessGameHistoryManager.isTakeBack()) {
            chessGameHistoryManager.setTakeBack(false);
            chessGameHistoryManager.setRePlay(false);
            scrollPane.deleteOldSaved();
            chessGameHistoryManager.deleteOldSaved();
            if (main.timeType != TimeType.NO_TIME) {
                chessGameManager.setTimeRemain(chessGameHistoryManager.getTimeRemain());
            }
            board = chessGameHistoryManager.getBoard();
        }
    }

    private void handleMoveWithOtherMode(Move move) {
        switch (main.getMode()) {
            case ONLINE -> main.socketClient.makeMove(
                move.getStart().getX() + "" + move.getStart().getY(),
                move.getEnd().getX() + "" + move.getEnd().getY(),
                move.getMoveType(),
                chessGameManager.getTimeRemainForOnlineMode());
            case AI -> {
                if (endGamePopup == null) {
                    handleAIMove();
                }
            }
        }
    }

    private void handleAIMove() {
        executorService.submit(() -> {
            board.clearColor();
            if(chessGameManager.getCurrentPlayer() instanceof ChessAIPlayer chessAIPlayer) {
                Move aiMove = chessAIPlayer.findBestMove(board, chessGameHistoryManager.getHistory().getNewestFEN());
                if(chessGameHistoryManager.isRePlay()) {
                    chessGameHistoryManager.setRePlay(false);
                    chessGameHistoryManager.returnOriginIndex();
                }
                String text = aiMove.makeAIMove(board, hashing, chessGameHistoryManager.getHistory(), chessImage, chessGameManager);
                board.handleSoundAfterMove(aiMove, chessSound);
                Gdx.app.postRunnable(() -> scrollPane.addValue(text, bitmapFont, manager, chessGameHistoryManager, chessImage));
                chessGameHistoryManager.increaseIndex();
                chessGameManager.switchPlayer(setting, chessGameHistoryManager, this);
                checkForEndGame(aiMove);
            }
        });
        if (setting.canGenerateSuggestMove()) {
            handleSuggestMove(chessGameHistoryManager.getHistory());
        }
    }

    private void handleSuggestMove(GameHistory history) {
        executorService.submit(() -> main.stockfish.suggestiveMove(history.getNewestFEN(), data -> {
            Spot start =  board.getSpot(
                AlgebraicNotation.changeRowAlgebraicNotationToRowPosition(data.charAt(1)),
                AlgebraicNotation.changeColAlgebraicNotationToColPosition(data.charAt(0)));
            Spot end = board.getSpot(
                AlgebraicNotation.changeRowAlgebraicNotationToRowPosition(data.charAt(3)),
                AlgebraicNotation.changeColAlgebraicNotationToColPosition(data.charAt(2)));
            board.setSuggestMove(new Move(start, end));
        }));
    }

    public void handleMove(Move move) {
        chessGameHistoryManager.increaseIndex();
        scrollPane.addValue(move.makeRealMove(board, hashing, chessGameHistoryManager.getHistory(), chessImage, chessGameManager), bitmapFont, manager, chessGameHistoryManager, chessImage);
        board.handleSoundAfterMove(move, chessSound);
        chessGameManager.switchPlayer(setting, chessGameHistoryManager, this);
        board.setSuggestMove(null);
        board.setPolygonSprite(null);
        checkForEndGame(move);
    }

    private void handleWhenPerformIllegalMove() {
        board.warnIllegalMove(selectedSpot.getPiece().isWhite());
        selectedSpot.setShowColor(true);
        chessSound.playIllegalSound();
    }

    private void checkForEndGame(Move move) {
        GameResult gameResult = null;
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
            }
        }
        if (gameResult != null) {
            showEndGamePopup(gameResult);
            handleAfterShowEndGamePopup(gameResult);
        }
    }

    private void handleWhenClickAnotherSpotCanNotMoveTo(Spot anotherSpot) {
        if(anotherSpot.getPiece() != null) {
            selectedSpot.setShowColor(false);
            selectedSpot = anotherSpot;
            board.clearGuidePoint();
            selectedSpot.setShowColor(true);
            selectedSpot.getPiece().calculateForPoint(board, selectedSpot);
        }else {
            selectedSpot = null;
            board.clearGuidePoint();
        }
    }

    public void showEndGamePopup(GameResult gameResult) {
        endGamePopup = new EndGamePopup(bitmapFont, bundle, manager, gameResult, main, this);
        stage.addActor(endGamePopup.getPopup());
        Gdx.input.setCatchKey(Input.Keys.BACK, false);
        multiplexer.removeProcessor(0);
        chessSound.playGameEndSound();
        chessGameManager.finish();
        chessGameHistoryManager.getHistory().handleForEndgameNotation(gameResult, scrollPane);
    }

    public void handleAfterShowEndGamePopup(GameResult result) {
        switch (main.getMode()) {
            case ONLINE -> {
                if (chessGameManager.getCurrentPlayer().isWhite() != Player.getInstance().isWhite())
                    main.socketClient.gameEnd(result);
            }
            case AI -> setting.setSuggestMove(false);
        }
        if (main.localDatabase != null) {
            HistoryModel model = new HistoryModel(
                main.timeType,
                OpponentPlayer.getInstance().getImageUrl(),
                OpponentPlayer.getInstance().isWhite(),
                OpponentPlayer.getInstance().getName(),
                result,
                main.getMode(),
                chessGameHistoryManager.getHistory().getPGN());
            main.localDatabase.insert(model);
        }
    }

    public void newGame() {
        chessGameHistoryManager.reset();
        setupBoard(chessGameHistoryManager.getHistory());
        scrollPane.reset();
        setupInputMultiplexer();
        chessGameManager.reset(chessGameHistoryManager.getHistory(), this);
        endGamePopup = null;
        if (main.getMode() != ChessMode.TWO_PERSONS) {
            if (main.getMode() == ChessMode.AI) {
                main.stockfish.setupNewGame();
                if (main.enableSuggesting) {
                    setting.setSuggestMove(true);
                }
            }
            handleSetupWithSpecificMode(chessGameHistoryManager.getHistory());
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            if (Objects.requireNonNull(main.getMode()) == ChessMode.ONLINE) {
                CheckPopup checkPopup = new CheckPopup(manager, bitmapFont, bundle, stage, this, false, main.backInterface);
                Image overlay = chessImage.getOverlay();
                overlay.setFillParent(true);
                overlay.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        checkPopup.getCheckPopup().remove();
                        overlay.remove();
                    }
                });
                stage.addActor(overlay);
                stage.addActor(checkPopup.getCheckPopup());
            } else {
                main.backInterface.back();
            }
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
        polygonSpriteBatch.dispose();
        shapeRenderer.dispose();
    }
}
