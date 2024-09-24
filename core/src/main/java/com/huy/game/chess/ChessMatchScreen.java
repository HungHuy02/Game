package com.huy.game.chess;

import com.badlogic.gdx.Gdx;
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
import com.huy.game.chess.manager.ChessImage;
import com.huy.game.chess.ui.BottomAppBar;
import com.huy.game.chess.ui.Colors;
import com.huy.game.chess.ui.PlayerInfo;
import com.huy.game.chess.ui.TopAppBar;


public class ChessMatchScreen implements Screen {

    private Stage stage;
    private final Main main;
    private Animation<Texture> animation;
    private Container<Image> image;
    private float stateTime;
    private Texture opacityBoard;
    private float centerX;
    private float centerY;
    private SpriteBatch batch;
    private ChessImage chessImage;
    private BitmapFont font;

    public ChessMatchScreen(Main main) {
        this.main = main;
    }

    @Override
    public void show() {
        main.manager.loadForChessMathScreen();
        batch = new SpriteBatch();
        opacityBoard = main.manager.getOpacityBoard();
        I18NBundle bundle = main.manager.getBundle("vi");
        chessImage = new ChessImage(main.manager);
        font = main.manager.getFont();
        stage = new Stage();
        PlayerInfo info = new PlayerInfo();
        animation = info.animation(chessImage);
        image = info.imageForChessMatchScreen(chessImage.getbBishop(), chessImage);
        info.horizontalGroupForChessMatchScreen(bundle.get("search"), image, chessImage, font);
        stage.act(Gdx.graphics.getDeltaTime());
        TopAppBar topAppBar = new TopAppBar(chessImage, font, main, bundle);
        BottomAppBar bottomAppBar = new BottomAppBar();
        bottomAppBar.setStack(chessImage, font, bundle);
        centerX = (Gdx.graphics.getWidth() - chessImage.getScaledBoardWidth()) / 2;
        centerY = (Gdx.graphics.getHeight() - chessImage.getScaledBoardHeight()) / 2;
        stateTime = 0f;
        stage.addActor(topAppBar.getStack());
        stage.addActor(info.getInfo());
        stage.addActor(bottomAppBar.getStack());
        chessImage.dispose();
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
        chessImage.dispose();
        font.dispose();
    }
}
