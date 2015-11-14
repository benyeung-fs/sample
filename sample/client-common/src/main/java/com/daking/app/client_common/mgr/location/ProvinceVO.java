package com.daking.app.client_common.mgr.location;

/**
 * 省份VO
 * Created by daking on 15/8/31.
 */
public class ProvinceVO {
    private int code;           // id
    private String name;        // 省份名
    private CityVO[] cities;    // 下属各城市

    public CityVO[] getCities() {
        return cities;
    }

    public void setCities(CityVO[] cities) {
        this.cities = cities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
