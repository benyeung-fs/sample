package com.daking.app.client_common.view.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daking.app.client_common.R;
import com.daking.app.client_common.mgr.base.ActivityMgr;
import com.daking.app.client_common.mgr.base.AppMgr;
import com.daking.app.client_common.mgr.event.ClientEvent;
import com.daking.app.client_common.util.view.ActivityUtil;
import com.daking.app.client_common.view.popup.WaitDialog;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;

/**
 * 项目Activity基类
 * Created by daking on 15/8/17.
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseActivity {
    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    private WeakReference<Activity> task = null;
    /**
     * 当前Activity的Action的渲染视图(即标题栏)
     */
    private View titleView = null;
    /**
     * 当前Activity渲染的视图View
     **/
    private View rootView = null;
    /**
     * 记录返回键退出标记时间
     */
    private long exitTime = 0;
    /**
     * 是否关注全局事件
     */
    private boolean isRegClientEvent = false;
    /**
     * 提示语组件
     */
    private Toast toast;

    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 飘提示语(支持html文本)
     */
    public void showToast(String tip) {
//        toast.cancel();
        toast.setText(Html.fromHtml(tip));
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG+"-->onCreate()");

        //设置渲染视图View
        rootView = LayoutInflater.from(this).inflate(bindLayout(), null);
        setContentView(rootView);
        //设置标题栏
        setTitleLayout();

        //将当前Activity存入队列
        task = new WeakReference<Activity>(this);
        ActivityMgr.getInstance().addTask(task);

        //初始化主视图
        initView(rootView);
        //初始化标题栏
        initTtile(titleView);
        //业务操作
        initData(this);

        // 始终显示overflow按钮
        displayOverflowMenu(this, false);
        // 竖屏显示
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 初始化提示语组件
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, TAG+"-->onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, TAG+"-->onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, TAG+"-->onResume()");
        WaitDialog.whetherShowWait();
        resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, TAG+"-->onPause()");
        pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, TAG+"-->onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, TAG+"-->onDestroy()");

        toast.cancel();
        toast = null;
        unregisterClientEvent();
        AppMgr.getInstance().addToRefWatcher(this);
        destroy();
        ActivityMgr.getInstance().removeTask(task);
    }

    /**
     * 获取当前Activity
     *
     * @return
     */
    protected Activity getContext() {
        if (task != null)
            return task.get();
        else
            return null;
    }

    /**
     * 获取当前Activity栈弱引用
     *
     * @return
     */
    protected WeakReference<Activity> getTask() {
        return task;
    }

    /**
     * 获取主渲染视图
     *
     * @return
     */
    protected View getRootView() {
        return rootView;
    }

    /**
     * 替换Fragment
     *
     * @param containerViewId 父容器资源id
     * @param fragment        要替换的Fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment) {
        getFragmentManager().beginTransaction().replace(containerViewId, fragment).commit();
    }


    /*********************************全局事件相关(开始)*********************************/
    /**
     * 监听全局事件ClientEvent
     * <br> 事件派发时会自动调用onEventMainThread,子类需重写此方法
     */
    protected void registerClientEvent() {
        if (!isRegClientEvent) {
            isRegClientEvent = true;
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 取消监听全局事件ClientEvent
     */
    protected void unregisterClientEvent() {
        if (isRegClientEvent) {
            isRegClientEvent = false;
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 接受到全局事件更新UI
     * <br> 子类需重写此方法
     */
    public void onEventMainThread(ClientEvent event) {
        if (isRegClientEvent) {
            throw new Error("BaseActivity监听全局事件,但没有重写onEventMainThread");
        }
    }
    /*********************************全局事件相关(结束)*********************************/


    /*********************************标题栏相关(开始)*********************************/
    /**
     * 获取标题栏渲染视图
     *
     * @return
     */
    protected View getTitleView() {
        return titleView;
    }

    /**
     * 是否显示标题栏
     */
    public void showTitle(boolean isShow){
        if (isShow){
            getSupportActionBar().show();
        }else{
            getSupportActionBar().hide();
        }
    }

    @Override
    public int bindTitleLayout() {
        return R.layout.actionbar_default;
    }

    @Override
    public String bindTitle() {
        return getContext().getResources().getText(R.string.actionbar_titleText).toString();
    }

    @Override
    public String bindBackText() {
        return null;
    }

    @Override
    public boolean allowBack() {
        return true;
    }

    /**
     * 设置标题内容
     */
    public void setTitleTxt(String txt){
        if (bindTitleLayout() == R.layout.actionbar_default){
            TextView tvTitle = (TextView) findViewById(R.id.tv_actionbar_title);
            tvTitle.setText(txt);
        }
    }

    /**
     * 设置标题栏
     */
    protected void setTitleLayout() {
        int layoutId = bindTitleLayout();
        ActionBar actionBar = getSupportActionBar();
        if (layoutId > 0 && null != actionBar) {
            actionBar.setDisplayShowHomeEnabled(false);
            // ActionBar设置自定义布局
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            titleView = inflater.inflate(layoutId, null);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(titleView, params);
        }
    }

    @Override
    public void initTtile(View view) {
        if (bindTitleLayout() == R.layout.actionbar_default) {
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_actionbar_title);
            TextView tvBack = (TextView) view.findViewById(R.id.tv_actionbar_back);
            ImageButton btnBack = (ImageButton) view.findViewById(R.id.btn_actionbar_back);

            String titleText = bindTitle();
            tvTitle.setText(titleText != null ? titleText : "");
            tvBack.setVisibility(View.INVISIBLE);
            btnBack.setVisibility(View.INVISIBLE);
            // 是否显示退后键
            int taskIndex = ActivityMgr.getInstance().hasTask(task);
            if (allowBack() && taskIndex > 0) {
                // 根据前一个Activity的bindBackText()确定返回键文本
                BaseActivity preAct = (BaseActivity) ActivityMgr.getInstance().getActivityByIndex(taskIndex - 1);
                if (null == preAct) return;
                String backText = preAct.bindBackText();
                if (backText != null && backText != "") {
                    tvBack.setText(backText);
                } else {
                    tvBack.setText(R.string.actionbar_backText);
                }

                tvBack.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.VISIBLE);

                tvBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onTitleClick(v);
                    }
                });
            }
        }
    }

    @Override
    public void onTitleClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_actionbar_back || i == R.id.tv_actionbar_back) {
            this.backHandler();
        }
    }
    /*********************************标题栏相关(结束)*********************************/


    /**
     * 显示或隐藏OverFlowMenu按钮
     *
     * @param mContext 上下文Context
     * @param isHide   是否隐藏
     */
    public void displayOverflowMenu(Context mContext, boolean isHide) {
        try {
            ViewConfiguration config = ViewConfiguration.get(mContext);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, isHide); // 显示或隐藏
            }
        } catch (Exception e) {
            Log.e("ActionBar", e.getMessage());
        }
    }

    /**
     * Actionbar点击返回键关闭事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.backHandler();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_right_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: // 返回键
                this.backHandler();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 处理后退操作(退出Act还是退出APP)
     */
    private void backHandler() {
        if (ActivityMgr.getInstance().getActivityNum() <= 1) {
            // 判断是否在3秒之内连续点击返回键，是则退出，否则不退出
            if (System.currentTimeMillis() - exitTime > 3000) {
                ActivityUtil.toast(getResources().getText(R.string.exit_app_tip).toString());
                // 将系统当前的时间赋值给exitTime
                exitTime = System.currentTimeMillis();
            } else {
                exitApp();
            }
        } else {
            if (allowBack()) exitActivity();
        }
    }

    /**
     * 退出此Activity
     */
    private void exitActivity() {
        finish();
        //关闭窗体动画显示
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_right_out);
    }

    /**
     * 退出应用程序
     */
    private void exitApp() {
        AppMgr.getInstance().exitApp();
    }

    /**
     * 进入全屏模式
     */
    public void enterFullScreen() {
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 退出全屏模式
     */
    public void exitFullScreen() {
        Window window = this.getWindow();
        final WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setAttributes(attrs);
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}
