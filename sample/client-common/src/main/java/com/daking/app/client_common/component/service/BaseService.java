package com.daking.app.client_common.component.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.daking.app.client_common.mgr.event.ClientEvent;

/**
 * 项目Service基类
 * Created by daking on 15/11/3.
 */
public abstract class BaseService extends Service implements IBaseService{
    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        Log.d(TAG, TAG + "-->onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, TAG+"-->onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, TAG+"-->onRebind()");
        super.onRebind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, TAG+"-->onBind()");
        return new ServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, TAG+"-->onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, TAG+"-->onDestroy()");
        super.onDestroy();
    }



    public class ServiceBinder extends Binder{
        public void setServiceData(ClientEvent event){
            BaseService.this.setServiceData(event);
        }

        public BaseService getService(){
            return BaseService.this;
        }
    }
}
