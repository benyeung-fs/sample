package com.daking.app.sample;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.daking.app.client_common.mgr.base.ActivityMgr;
import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.client_common.view.listview.CommonAdapter;
import com.daking.app.sample.mgr.main.MainVO;
import com.daking.app.sample.view.baidu.BDLocationActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {
    private ListView lvMain;
    private MainAdapter adapterMain;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public String bindTitle() {
        return (String) getApplicationContext().getResources().getText(R.string.act_main_name);
    }

    @Override
    public boolean allowBack() {
        return false;
    }

    @Override
    public void initView(View view) {
        lvMain = (ListView) view.findViewById(R.id.lv_main);
    }

    @Override
    public void initData(Context mContext) {
        List<MainVO> list = new ArrayList<MainVO>();

        MainVO mainVO1 = new MainVO("百度定位", "com.daking.app.sample.view.baidu.BDLocationActivity", MainVO.HANDLE_ACTIVITY);
        list.add(mainVO1);

        MainVO mainVO2 = new MainVO("Service生命周期(Log)", "com.daking.app.sample.view.service.ServiceLifeActivity", MainVO.HANDLE_ACTIVITY);
        list.add(mainVO2);
        MainVO mainVO13 = new MainVO("Service测试(Log)", "com.daking.app.sample.view.service.ServiceTestActivity", MainVO.HANDLE_ACTIVITY);
        list.add(mainVO13);
        MainVO mainVO14 = new MainVO("广播测试", "com.daking.app.sample.view.broadcast.BroadcastTestActivity", MainVO.HANDLE_ACTIVITY);
        list.add(mainVO14);

        MainVO mainVO3 = new MainVO("下拉刷新上拉加载列表", "com.daking.app.sample.view.listview.RefreshSwipeListView", MainVO.HANDLE_ACTIVITY);
        list.add(mainVO3);
        MainVO mainVO4 = new MainVO("联动ListView", "com.daking.app.sample.view.listview.RelationListViewActivity", MainVO.HANDLE_ACTIVITY);
        list.add(mainVO4);

        MainVO mainVO5 = new MainVO("底部导航栏", "com.daking.app.sample.view.viewpager.tabmenu.TabMenuActivity", MainVO.HANDLE_ACTIVITY);
        list.add(mainVO5);
        MainVO mainVO6 = new MainVO("微信6.0主界面", "", MainVO.HANDLE_ACTIVITY);
        list.add(mainVO6);

        MainVO mainVO7 = new MainVO("联动ScrollView", "com.daking.app.sample.view.scrollview.RelationScrollViewActivity", MainVO.HANDLE_ACTIVITY);
        list.add(mainVO7);

        MainVO mainVO8 = new MainVO("照相机", "com.daking.app.sample.view.photo.CameraActivity", MainVO.HANDLE_ACTIVITY);
        list.add(mainVO8);

        MainVO mainVO10 = new MainVO("Gallery", "com.daking.app.sample.view.photo.GalleryActivity", MainVO.HANDLE_ACTIVITY);
        list.add(mainVO10);

        MainVO mainVO11 = new MainVO("SharedPreferences", "com.daking.app.sample.view.data.SharedPreferencesActivity", MainVO.HANDLE_ACTIVITY);
        list.add(mainVO11);
        MainVO mainVO15 = new MainVO("常用ContentProvider", "com.daking.app.sample.view.data.ContentProviderActivity", MainVO.HANDLE_ACTIVITY);
        list.add(mainVO15);
        MainVO mainVO9 = new MainVO("SQLite", "com.daking.app.sample.view.data.SQLiteActivity", MainVO.HANDLE_ACTIVITY);
        list.add(mainVO9);
        MainVO mainVO12 = new MainVO("通知式控件", "com.daking.app.sample.view.alert.AlertActivity", MainVO.HANDLE_ACTIVITY);
        list.add(mainVO12);


        adapterMain = new MainAdapter(getApplicationContext(), list, R.layout.lv_item_main);
        lvMain.setAdapter(adapterMain);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainVO vo = adapterMain.getItem(position);
                if (vo.getType() == MainVO.HANDLE_ACTIVITY) {
                    // 跳转Activity
                    String content = vo.getContent();
                    if (content != null && content != "") {
                        try {
                            Class cls = Class.forName(content);
                            Activity act = (Activity) cls.newInstance();
                            cls = act.getClass();

                            ActivityMgr.getInstance().startActivity(MainActivity.this,
                                    new Intent(MainActivity.this, cls),
                                    ActivityMgr.ANIM_METHOD_PUSH);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                }
            }
        });
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


    public class MainAdapter extends CommonAdapter<MainVO> {

        public MainAdapter(Context context, List<MainVO> data, int layoutRes) {
            super(context, data, layoutRes);
        }

        @Override
        public void bind(ViewHolder holder, MainVO vo) {
            TextView tvTtile = (TextView) holder.getView(R.id.tv_title);
            tvTtile.setText(vo.getTitle());
        }
    }
}
