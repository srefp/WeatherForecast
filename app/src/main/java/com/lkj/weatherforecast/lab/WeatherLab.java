package com.lkj.weatherforecast.lab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lkj.weatherforecast.db.WeatherBaseHelper;
import com.lkj.weatherforecast.db.WeatherCursorWrapper;
import com.lkj.weatherforecast.db.WeatherDbSchema.WeatherTable;
import com.lkj.weatherforecast.entity.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.lkj.weatherforecast.db.WeatherDbSchema.WeatherTable.Cols.*;

/**
 * 天气单例，管理天气
 */
public class WeatherLab {
    private static WeatherLab sWeatherLab;

    private final Context mContext;
    private final SQLiteDatabase mDatabase;

    /**
     * 懒汉式单例模式
     *
     * @param context
     * @return
     */
    public static WeatherLab get(Context context) {
        if (sWeatherLab == null) {
            sWeatherLab = new WeatherLab(context);
        }

        return sWeatherLab;
    }

    /**
     * 私有化构造函数
     *
     * @param context
     */
    private WeatherLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new WeatherBaseHelper(mContext)
                .getWritableDatabase();
    }

    /**
     * 添加天气
     *
     * @param w
     */
    public void addWeather(Weather w) {
        ContentValues values = getContentValues(w);

        mDatabase.insert(WeatherTable.NAME, null, values);
    }

    /**
     * 从网络获取数据的时候先清除本地数据
     */
    public void onFetchWeatherFromInternet() {
        mDatabase.execSQL("delete from " + WeatherTable.NAME);
    }

    /**
     * 获取所有天气
     *
     * @return
     */
    public List<Weather> getWeathers() {

        List<Weather> weathers = new ArrayList<>();

//        weathers.add(new Weather(UUID.randomUUID(), "Clouds", new Date(), "30", "20", "Goood!"));
//        weathers.add(new Weather(UUID.randomUUID(), "Clouds", new Date(), "30", "20", "Goood!"));

        // 无网络的时候从SQLite提取数据
        // todo condition

        WeatherCursorWrapper cursor = queryWeathers(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                weathers.add(cursor.getWeather());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return weathers;
    }


    /**
     * 根据id获取对应的天气
     *
     * @param id
     * @return
     */
    public Weather getWeather(UUID id) {
        WeatherCursorWrapper cursor = queryWeathers(
                UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getWeather();
        } finally {
            cursor.close();
        }
    }

    /**
     * 根据天气对象获取图片
     * @param weather
     * @return
     */
//    public File getPhotoFile(Weather weather) {
//        File filesDir = mContext.getFilesDir();
//        return new File(filesDir, weather.getPhotoFilename());
//    }

    /**
     * 更新天气
     * @param weather
     */
//    public void updateWeather(Weather weather) {
//        String uuidString = weather.getUuid().toString();
//        ContentValues values = getContentValues(weather);
//        mDatabase.update(WeatherTable.NAME, values,
//                UUIDT + " = ?",
//                new String[]{uuidString});
//    }

    /**
     * 查询天气
     *
     * @param whereClause
     * @param whereArgs
     * @return
     */
    private WeatherCursorWrapper queryWeathers(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                WeatherTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new WeatherCursorWrapper(cursor);
    }

    /**
     * 持久化天气的时候，将天气对象转换为ContentValues
     *
     * @param weather
     * @return
     */
    private static ContentValues getContentValues(Weather weather) {
        ContentValues values = new ContentValues();
        values.put(UUID, weather.getUuid().toString());
        values.put(TYPE, weather.getType());
        values.put(DATE, weather.getDate().getTime());
        values.put(MAXT, weather.getMaxT());
        values.put(MINT, weather.getMinT());
        values.put(INFO, weather.getInfo());

        return values;
    }
}
