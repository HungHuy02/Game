package com.huy.game.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import java.util.Map;

public class PlayerInfo {

    private HorizontalGroup info;

    public PlayerInfo(String name, Map<String, Integer> map, ChessImage chessImage, Image image, boolean isWhite, boolean isPlayer1) {
        horizontalGroup(name, map, chessImage, image, isWhite, isPlayer1);
    }

    public HorizontalGroup getInfo() {
        return info;
    }

    private HorizontalGroup horizontalGroup(String name, Map<String, Integer> map, ChessImage chessImage, Image image, boolean isWhite, boolean isPlayer1) {
        info = new HorizontalGroup();
        info.setHeight(chessImage.getScaledPieceSize());
        info.padLeft(16);
        info.padRight(16);
        if(isPlayer1) {
            info.setPosition(0, (Gdx.graphics.getHeight() / 5) - chessImage.getScaledPieceSize());
        }else {
            info.setPosition(0, Gdx.graphics.getHeight() * 4 / 5);
        }
        info.addActor(image(image, chessImage));
        info.addActor(verticalGroup(name, map, chessImage,isWhite));
        return info;
    }

    private Image image(Image image, ChessImage chessImage) {
        image.setSize(chessImage.getScaledPieceSize(), chessImage.getScaledPieceSize());
        return image;
    }

    private VerticalGroup verticalGroup(String name, Map<String, Integer> map, ChessImage chessImage,boolean isWhite) {
        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.addActor(name(name));
        verticalGroup.addActor(capturedPiecesActor(map, chessImage, isWhite));
        return verticalGroup;
    }

    private Label name(String name) {
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        Label label = new Label(name, skin);
        return label;
    }

    private CapturedPiecesActor capturedPiecesActor(Map<String, Integer> map ,ChessImage chessImage, boolean isWhite) {
        return new CapturedPiecesActor(map, chessImage, isWhite);
    }
}
