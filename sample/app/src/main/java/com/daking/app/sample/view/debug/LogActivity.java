package com.daking.app.sample.view.debug;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.sample.R;

/**
 * 日志显示
 */
public class LogActivity extends BaseActivity {
    /**外部传入的日志内容对应的参数*/
    public static final String LOG = "log";
    /**外部传入的标题对应的参数*/
    public static final String TITLE = "title";

    private TextView tvLog;

    @Override
    public int bindLayout() {
        return R.layout.activity_log;
    }

    @Override
    public void initView(View view) {
        tvLog = (TextView) view.findViewById(R.id.tv_log);
    }

    @Override
    public void initData(Context mContext) {
        Intent intent = getIntent();
        String log = intent.getStringExtra(LOG);
        if (log != null){
            tvLog.setText(log);
        }
        String title = intent.getStringExtra(TITLE);
        if (title != null){
            setTitleTxt(title);
        }
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
}
