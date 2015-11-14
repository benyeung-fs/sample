package com.daking.app.sample.view.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.sample.R;

/**
 * 偏好设定
 */
public class SharedPreferencesActivity extends BaseActivity {
    private static final String SHAREDPREFER_XML = "SHAREDPREFER_XML";
    private static final String KEY_CODE = "code";

    private EditText etCode;
    private Button btn1; // 设置
    private Button btn2; // 获取
    private Button btn3; // 删除

    @Override
    public int bindLayout() {
        return R.layout.activity_sharedpreferences;
    }

    @Override
    public String bindTitle() {
        return (String) getApplicationContext().getResources().getText(R.string.act_sharedPrefer_name);
    }

    @Override
    public void initView(View view) {
        etCode = (EditText) view.findViewById(R.id.et_sharedPrefer_Code);
        btn1 = (Button) view.findViewById(R.id.btn_sharedPrefer_btn1);
        btn2 = (Button) view.findViewById(R.id.btn_sharedPrefer_btn2);
        btn3 = (Button) view.findViewById(R.id.btn_sharedPrefer_btn3);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etCode.getText().toString().trim();
                SharedPreferences.Editor editor = getSharedPreferences(SHAREDPREFER_XML, Context.MODE_PRIVATE).edit();
                editor.putString(KEY_CODE, code);
                editor.commit();
                editor = null;
                showToast(getResources().getText(R.string.act_sharedPrefer_btn1).toString());
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFER_XML, Context.MODE_PRIVATE);
                String value = sharedPreferences.getString(KEY_CODE, "未设置过口令!");
                sharedPreferences = null;
                showToast(value);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(SHAREDPREFER_XML, Context.MODE_PRIVATE).edit();
                editor.remove(KEY_CODE);
                editor.commit();
                editor = null;
                showToast(getResources().getText(R.string.act_sharedPrefer_btn3).toString());
            }
        });
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

    }
}
