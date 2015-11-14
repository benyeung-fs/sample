package com.daking.app.sample.view.listview;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daking.app.client_common.util.view.ActivityUtil;
import com.daking.app.client_common.util.view.DisplayUtil;
import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.client_common.view.listview.CommonAdapter;
import com.daking.app.client_common.view.listview.PullToRefreshSwipeMenuListView;
import com.daking.app.client_common.view.listview.pulltorefresh.RefreshTime;
import com.daking.app.client_common.view.listview.swipemenu.SwipeMenu;
import com.daking.app.client_common.view.listview.swipemenu.SwipeMenuCreator;
import com.daking.app.client_common.view.listview.swipemenu.SwipeMenuItem;
import com.daking.app.sample.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 刷新侧滑listview
 */
public class RefreshSwipeListView extends BaseActivity implements PullToRefreshSwipeMenuListView.IXListViewListener {
    private List<ApplicationInfo> allAppList;
    private List<ApplicationInfo> curAppList;
    private int curIndex;
    private final int PAGESIZE = 5;
    private PullToRefreshSwipeMenuListView lvRefreshSwipe;
    private AppAdapter appAdapter;
    private SimpleDateFormat refreshDataFormat;

    @Override
    public int bindLayout() {
        return R.layout.activity_refreshswipe_listview;
    }

    @Override
    public void initView(View view) {
        refreshDataFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());

        allAppList = getPackageManager().getInstalledApplications(0);
        curAppList = new ArrayList<ApplicationInfo>();

        appAdapter = new AppAdapter(getApplicationContext(), curAppList, R.layout.lv_item_refreshswipe);
        lvRefreshSwipe = (PullToRefreshSwipeMenuListView) view.findViewById(R.id.lv_refreshswipe);
        lvRefreshSwipe.setAdapter(appAdapter);
        lvRefreshSwipe.setPullRefreshEnable(true);
        lvRefreshSwipe.setPullLoadEnable(true);
        lvRefreshSwipe.setXListViewListener(this);

        // 侧滑
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(new ColorDrawable((Color.rgb(0xC9, 0xC9, 0xCE))));
                openItem.setWidth(DisplayUtil.dip2px(90));
                openItem.setTitle("Open");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);

                SwipeMenuItem delItem = new SwipeMenuItem(getApplicationContext());
                delItem.setBackground(new ColorDrawable((Color.rgb(0xF9, 0x3F, 0x25))));
                delItem.setWidth(DisplayUtil.dip2px(90));
                delItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(delItem);
            }
        };
        lvRefreshSwipe.setMenuCreator(creator);

        lvRefreshSwipe.setOnMenuItemClickListener(new PullToRefreshSwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                ApplicationInfo appInfo = appAdapter.getItem(position);
                switch (index) {
                    case 0:
                        ActivityUtil.openApk(appInfo.packageName);
                        break;
                    case 1:
                        ActivityUtil.uninstallApk(appInfo.packageName);
                        break;
                }
            }
        });

        lvRefreshSwipe.setOnSwipeListener(new PullToRefreshSwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {

            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });
    }

    @Override
    public void initData(Context mContext) {
        onRefresh();
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
    public void onRefresh() {
        appAdapter.clear();
        curIndex = 0;
        updateAdapter(0, PAGESIZE);

        RefreshTime.setRefreshTime(getApplicationContext(), refreshDataFormat.format(new Date()));
        lvRefreshSwipe.setRefreshTime(RefreshTime.getRefreshTime(getApplicationContext()));
        lvRefreshSwipe.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        int endIndex = curIndex + PAGESIZE + 1;
        updateAdapter(curIndex+1, endIndex);
        lvRefreshSwipe.stopLoadMore();
    }

    private void updateAdapter(int start, int end){
        for (int i = start; i<end; i++){
            if (i >= allAppList.size()) break;

            ApplicationInfo appInfo = allAppList.get(i);
            if (appInfo != null){
                appAdapter.add(appInfo);
                curIndex++;
            }
        }
    }



    class AppAdapter extends CommonAdapter<ApplicationInfo>{
        public AppAdapter(Context context, List<ApplicationInfo> data, int layoutRes) {
            super(context, data, layoutRes);
        }

        @Override
        public void bind(ViewHolder holder, ApplicationInfo data) {
            // 应用图标
            ImageView ivIcon = (ImageView)holder.getView(R.id.iv_refreshswipe_icon);
            ivIcon.setImageDrawable(data.loadIcon(getPackageManager()));
            // 应用名
            TextView tvName = (TextView)holder.getView(R.id.tv_refreshswipe_name);
            tvName.setText(data.loadLabel(getPackageManager()));
        }
    }
}
