package com.huy.game.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.huy.game.Main;
import com.huy.game.chess.ai.ChessAI;
import com.huy.game.chess.core.Board;
import com.huy.game.chess.core.BoardSetting;
import com.huy.game.chess.core.GameHistory;
import com.huy.game.chess.core.Move;
import com.huy.game.chess.core.Pawn;
import com.huy.game.chess.core.Spot;
import com.huy.game.chess.core.ZobristHashing;
import com.huy.game.chess.manager.ChessAIPlayer;
import com.huy.game.chess.manager.ChessGameManager;
import com.huy.game.chess.manager.ChessOnlinePlayer;
import com.huy.game.chess.ui.BottomAppBar;
import com.huy.game.chess.ui.ChessImage;
import com.huy.game.chess.ui.ChessSound;
import com.huy.game.chess.ui.Colors;
import com.huy.game.chess.ui.NotationHistoryScrollPane;
import com.huy.game.chess.ui.OptionsPopup;
import com.huy.game.chess.ui.PlayerInfo;
import com.huy.game.chess.ui.TopAppBar;

public class ChessScreen extends InputAdapter implements Screen {
    private final Main main;
    private Stage stage;
    private BoardSetting setting;

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
    private OptionsPopup optionsPopup;

    private ChessSound chessSound;
    private GameHistory gameHistory;
    private ZobristHashing hashing;

    public ChessScreen(Main main) {
        this.main = main;
    }

    @Override
    public void show() {
        gameHistory = new GameHistory();
        setting = new BoardSetting();
        chessImage = new ChessImage();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/Montserrat-SemiBold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.,!?\"'()[]:;+-*/=%éèàùâêîôûçđăĩũơưÁÉÈÀÙÂÊÎÔÛÇĐĂĨŨƠƯ" +
            "àáảãạăắằẵẳặâầấậẩẫđèéẻẽẹêềếểễệìíỉĩịòóỏõọôồốổỗộơờớởỡợùúủũụưừứửữựỳýỷỹỵ";
        parameter.characters += "ăâêôươ";
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.minFilter = Texture.TextureFilter.Linear;
        bitmapFont = generator.generateFont(parameter);
        generator.dispose();
        stage = new Stage();
        chessGameManager = new ChessGameManager(main.getMode());
        PlayerInfo player1Info = new PlayerInfo("Tùy chọn", chessGameManager.getPlayer1().getMap(), chessImage, chessImage.getbBishop(), true, true, bitmapFont, chessGameManager.getTimeList());
        PlayerInfo player2Info = new PlayerInfo("2", chessGameManager.getPlayer2().getMap(), chessImage, chessImage.getbQueen(), false, false, bitmapFont, chessGameManager.getTimeList());
        chessAI = new ChessAI();
        chessSound = new ChessSound();
        board = new Board();
        TopAppBar appBar = new TopAppBar(chessImage, bitmapFont, main);
        scrollPane = new NotationHistoryScrollPane();
        optionsPopup = new OptionsPopup(setting ,bitmapFont);
        BottomAppBar bottomAppBar = new BottomAppBar(chessImage, bitmapFont, optionsPopup.getOptionsPopup());
        selectedSpot = null;
        stage.addActor(appBar.getStack());
        stage.addActor(scrollPane.getScrollPane());
        stage.addActor(player1Info.getInfo());
        stage.addActor(player2Info.getInfo());
        stage.addActor(bottomAppBar.getStack());
        stage.addActor(optionsPopup.getOptionsPopup());

        centerX = (Gdx.graphics.getWidth() - chessImage.getScaledBoardWidth()) / 2;
        centerY = (Gdx.graphics.getHeight() - chessImage.getScaledBoardHeight()) / 2;

        board.resetBoard(chessImage);
        hashing = new ZobristHashing(board.getSpots());
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
        if(setting.isRotate()) {
            board.renderRotateBoard(batch, chessImage.getSpotSize(), chessImage.getScaledPieceSize(), centerX, centerY, chessImage);
        }else {
            board.renderBoard(batch, chessImage.getSpotSize(), chessImage.getScaledPieceSize(), centerX, centerY, chessImage);
        }
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
            handleClick(screenX, screenY);
        }
    }

    private void handleClick(int screenX, int screenY) {
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
                            scrollPane.addValue(move.makeRealMove(board, hashing, gameHistory), bitmapFont);
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
    }

    private void handlePlayerClickWhenPlayWithAI(int screenX, int screenY) {
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
                            scrollPane.addValue(move.makeRealMove(board, hashing, gameHistory), bitmapFont);
                            selectedSpot = null;
                            chessGameManager.switchPlayer(board);
                            if(board.isCheckmate(chessGameManager.getCurrentPlayer().isWhite())) {
                                chessSound.playGameEndSound();
                                board.setEnd();
                            }
                            Thread thread = new Thread(() -> {
                                board.clearColor();
                                Move aiMove = chessAI.findBestMove(board, chessGameManager.getCurrentPlayer().isWhite());
                                board.handleMoveColorAndSound(board.getSpot(aiMove.getStart().getX(), aiMove.getStart().getY()), board.getSpot(aiMove.getEnd().getX(), aiMove.getEnd().getY()), chessSound, chessGameManager);
                                String text = aiMove.makeAIMove(board);
                                Gdx.app.postRunnable(() -> scrollPane.addValue(text, bitmapFont));
                                chessGameManager.switchPlayer(board);
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

//    @Override
//    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//
//        if(chessGameManager.getCurrentPlayer().isWhite()) {
//            int boardX = (int) Math.floor((screenX - centerX) / chessImage.getSpotSize());
//            int boardY = (int) Math.floor((Gdx.graphics.getHeight() - screenY - centerY) / chessImage.getSpotSize());
//
//            if (board.isWithinBoard(boardX, boardY)) {
//                if(board.isPromoting()) {
//                    board.handlePawnPromotion(boardX, boardY, chessImage, chessSound, chessGameManager);
//                }else {
//                    if (selectedSpot == null) {
//                        selectedSpot = board.getSpot(boardY, boardX);
//                        selectedSpot.setShowColor(true);
//                        if(selectedSpot.getPiece() instanceof Pawn) {
//                            ((Pawn) selectedSpot.getPiece()).setTurn(chessGameManager.getCurrentTurn());
//                        }
//                        if(selectedSpot.getPiece() == null || selectedSpot.getPiece().isWhite() != chessGameManager.getCurrentPlayer().isWhite()) {
//                            if(!selectedSpot.isIdentificationColor()) {
//                                selectedSpot.setShowColor(false);
//                            }
//                            selectedSpot = null;
//                        }else {
//                            selectedSpot.getPiece().calculateForPoint(board, selectedSpot);
//                        }
//                    } else {
//                        Spot secondSpot = board.getSpot(boardY, boardX);
//                        Move move = new Move(selectedSpot, secondSpot);
//                        Board testBoard = board.cloneBoard();
//                        boolean canMove = selectedSpot.getPiece().canMove(board, testBoard.getSpots(),selectedSpot, secondSpot);
//                        if(canMove && selectedSpot.getPiece().isWhite() == chessGameManager.getCurrentPlayer().isWhite()) {
//                            if(selectedSpot.getPiece() instanceof Pawn ) {
//                                if((selectedSpot.getX() == 6 && selectedSpot.getPiece().isWhite()) || (selectedSpot.getX() == 1 && !selectedSpot.getPiece().isWhite())) {
//                                    board.setPromoting(true);
//                                    board.setPromotingSpot(secondSpot);
//                                }
//                            }
//                            board.clearColorAndPoint();
//                            move.makeMove(testBoard);
//                            if(testBoard.isKingSafe(chessGameManager.getCurrentPlayer().isWhite())) {
//                                board.handleMoveColorAndSound(selectedSpot, secondSpot, chessSound, chessGameManager);
//                                scrollPane.addValue(move.makeRealMove(board, hashing, gameHistory), bitmapFont);
//                                selectedSpot = null;
//                                chessGameManager.switchPlayer(board);
//                                if(board.isCheckmate(chessGameManager.getCurrentPlayer().isWhite())) {
//                                    chessSound.playGameEndSound();
//                                    board.setEnd();
//                                }
//                            }else {
//                                board.warnIllegalMove(selectedSpot.getPiece().isWhite());
//                                selectedSpot.setShowColor(true);
//                                chessSound.playIllegalSound();
//                            }
//                        }else {
//                            if(secondSpot.getPiece() != null) {
//                                selectedSpot.setShowColor(false);
//                                selectedSpot = secondSpot;
//                                board.clearGuidePoint();
//                                selectedSpot.setShowColor(true);
//                                selectedSpot.getPiece().calculateForPoint(board, selectedSpot);
//                            }else {
//                                selectedSpot = null;
//                                board.clearGuidePoint();
//                            }
//                        }
//                    }
//                }
//            }
//        }else {
//            Thread thread = new Thread(() -> {
//                board.clearColor();
//                Move move = chessAI.findBestMove(board, false);
//                board.handleMoveColorAndSound(board.getSpot(move.getStart().getX(), move.getStart().getY()), board.getSpot(move.getEnd().getX(), move.getEnd().getY()), chessSound, chessGameManager);
//                String text = move.makeAIMove(board);
//                Gdx.app.postRunnable(() -> scrollPane.addValue(text, bitmapFont));
//                chessGameManager.switchPlayer(board);
//            });
//            thread.start();
//        }
//
//        return super.touchDown(screenX, screenY, pointer, button);
//    }

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
