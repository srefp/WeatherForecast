package com.lkj.weatherforecast.list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lkj.weatherforecast.R;
import com.lkj.weatherforecast.entity.Weather;
import com.lkj.weatherforecast.lab.WeatherLab;
import com.lkj.weatherforecast.settings.SettingsActivity;
import com.lkj.weatherforecast.util.JsonFetcher;
import com.lkj.weatherforecast.util.Utility;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.lkj.weatherforecast.settings.SettingListFragment.LNG_AND_LAT;
import static com.lkj.weatherforecast.settings.SettingListFragment.TEMPERATURE_UNITS;

/**
 * 天气列表碎片
 */
public class WeatherListFragment extends Fragment {

    // 天气列表模型
    private List<Weather> mWeathers;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // 副标题是否可见
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    // 天气列表
    private RecyclerView mWeatherRecyclerView;
    // 天气适配器
    private WeatherAdapter mAdapter;
    // 副标题是否可见
    private boolean mSubtitleVisible;
    // 回调接口
    private Callbacks mCallbacks;

    // 列表视图的控件
    private TextView mListDate;
    private TextView mListMaxTemperature;
    private TextView mListMinTemperature;
    private ImageView mListImage;
    private TextView mListType;

    /**
     * 回调接口，当天气被选择的时候更新
     */
    public interface Callbacks {
        void onWeatherSelected(Weather weather);
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

        new FetchWeathersTask().execute();

        setHasOptionsMenu(true);

    }

    /**
     * 创建视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment_weather, container, false);

        mWeatherRecyclerView = view.findViewById(R.id.weather_recycler_view);
        mWeatherRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 初始化天气列表视图的控件
        mListDate = view.findViewById(R.id.list_date);
        mListMaxTemperature = view.findViewById(R.id.list_max_temperature);
        mListMinTemperature = view.findViewById(R.id.list_min_temperature);
        mListImage = view.findViewById(R.id.list_image);
        mListType = view.findViewById(R.id.list_type);

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();


        return view;
    }

    /**
     * 从其它视图跳到该界面的时候更新界面
     */
    @Override
    public void onResume() {
        super.onResume();
        new FetchWeathersTask().execute();
        updateUI();

    }

    /**
     * 保存副标题可见状态
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
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
     * 创建选择菜单
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_weather_list_menu, menu);

        MenuItem subtitleItem = menu.findItem(R.id.settings);

//        if (mSubtitleVisible) {
//            subtitleItem.setTitle(R.string.hide_subtitle);
//        } else {
//            subtitleItem.setTitle(R.string.settings);
//        }
    }

    /**
     * 点击选择菜单的时候进行相应的事件处理
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_location:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);

                //将功能Scheme以URI的方式传入data
                Uri uri = Uri.parse("androidamap://myLocation?sourceApplication=softname");
                intent.setData(uri);

                //启动该页面即可
                startActivity(intent);

//                Weather weather = new Weather();
//                WeatherLab.get(getActivity()).addWeather(weather);

                // 暂时在这里向数据库插入数据
//                getWeatherInformation();

//                mCallbacks.onWeatherSelected(weather);
                return true;
            case R.id.settings:
                startActivity(SettingsActivity.newIntent(getActivity()));

//                mSubtitleVisible = !mSubtitleVisible;
//                getActivity().invalidateOptionsMenu();
//                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveWeatherInformation(List<Weather> weathers) {
        // 清空数据
        WeatherLab.get(getActivity()).onFetchWeatherFromInternet();
        // 插入数据
        for (Weather weather : weathers) {
            WeatherLab.get(getActivity()).addWeather(weather);
        }
//        WeatherLab.get(getActivity()).addWeather(new Weather(UUID.randomUUID(), "Clouds", new Date(), "31", "21", "Goood!"));

        // 更新视图界面
        updateUI();
    }

    /**
     * 设置工具栏子标题
     */
    private void updateSubtitle() {
        WeatherLab weatherLab = WeatherLab.get(getActivity());
        int weatherCount = weatherLab.getWeathers().size();
//        String subtitle = getString(R.string.subtitle_format, weatherCount);

//        if (!mSubtitleVisible) {
//            subtitle = null;
//        }
//
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    /**
     * 更新界面
     */
    public void updateUI() {
        WeatherLab weatherLab = WeatherLab.get(getActivity());
        List<Weather> weathers = weatherLab.getWeathers();


        if (mAdapter == null) {
            mAdapter = new WeatherAdapter(weathers);
            mWeatherRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setWeathers(weathers);
            mAdapter.notifyDataSetChanged();
        }

        if (mWeathers != null) {
            if (mWeathers.size() > 0) {
                updateTodayWeather(mWeathers.get(0));
            }
        }

        //更新副标题
        updateSubtitle();
    }

    /**
     * 天气控件管理类
     */
    private class WeatherHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // 天气模型
        private Weather mWeather;

        private final ImageView mWeatherImageView; // 天气图片
        private final TextView mDateTextView; // 天气日期
        private final TextView mTypeTextView; // 天气类型
        private final TextView mMaxTemperature; // 最高温度
        private final TextView mMinTemperature; // 最低温度

        /**
         * 构造方法：实例化图片、文字控件
         *
         * @param inflater
         * @param parent
         */
        public WeatherHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_weather, parent, false));
            itemView.setOnClickListener(this);

            // 通过ViewHolder的itemView实例化成员变量
            mWeatherImageView = itemView.findViewById(R.id.list_item_weather_image);
            mDateTextView = itemView.findViewById(R.id.list_item_weather_date);
            mTypeTextView = itemView.findViewById(R.id.list_item_weather_type);
            mMaxTemperature = itemView.findViewById(R.id.list_item_maxt);
            mMinTemperature = itemView.findViewById(R.id.list_item_mint);
        }

        /**
         * 绑定对应的天气模型
         *
         * @param weather
         */
        public void bind(Weather weather) {
            mWeather = weather;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            String dayOfWeek = simpleDateFormat.format(mWeather.getDate());

            mWeatherImageView.setImageResource(mWeather.getImage());
            mDateTextView.setText(dayOfWeek);
            mTypeTextView.setText(mWeather.getType());

            String temperatureUnits = pref.getString(TEMPERATURE_UNITS, "Celsius");
            if ("Celsius".equals(temperatureUnits)) {
                mMaxTemperature.setText(mWeather.getMaxT() + "℃");
                mMinTemperature.setText(mWeather.getMinT() + "℃");
            } else if ("Fahrenheit".equals(temperatureUnits)) {
                mMaxTemperature.setText(Utility.C2F(Integer.parseInt(mWeather.getMaxT())) + "℉");
                mMinTemperature.setText(Utility.C2F(Integer.parseInt(mWeather.getMinT())) + "℉");
            }
        }

        /**
         * 点击天气列表项的时候调用回调方法
         *
         * @param view
         */
        @Override
        public void onClick(View view) {
            mCallbacks.onWeatherSelected(mWeather);
        }
    }

    /**
     * 天气适配器
     */
    private class WeatherAdapter extends RecyclerView.Adapter<WeatherHolder> {


        /**
         * 构造方法：实例化天气列表模型
         *
         * @param weathers
         */
        public WeatherAdapter(List<Weather> weathers) {
            mWeathers = weathers;
        }

        /**
         * 创建视图支持器
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new WeatherHolder(layoutInflater, parent);
        }

        /**
         * 调用视图支持器来绑定天气模型和天气列表项视图控件
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(WeatherHolder holder, int position) {
            Weather weather = mWeathers.get(position);
            holder.bind(weather);
        }

        /**
         * 获取天气列表中天气的总数
         *
         * @return
         */
        @Override
        public int getItemCount() {
            return mWeathers.size();
        }

        /**
         * 设置天气列表模型
         *
         * @param weathers
         */
        public void setWeathers(List<Weather> weathers) {
            mWeathers = weathers;
        }
    }

    private class FetchWeathersTask extends AsyncTask<Void, Void, List<Weather>> {
        @Override
        protected List<Weather> doInBackground(Void... voids) {
            return new JsonFetcher().fetchWeathers(pref.getString(LNG_AND_LAT, "112.9,28.2"));
        }

        @Override
        protected void onPostExecute(List<Weather> weathers) {
            mWeathers = weathers;

            saveWeatherInformation(weathers);
            updateTodayWeather(weathers.get(0));
        }
    }

    private void updateTodayWeather(Weather weather) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d", Locale.ENGLISH);
        String monthAndDay = simpleDateFormat.format(weather.getDate());
        mListDate.setText("Today, " + monthAndDay);
        String temperatureUnits = pref.getString(TEMPERATURE_UNITS, "Celsius");
        if ("Celsius".equals(temperatureUnits)) {
            mListMaxTemperature.setText(weather.getMaxT() + "℃");
            mListMinTemperature.setText(weather.getMinT() + "℃");
        } else if ("Fahrenheit".equals(temperatureUnits)) {
            mListMaxTemperature.setText(String.valueOf(Utility.C2F(Integer.parseInt(weather.getMaxT()))) + "℉");
            mListMinTemperature.setText(String.valueOf(Utility.C2F(Integer.parseInt(weather.getMinT()))) + "℉");
        }
        mListImage.setImageResource(weather.getImage());
        mListType.setText(weather.getType());
    }
}
