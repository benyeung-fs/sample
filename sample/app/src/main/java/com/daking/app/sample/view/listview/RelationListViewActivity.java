package com.daking.app.sample.view.listview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.client_common.view.listview.CommonAdapter;
import com.daking.app.client_common.view.listview.RelationListView;
import com.daking.app.sample.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 联动ListView
 * */
public class RelationListViewActivity extends BaseActivity {
    private RelationListView rlv1;
    private RelationListView rlv2;

    private RelationAdapter adapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_relation_list_view;
    }

    @Override
    public String bindTitle() {
        return (String) getApplicationContext().getResources().getText(R.string.act_relationListView_name);
    }

    @Override
    public void initView(View view) {
        rlv1 = (RelationListView) view.findViewById(R.id.rlv1);
        rlv2 = (RelationListView) view.findViewById(R.id.rlv2);
        rlv1.setRelationListView(rlv2);
        rlv2.setRelationListView(rlv1);
    }

    @Override
    public void initData(Context mContext) {
        List<String> list = new ArrayList<String>();
        for(int i=0; i<20; i++){
            list.add("listiem"+i);
        }
        adapter = new RelationAdapter(getApplicationContext(), list, R.layout.lv_item_main);
        rlv1.setAdapter(adapter);
        rlv2.setAdapter(adapter);
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


    class RelationAdapter extends CommonAdapter<String>{

        public RelationAdapter(Context context, List<String> data, int layoutRes) {
            super(context, data, layoutRes);
        }

        @Override
        public void bind(ViewHolder holder, String data) {
            TextView tvTtile = (TextView) holder.getView(R.id.tv_title);
            tvTtile.setText(data);
        }
    }
}
