package com.daking.app.sample.view.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.daking.app.client_common.util.time.TimeUtil;

/**
 * 短信广播接收器
 */
public class SmsReceiver extends BroadcastReceiver {
    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, TAG+":收到短信!");

        Bundle bundle = intent.getExtras();
        SmsMessage msg = null;
        if (null != bundle){
            // "pdus"中存放了每条新接收到的短信数据
            Object[] smsObj = (Object[]) bundle.get("pdus");
            for (Object obj:smsObj) {
                msg = SmsMessage.createFromPdu((byte[]) obj);
                String addr = msg.getOriginatingAddress();
                long date = msg.getTimestampMillis();
                String body = msg.getDisplayMessageBody();

                String sms = "号码:"+addr+"\n"
                        +"时间:"+ TimeUtil.getTime(date)+"\n"
                        +"内容:"+body+"\n";
                Log.i(TAG, TAG+":\n"+sms);
            }
        }
    }
}
