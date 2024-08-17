package com.huy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen extends InputAdapter implements Screen {

    private SpriteBatch batch;
    private Texture image;
    private float timer;
    private Main main;

    public FirstScreen(Main main) {
        this.main = main;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        image = new Texture("logo.png");
        timer = 0;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        float widthRatio = (float) (screenWidth - screenWidth / 3) / image.getWidth();
        float heightRatio = (float) (screenHeight - screenHeight/ 3) / image.getHeight();
        float scale = Math.min(widthRatio, heightRatio);
        float imageWidth = image.getWidth() * scale;
        float imageHeight = image.getHeight() * scale;
        batch.draw(image, (screenWidth - imageWidth) / 2, (screenHeight - imageHeight) / 2, imageWidth, imageHeight);
        batch.end();
        timer += delta;
        if(timer >= 3f) {
            main.toChessScreen();
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        main.toChessScreen();
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
