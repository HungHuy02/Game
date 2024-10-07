package com.huy.game.android.base;

import com.huy.game.android.network.retrofit.RetrofitClient;
import com.huy.game.android.utils.Constants;
import com.huy.game.android.utils.StorageUtils;
import com.huy.game.chess.manager.GameSetting;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitClient.getInstance().createClient(this);
        handleSetting();
    }

    private void handleSetting() {
        StorageUtils storageUtils = StorageUtils.getInstance(this);
        String language = storageUtils.getStringValue(Constants.DATASTORE_LANGUAGE);
        language = language.equals("null") ? "vi" : language;
        boolean mute = storageUtils.getBooleanValue(Constants.DATASTORE_MUTE);
        GameSetting.getInstance().setSetting(language, mute);
    }
}
