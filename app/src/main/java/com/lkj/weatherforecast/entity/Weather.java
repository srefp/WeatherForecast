package com.lkj.weatherforecast.entity;

import java.util.Date;
import java.util.UUID;

public class Weather {

    private UUID uuid; //编号
    private String type; //天气类型
    private Date date; // 日期
    private String maxT; // 最大温度
    private String minT; // 最小温度
    private String info; // 天气信息

    public Weather() {
        this(UUID.randomUUID());
    }

    public Weather(UUID id) {
        uuid = id;
        date = new Date();
    }

    public Weather(UUID uuid, String type, Date date, String maxT, String minT, String info) {
        this.uuid = uuid;
        this.type = type;
        this.date = date;
        this.maxT = maxT;
        this.minT = minT;
        this.info = info;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMaxT() {
        return maxT;
    }

    public void setMaxT(String maxT) {
        this.maxT = maxT;
    }

    public String getMinT() {
        return minT;
    }

    public void setMinT(String minT) {
        this.minT = minT;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPhotoFilename() {
        return "IMG_" + getUuid().toString() + ".jpg";
    }
}
