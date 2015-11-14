package com.daking.app.sample.view.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daking.app.client_common.component.service.BaseService;
import com.daking.app.client_common.mgr.base.ActivityMgr;
import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.sample.R;

/**
 * Service生命周期
 */
public class ServiceLifeActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvMsg;
    private Button btnStart;
    private Button btnStop;
    private Button btnBind;
    private Button btnUnbind;
    private Button btnAct;

    private boolean isBind;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, TAG + "-->onServiceConnected()");
            BaseService.ServiceBinder binder = (BaseService.ServiceBinder) service;
            binder.setServiceData(null);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, TAG + "-->onServiceDisconnected()");
        }
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_servicelife;
    }

    @Override
    public String bindTitle() {
        return (String) getApplicationContext().getResources().getText(R.string.act_serviceLife_name);
    }

    @Override
    public void initView(View view) {
        tvMsg = (TextView) view.findViewById(R.id.tv_serviceLife_msg);
        btnStart = (Button) view.findViewById(R.id.btn_serviceLife_start);
        btnStop = (Button) view.findViewById(R.id.btn_serviceLife_stop);
        btnBind = (Button) view.findViewById(R.id.btn_serviceLife_bind);
        btnUnbind = (Button) view.findViewById(R.id.btn_serviceLife_unbind);
        btnAct = (Button) view.findViewById(R.id.btn_serviceLife_act);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnBind.setOnClickListener(this);
        btnUnbind.setOnClickListener(this);
        btnAct.setOnClickListener(this);
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
        unbindService();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_serviceLife_start:
                Intent intentStart = new Intent(ServiceLifeActivity.this, LifeService.class);
                startService(intentStart);
                break;
            case R.id.btn_serviceLife_stop:
                Intent intentStop = new Intent(ServiceLifeActivity.this, LifeService.class);
                stopService(intentStop);
                break;
            case R.id.btn_serviceLife_bind:
                if (!isBind) {
                    Intent intentBind = new Intent(ServiceLifeActivity.this, LifeService.class);
                    bindService(intentBind, mServiceConnection, Context.BIND_AUTO_CREATE);
                    isBind = true;
                }
                break;
            case R.id.btn_serviceLife_unbind:
                unbindService();
                break;
            case R.id.btn_serviceLife_act:
                Intent intentAct = new Intent(ServiceLifeActivity.this, ServiceLifeActivity.class);
                ActivityMgr.getInstance().startActivity(intentAct, ActivityMgr.ANIM_METHOD_PUSH);
                break;
        }
    }

    private void unbindService() {
        if (isBind) {
            unbindService(mServiceConnection);
            isBind = false;
        }
    }

}
