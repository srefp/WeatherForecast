package com.lkj.weatherforecast.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.lkj.weatherforecast.db.WeatherDbSchema.WeatherTable;
import com.lkj.weatherforecast.entity.Weather;

import java.util.Date;
import java.util.UUID;

/**
 * Android查询数据是通过cursor来实现的
 * 该类之间获取cursor当前位置对应的crime
 */
public class WeatherCursorWrapper extends CursorWrapper {
    public WeatherCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Weather getWeather() {
        String uuidString = getString(getColumnIndex(WeatherTable.Cols.UUIDT));
        String type = getString(getColumnIndex(WeatherTable.Cols.TYPE));
        long date = getLong(getColumnIndex(WeatherTable.Cols.DATE));
        String maxT = getString(getColumnIndex(WeatherTable.Cols.MINT));
        String minT = getString(getColumnIndex(WeatherTable.Cols.MINT));
        String info = getString(getColumnIndex(WeatherTable.Cols.INFO));

        Weather weather = new Weather(UUID.fromString(uuidString));

        weather.setType(type);
        weather.setDate(new Date(date));
        weather.setMaxT(maxT);
        weather.setMinT(minT);
        weather.setInfo(info);

        return weather;
    }

}
