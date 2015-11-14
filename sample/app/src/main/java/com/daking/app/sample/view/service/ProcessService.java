package com.daking.app.sample.view.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.daking.app.client_common.component.service.BaseService;
import com.daking.app.client_common.mgr.event.ClientEvent;

/**
 * 独立进程Service
 */
public class ProcessService extends BaseService {
    public static String PROCESS_NAME = "com.daking.app.sample:sampleProcess1";

    private boolean isAlive = false;

    public ProcessService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        isAlive = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(){
            @Override
            public void run() {
                super.run();

                while (isAlive){
                    try {
                        sleep(5000);

                        Log.i(TAG, TAG+" is running...");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isAlive = false;
    }

    @Override
    public void setServiceData(ClientEvent event) {

    }
}
