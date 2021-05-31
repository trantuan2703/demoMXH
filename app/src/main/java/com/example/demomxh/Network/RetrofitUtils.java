package com.example.demomxh.Network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    private Retrofit retrofit;
    private static RetrofitUtils retrofitUtils;

    public RetrofitUtils() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        retrofit = new Retrofit.Builder().
                baseUrl(API.ROOT).
                addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).
                build();
    }

    public static RetrofitUtils getInstance(){
        if (retrofitUtils==null){
            retrofitUtils = new RetrofitUtils();
        }
        return retrofitUtils;
    }

    public RetrofitService createService(){
        return retrofit.create(RetrofitService.class);
    }
}
