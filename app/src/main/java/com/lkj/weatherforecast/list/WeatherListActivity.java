package com.lkj.weatherforecast.list;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lkj.weatherforecast.R;
import com.lkj.weatherforecast.base.SingleFragmentActivity;
import com.lkj.weatherforecast.interfaces.ICommunication;
import com.lkj.weatherforecast.service.NotificationService;
import com.lkj.weatherforecast.weather.WeatherFragment;
import com.lkj.weatherforecast.entity.Weather;
import com.lkj.weatherforecast.weather.WeatherPagerActivity;

import java.util.concurrent.TimeUnit;

import static com.lkj.weatherforecast.settings.SettingListFragment.LNG_AND_LAT;
import static com.lkj.weatherforecast.settings.SettingListFragment.NOTIFICATION;

/**
 * 天气列表界面对应的活动
 */
public class WeatherListActivity extends SingleFragmentActivity implements WeatherListFragment.Callbacks, WeatherFragment.Callbacks {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    /**
     * 实现SingleFragmentActivity的抽象方法，实例化WeatherListFragment
     *
     * @return
     */
    @Override
    protected Fragment createFragment() {
        return new WeatherListFragment();
    }

    /**
     * 平板模式中
     * 获取布局资源id
     *
     * @return
     */
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    /**
     * 平板模式中：
     * 天气被选择的时候，平板则添加fragment
     * <p>
     * 手机模式中：
     * 开启WeatherPagerActivity可以翻页
     *
     * @param weather
     */
    @Override
    public void onWeatherSelected(Weather weather) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = WeatherPagerActivity.newIntent(this, weather.getUuid());
            startActivity(intent);
        } else {
            Fragment newDetail = WeatherFragment.newInstance(weather.getUuid());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    /**
     * 天气更新的时候更新对应的fragment
     *
     * @param weather
     */
    @Override
    public void onWeatherUpdated(Weather weather) {
        WeatherListFragment listFragment = (WeatherListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获得SharedPreferences中data对应的对象
        pref = getSharedPreferences("data", MODE_PRIVATE);
        //获得编辑器
        editor = pref.edit();

        startServiceClick();
        bindServiceClick();

//        try {
//            TimeUnit.SECONDS.sleep(2);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        new Thread(() -> {
            while (true) {
                Looper.prepare();

                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callServiceMethod();

                Looper.loop();
            }
        }).start();

    }

    /**
     * 调度服务
     */


    private static final String TAG = WeatherListActivity.class.getName();
    private boolean mIsServiceBind;
    private ICommunication mICommunication;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //解绑服务
        unbindServiceClick();
        //停止服务
        stopServiceClick();

        if (mConnection != null && mIsServiceBind) {
            unbindService(mConnection);
            mIsServiceBind = false;
            Log.d(TAG, "unbindServiceClick...");
        }

        Log.d(TAG, "onDestroy...");
    }

    /**
     * 开启服务
     */
    public void startServiceClick() {
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }

    /**
     * 停止服务
     */
    public void stopServiceClick() {
        Intent intent = new Intent(this, NotificationService.class);
        stopService(intent);
    }


    public void callServiceMethod() {
        Log.d(TAG, "call service inner method...");
        if (mIsServiceBind) {
            if ("Enabled".equals(pref.getString(NOTIFICATION, "Disabled"))) {
                mICommunication.callServiceInnerMethod(pref.getString(LNG_AND_LAT, "112.9,28.2"));
            }
        } else {
            Toast.makeText(this, "服务未绑定！", Toast.LENGTH_SHORT).show();
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "mServiceConnected...");
            mICommunication = (ICommunication) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "mServiceDisconnected...");
            mICommunication = null;
        }
    };

    /**
     * 绑定服务
     */
    public void bindServiceClick() {
        startServiceClick();
        Intent intent = new Intent(this, NotificationService.class);
        mIsServiceBind = bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    /**
     * 解绑服务
     */
    public void unbindServiceClick() {
        if (mConnection != null && mIsServiceBind) {
            unbindService(mConnection);
            mIsServiceBind = false;
            Log.d(TAG, "unbindServiceClick...");
        }
    }

}
