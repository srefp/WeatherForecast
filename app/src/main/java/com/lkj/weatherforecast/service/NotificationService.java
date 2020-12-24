package com.lkj.weatherforecast.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.lkj.weatherforecast.R;
import com.lkj.weatherforecast.entity.Weather;
import com.lkj.weatherforecast.interfaces.ICommunication;
import com.lkj.weatherforecast.util.JsonFetcher;

public class NotificationService extends Service {
    private static final String TAG = NotificationService.class.getName();

    private class InnerBinder extends Binder implements ICommunication {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void callServiceInnerMethod(String location) {
            sayHello();
            notification(location);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind...");
        return new InnerBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate...");
    }


    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, "onStart...");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand...");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroyed...");
    }

    private void sayHello() {
        Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notification(String location) {
        Weather weather = new JsonFetcher().fetchWeathers(location).get(0);
        // 1. 创建一个通知(必须设置channelId)
        Context context = getApplicationContext();
        String channelId = "ChannelId"; // 通知渠道
        Notification notification = new Notification.Builder(context)
                .setChannelId(channelId)
                .setSmallIcon(R.mipmap.icon)
                .setContentTitle("WeatherForecast")
                .setContentText("Forecast: " + weather.getType() +
                        " High: " + weather.getMaxT() + "°" +
                        " Low: " + weather.getMinT() + "°")
                .build();

        // 2. 获取系统的通知管理器(必须设置channelId)
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(
                channelId,
                "通知的渠道名称",
                NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
        // 3. 发送通知(Notification与NotificationManager的channelId必须对应)
        notificationManager.notify(1, notification);
    }
}
