package com.lkj.weatherforecast.entity;

public class Setting {
    private String key;
    private String value;
    private boolean visible;

    public Setting(String key) {
        this.key = key;
    }

    public Setting(String key, String value, boolean visible) {
        this.key = key;
        this.value = value;
        this.visible = visible;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
