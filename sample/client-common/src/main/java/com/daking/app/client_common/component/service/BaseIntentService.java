package com.daking.app.client_common.component.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * IntentService基类
 * Created by daking on 15/11/3.
 */
public abstract class BaseIntentService extends IntentService {
    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();

    public BaseIntentService() {
        super("BaseIntentService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BaseIntentService(String name) {
        super(name);
    }

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
        return null;
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
}
