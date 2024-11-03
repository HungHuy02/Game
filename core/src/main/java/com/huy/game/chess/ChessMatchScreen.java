package com.huy.game.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.huy.game.Main;
import com.huy.game.chess.core.BoardSetting;
import com.huy.game.chess.events.ChessGameOnlineEvent;
import com.huy.game.chess.manager.ChessGameAssesManager;
import com.huy.game.chess.manager.ChessImage;
import com.huy.game.chess.manager.ChessSound;
import com.huy.game.chess.manager.OpponentPlayer;
import com.huy.game.chess.manager.Player;
import com.huy.game.chess.ui.BottomAppBar;
import com.huy.game.chess.ui.Colors;
import com.huy.game.chess.ui.PlayerInfo;
import com.huy.game.chess.ui.TopAppBar;

import javax.inject.Inject;


public class ChessMatchScreen extends InputAdapter implements Screen {

    private final Main main;
    private Stage stage;

    @Inject
    ChessGameAssesManager manager;
    @Inject
    SpriteBatch batch;
    @Inject
    ChessImage chessImage;
    @Inject
    BitmapFont font;
    @Inject
    I18NBundle bundle;
    @Inject
    BoardSetting setting;
    @Inject
    ChessSound chessSound;

    private Animation<Texture> animation;
    private Container<Image> image;
    private float stateTime;
    private Texture opacityBoard;
    private float centerX;
    private float centerY;

    public ChessMatchScreen(Main main) {
        this.main = main;
    }

    @Override
    public void show() {
        stage = new Stage();
        manager.loadForChessMathScreen();
        opacityBoard = manager.getOpacityBoard();
        PlayerInfo info = new PlayerInfo();
        animation = info.animation(chessImage);
        image = info.imageForChessMatchScreen(chessImage.getbBishop(), chessImage);
        info.horizontalGroupForChessMatchScreen(bundle.get("search"), image, chessImage, font);
        stage.act(Gdx.graphics.getDeltaTime());
        TopAppBar topAppBar = new TopAppBar(chessImage, font, main, bundle);
        BottomAppBar bottomAppBar = new BottomAppBar();
        bottomAppBar.setStack( main, chessImage, font, bundle);
        centerX = (Gdx.graphics.getWidth() - chessImage.getScaledBoardWidth()) / 2;
        centerY = (Gdx.graphics.getHeight() - chessImage.getScaledBoardHeight()) / 2;
        stateTime = 0f;
        stage.addActor(topAppBar.getStack());
        stage.addActor(info.getInfo());
        stage.addActor(bottomAppBar.getStack());
        Gdx.input.setInputProcessor(stage);

        ChessGameOnlineEvent.getInstance().setMatchListener((name, isWhite, imageUrl) -> {
            OpponentPlayer.getInstance().setData(name, isWhite, imageUrl);
            if(isWhite) {
                setting.setRotate(true);
            }
            Player.getInstance().setWhite(!isWhite);
            Gdx.app.postRunnable(main::toChessScreen);
        });
        main.socketClient.requestToPlayGame(Player.getInstance().getName(), Player.getInstance().getImageUrl(), Player.getInstance().getElo());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Colors.GREY_900_94);
        batch.begin();
        batch.draw(opacityBoard, centerX, centerY, chessImage.getScaledBoardWidth(), chessImage.getScaledBoardHeight());
        batch.end();
        stateTime += Gdx.graphics.getDeltaTime();
        Texture texture = animation.getKeyFrame(stateTime, true);
        image.clear();
        image.setActor(new Image(texture));
        stage.draw();
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
        stage.dispose();
        batch.dispose();
    }
}
