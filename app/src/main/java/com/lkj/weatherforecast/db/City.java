package com.lkj.weatherforecast.db;

import com.lkj.weatherforecast.lab.LocationLab;

import org.litepal.crud.DataSupport;

import java.util.List;

public class City extends DataSupport {

    private int id;

    private String cityName;

    private int cityCode;

    private int provinceId;

    private String lngAndLat;

    public String getLngAndLat() {
        return lngAndLat;
    }

    public void setLngAndLat(String lngAndLat) {
        this.lngAndLat = lngAndLat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

}
