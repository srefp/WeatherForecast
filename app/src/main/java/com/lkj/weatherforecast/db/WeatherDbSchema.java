package com.lkj.weatherforecast.db;

/**
 * 天气的数据库存储模型
 */
public class WeatherDbSchema {
    public static class WeatherTable {
        public static final String NAME = "weathers";

        public static class Cols {
            public static final String UUIDT = "uuid";
            public static final String TYPE = "type";
            public static final String DATE = "date";
            public static final String MAXT = "maxt";
            public static final String MINT = "mint";
            public static final String INFO = "info";
        }

    }
}
