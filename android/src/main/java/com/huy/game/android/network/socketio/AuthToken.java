package com.huy.game.android.network.socketio;

import android.content.Context;
import android.content.Intent;

import com.huy.game.android.models.request.RefreshRequest;
import com.huy.game.android.models.response.RefreshResponse;
import com.huy.game.android.network.retrofit.repository.AuthRepository;
import com.huy.game.android.utils.Constants;
import com.huy.game.android.utils.StorageUtils;
import com.huy.game.android.view.LoginWayActivity;

public class AuthToken implements com.huy.game.shared.network.AuthToken {

    private final Context context;

    public AuthToken(Context context) {
        this.context = context;
    }

    @Override
    public String getNewAccessToken() {
        AuthRepository authRepository = new AuthRepository();
        String refreshToken = StorageUtils.getInstance(context).getStringValue(Constants.DATASTORE_REFRESH_TOKEN);
        RefreshRequest request = new RefreshRequest(refreshToken);
        retrofit2.Response<RefreshResponse> refreshResponseResponse = authRepository.refreshToken(request);
        if(refreshResponseResponse.isSuccessful()) {
            assert refreshResponseResponse.body() != null;
            String newAccessToken = refreshResponseResponse.body().getAccessToken();
            StorageUtils.getInstance(context).setStringValue(Constants.DATASTORE_ACCESS_TOKEN, newAccessToken);
            return newAccessToken;
        }else {
            Intent intent = new Intent( context, LoginWayActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        return null;
    }

    @Override
    public String getCurrentAccessToken() {
        return StorageUtils.getInstance(context).getStringValue(Constants.DATASTORE_ACCESS_TOKEN);
    }
}
