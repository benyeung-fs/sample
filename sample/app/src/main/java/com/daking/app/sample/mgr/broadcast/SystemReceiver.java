package com.daking.app.sample.mgr.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.daking.app.client_common.util.view.ActivityUtil;

/**
 * 系统广播接收器
 */
public class SystemReceiver extends BroadcastReceiver {

    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();

    public SystemReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)){
            showTip("开机完毕~");
        }else if (Intent.ACTION_BATTERY_CHANGED.equals(action)){
            showTip("电量改变~");
        }else if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)){
            showTip("飞机模式改变~");
        }
    }

    private void showTip(String tip){
        ActivityUtil.toast(tip);
        Log.i(TAG, TAG+":"+tip);
    }
}
