package com.tuigroup.codewars.data.remote.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class NetworkSlowdownInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        sleep();
        Log.d("NetworkSlowdown", "Network slowdown done");

        return chain.proceed(chain.request());
    }

    private void sleep() {
        try {
            Log.d("NetworkSlowdown", "Sleeping for 2 seconds");
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            Log.e("NetworkSlowdown", "Interrupted", e);
        }
    }
}