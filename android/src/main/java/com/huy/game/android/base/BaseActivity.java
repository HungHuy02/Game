package com.huy.game.android.base;

import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatActivity;

import com.huy.game.chess.manager.GameSetting;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale locale = Locale.forLanguageTag(GameSetting.getInstance().getLanguage());
        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        super.attachBaseContext(newBase.createConfigurationContext(config));
    }
}
