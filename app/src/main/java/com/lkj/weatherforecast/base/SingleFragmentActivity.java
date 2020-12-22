package com.lkj.weatherforecast.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.lkj.weatherforecast.R;

/**
 * 只有一个碎片的活动
 * WeatherListActivity和WeatherActivity中都会继承该类
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
    /**
     * 在WeatherListActivity和WeatherActivity中实现的方法
     * 决定Activity绑定那个fragment
     * @return
     */
    protected abstract Fragment createFragment();

    /**
     * 平板模式下：
     * 获取fragment资源
     * @return
     */
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    /**
     * 创建时绑定对应的fragment
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        // FrameLayout是CrimeFragment的视图容器，可以托管fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
