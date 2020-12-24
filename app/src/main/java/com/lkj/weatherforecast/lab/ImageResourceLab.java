package com.lkj.weatherforecast.lab;

import com.lkj.weatherforecast.R;

import java.util.HashMap;
import java.util.Map;

public class ImageResourceLab {
    private static final Map<String, Integer> imageResourceMap;

    static {
        imageResourceMap = new HashMap<String, Integer>();
        imageResourceMap.put("Cloudy", R.drawable.cloudy);
        imageResourceMap.put("Sunny", R.drawable.sunny);
        imageResourceMap.put("Overcast", R.drawable.overcast);
        imageResourceMap.put("Light Rain", R.drawable.light_rain);
    }

    public static Integer getImageResource(String weatherType) {
        return imageResourceMap.get(weatherType);
    }
}
