package com.daking.app.client_common.mgr.location;

/**
 * 区域VO
 * Created by daking on 15/8/31.
 */
public class DistrictVO {
    private int code;       // id
    private String name;    // 区域名
    private int cityCode;   // 所属城市id

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
}
