package com.lkj.weatherforecast.util;

import android.net.Uri;
import android.util.Log;

import com.lkj.weatherforecast.entity.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JsonFetcher {

    private static final String TAG = JsonFetcher.class.getName();

    /**
     * 从网络获取字节
     *
     * @param urlSpec
     * @return
     * @throws IOException
     */
    public byte[] getUrlBytes(String urlSpec) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlSpec);
            connection = (HttpURLConnection) url.openConnection();
            String redirect = connection.getHeaderField("Location");
            if (redirect != null) {
                connection = (HttpURLConnection) new URL(redirect).openConnection();
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            Log.d(TAG, String.valueOf(connection.getResponseCode()));
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with" + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return null;
    }

    /**
     * 返回字符串
     *
     * @param urlSpec
     * @return
     * @throws IOException
     */
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public String fetchLgnAndLatByCityName(String cityName) {
        String lngAndLat = null;
        try {
            String url = Uri.parse("http://api.map.baidu.com/geocoder")
                    .buildUpon()
                    .appendQueryParameter("address", "城市名称")
                    .appendQueryParameter("output", "json")
                    .appendQueryParameter("key", "37492c0ee6f924cb5e934fa08c6b1676")
                    .appendQueryParameter("city", cityName)
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "收到JSON：" + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            lngAndLat = jsonBody.getJSONObject("result").getJSONObject("location").getString("lng") +
                    "," + jsonBody.getJSONObject("result").getJSONObject("location").getString("lat");
            Log.i(TAG, "......经纬度为：" + lngAndLat);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lngAndLat;
    }

    /**
     *
     * @param location 经度和纬度
     * @return
     */
    public List<Weather> fetchWeathers(String location) {
        List<Weather> weathers = new ArrayList<>();

        try {
            String url = Uri.parse("https://devapi.qweather.com/v7/weather/3d")
                    .buildUpon()
                    .appendQueryParameter("location", location)
                    .appendQueryParameter("key", "6111a1865f3b402f89112c05547febeb")
                    .appendQueryParameter("lang", "en")
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "收到JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(weathers, jsonBody);
        } catch (IOException e) {
            Log.e(TAG, "获取天气信息失败...", e);
        } catch (JSONException e) {
            Log.e(TAG, "JSON转换失败...", e);
        }

        return weathers;
    }

    /**
     * 处理获取的json对象
     *
     * @param items
     * @param jsonBody
     * @throws IOException
     * @throws JSONException
     */
    private void parseItems(List<Weather> items, JSONObject jsonBody) throws IOException, JSONException {
        JSONArray photoJsonArray = jsonBody.getJSONArray("daily");

        for (int i = 0; i < photoJsonArray.length(); i++) {

            JSONObject weatherJsonObject = photoJsonArray.getJSONObject(i);

            Weather weather = new Weather();
            weather.setUuid(UUID.randomUUID());
            weather.setDate(new Date(weatherJsonObject.getString("fxDate").replaceAll("-", "/")));
            weather.setType(weatherJsonObject.getString("textDay"));
            weather.setInfo("Humidity: " + weatherJsonObject.getString("humidity") + " %\n" +
                    "Pressure: " + weatherJsonObject.getString("pressure") + " hPa\n" +
                    "Wind: " + weatherJsonObject.getString("windSpeedDay") + " km/h " + weatherJsonObject.getString("windDirDay"));
            weather.setMaxT(weatherJsonObject.getString("tempMax"));
            weather.setMinT(weatherJsonObject.getString("tempMin"));

            Log.d(TAG, weather.toString() + "...");

            items.add(weather);
        }
    }

}
