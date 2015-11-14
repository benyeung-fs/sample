package com.daking.app.sample.view.baidu;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.daking.app.client_common.mgr.event.AppEventName;
import com.daking.app.client_common.mgr.event.ClientEvent;
import com.daking.app.client_common.mgr.location.BDLoactionMgr;
import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.sample.R;

import java.util.List;

public class BDLocationActivity extends BaseActivity {
    private TextView tvMsg;

    @Override
    public int bindLayout() {
        return R.layout.activity_bdlocation;
    }

    @Override
    public String bindTitle() {
        return (String) getApplicationContext().getResources().getText(R.string.act_bdlocation_name);
    }

    @Override
    public void initView(View view) {
        tvMsg = (TextView) view.findViewById(R.id.tv_msg);

        Button btn1 = (Button) view.findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BDLoactionMgr.getInstance().start();
                tvMsg.setText("");
            }
        });
        Button btn2 = (Button) view.findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BDLoactionMgr.getInstance().stop();
                tvMsg.setText("");
            }
        });

        this.registerClientEvent();
    }

    @Override
    public void initData(Context mContext) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onEventMainThread(ClientEvent event) {
        int type = event.getType();
        switch (type){
            case AppEventName.LOCATION_SUCCESS:
                BDLocation location = BDLoactionMgr.getInstance().getBdLocation();
                if (location != null){
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
                    tvMsg.setText(
                            getResources().getText(R.string.act_bdlocation_tvMsg).toString()
                                    + sb.toString());
                }
                break;
        }
    }
}
