package com.daking.app.sample.view.broadcast;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.sample.R;


/**
 * 广播测试
 */
public class BroadcastTestActivity extends BaseActivity implements View.OnClickListener {
    private Button btnReg;
    private Button btnUnreg;

    private TestBroadcastReceiver testBroadcastReceiver;

    @Override
    public int bindLayout() {
        return R.layout.activity_broadcast_test;
    }

    @Override
    public void initView(View view) {
        btnReg = (Button) view.findViewById(R.id.btn_broadcastTest_reg);
        btnUnreg = (Button) view.findViewById(R.id.btn_broadcastTest_unreg);

        btnReg.setOnClickListener(this);
        btnUnreg.setOnClickListener(this);
    }

    @Override
    public void initData(Context mContext) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        if (testBroadcastReceiver != null){
            unregisterReceiver(testBroadcastReceiver);
            testBroadcastReceiver = null;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_broadcastTest_reg:
                if (testBroadcastReceiver == null){
                    testBroadcastReceiver = new TestBroadcastReceiver();
                    IntentFilter filter = new IntentFilter();
                    filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                    registerReceiver(testBroadcastReceiver, filter);
                }else{
                    showToast("已注册!");
                }
                break;
            case R.id.btn_broadcastTest_unreg:
                if (testBroadcastReceiver != null){
                    unregisterReceiver(testBroadcastReceiver);
                    testBroadcastReceiver = null;
                }else{
                    showToast("未注册过!");
                }
                break;
        }
    }
}
