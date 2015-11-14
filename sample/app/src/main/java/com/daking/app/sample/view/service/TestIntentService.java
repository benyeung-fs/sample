package com.daking.app.sample.view.service;

import android.content.Intent;
import android.util.Log;

import com.daking.app.client_common.component.service.BaseIntentService;
import com.daking.app.client_common.mgr.base.ActivityMgr;

/**
 * 测试IntentService
 * Created by daking on 15/11/3.
 */
public class TestIntentService extends BaseIntentService {
    public static final String INTENT_ACTION_NAME = "com.daking.app.sample.view.service.TestIntentService";

    public static final String ACTOIN1 = "S1";
    public static final String ACTOIN2 = "S2";
    public static final String ACTOIN3 = "S3";

    public TestIntentService() {
        super("TestIntentService");
    }

    public TestIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getStringExtra("param");
        switch (action) {
            case ACTOIN1:
                showLog(ACTOIN1);
                break;
            case ACTOIN2:
                showLog(ACTOIN2);
                break;
            case ACTOIN3:
                showLog(ACTOIN3);
                break;
            default:
                return;
        }
        // 模拟耗时操作时间
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**输出日志*/
    private void showLog(String content) {
        Log.i(TAG, TAG + ":" + content);
    }
}
