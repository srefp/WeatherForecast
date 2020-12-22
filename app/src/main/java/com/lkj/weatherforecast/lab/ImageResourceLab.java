package com.lkj.weatherforecast.lab;

import com.lkj.weatherforecast.R;

import java.util.HashMap;
import java.util.Map;

public class ImageResourceLab {
    private static Map<String, Integer> imageResourceMap;

    static {
        imageResourceMap = new HashMap<String, Integer>();
        imageResourceMap.put("Clouds", R.drawable.clouds);
        imageResourceMap.put("Blizzard", R.drawable.blizzard);
        imageResourceMap.put("Clear", R.drawable.clear);
        imageResourceMap.put("Heavy Rain", R.drawable.heavy_rain);
    }

    public static Integer getImageResource(String weatherType) {
        return imageResourceMap.get(weatherType);
    }
}
