package com.huy.game.chess.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;

public class BottomAppBar {

    private Stack stack;

    public BottomAppBar(ChessImage chessImage, BitmapFont font) {
        setStack(chessImage, font);
    }

    public Stack getStack() {
        return stack;
    }

    private void setStack(ChessImage chessImage, BitmapFont font) {
        stack = new Stack();
        stack.setSize(Gdx.graphics.getWidth(), 150f);
        stack.setPosition(0, 0);
        stack.add(backgroundContainer(chessImage));
        stack.add(horizontalGroup(chessImage, font));
    }

    private Container<Image> backgroundContainer(ChessImage chessImage) {
        Container<Image> backgroundContainer = new Container<>(chessImage.getAppbarBackground());
        backgroundContainer.prefHeight(150f);
        return backgroundContainer;
    }

     private HorizontalGroup horizontalGroup(ChessImage chessImage, BitmapFont font) {
         Label.LabelStyle labelStyle = new Label.LabelStyle();
         labelStyle.font = font;
         HorizontalGroup horizontalGroup = new HorizontalGroup();
         horizontalGroup.addActor(verticalGroup(chessImage.getOptions(), "Tùy chọn", labelStyle));
         horizontalGroup.addActor(verticalGroup(chessImage.getPlus(), "Mới", labelStyle));
         horizontalGroup.addActor(verticalGroup(chessImage.getBack(), "Quay lại", labelStyle));
         horizontalGroup.addActor(verticalGroup(chessImage.getForwards(), "Tiếp", labelStyle));
         return horizontalGroup;
     }

     private VerticalGroup verticalGroup(Image image, String name, Label.LabelStyle labelStyle) {
         VerticalGroup verticalGroup = new VerticalGroup();
         verticalGroup.addActor(image(image));
         verticalGroup.addActor(name(name, labelStyle));
         return verticalGroup;
     }

     private Container<Image> image(Image image) {
         Container<Image> container = new Container<>(image);
         container.prefWidth(Gdx.graphics.getWidth() / 4);
         image.setScaling(Scaling.fit);
         container.addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent event, float x, float y) {
                 super.clicked(event, x, y);
             }
         });
         return  container;
     }

     private Label name(String name, Label.LabelStyle labelStyle) {
         Label label = new Label(name, labelStyle);
         return label;
     }

}
