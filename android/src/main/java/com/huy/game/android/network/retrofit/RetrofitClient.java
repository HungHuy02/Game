package com.huy.game.android.network.retrofit;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huy.game.android.local.Local;

import okhttp3.OkHttpClient;
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

//        httpClient.addInterceptor(chain -> {
//            Request originalRequest = chain.request();
//            String url = originalRequest.url().toString();
//            boolean isHeaderRequired = !url.contains("api/auth");
//
//            Request.Builder builder = originalRequest.newBuilder();
//
//            if (isHeaderRequired) {
//                String accessToken = "";
//                try {
//                    accessToken = getAccessTokenSync(context);
//                } catch (ExecutionException | InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                builder.header("Authorization", "Bearer " + accessToken);
//            }
//
//            Request newRequest = builder.build();
//            return chain.proceed(newRequest);
//        });

        return httpClient.build();
    }
}
