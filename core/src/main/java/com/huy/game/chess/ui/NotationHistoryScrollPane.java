package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.StringBuilder;
import com.huy.game.chess.core.GameHistory;
import com.huy.game.chess.manager.ChessGameAssesManager;
import com.huy.game.chess.manager.ChessGameHistoryManager;
import com.huy.game.chess.manager.ChessImage;

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

    public void addValue(String value, BitmapFont font, ChessGameAssesManager manager, ChessGameHistoryManager historyManager, ChessImage chessImage) {
        handleClearColor();
        addSequenceNumber(font, historyManager.getHistory());
        Skin skin = manager.getSkin();
        index++;
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.up = skin.getDrawable("button-normal");
        historyManager.getHistory().appendString(value);
        TextButton button = new TextButton(value, style);
        button.padLeft(20);
        button.padRight(20);
        button.padTop(10);
        button.padBottom(10);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                handleClearColor();
                historyManager.setRePlay(true);
                index = horizontalGroup.getChildren().indexOf(button, true);
                historyManager.setBoard(changeToTrueIndex(index), chessImage);
                handleNewColor(manager);
            }
        });
        horizontalGroup.addActor(button);
        scrollPane.layout();
        scrollPane.setScrollX(horizontalGroup.getWidth());
    }

    private void addSequenceNumber(BitmapFont font, GameHistory history) {
        if(index % 3 == 2 || index == -1) {
            index++;
            Label.LabelStyle style = new Label.LabelStyle();
            style.font = font;
            style.fontColor = Colors.GREY_600;
            StringBuilder builder = new StringBuilder();
            builder.append(index / 3 + 1);
            builder.append('.');
            String text = builder.toString();
            history.appendString(text);
            Label label = new Label(text, style);
            Container<Label> labelContainer = new Container<>(label);
            labelContainer.padLeft(16);
            labelContainer.padRight(16);
            horizontalGroup.addActor(labelContainer);
        }
    }

    private void handleClearColor() {
        if(index != -1) {
            Actor child = horizontalGroup.getChild(index);
            if(child instanceof TextButton textButton) {
                textButton.getStyle().up = null;
            }
        }
    }

    private TextButton handleNewColor(ChessGameAssesManager manager) {
        Skin skin = manager.getSkin();
        Actor child = horizontalGroup.getChild(index);
        if(child instanceof  TextButton button) {
            button.getStyle().up = skin.getDrawable("button-normal");
            return button;
        }else {
            throw new RuntimeException("Notation Scroll Pane error");
        }
    }

    private void handleScroll(TextButton button) {
        scrollPane.scrollTo(button.getX(), button.getY(), button.getWidth(), button.getHeight());
    }

    public void handleChangeFocus(int trueIndex, ChessGameAssesManager manager) {
        handleClearColor();
        index = changeToThisIndex(trueIndex);
        handleScroll(handleNewColor(manager));
    }

    private int changeToTrueIndex(int index) {
        return index - index / 3 - 1;
    }

    private int changeToThisIndex(int trueIndex) {
        return trueIndex + 1 + trueIndex / 2;
    }
}
