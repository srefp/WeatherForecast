package com.lkj.weatherforecast.util;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    private static final String TAG = HttpUtil.class.getName();

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(address)
                .get()
                .build();

        Log.d(TAG, "...网址为：" + request.toString());
        client.newCall(request).enqueue(callback);

    }

}
