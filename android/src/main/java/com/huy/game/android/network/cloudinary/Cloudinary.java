package com.huy.game.android.network.cloudinary;

import android.content.Context;
import android.net.Uri;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.UploadCallback;
import com.huy.game.android.local.Local;

import java.util.HashMap;
import java.util.Map;

public class Cloudinary {

    private static Cloudinary Instance;
    private boolean isInit = false;

    private Cloudinary() {}

    public static Cloudinary getInstance() {
        if(Instance == null) {
            Instance = new Cloudinary();
        }
        return Instance;
    }

    public void init(Context context) {
        if(!isInit) {
            Map<String, Object> config = new HashMap<>();
            config.put("cloud_name", Local.CLOUD_NAME);
            config.put("api_key", Local.CLOUD_API_KEY);
            config.put("api_secret", Local.CLOUD_API_SECRET);
            config.put("secure", true);
            MediaManager.init(context, config);
            isInit = true;
        }
    }

    public void uploadImage(Uri uri, UploadCallback callback) {
        MediaManager.get().upload(uri)
            .unsigned("mgljwhjt")
//          .option("moderation", "aws_rek")
            .callback(callback)
            .dispatch();
    }
}
