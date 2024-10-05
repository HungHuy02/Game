package com.huy.game.android.network.retrofit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huy.game.android.local.Local;
import com.huy.game.android.models.request.RefreshRequest;
import com.huy.game.android.models.response.RefreshResponse;
import com.huy.game.android.network.retrofit.repository.AuthRepository;
import com.huy.game.android.utils.Constants;
import com.huy.game.android.utils.StorageUtils;
import com.huy.game.android.view.LoginWayActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient Instance;

    private RetrofitClient() {

    }

    public static RetrofitClient getInstance() {
        if(Instance == null) {
            Instance = new RetrofitClient();
        }
        return Instance;
    }

    private Retrofit retrofit = null;

    public Retrofit getClient(){
        if (retrofit == null) {
            Log.e("error", "Retrofit not init");
        }
        return retrofit;
    }

    public void createClient(Context context) {
        OkHttpClient httpClient = setupOkHttpClient(context);

        Gson gson = new GsonBuilder()
            .setLenient()
            .create();

        retrofit = new Retrofit.Builder()
            .baseUrl(Local.SERVER_ADDRESS)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build();
    }

    private OkHttpClient setupOkHttpClient(Context context)  {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request originalRequest = chain.request();

            String accessToken = StorageUtils.getInstance(context).getStringValue(Constants.DATASTORE_ACCESS_TOKEN);

            Request.Builder builder = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken);

            Request newRequest = builder.build();
            Response response = chain.proceed(newRequest);
            if (response.code() == 401) {
                if(chain.request().url().toString().endsWith("token")) {
                    Intent intent = new Intent( context, LoginWayActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    return response;
                }else {
                    AuthRepository authRepository = new AuthRepository();
                    String refreshToken = StorageUtils.getInstance(context).getStringValue(Constants.DATASTORE_REFRESH_TOKEN);
                    RefreshRequest request = new RefreshRequest(refreshToken);
                    retrofit2.Response<RefreshResponse> refreshResponseResponse = authRepository.refreshToken(request);
                    if(refreshResponseResponse.isSuccessful()) {
                        response.close();
                        assert refreshResponseResponse.body() != null;
                        String newAccessToken = refreshResponseResponse.body().getAccessToken();
                        StorageUtils.getInstance(context).setStringValue(Constants.DATASTORE_ACCESS_TOKEN, newAccessToken);
                        Request updatedRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + newAccessToken)
                            .build();

                        return chain.proceed(updatedRequest);
                    }
                }

            }
            return response;
        });
        return httpClient.build();
    }
}
