package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.StringBuilder;
import com.huy.game.Main;
import com.huy.game.chess.ChessScreen;
import com.huy.game.chess.enums.GameResult;
import com.huy.game.chess.manager.ChessGameAssesManager;

public class EndGamePopup {

    private Window endGamePopup;
    private Label newScoreLabel;

    public EndGamePopup(BitmapFont font, I18NBundle bundle, ChessGameAssesManager manager, GameResult result, Main main, ChessScreen screen) {
        setEndGamePopup(font, bundle, manager, result, main, screen);
    }

    public Window getPopup() {
        return endGamePopup;
    }

    private void setEndGamePopup(BitmapFont font, I18NBundle bundle, ChessGameAssesManager manager, GameResult result, Main main, ChessScreen screen) {
        Skin skin = manager.getSkin();
        TextButton.TextButtonStyle style = skin.get(TextButton.TextButtonStyle.class);
        style.font = font;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        endGamePopup = new Window("", skin);
        endGamePopup.setSize(Gdx.graphics.getWidth() - 100, 496);
        endGamePopup.setPosition((Gdx.graphics.getWidth() - endGamePopup.getWidth()) / 2,
            (Gdx.graphics.getHeight() - endGamePopup.getHeight()) / 2);
        endGamePopup.add(stack(manager, labelStyle,bundle)).fillX().expandX().height(100).padTop(32);
        endGamePopup.row();
        endGamePopup.add(winText(labelStyle, bundle, result)).center().height(200);
        endGamePopup.row();
        newScore(labelStyle);
        endGamePopup.add(newScoreLabel).padBottom(32);
        endGamePopup.row();
        Table buttonTable = new Table();
        buttonTable.add(rePlayButton(style, bundle, screen)).expandX().fillX().padLeft(32).padRight(16).height(100);
        buttonTable.add(newGameButton(style, bundle, main)).expandX().fillX().padLeft(16).padRight(32).height(100);
        endGamePopup.add(buttonTable).expandX().fillX().height(100).padBottom(32);
    }

    private Stack stack(ChessGameAssesManager chessGameAssesManager, Label.LabelStyle style, I18NBundle bundle) {
        Image image = new Image(chessGameAssesManager.getClose());
        Container<Image> container = new Container<>(image);
        container.padLeft(32);
        container.setFillParent(true);
        container.align(Align.left);
        container.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                endGamePopup.remove();
            }
        });
        Label label = new Label(bundle.get("gameEnd"), style);
        label.setFontScale(1.5f);
        label.setFillParent(true);
        label.setAlignment(Align.center);
        Stack stack = new Stack();
        stack.addActor(label);
        stack.addActor(container);
        return stack;
    }

    private VerticalGroup winText(Label.LabelStyle style, I18NBundle bundle, GameResult result) {
        VerticalGroup verticalGroup = new VerticalGroup();
        String title;
        String reason = switch (result) {
            case WHITE_WIN -> {
                title = bundle.get("whiteWin");
                yield bundle.get("checkmate");
            }
            case BLACK_WIN -> {
                title = bundle.get("blackWin");
                yield bundle.get("checkmate");
            }
            case DRAW_STALEMATE -> {
                title = bundle.get("draw");
                yield bundle.get("stalemate");
            }
            case DRAW_THREEFOLD -> {
                title = bundle.get("draw");
                yield bundle.get("threefold");
            }
            case DRAW_FIFTY_MOVE -> {
                title = bundle.get("draw");
                yield bundle.get("fifty-move");
            }
            case DRAW_INSUFFICIENT -> {
                title = bundle.get("draw");
                yield bundle.get("insufficient");
            }
            case DRAW_AGREEMENT -> {
                title = bundle.get("draw");
                yield bundle.get("agreement");
            }
        };
        Label winText = new Label(title, style);
        Label reasonText = new Label(reason, style);
        verticalGroup.addActor(winText);
        verticalGroup.addActor(reasonText);
        return verticalGroup;
    }

    private void newScore(Label.LabelStyle style) {
        newScoreLabel = new Label("", style);
    }

    private TextButton newGameButton(TextButton.TextButtonStyle style, I18NBundle bundle, Main main) {
        TextButton textButton = new TextButton(bundle.get("newBoard"), style);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                main.backInterface.newGame(main.getMode());
                endGamePopup.remove();
            }
        });
        return textButton;
    }

    private TextButton rePlayButton(TextButton.TextButtonStyle style, I18NBundle bundle, ChessScreen chessScreen) {
        TextButton textButton = new TextButton(bundle.get("rePlay"), style);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                chessScreen.newGame();
                endGamePopup.remove();
            }
        });
        return textButton;
    }

    public void setNewScore(int oldElo, int newElo) {
        StringBuilder builder = new StringBuilder();
        builder.append("Elo: ");
        builder.append(newElo);
        builder.append(" (");
        int change = newElo - oldElo;
        builder.append(change > 0 ? "+" + change : change);
        builder.append(')');
        newScoreLabel.setText(builder.toString());
    }
}
