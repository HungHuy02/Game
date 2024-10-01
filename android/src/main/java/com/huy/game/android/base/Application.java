package com.huy.game.android.base;

import com.huy.game.android.network.retrofit.RetrofitClient;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitClient.getInstance().createClient(this);
    }
}
