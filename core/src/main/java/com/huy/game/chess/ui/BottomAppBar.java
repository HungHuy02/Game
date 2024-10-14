package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Scaling;
import com.huy.game.chess.manager.ChessImage;

public class BottomAppBar {

    private Stack stack;

    public BottomAppBar() {}

    public BottomAppBar(ChessImage chessImage, BitmapFont font, Window option, Window check,Stage stage, I18NBundle bundle) {
        setStack(chessImage, font, option, check, stage, bundle);
    }

    public Stack getStack() {
        return stack;
    }

    private void setStack(ChessImage chessImage, BitmapFont font, Window option, Window check,Stage stage, I18NBundle bundle) {
        stack = new Stack();
        stack.setSize(Gdx.graphics.getWidth(), 150f);
        stack.setPosition(0, 0);
        stack.add(backgroundContainer(chessImage));
        stack.add(horizontalGroup(chessImage, font, option, check ,stage, bundle));
    }

    public void setStack(ChessImage chessImage,BitmapFont font, I18NBundle bundle) {
        stack = new Stack();
        stack.setSize(Gdx.graphics.getWidth(), 150f);
        stack.setPosition(0, 0);
        stack.add(backgroundContainer(chessImage));
        stack.add(horizontalGroupForChessMatchScreen(chessImage, font, bundle));
    }

    private HorizontalGroup horizontalGroupForChessMatchScreen(ChessImage chessImage, BitmapFont font, I18NBundle bundle) {
        Image image = chessImage.getCancer();
        Container<Image> container = new Container<>(image);
        container.prefWidth(Gdx.graphics.getWidth() / 4);
        image.setScaling(Scaling.fit);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        Label label = new Label(bundle.get("cancel"), labelStyle);
        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.addActor(container);
        verticalGroup.addActor(label);
        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(verticalGroup);
        horizontalGroup.align(Align.center);
        return horizontalGroup;
    }

    private Container<Image> backgroundContainer(ChessImage chessImage) {
        Container<Image> backgroundContainer = new Container<>(chessImage.getAppbarBackground());
        backgroundContainer.prefHeight(150f);
        return backgroundContainer;
    }

     private HorizontalGroup horizontalGroup(ChessImage chessImage, BitmapFont font, Window option, Window check,Stage stage, I18NBundle bundle) {
         Label.LabelStyle labelStyle = new Label.LabelStyle();
         labelStyle.font = font;
         HorizontalGroup horizontalGroup = new HorizontalGroup();
         horizontalGroup.addActor(optionsButton(chessImage , chessImage.getOptions(), bundle.get("options"), labelStyle, option, stage));
         horizontalGroup.addActor(newButton(chessImage ,chessImage.getPlus(), bundle.get("new"), labelStyle, check, stage));
         horizontalGroup.addActor(verticalGroup(chessImage.getBack(), bundle.get("back"), labelStyle, option, stage));
         horizontalGroup.addActor(verticalGroup(chessImage.getForwards(), bundle.get("forwards"), labelStyle, option, stage));
         return horizontalGroup;
     }

     private VerticalGroup verticalGroup(Image image, String name, Label.LabelStyle labelStyle, Window window, Stage stage) {
         VerticalGroup verticalGroup = new VerticalGroup();
         verticalGroup.addActor(image(image, window, stage));
         verticalGroup.addActor(name(name, labelStyle));
         return verticalGroup;
     }

    private VerticalGroup optionsButton(ChessImage chessImage ,Image image, String name, Label.LabelStyle labelStyle, Window window, Stage stage) {
        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.addActor(image(image, window, stage));
        verticalGroup.addActor(name(name, labelStyle));
        verticalGroup.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Image overlay = chessImage.getOverlay();
                overlay.setFillParent(true);
                overlay.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        window.remove();
                        overlay.remove();
                    }
                });
                stage.addActor(overlay);
                stage.addActor(window);
            }
        });
        return verticalGroup;
    }

    private VerticalGroup newButton(ChessImage chessImage ,Image image, String name, Label.LabelStyle labelStyle, Window window, Stage stage) {
        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.addActor(image(image, window, stage));
        verticalGroup.addActor(name(name, labelStyle));
        verticalGroup.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Image overlay = chessImage.getOverlay();
                overlay.setFillParent(true);
                overlay.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        window.remove();
                        overlay.remove();
                    }
                });
                stage.addActor(overlay);
                stage.addActor(window);
            }
        });
        return verticalGroup;
    }

     private Container<Image> image(Image image, Window window, Stage stage) {
         Container<Image> container = new Container<>(image);
         container.prefWidth(Gdx.graphics.getWidth() / 4);
         image.setScaling(Scaling.fit);
         return  container;
     }

     private Label name(String name, Label.LabelStyle labelStyle) {
         Label label = new Label(name, labelStyle);
         return label;
     }

}
