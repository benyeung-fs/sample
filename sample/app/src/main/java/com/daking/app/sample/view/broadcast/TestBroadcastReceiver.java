package com.daking.app.sample.view.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.daking.app.client_common.util.view.ActivityUtil;

/**
 * 广播接收器测试
 * Created by daking on 15/11/4.
 */
public class TestBroadcastReceiver extends BroadcastReceiver {
    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")){
            showTip("网络状态发生改变~");
        }
    }

    private void showTip(String tip){
        ActivityUtil.toast(tip);
        Log.i(TAG, TAG + ":" + tip);
    }
}
