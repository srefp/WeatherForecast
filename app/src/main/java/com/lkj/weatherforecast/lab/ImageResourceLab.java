package com.lkj.weatherforecast.lab;

import com.lkj.weatherforecast.R;

import java.util.HashMap;
import java.util.Map;

public class ImageResourceLab {
    private static Map<String, Integer> imageResourceMap;

    static {
        imageResourceMap = new HashMap<String, Integer>();
        imageResourceMap.put("Cloudy", R.drawable.cloudy);
        imageResourceMap.put("Sunny", R.drawable.sunny);
    }

    public static Integer getImageResource(String weatherType) {
        return imageResourceMap.get(weatherType);
    }
}
