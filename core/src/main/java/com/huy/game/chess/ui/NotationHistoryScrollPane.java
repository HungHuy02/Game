package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.huy.game.chess.manager.ChessGameAssesManager;

public class NotationHistoryScrollPane {

    private ScrollPane scrollPane;
    private HorizontalGroup horizontalGroup;
    private int index = -1;

    public NotationHistoryScrollPane() {
        setScrollPane();
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    private void setScrollPane() {
        horizontalGroup = new HorizontalGroup();
        scrollPane = new ScrollPane(horizontalGroup);
        scrollPane.setScrollingDisabled(false, true);
        scrollPane.setOverscroll(true, true);
        scrollPane.setSize(Gdx.graphics.getWidth(), 50);
        scrollPane.setPosition(0, Gdx.graphics.getHeight() - 150);
    }

    public void addValue(String value, BitmapFont font, ChessGameAssesManager manager) {
        if(index >= 1) {
            Actor child = horizontalGroup.getChild(index);
            if(child instanceof TextButton) {
                ((TextButton) child).getStyle().up = null;
            }
        }
        if(index % 3 == 2 || index == -1) {
            Label.LabelStyle style = new Label.LabelStyle();
            style.font = font;
            style.fontColor = Colors.GREY_600;
            Label label = new Label((index / 2 + 1) + ".", style);
            Container<Label> labelContainer = new Container<>(label);
            labelContainer.padLeft(16);
            labelContainer.padRight(16);
            horizontalGroup.addActor(labelContainer);
            index++;
        }
        Skin skin = manager.getSkin();
        index++;
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.up = skin.getDrawable("button-normal");
        TextButton button = new TextButton(value, style);
        button.padLeft(20);
        button.padRight(20);
        button.padTop(10);
        button.padBottom(10);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(index != -1) {
                    Actor child = horizontalGroup.getChild(index);
                    if(child instanceof TextButton) {
                        ((TextButton) child).getStyle().up = null;
                    }
                }
                index = horizontalGroup.getChildren().indexOf(button, true);
                Actor child = horizontalGroup.getChild(index);
                if(child instanceof  TextButton) {
                    ((TextButton) child).getStyle().up = skin.getDrawable("button-normal");
                }
            }
        });
        horizontalGroup.addActor(button);
        scrollPane.layout();
        scrollPane.setScrollX(horizontalGroup.getWidth());
    }


}
