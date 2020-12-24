package com.lkj.weatherforecast.lab;

import java.util.HashMap;
import java.util.Map;

public class LocationLab {
    private static final Map<String, String> cityLocationMap;

    static {
        cityLocationMap = new HashMap<>();
        cityLocationMap.put("Changsha", "112.9,28.2");
        cityLocationMap.put("Beijing", "116.4,39.9");
        cityLocationMap.put("Shanghai", "121.5,31.2");
    }

    public static String getLocationByCity(String city) {
        return cityLocationMap.get(city);
    }
}
