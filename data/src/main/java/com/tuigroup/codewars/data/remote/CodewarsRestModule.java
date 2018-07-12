package com.tuigroup.codewars.data.remote;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuigroup.codewars.data.BuildConfig;
import com.tuigroup.codewars.data.remote.interceptor.ConnectivityInterceptor;
import com.tuigroup.codewars.data.remote.interceptor.NetworkSlowdownInterceptor;
import com.tuigroup.codewars.data.remote.interceptor.TokenAttachingInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class CodewarsRestModule {

    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final int DISK_CACHE_SIZE = 10 * 1024 * 1024;

    private static final String BASE_URL = BuildConfig.CODEWARS_URL;
    private static final int TIMEOUT_SECONDS = 30;
    private static final boolean ENABLE_SLOW_REQUEST = false;

    @Provides
    @Singleton
    Cache provideCache(Context context) {
        File cacheDir = context.getCacheDir();
        return new Cache(cacheDir, DISK_CACHE_SIZE);
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    @Singleton
    TokenAttachingInterceptor provideTokenAttachingInterceptor() {
        // TODO Move the token
        return new TokenAttachingInterceptor("-hnQL3B3PzCFjsVwQzs9");
    }

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient(Context context,
                                            Cache cache,
                                            HttpLoggingInterceptor loggingInterceptor,
                                            TokenAttachingInterceptor tokenAttachingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);
        builder.addInterceptor(tokenAttachingInterceptor);
        builder.addInterceptor(new ConnectivityInterceptor(context.getApplicationContext()));
        builder.cache(cache);
        builder.readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        builder.connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        if (ENABLE_SLOW_REQUEST) {
            builder.addInterceptor(new NetworkSlowdownInterceptor());
        }

        return builder.build();
    }

    @Provides
    @Singleton
    static Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat(DATE_PATTERN)
                .create();
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    UserRestApi provideUserRestApi(Retrofit retrofit) {
        return retrofit.create(UserRestApi.class);
    }
}