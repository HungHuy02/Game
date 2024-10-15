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
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.huy.game.Main;
import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.BoardSetting;
import com.huy.game.chess.core.GameHistory;
import com.huy.game.chess.core.Move;
import com.huy.game.chess.core.Pawn;
import com.huy.game.chess.core.Spot;
import com.huy.game.chess.core.ZobristHashing;
import com.huy.game.chess.enums.ChessMode;
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
import com.huy.game.chess.ui.BottomAppBar;
import com.huy.game.chess.ui.CheckPopup;
import com.huy.game.chess.ui.Colors;
import com.huy.game.chess.ui.EndGamePopup;
import com.huy.game.chess.ui.NotationHistoryScrollPane;
import com.huy.game.chess.ui.OptionsPopup;
import com.huy.game.chess.ui.PlayerInfo;
import com.huy.game.chess.ui.TopAppBar;

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

    private ChessGameHistoryManager chessGameHistoryManager;
    private ZobristHashing hashing;
    private InputMultiplexer multiplexer;

    public ChessScreen(Main main) {
        this.main = main;
    }

    @Override
    public void show() {
        GameHistory gameHistory = new GameHistory();
        chessGameHistoryManager = new ChessGameHistoryManager(gameHistory);
        stage = new Stage();
        shapeRenderer = new ShapeRenderer();
        chessGameManager = new ChessGameManager(main.getMode(), Player.getInstance().isWhite(), main.stockfish);
        PlayerInfo player1Info = new PlayerInfo(Player.getInstance().getName(), chessGameManager.getPlayer1().getMap(), chessImage, chessImage.getbBishop(), Player.getInstance().isWhite(), true, bitmapFont, chessGameManager.getTimeList());
        PlayerInfo player2Info = new PlayerInfo(OpponentPlayer.getInstance().getName(), chessGameManager.getPlayer2().getMap(), chessImage, chessImage.getbQueen(), !Player.getInstance().isWhite(), false, bitmapFont, chessGameManager.getTimeList());
        board = new Board();
        TopAppBar appBar = new TopAppBar(chessImage, bitmapFont, main, bundle);
        scrollPane = new NotationHistoryScrollPane();
        CheckPopup checkPopup = new CheckPopup(manager, bitmapFont, bundle, stage);
        OptionsPopup optionsPopup = new OptionsPopup(setting, bitmapFont, bundle, manager, checkPopup.getCheckPopup(), chessImage ,stage);
        BottomAppBar bottomAppBar = new BottomAppBar(chessImage, bitmapFont, optionsPopup.getOptionsPopup(), checkPopup.getCheckPopup(),stage, bundle);
        selectedSpot = null;
        stage.addActor(appBar.getStack());
        stage.addActor(scrollPane.getScrollPane());
        stage.addActor(player1Info.getInfo());
        stage.addActor(player2Info.getInfo());
        stage.addActor(bottomAppBar.getStack());
        centerX = (Gdx.graphics.getWidth() - chessImage.getScaledBoardWidth()) / 2;
        centerY = (Gdx.graphics.getHeight() - chessImage.getScaledBoardHeight()) / 2;
        board.resetBoard(chessImage);
        hashing = new ZobristHashing(board.getSpots());
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
        if(main.getMode() == ChessMode.ONLINE) {
            ChessGameOnlineEvent.getInstance().setPlayerMoveListener((from, to) -> {
                Spot start = board.getSpot(from.charAt(0) - '0', from.charAt(1) - '0');
                Spot end = board.getSpot(to.charAt(0) - '0', to.charAt(1) - '0');
                Move move = new Move(start,end);
                board.handleMoveColorAndSound(start, end, chessSound, chessGameManager);
                scrollPane.addValue(move.makeRealMove(board, hashing, gameHistory, chessGameManager), bitmapFont, manager, chessGameHistoryManager, chessImage);
                chessGameManager.switchPlayer(board);
            });
            main.socketClient.getMoveFromOpponent();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Colors.GREY_900_94);
        batch.begin();
        batch.draw(chessImage.getChessBoard(), centerX, centerY, chessImage.getScaledBoardWidth(), chessImage.getScaledBoardHeight());
        batch.end();

        if(chessGameHistoryManager.isRePlay()) {
            batch.begin();
            chessGameHistoryManager.getBoard().renderBoard(batch, chessImage.getSpotSize(), chessImage.getScaledPieceSize(), centerX, centerY, chessImage);
            batch.end();
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

            if(board.isPromoting()) {
                board.showPromoteSelection(batch ,shapeRenderer, centerX, centerY, chessImage);
            }
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch (main.getMode()) {
            case TWO_PERSONS:
                handleClick(screenX, screenY);
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
            handlePlayerClickWhenPlayWithAI(screenX, screenY);
        }
    }

    private void handleClickWhenPlayOnline(int screenX, int screenY) {
        if(!(chessGameManager.getCurrentPlayer() instanceof ChessOnlinePlayer)) {
            handlePlayerClickWhenPlayOnline(screenX, screenY);
        }
    }

    private void handleClick(int screenX, int screenY) {
        int boardX = (int) Math.floor((screenX - centerX) / chessImage.getSpotSize());
        int boardY = (int) Math.floor((Gdx.graphics.getHeight() - screenY - centerY) / chessImage.getSpotSize());
        if(setting.isRotate()) {
            boardX = 7 - boardX;
            boardY = 7 - boardY;
        }

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
                            scrollPane.addValue(move.makeRealMove(board, hashing, chessGameHistoryManager.getHistory(), chessGameManager), bitmapFont, manager, chessGameHistoryManager, chessImage);
                            selectedSpot = null;
                            chessGameManager.switchPlayer(board);
                            if(board.isCheckmate(chessGameManager.getCurrentPlayer().isWhite())) {
                                chessSound.playGameEndSound();
                                EndGamePopup popup = new EndGamePopup(bitmapFont, manager.getBundle("vi"), manager, !chessGameManager.getCurrentPlayer().isWhite());
                                stage.addActor(popup.getPopup());
                                multiplexer.removeProcessor(0);
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
    }

    private void handlePlayerClickWhenPlayWithAI(int screenX, int screenY) {
        int boardX = (int) Math.floor((screenX - centerX) / chessImage.getSpotSize());
        int boardY = (int) Math.floor((Gdx.graphics.getHeight() - screenY - centerY) / chessImage.getSpotSize());
        if(setting.isRotate()) {
            boardX = 7 - boardX;
            boardY = 7 - boardY;
        }

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
                            scrollPane.addValue(move.makeRealMove(board, hashing, chessGameHistoryManager.getHistory(), chessGameManager), bitmapFont, manager, chessGameHistoryManager, chessImage);
                            selectedSpot = null;
                            chessGameManager.switchPlayer(board);
                            if(board.isCheckmate(chessGameManager.getCurrentPlayer().isWhite())) {
                                EndGamePopup popup = new EndGamePopup(bitmapFont, manager.getBundle("vi"), manager, !chessGameManager.getCurrentPlayer().isWhite());
                                stage.addActor(popup.getPopup());
                                multiplexer.removeProcessor(0);
                            }
                            Thread thread = new Thread(() -> {
                                board.clearColor();
                                if(chessGameManager.getCurrentPlayer() instanceof ChessAIPlayer) {
                                    Move aiMove = ((ChessAIPlayer) chessGameManager.getCurrentPlayer()).findBestMove(board, chessGameHistoryManager.getHistory().getNewestFEN());
                                    board.handleMoveColorAndSound(board.getSpot(aiMove.getStart().getX(), aiMove.getStart().getY()), board.getSpot(aiMove.getEnd().getX(), aiMove.getEnd().getY()), chessSound, chessGameManager);
                                    String text = aiMove.makeAIMove(board, hashing, chessGameHistoryManager.getHistory(), chessGameManager);
                                    Gdx.app.postRunnable(() -> scrollPane.addValue(text, bitmapFont, manager, chessGameHistoryManager, chessImage));
                                    chessGameManager.switchPlayer(board);
                                }

                            });
                            thread.start();
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
    }

    private void handlePlayerClickWhenPlayOnline(int screenX, int screenY) {
        int boardX = (int) Math.floor((screenX - centerX) / chessImage.getSpotSize());
        int boardY = (int) Math.floor((Gdx.graphics.getHeight() - screenY - centerY) / chessImage.getSpotSize());
        if(setting.isRotate()) {
            boardX = 7 - boardX;
            boardY = 7 - boardY;
        }
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
                            main.socketClient.makeMove(selectedSpot.getX() + "" + selectedSpot.getY(), secondSpot.getX() + "" + secondSpot.getY());
                            scrollPane.addValue(move.makeRealMove(board, hashing, chessGameHistoryManager.getHistory(), chessGameManager), bitmapFont, manager, chessGameHistoryManager, chessImage);
                            selectedSpot = null;
                            chessGameManager.switchPlayer(board);
                            if(board.isCheckmate(chessGameManager.getCurrentPlayer().isWhite())) {
                                chessSound.playGameEndSound();
                                EndGamePopup popup = new EndGamePopup(bitmapFont, manager.getBundle("vi"), manager, !chessGameManager.getCurrentPlayer().isWhite());
                                stage.addActor(popup.getPopup());
                                multiplexer.removeProcessor(0);
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
        shapeRenderer.dispose();
    }
}
