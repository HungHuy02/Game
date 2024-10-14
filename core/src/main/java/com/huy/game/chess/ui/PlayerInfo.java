package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.huy.game.chess.manager.ChessImage;

import java.util.Map;

public class PlayerInfo {

    private HorizontalGroup info;

    public PlayerInfo(String name, Map<String, Integer> map, ChessImage chessImage, Texture image, boolean isWhite, boolean isPlayer1, BitmapFont font, Map<String, Integer> timeList) {
        horizontalGroup(name, map, chessImage, image, isWhite, isPlayer1, font, timeList);
    }

    public PlayerInfo() {}

    public HorizontalGroup getInfo() {
        return info;
    }

    private void horizontalGroup(String name, Map<String, Integer> map, ChessImage chessImage, Texture image, boolean isWhite, boolean isPlayer1, BitmapFont font, Map<String, Integer> timeList) {
        info = new HorizontalGroup();
        info.setSize(Gdx.graphics.getWidth() ,chessImage.getScaledPieceSize());
        info.padRight(16);
        if(isPlayer1) {
            info.setPosition(0, (Gdx.graphics.getHeight() / 5) - chessImage.getScaledPieceSize());
        }else {
            info.setPosition(0, Gdx.graphics.getHeight() * 4 / 5);
        }
        info.addActor(image(image, chessImage));
        info.addActor(verticalGroup(name, map, chessImage,isWhite, font));
        info.addActor(new Timer(timeList, font, isPlayer1, chessImage));
    }

    public void horizontalGroupForChessMatchScreen(String name,Container<Image> image, ChessImage chessImage, BitmapFont font) {
        info = new HorizontalGroup();
        info.setSize(Gdx.graphics.getWidth() ,chessImage.getScaledPieceSize());
        info.padRight(16);
        info.setPosition(0, Gdx.graphics.getHeight() * 4 / 5);
        info.addActor(image);
        info.addActor(name(name, font));
    }

    public Animation<Texture> animation(ChessImage chessImage) {
        Texture[] textures = new Texture[5];
        textures[0] = chessImage.getbBishop();
        textures[1] = chessImage.getbKnight();
        textures[2] = chessImage.getbPawn();
        textures[3] = chessImage.getwBishop();
        textures[4] = chessImage.getwKing();
        Animation<Texture> animation = new Animation<>(0.5f, textures);
        return animation;
    }

    public Container<Image> imageForChessMatchScreen(Texture image, ChessImage chessImage) {
        Container<Image> avatar = new Container<>(new Image(image));
        float scale = chessImage.getScaledPieceSize() / image.getWidth();
        avatar.setOrigin(Align.center);
        avatar.setScale(scale);
        return avatar;
    }

    private Image image(Texture image, ChessImage chessImage) {
        Image avatar = new Image(image);
        float scale = chessImage.getScaledPieceSize() / image.getWidth();
        avatar.setOrigin(Align.center);
        avatar.setScale(scale);
        Pixmap.downloadFromUrl("https://picsum.photos/seed/picsum/200/300", new Pixmap.DownloadPixmapResponseListener() {
            @Override
            public void downloadComplete(Pixmap pixmap) {
                Texture texture = new Texture(pixmap);
                avatar.setDrawable(new TextureRegionDrawable(texture));
            }

            @Override
            public void downloadFailed(Throwable t) {

            }
        });
        return avatar;
    }

    private VerticalGroup verticalGroup(String name, Map<String, Integer> map, ChessImage chessImage,boolean isWhite, BitmapFont font) {
        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.space(50f);
        verticalGroup.addActor(name(name, font));
        verticalGroup.addActor(capturedPiecesActor(map, chessImage, isWhite, font));
        return verticalGroup;
    }

    private Label name(String name, BitmapFont font) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        Label label = new Label(name, labelStyle);
        label.getStyle().font = font;
        return label;
    }

    private CapturedPiecesActor capturedPiecesActor(Map<String, Integer> map , ChessImage chessImage, boolean isWhite, BitmapFont font) {
        return new CapturedPiecesActor(map, chessImage, isWhite, font);
    }
}
