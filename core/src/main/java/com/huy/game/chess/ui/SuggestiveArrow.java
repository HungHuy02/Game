package com.huy.game.chess.ui;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SuggestiveArrow {

    public static PolygonSprite createArrow(float height, float angle, float spotSize, float originX, float originY) {
        Texture textureSolid = new Texture(generateSolidColorPixmap());
        float[] vertices = new float[] {
            originX + 10, originY,
            originX + 40, originY,
            originX + 40, originY + height - spotSize / 2,
            originX + 50, originY + height - spotSize / 2,
            originX + 25, originY + height,
            originX, originY + height - spotSize / 2,
            originX + 10, originY + height - spotSize / 2
        };

        PolygonRegion arrowRegion = new PolygonRegion(new TextureRegion(textureSolid), vertices, new short[]{
            0, 1, 2,
            0, 2, 6,
            3, 4, 5
        });

        PolygonSprite arrow = new PolygonSprite(arrowRegion);
        arrow.setOrigin(originX + 25, originY);
        arrow.setRotation(angle);
        return arrow;
    }

    public static PolygonSprite createArrowForKnight(float angle, float spotSize, float originX, float originY) {
        Texture textureSolid = new Texture(generateSolidColorPixmap());
        float[] vertices = new float[] {
            originX, originY,
            originX + 30, originY,
            originX + 30, originY + spotSize + 15,
            originX + 15 - 1.5f * spotSize, originY + spotSize + 15,
            originX + 15 - 1.5f * spotSize, originY + spotSize + 25,
            originX + 15 - 2 * spotSize, originY + spotSize,
            originX + 15 - 1.5f * spotSize, originY + spotSize - 25,
            originX + 15 - 1.5f * spotSize, originY + spotSize - 15,
            originX, originY + spotSize - 15
        };
        PolygonRegion arrowRegion = new PolygonRegion(new TextureRegion(textureSolid), vertices, new short[]{
            0, 1, 8,
            1, 2, 8,
            2, 3, 8,
            4, 5 ,6,
            3, 7, 8
        });
        PolygonSprite arrow = new PolygonSprite(arrowRegion);
        arrow.setOrigin(originX + 15, originY);
        arrow.setRotation(angle);
        return arrow;
    }

    public static PolygonSprite createArrowForKnightReverse( float angle, float spotSize, float originX, float originY) {
        Texture textureSolid = new Texture(generateSolidColorPixmap());
        float[] vertices = new float[] {
            originX + 30, originY,
            originX, originY,
            originX, originY + spotSize + 15,
            originX + 15 + 1.5f * spotSize, originY + spotSize + 15,
            originX + 15 + 1.5f * spotSize, originY + spotSize + 25,
            originX + 15 + 2 * spotSize, originY + spotSize,
            originX + 15 + 1.5f * spotSize, originY + spotSize - 25,
            originX + 15 + 1.5f * spotSize, originY + spotSize - 15,
            originX + 30, originY + spotSize - 15
        };
        PolygonRegion arrowRegion = new PolygonRegion(new TextureRegion(textureSolid), vertices, new short[]{
            0, 1, 8,
            1, 2, 8,
            2, 3, 8,
            4, 5 ,6,
            3, 7, 8
        });
        PolygonSprite arrow = new PolygonSprite(arrowRegion);
        arrow.setOrigin(originX + 15, originY);
        arrow.setRotation(angle);
        return arrow;

    }



    private static Pixmap generateSolidColorPixmap() {
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(Colors.SUGGEST_COLOR);
        pix.fill();
        return pix;
    }
}
