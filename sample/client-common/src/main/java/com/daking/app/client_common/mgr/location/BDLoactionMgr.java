package com.daking.app.client_common.mgr.location;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;

import com.google.gson.Gson;

import com.daking.app.client_common.R;
import com.daking.app.client_common.mgr.event.AppEventName;
import com.daking.app.client_common.mgr.event.ClientEvent;
import com.daking.app.client_common.mgr.setting.SettingMgr;
import com.daking.app.client_common.util.file.FileUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 百度地图管理器
 * Created by daking on 15/8/30.
 */
public class BDLoactionMgr {
    // 经纬度规格
    public static final String COORTYPE_GCJ = "gcj02";  // 国家测绘局标准
    public static final String COORTYPE_BD = "bd09ll";  //百度经纬度标准
    public static final String COORTYPE_BD2 = "bd09";   //百度墨卡托标准

    private volatile static BDLoactionMgr locationMgr;

    private boolean isInit;             // 是否初始化
    private boolean isLocation;         // 是否定位成功
    private BDLocationVO bdLocationVO;  // 定位信息
    private BDLocation bdLocation;      // 百度sdk返回的定位信息

    private Map<String, ProvinceVO> code2province;   // id找省份
    private Map<String, CityVO> code2city;           // id找城市
    private Map<String, DistrictVO> code2District;   // id找市区
    private Map<String, ProvinceVO> name2province;   // 名字找省份
    private Map<String, CityVO> name2city;           // 名字找城市
    private Map<String, CityVO> code2hotCity;        // 热门城市

    private LocationClient locationClient = null;
    private BDLocationListener bdLocationListener = null;

    public static BDLoactionMgr getInstance() {
        if (locationMgr == null) {
            synchronized (BDLoactionMgr.class) {
                if (locationMgr == null) {
                    locationMgr = new BDLoactionMgr();
                }
            }
        }
        return locationMgr;
    }

    /**
     * 初始化百度地图管理器(必须要在主线程中调用)
     */
    public void init(Context context) {
        if (isInit) return;

        SDKInitializer.initialize(context);

        locationClient = new LocationClient(context);
        bdLocationListener = new BaiduLocationListener();
        locationClient.registerLocationListener(bdLocationListener);
        initLocationOption();

        bdLocationVO = new BDLocationVO();
        initAllCity();

        isInit = true;
    }

    public void destroy() {
        stop();
        isLocation = false;
    }

    /**
     * 开始定位
     */
    public void start() {
        if (isInit) {
            locationClient.start();
        }
    }

    /**
     * 停止定位
     */
    public void stop() {
        if (isInit) {
            locationClient.stop();
        }
    }

    /**
     * 获取定位信息
     */
    public BDLocationVO getBdLocationVO() {
        if (isLocation) {
            return bdLocationVO;
        }
        return null;
    }

    /**
     * 获取百度定位信息
     */
    public BDLocation getBdLocation() {
        return bdLocation;
    }

    /**
     * 通过id找省份
     */
    public ProvinceVO getProvinceByCode(int code) {
        return code2province.get(String.valueOf(code));
    }

    /**
     * 通过id找城市
     */
    public CityVO getCityByCode(int code) {
        return code2city.get(String.valueOf(code));
    }

    /**
     * 通过id找市区
     */
    public DistrictVO getDistrictByCode(int code) {
        return code2District.get(String.valueOf(code));
    }

    /**
     * 通过名字找省份
     */
    public ProvinceVO getProvinceByName(String name) {
        return code2province.get(name);
    }

    /**
     * 通过名字找城市
     */
    public CityVO getCityByName(String name) {
        return code2city.get(name);
    }

    /**通过模糊名字找到省份*/
    public ProvinceVO findProvinceByName(String name) {
        if (null == name) return null;

        ProvinceVO vo = getProvinceByName(name);
        if (vo == null) {
            Iterator<Map.Entry<String, ProvinceVO>> it = name2province.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, ProvinceVO> entry = it.next();
                ProvinceVO value = entry.getValue();
                String proName = value.getName();
                if (proName.contains(name) || name.contains(proName)) {
                    vo = value;
                    break;
                }
            }
        }
        return vo;
    }

    /**通过模糊名字找到城市*/
    public CityVO findCityByName(String name) {
        if (null == name) return null;

        CityVO vo = getCityByName(name);
        if (vo == null) {
            Iterator<Map.Entry<String, CityVO>> it = name2city.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, CityVO> entry = it.next();
                CityVO value = entry.getValue();
                String cName = value.getName();
                if (cName.contains(name) || name.contains(cName)) {
                    vo = value;
                    break;
                }
            }
        }
        return vo;
    }

    private void initLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式(分为高精度，低功耗，仅设备)
        option.setCoorType(COORTYPE_BD);//可选，默认gcj02，设置返回的定位结果坐标系，
        option.setScanSpan(10000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationClient.setLocOption(option);
    }

    private void initAllCity() {
        code2province = new HashMap<String, ProvinceVO>();
        code2city = new HashMap<String, CityVO>();
        code2District = new HashMap<String, DistrictVO>();
        name2province = new HashMap<String, ProvinceVO>();
        name2city = new HashMap<String, CityVO>();
        code2hotCity = new HashMap<String, CityVO>();
        String rawContent = FileUtil.getRaw(R.raw.allcity);
        try {
            JSONArray array = new JSONArray(rawContent);
            // 省份
            for (int i = 0; i < array.length(); i++) {
                String value = array.getString(i);
                ProvinceVO provinceVO = new Gson().fromJson(value, ProvinceVO.class);
                code2province.put(String.valueOf(provinceVO.getCode()), provinceVO);
                name2province.put(provinceVO.getName(), provinceVO);
                CityVO[] cities = provinceVO.getCities();
                if (cities == null) continue;
                // 城市
                for (int j = 0; j < cities.length; j++) {
                    CityVO cityVO = cities[j];
                    cityVO.setProvinceCode(provinceVO.getCode());
                    code2city.put(String.valueOf(cityVO.getCode()), cityVO);
                    name2city.put(cityVO.getName(), cityVO);
                    if (cityVO.getHot() == CityVO.HOT) {
                        code2hotCity.put(String.valueOf(cityVO.getCode()), cityVO);
                    }
                    DistrictVO[] districts = cityVO.getDistricts();
                    if (districts == null) continue;
                    // 区域
                    for (int k = 0; k < districts.length; k++) {
                        DistrictVO districtVO = districts[k];
                        districtVO.setCityCode(cityVO.getCode());
                        code2District.put(String.valueOf(districtVO.getCode()), districtVO);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 实现实时位置回调监听
     */
    public class BaiduLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            bdLocation = location;
            int locType = location.getLocType();
            boolean isResult = false;
            if (locType == BDLocation.TypeGpsLocation) { // gps定位
                isResult = true;
                bdLocationVO.setLocType(BDLocationVO.LOC_GPS);
                bdLocationVO.setAddr(location.getAddrStr());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) { // 网络定位
                isResult = true;
                bdLocationVO.setLocType(BDLocationVO.LOC_NETWORK);
                bdLocationVO.setAddr(location.getAddrStr());
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) { // 离线定位
                isResult = true;
                bdLocationVO.setLocType(BDLocationVO.LOC_OFFLINE);
            }

            if (isResult) {
                isLocation = true;
                bdLocationVO.setTime(System.currentTimeMillis());
                bdLocationVO.setLatitude(location.getLatitude());
                bdLocationVO.setLongitude(location.getLongitude());
                bdLocationVO.setCountry(location.getCountry());
                bdLocationVO.setProvince(location.getProvince());
                bdLocationVO.setCity(location.getCity());
                bdLocationVO.setRadius(location.getRadius());
                bdLocationVO.setDesc(location.getLocationDescribe());
                // 设置自定义code
                ProvinceVO proVo = findProvinceByName(bdLocationVO.getProvince());
                if (proVo != null) bdLocationVO.setProvinceCode(proVo.getCode());
                CityVO cityVo = findCityByName(bdLocationVO.getCity());
                if (cityVo != null) bdLocationVO.setCityCode(cityVo.getCode());

                EventBus.getDefault().post(new ClientEvent(AppEventName.LOCATION_SUCCESS));
            }

            if (SettingMgr.getInstance().isDebug()) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlongitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\ncountry :");
                sb.append(location.getCountry());
                sb.append("\nprovince :");
                sb.append(location.getProvince());
                sb.append("\ncity :");
                sb.append(location.getCity());
                if (bdLocationVO != null) {
                    sb.append("\nprovinceCode :");
                    sb.append(bdLocationVO.getProvinceCode());
                    sb.append("\ncityCode :");
                    sb.append(bdLocationVO.getCityCode());
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：公里每小时
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndirection : ");
                    sb.append(location.getDirection());
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");

                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());
                    //运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                sb.append("\nlocationdescribe : ");// 位置语义化信息
                sb.append(location.getLocationDescribe());
                List<Poi> list = location.getPoiList();// POI信息
                if (list != null) {
                    sb.append("\npoilist size = : ");
                    sb.append(list.size());
                    for (Poi p : list) {
                        sb.append("\npoi= : ");
                        sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                    }
                }
                Log.i("Baidu", sb.toString());
            }
        }
    }
}
