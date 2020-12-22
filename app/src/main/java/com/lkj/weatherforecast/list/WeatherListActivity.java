package com.lkj.weatherforecast.list;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.lkj.weatherforecast.R;
import com.lkj.weatherforecast.base.SingleFragmentActivity;
import com.lkj.weatherforecast.weather.WeatherFragment;
import com.lkj.weatherforecast.entity.Weather;
import com.lkj.weatherforecast.weather.WeatherPagerActivity;

/**
 * 天气列表界面对应的活动
 */
public class WeatherListActivity extends SingleFragmentActivity implements WeatherListFragment.Callbacks, WeatherFragment.Callbacks {
    /**
     * 实现SingleFragmentActivity的抽象方法，实例化WeatherListFragment
     * @return
     */
    @Override
    protected Fragment createFragment() {
        return new WeatherListFragment();
    }

    /**
     * 平板模式中
     * 获取布局资源id
     * @return
     */
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    /**
     * 平板模式中：
     * 天气被选择的时候，平板则添加fragment
     *
     * 手机模式中：
     * 开启WeatherPagerActivity可以翻页
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
     * @param weather
     */
    @Override
    public void onWeatherUpdated(Weather weather) {
        WeatherListFragment listFragment = (WeatherListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
