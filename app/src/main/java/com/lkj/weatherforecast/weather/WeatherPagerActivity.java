package com.lkj.weatherforecast.weather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.lkj.weatherforecast.R;
import com.lkj.weatherforecast.entity.Weather;
import com.lkj.weatherforecast.lab.WeatherLab;

import java.util.List;
import java.util.UUID;

/**
 * 翻页视图活动
 */
public class WeatherPagerActivity extends AppCompatActivity implements WeatherFragment.Callbacks {

    private static final String EXTRA_WEATHER_ID = "com.lkj.weatherforecast.weather_id";

    // 翻页视图
    private ViewPager mViewPager;
    // 列表
    private List<Weather> mWeathers;

    /**
     * 返回创建WeatherPageActivity的意图，供其它视图开启WeatherPageActivity
     * @param packageContext
     * @param crimeId
     * @return
     */
    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, WeatherPagerActivity.class);
        intent.putExtra(EXTRA_WEATHER_ID, crimeId);
        return intent;
    }

    /**
     * 创建WeatherPageActivity时：
     * 实例化ViewPager、天气列表模型
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_WEATHER_ID);

        mViewPager = findViewById(R.id.crime_view_pager);

        mWeathers = WeatherLab.get(this).getWeathers();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Weather weather = mWeathers.get(position);
                return WeatherFragment.newInstance(weather.getUuid());
            }

            @Override
            public int getCount() {
                return mWeathers.size();
            }
        });

        for (int i = 0; i < mWeathers.size(); i++) {
            if (mWeathers.get(i).getUuid().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onWeatherUpdated(Weather weather) {

    }
}
