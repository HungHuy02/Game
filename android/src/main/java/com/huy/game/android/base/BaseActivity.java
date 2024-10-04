package com.huy.game.android.base;

import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatActivity;

import com.huy.game.android.globalstate.UserState;
import com.huy.game.android.utils.Constants;
import com.huy.game.android.utils.StorageUtils;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        String language = StorageUtils.getInstance(newBase).getStringValue(Constants.DATASTORE_LANGUAGE);
        language = language.equals("null") ? "vi" : language;
        UserState.getInstance().setLanguage(language);
        Locale locale = new Locale(language);
        Configuration config = newBase.getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        super.attachBaseContext(newBase.createConfigurationContext(config));
    }
}
