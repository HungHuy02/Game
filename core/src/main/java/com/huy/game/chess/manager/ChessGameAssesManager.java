package com.huy.game.chess.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class ChessGameAssesManager {

    public final AssetManager manager = new AssetManager();

    public void loadAll() {
        loadImages();
        loadSounds();
        loadFont();
        loadSkin();
        manager.finishLoading();
    }

    public void loadForChessMathScreen() {
        manager.load("chess/images/opacity_board.png", Texture.class);
        manager.load("chess/images/close_24dp.png", Texture.class);
        manager.finishLoading();
    }

    public Texture getOpacityBoard() {
        return manager.get("chess/images/opacity_board.png", Texture.class);
    }

    public Texture getClose() {
        return manager.get("chess/images/close_24dp.png", Texture.class);
    }

    public void loadImages() {
        manager.load("chess/images/chess_board.png", Texture.class);
        manager.load("chess/images/position.png", Texture.class);
        manager.load("chess/images/position_rotate.png", Texture.class);
        manager.load("chess/images/wbishop.png", Texture.class);
        manager.load("chess/images/wking.png", Texture.class);
        manager.load("chess/images/wknight.png", Texture.class);
        manager.load("chess/images/wpawn.png", Texture.class);
        manager.load("chess/images/wqueen.png", Texture.class);
        manager.load("chess/images/wrook.png", Texture.class);

        manager.load("chess/images/bbishop.png", Texture.class);
        manager.load("chess/images/bking.png", Texture.class);
        manager.load("chess/images/bknight.png", Texture.class);
        manager.load("chess/images/bpawn.png", Texture.class);
        manager.load("chess/images/bqueen.png", Texture.class);
        manager.load("chess/images/brook.png", Texture.class);

        manager.load("chess/images/icons8-left-100.png", Texture.class);
        manager.load("chess/images/icons8-bulleted-list-100.png", Texture.class);
        manager.load("chess/images/icons8-plus-100.png", Texture.class);
        manager.load("chess/images/icons8-back-100.png", Texture.class);
        manager.load("chess/images/icons8-forward-100.png", Texture.class);
        manager.load("chess/images/appbar_background.png", Texture.class);
        manager.load("chess/images/timer_20dp.png", Texture.class);
        manager.load("chess/images/overlay.png", Texture.class);
    }

    public Texture getChessBoard() {
        return manager.get("chess/images/chess_board.png", Texture.class);
    }

    public Texture getPositionNotation() {
        return manager.get("chess/images/position.png", Texture.class);
    }

    public Texture getRotatePositionNotion() {
        return manager.get("chess/images/position_rotate.png", Texture.class);
    }

    public Texture getWhiteBishop() {
        return manager.get("chess/images/wbishop.png", Texture.class);
    }

    public Texture getWhiteKing() {
        return manager.get("chess/images/wking.png", Texture.class);
    }

    public Texture getWhiteKnight() {
        return manager.get("chess/images/wknight.png", Texture.class);
    }

    public Texture getWhitePawn() {
        return manager.get("chess/images/wpawn.png", Texture.class);
    }

    public Texture getWhiteQueen() {
        return manager.get("chess/images/wqueen.png", Texture.class);
    }

    public Texture getWhiteRook() {
        return manager.get("chess/images/wrook.png", Texture.class);
    }

    public Texture getBlackBishop() {
        return manager.get("chess/images/bbishop.png", Texture.class);
    }

    public Texture getBlackKing() {
        return manager.get("chess/images/bking.png", Texture.class);
    }

    public Texture getBlackKnight() {
        return manager.get("chess/images/bknight.png", Texture.class);
    }

    public Texture getBlackPawn() {
        return manager.get("chess/images/bpawn.png", Texture.class);
    }

    public Texture getBlackQueen() {
        return manager.get("chess/images/bqueen.png", Texture.class);
    }

    public Texture getBlackRook() {
        return manager.get("chess/images/brook.png", Texture.class);
    }

    public Texture getBackArrow() {
        return manager.get("chess/images/icons8-left-100.png", Texture.class);
    }

    public Texture getOptionsButton() {
        return manager.get("chess/images/icons8-bulleted-list-100.png", Texture.class);
    }

    public Texture getNewButton() {
        return manager.get("chess/images/icons8-plus-100.png", Texture.class);
    }

    public Texture getAppbarBackground() {
        return manager.get("chess/images/appbar_background.png", Texture.class);
    }

    public Texture getTimer() {
        return manager.get("chess/images/timer_20dp.png", Texture.class);
    }

    public Texture getOverlay() {
        return manager.get("chess/images/overlay.png", Texture.class);
    }

    public Texture getBackButton() {
        return manager.get("chess/images/icons8-back-100.png", Texture.class);
    }

    public Texture getForwardsButton() {
        return manager.get("chess/images/icons8-forward-100.png", Texture.class);
    }

    public void loadSounds() {
        manager.load("chess/sounds/move-self.mp3", Sound.class);
        manager.load("chess/sounds/illegal.mp3", Sound.class);
        manager.load("chess/sounds/castle.mp3", Sound.class);
        manager.load("chess/sounds/capture.mp3", Sound.class);
        manager.load("chess/sounds/move-check.mp3", Sound.class);
        manager.load("chess/sounds/promote.mp3", Sound.class);
        manager.load("chess/sounds/game-end.mp3", Sound.class);
    }

    public Sound getMoveSound() {
        return manager.get("chess/sounds/move-self.mp3", Sound.class);
    }

    public Sound getIllegalSound() {
        return manager.get("chess/sounds/illegal.mp3", Sound.class);
    }

    public Sound getCastleSound() {
        return manager.get("chess/sounds/castle.mp3", Sound.class);
    }

    public Sound getCaptureSound() {
        return manager.get("chess/sounds/capture.mp3", Sound.class);
    }

    public Sound getMoveCheckSound() {
        return manager.get("chess/sounds/move-check.mp3", Sound.class);
    }

    public Sound getPromoteSound() {
        return manager.get("chess/sounds/promote.mp3", Sound.class);
    }

    public Sound getGameEndSound() {
        return manager.get("chess/sounds/game-end.mp3", Sound.class);
    }

    public void loadSkin() {
        manager.load("ui/uiskin.json", Skin.class);
    }

    public Skin getSkin() {
        return manager.get("ui/uiskin.json", Skin.class);
    }

    public void loadFont() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter font = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        font.fontFileName = "ui/Montserrat-SemiBold.ttf";
        font.fontParameters.size = 32;
        font.fontParameters.characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.,!?\"'()[]:;+-*/=%éèàùâêîôûçđăĩũơưÁÉÈÀÙÂÊÎÔÛÇĐĂĨŨƠƯ" +
            "àáảãạăắằẵẳặâầấậẩẫđèéẻẽẹêềếểễệìíỉĩịòóỏõọôồốổỗộơờớởỡợùúủũụưừứửữựỳýỷỹỵ";
        font.fontParameters.characters += "ăâêôươ";
        manager.load("ui/Montserrat-SemiBold.ttf", BitmapFont.class, font);
    }

    public BitmapFont getFont() {
        return manager.get("ui/Montserrat-SemiBold.ttf", BitmapFont.class);
    }

    public I18NBundle getBundle(String languageCode) {
        Locale locale = new Locale(languageCode);
        FileHandle baseFileHandle = Gdx.files.internal("i18N/bundle");
        return I18NBundle.createBundle(baseFileHandle, locale, "UTF-8");
    }

    public void dispose() {
        manager.dispose();
    }
}
