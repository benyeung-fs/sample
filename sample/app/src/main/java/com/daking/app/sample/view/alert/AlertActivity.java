package com.daking.app.sample.view.alert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.daking.app.client_common.util.bitmap.BitmapUtil;
import com.daking.app.client_common.util.time.TimeUtil;
import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.sample.MainActivity;
import com.daking.app.sample.R;

/**
 * 通知式控件
 */
public class AlertActivity extends BaseActivity implements View.OnClickListener {
    private Button btnShowNotice;
    private Button btnCloseNotice;

    private static int NOTICE_ID = 1; // 通知ID
    private NotificationManager mNotificationMgr;

    @Override
    public int bindLayout() {
        return R.layout.activity_alert;
    }

    @Override
    public String bindTitle() {
        return (String) getApplicationContext().getResources().getText(R.string.act_alert_name);
    }

    @Override
    public void initView(View view) {
        btnShowNotice = (Button) view.findViewById(R.id.btn_alert_showNotice);
        btnCloseNotice = (Button) view.findViewById(R.id.btn_alert_closeNotice);

        btnShowNotice.setOnClickListener(this);
        btnCloseNotice.setOnClickListener(this);
    }

    @Override
    public void initData(Context mContext) {
        mNotificationMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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
            case R.id.btn_alert_showNotice:
                // 设置点开通知栏的行为
                Intent intent = new Intent(AlertActivity.this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                Notification.Builder builder = new Notification.Builder(this);
                builder.setContentTitle("叶良辰") // 标题
                        .setContentText("我有一百种方法让你呆不下去~") // 内容
                        .setContentIntent(pendingIntent) // 通知栏的点击意图
                        .setTicker("收到叶良辰发来的消息") // 收到消息时状态栏显示的文字
                        .setWhen(TimeUtil.getCurrentTimeInLong()) // 通知时间
                        .setSmallIcon(R.mipmap.ic_logo) // 图标
                        .setAutoCancel(true) // 用户点击Notification面板是否让通知取消,默认为不取消.
                        .setDefaults(Notification.DEFAULT_ALL) // 提示方式:声音,闪灯,震动
                        .setOngoing(false); // 是否为一个正在进行中的通知.
                mNotificationMgr.notify(NOTICE_ID, builder.getNotification());
                break;
            case R.id.btn_alert_closeNotice:
                mNotificationMgr.cancel(NOTICE_ID);
                break;
        }
    }
}
