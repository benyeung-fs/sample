package com.daking.app.sample.view.viewpager.tabmenu;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.daking.app.client_common.view.base.BaseFragmentV4;
import com.daking.app.sample.R;

/**
 * 底部导航栏的第一个界面
 * Created by daking on 15/11/2.
 */
public class TabMenuFragment1 extends BaseFragmentV4 {

    @Override
    public int bindLayout() {
        return R.layout.layout_tabmenu_fragment;
    }

    @Override
    public void initView(View view) {
        TextView tv = (TextView) view.findViewById(R.id.tv_tabmenu_fragment);
        tv.setText(this.getClass().getSimpleName());
    }

    @Override
    public void initData(Context mContext) {

    }
}
