package com.daking.app.sample.view.scrollview;

import android.content.Context;
import android.view.View;

import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.client_common.view.scrollview.RelationScrollView;
import com.daking.app.sample.R;

/**
 * 联动ScrollView
 * */
public class RelationScrollViewActivity extends BaseActivity {
    private RelationScrollView rsv1;
    private RelationScrollView rsv2;

    @Override
    public String bindTitle() {
        return (String) getApplicationContext().getResources().getText(R.string.act_relationScrollView_name);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_relation_scroll_view;
    }

    @Override
    public void initView(View view) {
        rsv1 = (RelationScrollView) view.findViewById(R.id.rsv1);
        rsv2 = (RelationScrollView) view.findViewById(R.id.rsv2);
        rsv1.setRelationScrollView(rsv2);
        rsv2.setRelationScrollView(rsv1);
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
