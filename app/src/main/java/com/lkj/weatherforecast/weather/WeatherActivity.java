//package com.lkj.weatherforecast.weather;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.v4.app.Fragment;
//
//import com.lkj.weatherforecast.base.SingleFragmentActivity;
//
//import java.util.UUID;
//
//public class WeatherActivity extends SingleFragmentActivity {
//
//    private static final String EXTRA_WEATHER_ID = "com.lkj.weatherforecast.weather_id";
//
//    /**
//     * 实例化天气内容视图的activity
//     * @param packageContext
//     * @param crimeId
//     * @return
//     */
//    public static Intent newIntent(Context packageContext, UUID crimeId) {
//        Intent intent = new Intent(packageContext, WeatherActivity.class);
//        intent.putExtra(EXTRA_WEATHER_ID, crimeId);
//        return intent;
//    }
//
//    /**
//     * 获取对应的fragment
//     * @return
//     */
//    @Override
//    protected Fragment createFragment() {
//        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_WEATHER_ID);
//        return WeatherFragment.newInstance(crimeId);
//    }
//}
