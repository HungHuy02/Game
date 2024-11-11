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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.huy.game.chess.enums.TimeType;
import com.huy.game.chess.manager.ChessImage;
import com.huy.game.chess.manager.OpponentPlayer;
import com.huy.game.chess.manager.Player;

import java.util.Map;

public class PlayerInfo {

    private HorizontalGroup info;

    public PlayerInfo(String name, Map<String, Integer> map, ChessImage chessImage, Texture image, boolean isWhite, boolean isPlayer1, BitmapFont font, Map<String, Integer> timeList, TimeType timeType) {
        horizontalGroup(name, map, chessImage, image, isWhite, isPlayer1, font, timeList, timeType);
    }

    public PlayerInfo(String name, Map<String, Integer> map, ChessImage chessImage, Texture image, boolean isWhite, boolean isPlayer1, BitmapFont font) {
        horizontalGroup(name, map, chessImage, image, isWhite, isPlayer1, font, null, TimeType.NO_TIME);
    }

    public PlayerInfo() {}

    public HorizontalGroup getInfo() {
        return info;
    }

    private void horizontalGroup(String name, Map<String, Integer> map, ChessImage chessImage, Texture image, boolean isWhite, boolean isPlayer1, BitmapFont font, Map<String, Integer> timeList, TimeType type) {
        info = new HorizontalGroup();
        info.setSize(Gdx.graphics.getWidth() ,chessImage.getScaledPieceSize());
        info.padRight(16);
        if(isPlayer1) {
            info.setPosition(0, ((float) Gdx.graphics.getHeight() / 5) - chessImage.getScaledPieceSize());
            info.addActor(image(image, chessImage, Player.getInstance().getImageUrl()));
        }else {
            info.setPosition(0, (float) (Gdx.graphics.getHeight() * 4) / 5);
            info.addActor(image(image, chessImage, OpponentPlayer.getInstance().getImageUrl()));
        }
        info.addActor(verticalGroup(name, map, chessImage,isWhite, font));
        if (type != TimeType.NO_TIME) {
            info.addActor(new Timer(timeList, font, isPlayer1, chessImage));
        }
    }

    public void horizontalGroupForChessMatchScreen(String name,Container<Image> image, ChessImage chessImage, BitmapFont font) {
        info = new HorizontalGroup();
        info.setSize(Gdx.graphics.getWidth() ,chessImage.getScaledPieceSize());
        info.padRight(16);
        info.setPosition(0, (float) (Gdx.graphics.getHeight() * 4) / 5);
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
        return new Animation<>(0.5f, textures);
    }

    public Container<Image> imageForChessMatchScreen(Texture image, ChessImage chessImage) {
        Container<Image> avatar = new Container<>(new Image(image));
        float scale = chessImage.getScaledPieceSize() / image.getWidth();
        avatar.setOrigin(Align.center);
        avatar.setScale(scale);
        return avatar;
    }

    private Container<Image> image(Texture image, ChessImage chessImage, String imageUrl) {
        Container<Image> container = new Container<>();
        Image avatar = new Image(image);
        float scale = chessImage.getScaledPieceSize() / image.getWidth();
        avatar.setOrigin(Align.center);
        avatar.setScale(scale);
        if(imageUrl != null && !imageUrl.equals("")) {
            Pixmap.downloadFromUrl( imageUrl, new Pixmap.DownloadPixmapResponseListener() {
                @Override
                public void downloadComplete(Pixmap pixmap) {
                    Texture texture = new Texture(pixmap);
                    avatar.setDrawable(new TextureRegionDrawable(texture));
                    avatar.setScaling(Scaling.contain);
                    container.prefSize(chessImage.getSpotSize(), chessImage.getSpotSize());
                }

                @Override
                public void downloadFailed(Throwable t) {
                    throw new RuntimeException(t.getMessage());
                }
            });
        }
        container.setActor(avatar);
        return container;
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
