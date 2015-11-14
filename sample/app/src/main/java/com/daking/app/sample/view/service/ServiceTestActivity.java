package com.daking.app.sample.view.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.daking.app.client_common.util.service.AtyMgrUtil;
import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.sample.R;

/**
 * Service测试
 */
public class ServiceTestActivity extends BaseActivity implements View.OnClickListener {
    private Button btnIntentService;
    private Button btnStartFg;
    private Button btnStopFg;
    private Button btnStartBg;
    private Button btnStopBg;
    private Button btnStartProcess;
    private Button btnStopProcess;

    /**
     * 此PI与BgService类中onStartCommand定义的闹钟PI是对等的.
     * BgService中使用此PI来发送广播,进而重启后台轮询服务BgService;
     * 此Aty使用此PI来取消闹钟.
     */
    private PendingIntent piBgService;

    @Override
    public int bindLayout() {
        return R.layout.activity_servicetest;
    }

    @Override
    public String bindTitle() {
        return (String) getApplicationContext().getResources().getText(R.string.act_serviceTest_name);
    }

    @Override
    public void initView(View view) {
        btnIntentService = (Button) view.findViewById(R.id.btn_serviceTest_intentService);
        btnStartFg = (Button) view.findViewById(R.id.btn_serviceTest_startFg);
        btnStopFg = (Button) view.findViewById(R.id.btn_serviceTest_stopFg);
        btnStartBg = (Button) view.findViewById(R.id.btn_serviceTest_startBg);
        btnStopBg = (Button) view.findViewById(R.id.btn_serviceTest_stopBg);
        btnStartProcess = (Button) view.findViewById(R.id.btn_serviceTest_startProcess);
        btnStopProcess = (Button) view.findViewById(R.id.btn_serviceTest_stopProcess);

        btnIntentService.setOnClickListener(this);
        btnStartFg.setOnClickListener(this);
        btnStopFg.setOnClickListener(this);
        btnStartBg.setOnClickListener(this);
        btnStopBg.setOnClickListener(this);
        btnStartProcess.setOnClickListener(this);
        btnStopProcess.setOnClickListener(this);
    }

    @Override
    public void initData(Context mContext) {
        Intent intentPi = new Intent(getApplicationContext(), AlarmReceiver.class);
        intentPi.setFlags(PendingIntent.FLAG_UPDATE_CURRENT);
        piBgService = PendingIntent.getBroadcast(getApplicationContext(), BgService.REQCODE_BGSERVICE,
                intentPi, 0);
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
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_serviceTest_intentService:
                // 因为Android5.0以后不允许隐式启动服务,因此必须设置Action和包名
                Intent intent1 = new Intent();
                intent1.setAction(TestIntentService.INTENT_ACTION_NAME);
                intent1.setPackage(getPackageName());
                intent1.putExtra("param", TestIntentService.ACTOIN1);

                Intent intent2 = new Intent();
                intent2.setAction(TestIntentService.INTENT_ACTION_NAME);
                intent2.setPackage(getPackageName());
                intent2.putExtra("param", TestIntentService.ACTOIN2);

                Intent intent3 = new Intent();
                intent3.setAction(TestIntentService.INTENT_ACTION_NAME);
                intent3.setPackage(getPackageName());
                intent3.putExtra("param", TestIntentService.ACTOIN3);

                startService(intent1);
                startService(intent2);
                startService(intent3);
                break;
            case R.id.btn_serviceTest_startFg:
                startService(new Intent(ServiceTestActivity.this, FgService.class));
                break;
            case R.id.btn_serviceTest_stopFg:
                stopService(new Intent(ServiceTestActivity.this, FgService.class));
                break;
            case R.id.btn_serviceTest_startBg:
                Intent intentBg = new Intent(ServiceTestActivity.this, BgService.class);
                startService(intentBg);
                break;
            case R.id.btn_serviceTest_stopBg:
                stopService(new Intent(ServiceTestActivity.this, BgService.class));
                AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmMgr.cancel(piBgService);
                break;
            case R.id.btn_serviceTest_startProcess:
                startService(new Intent(ServiceTestActivity.this, ProcessService.class));
                break;
            case R.id.btn_serviceTest_stopProcess:
                if (AtyMgrUtil.isProcessRunning(ProcessService.PROCESS_NAME)) {
                    stopService(new Intent(ServiceTestActivity.this, ProcessService.class));
                } else {
                    showToast("独立进程服务\""+ProcessService.PROCESS_NAME+"\"未被启动!");
                }
                break;
        }
    }
}
