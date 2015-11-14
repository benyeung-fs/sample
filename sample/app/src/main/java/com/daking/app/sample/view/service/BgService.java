package com.daking.app.sample.view.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.daking.app.client_common.component.service.BaseService;
import com.daking.app.client_common.mgr.event.ClientEvent;
import com.daking.app.client_common.util.time.TimeUtil;

/**
 * 后台服务
 * Created by daking on 15/11/3.
 */
public class BgService extends BaseService {
    public static final int REQCODE_BGSERVICE = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 每次启动服务都开启一条新的线程来处理要执行的操作,如请求服务端,此处是打印当前时间
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, TAG+ TimeUtil.getCurrentTimeInString());
            }
        }).start();
        // 利用闹钟定时发送广播,从而再次启动此服务
        long startTime = SystemClock.elapsedRealtime() + 2*1000;
        Intent intentPi = new Intent(getApplicationContext(), AlarmReceiver.class);
        intentPi.setFlags(PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pi = PendingIntent.getBroadcast(this, BgService.REQCODE_BGSERVICE, intentPi, 0);
        ((AlarmManager) getSystemService(Context.ALARM_SERVICE))
                .set(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void setServiceData(ClientEvent event) {

    }
}
