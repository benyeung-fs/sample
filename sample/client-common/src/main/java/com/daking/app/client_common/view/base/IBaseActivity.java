package com.daking.app.client_common.view.base;

import android.content.Context;
import android.view.View;

/**
 * Activity接口
 * Created by daking on 15/8/17.
 */
public interface IBaseActivity {
    /**
     * 绑定渲染视图的布局文件
     * @return 布局文件资源id
     */
    public int bindLayout();
    /**
     * 绑定标题栏渲染视图的布局文件
     * @return 布局文件资源id
     */
    public int bindTitleLayout();

    /**
     * 此窗体的标题
     */
    public CharSequence bindTitle();

    /**
     * 退后键文本
     */
    public String bindBackText();

    /**
     * 是否允许退后
     */
    public boolean allowBack();

    /**
     * 初始化主视图（onCreate方法中调用）
     */
    public void initView(final View view);

    /**
     * 初始化标题栏（onCreate方法中调用）
     */
    public void initTtile(final View view);

    /**
     * 数据处理操作（onCreate方法中调用）
     * @param mContext  当前Activity对象
     */
    public void initData(Context mContext);

    /**
     * 恢复刷新相关操作（onResume方法中调用）
     */
    public void resume();

    /**
     * 暂停相关操作（onPause方法中调用）
     */
    public void pause();

    /**
     * 销毁、释放资源相关操作（onDestroy方法中调用）
     */
    public void destroy();

    /**
     * 标题栏点击事件
     * @param view
     */
    public void onTitleClick(final View view);
}
