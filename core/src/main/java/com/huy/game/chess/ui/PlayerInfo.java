package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

import java.util.Map;

public class PlayerInfo {

    private HorizontalGroup info;

    public PlayerInfo(String name, Map<String, Integer> map, ChessImage chessImage, Texture image, boolean isWhite, boolean isPlayer1, BitmapFont font, Map<String, Integer> timeList) {
        horizontalGroup(name, map, chessImage, image, isWhite, isPlayer1, font, timeList);
    }

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

    private Image image(Texture image, ChessImage chessImage) {
        Image avatar = new Image(image);
        float scale = chessImage.getScaledPieceSize() / image.getWidth();
        avatar.setOrigin(Align.center);
        avatar.setScale(scale);
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
