package com.daking.app.client_common.mgr.location;

import com.google.gson.Gson;

/**
 * 百度定位信息
 * Created by daking on 15/8/30.
 */
public class BDLocationVO {
    public static final int LOC_GPS = 1;        // gps定位结果
    public static final int LOC_NETWORK = 2;    // 网络定位结果
    public static final int LOC_OFFLINE = 3;    // 离线定位结果

    // 百度地图提供
    private long time;          // 定位时间(毫秒)
    private int locType;        // 定位结果码
    private double latitude;    // 纬度
    private double longitude;   // 经度
    private String country;     // 国家
    private String province;    // 省份
    private String city;        // 城市
    private float radius;
    private String addr;        // 详细地址
    private String desc;        // 位置语义化信息

    // 自定义
    private int provinceCode;    // 省份id
    private int cityCode;       // 城市id

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLocType() {
        return locType;
    }

    public void setLocType(int locType) {
        this.locType = locType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
}
