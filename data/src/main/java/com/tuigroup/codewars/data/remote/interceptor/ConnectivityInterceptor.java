package com.tuigroup.codewars.data.remote.interceptor;

import android.content.Context;

import com.tuigroup.codewars.data.remote.exception.NoConnectivityException;
import com.tuigroup.codewars.data.util.NetworkUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {

    private Context context;

    public ConnectivityInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            throw new NoConnectivityException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }
}