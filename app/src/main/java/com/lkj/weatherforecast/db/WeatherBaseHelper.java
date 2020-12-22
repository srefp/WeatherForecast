package com.lkj.weatherforecast.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lkj.weatherforecast.db.WeatherDbSchema.WeatherTable;

public class WeatherBaseHelper extends SQLiteOpenHelper {
    // 指定数据库的版本
    private static final int VERSION = 1;
    // 指定数据库的名字
    private static final String DATABASE_NAME = "weatherBase.db";

    /**
     * 构造函数
     *
     * @param context
     */
    public WeatherBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * 创建表
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + WeatherTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                WeatherTable.Cols.UUID + ", " +
                WeatherTable.Cols.TYPE + ", " +
                WeatherTable.Cols.DATE + ", " +
                WeatherTable.Cols.MAXT + ", " +
                WeatherTable.Cols.MINT + ", " +
                WeatherTable.Cols.INFO +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
