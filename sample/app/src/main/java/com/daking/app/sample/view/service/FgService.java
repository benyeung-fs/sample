package com.daking.app.sample.view.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;

import com.daking.app.client_common.component.service.BaseService;
import com.daking.app.client_common.mgr.event.ClientEvent;
import com.daking.app.client_common.util.time.TimeUtil;
import com.daking.app.sample.MainActivity;
import com.daking.app.sample.R;

/**
 * 前台服务
 * Created by daking on 15/11/3.
 */
public class FgService extends BaseService {
    public static final int SERVICE_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setContentTitle("前台服务")
                .setContentText("正在运行...")
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(),
                        0, new Intent(this, MainActivity.class), 0))
                .setWhen(TimeUtil.getCurrentTimeInLong())
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL);
        startForeground(SERVICE_ID, builder.getNotification());
    }

    @Override
    public void setServiceData(ClientEvent event) {

    }
}
