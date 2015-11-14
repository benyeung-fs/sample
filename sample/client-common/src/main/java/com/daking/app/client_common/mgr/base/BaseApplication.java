package com.daking.app.client_common.mgr.base;

import android.app.Application;
import android.util.Log;

/**
 * 基本Application
 * Created by daking on 15/11/14.
 */
public class BaseApplication extends Application{
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, TAG+" onCreate()");

        InitMgr.getInstance().init(this);
    }
}
