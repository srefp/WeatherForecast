package com.lkj.weatherforecast.settings;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.lkj.weatherforecast.base.SingleFragmentActivity;

public class ChooseAreaActivity extends SingleFragmentActivity {
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ChooseAreaActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new ChooseAreaFragment();
    }

}
