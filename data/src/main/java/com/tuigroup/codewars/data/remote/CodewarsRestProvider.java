package com.tuigroup.codewars.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuigroup.codewars.data.BuildConfig;
import com.tuigroup.codewars.data.remote.interceptor.NetworkSlowdownInterceptor;
import com.tuigroup.codewars.data.remote.interceptor.TokenAttachingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CodewarsRestProvider {

    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private static final String BASE_URL = BuildConfig.CODEWARS_URL;
    private static final int TIMEOUT_SECONDS = 30;
    private static final boolean ENABLE_SLOW_REQUEST = false;

    private static Retrofit RETROFIT = null;

    private static OkHttpClient buildOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        // TODO Move the token
        builder.addInterceptor(new TokenAttachingInterceptor("-hnQL3B3PzCFjsVwQzs9"));
        builder.readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        builder.connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        if (ENABLE_SLOW_REQUEST) {
            builder.addInterceptor(new NetworkSlowdownInterceptor());
        }

        return builder.build();
    }

    private static Retrofit buildRetrofit() {
        if (RETROFIT == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat(DATE_PATTERN)
                    .create();
            RETROFIT = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(buildOkHttpClient())
                    .build();
        }
        return RETROFIT;
    }

    public static <T> T getRestApi(Class<T> restAPI) {
        return buildRetrofit().create(restAPI);
    }
}