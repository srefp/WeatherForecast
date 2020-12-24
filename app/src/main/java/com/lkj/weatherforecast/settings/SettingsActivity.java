package com.lkj.weatherforecast.settings;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.lkj.weatherforecast.R;
import com.lkj.weatherforecast.base.SingleFragmentActivity;

public class SettingsActivity extends SingleFragmentActivity implements SettingListFragment.Callbacks {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new SettingListFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
    }

    @Override
    public void onSettingsChanged() {

    }
}