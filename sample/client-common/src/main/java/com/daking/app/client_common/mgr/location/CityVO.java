package com.daking.app.client_common.mgr.location;

/**
 * 城市VO
 * Created by daking on 15/8/31.
 */
public class CityVO {
    public static final int NOT_HOT = 0;
    public static final int HOT = 1;

    private int code;               // id
    private String name;            // 城市名
    private int hot;                // 是否热门城市,是1否0
    private DistrictVO[] districts; // 下属区域
    private int provinceCode;       // 所属省份id

    public DistrictVO[] getDistricts() {
        return districts;
    }

    public void setDistricts(DistrictVO[] districts) {
        this.districts = districts;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
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

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
