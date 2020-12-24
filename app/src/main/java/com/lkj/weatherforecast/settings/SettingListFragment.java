package com.lkj.weatherforecast.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lkj.weatherforecast.R;

import static android.content.Context.MODE_PRIVATE;


public class SettingListFragment extends Fragment implements View.OnClickListener {

    // 回调接口
    private SettingListFragment.Callbacks mCallbacks;

    private TextView mLocationValueTextView;
    private TextView mTemperatureUnitsValueTextView;
    private TextView mWeatherNotificationValueTextView;
    private CheckBox mCheckBox;

    private ConstraintLayout first;
    private ConstraintLayout second;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public static final String TEMPERATURE_UNITS = "Temperature Units";
    public static final String LOCATION = "Location";
    public static final String NOTIFICATION = "Notification";
    public static final String LNG_AND_LAT = "LngAndLat";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first:
                startActivity(ChooseAreaActivity.newIntent(getActivity()));
                break;
            case R.id.second:
                if ("Fahrenheit".equals(mTemperatureUnitsValueTextView.getText().toString())) {
                    mTemperatureUnitsValueTextView.setText("Celsius");
                    editor.putString(TEMPERATURE_UNITS, "Celsius");
                    editor.commit();
                } else if ("Celsius".equals(mTemperatureUnitsValueTextView.getText().toString())) {
                    mTemperatureUnitsValueTextView.setText("Fahrenheit");
                    editor.putString(TEMPERATURE_UNITS, "Fahrenheit");
                    editor.commit();
                }
                break;

        }
    }

    /**
     * 回调接口，当设置改变的时候更新设置的视图
     */
    public interface Callbacks {
        void onSettingsChanged();
    }

    /**
     * fragment被绑定到activity的时候实例化回调对象
     * 
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    /**
     * fragment被创建时，显示选择菜单
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获得SharedPreferences中data对应的对象
        pref = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        //获得编辑器
        editor = pref.edit();

        // todo 更新UI

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment_settings, container, false);

        mLocationValueTextView = view.findViewById(R.id.list_item_location_value);
        mTemperatureUnitsValueTextView = view.findViewById(R.id.list_item_location_temperature_units_value);
        mWeatherNotificationValueTextView = view.findViewById(R.id.list_item_weather_notifications_value);

        first = view.findViewById(R.id.first);
        second = view.findViewById(R.id.second);
        mCheckBox = view.findViewById(R.id.list_item_checkbox);

        first.setOnClickListener(this::onClick);
        second.setOnClickListener(this::onClick);
        mCheckBox.setChecked(!"Disabled".equals(pref.getString(NOTIFICATION, "")));

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putString(NOTIFICATION, "Enabled");
                    editor.commit();
                } else {
                    editor.putString(NOTIFICATION, "Disabled");
                    editor.commit();
                }
                updateUI();
            }
        });

        updateUI();

        return view;
    }

    /**
     * 从其它视图跳到该界面的时候更新界面
     */
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * 与activity解绑的时候释放回调接口对象
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * 从SharedPreference获取存储的内容，更新UI
     */
    public void updateUI() {

        String location = pref.getString(LOCATION, "changsha");
        String temperatureUnits = pref.getString(TEMPERATURE_UNITS, "Celsius");
        String notification = pref.getString(NOTIFICATION, "Enabled");
        mLocationValueTextView.setText(location);
        mTemperatureUnitsValueTextView.setText(temperatureUnits);
        mWeatherNotificationValueTextView.setText(notification);
    }

}
