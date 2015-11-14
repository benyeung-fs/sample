package com.daking.app.client_common.mgr.base;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.daking.app.client_common.mgr.setting.SettingMgr;
import com.daking.app.client_common.util.service.AtyMgrUtil;
import com.daking.app.client_common.util.view.DisplayUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.Vector;

/**
 * 应用管理器
 * Created by daking on 15/8/21.
 */
public class AppMgr{
    private volatile static AppMgr appMgr;

    private Context context;
    private boolean isInit; // 是否初始化

    private RefWatcher refWatcher; // 引用监视器,来自于LeakCanary开源库,用于检测内存泄漏

    private int screen_w_px; // 屏幕宽度,像素
    private int screen_h_px; // 屏幕高度,像素
    private int screen_w_dp; // 屏幕宽度,dp
    private int screen_h_dp; // 屏幕高度,dp
    private float screen_density; // 屏幕密度/160
    private float screen_densityDpi; // 屏幕密度

    private Vector<BaseMgr> baseMgrs;

    public static AppMgr getInstance()
    {
        if( appMgr==null ) {
            synchronized ( AppMgr.class ){
                if ( appMgr==null ) {
                    appMgr = new AppMgr();
                }
            }
        }
        return appMgr;
    }

    /**
     * 初始化
     * @param context 传入activity.getApplicationContext
     */
    public void init( Context context ) {
        if (isInit) return;

        this.context = context;
        // 开启内存泄漏检测
        if ( SettingMgr.getInstance().isDebug() )
        {
            refWatcher = LeakCanary.install( (Application)context );
        }
        // 计算屏幕宽高
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screen_densityDpi = dm.densityDpi;
        screen_density = dm.density;
        screen_w_px = dm.widthPixels;
        screen_h_px = dm.heightPixels;
        screen_w_dp = DisplayUtil.px2dip(screen_w_px);
        screen_h_dp = DisplayUtil.px2dip(screen_h_px);
        Log.i( InitMgr.TAG, "屏幕密度:"+screen_densityDpi+"dp, "+"屏幕宽:"+screen_w_px+"像素, "+screen_w_dp+"dp;  "+
                "屏幕高:"+screen_h_px+"像素, "+screen_h_dp+"dp." );

        baseMgrs = new Vector<BaseMgr>();

        isInit = true;
    }

    /**获取应用的上下文*/
    public Context getContext()
    {
        return context;
    }

    /**获取内存泄漏监视器*/
    public RefWatcher getRefWatcher()
    {
        return refWatcher;
    }

    /**获取屏幕密度*/
    public float getScreen_densityDpi()
    {
        return screen_densityDpi;
    }
    /**获取屏幕密度/160*/
    public float getScreen_density()
    {
        return screen_density;
    }
    /**获取屏幕宽度(像素)*/
    public int getScreen_w_px()
    {
        return screen_w_px;
    }
    /**获取屏幕高度(像素)*/
    public int getScreen_h_px()
    {
        return screen_h_px;
    }
    /**获取屏幕宽度dp*/
    public int getScreen_w_dp()
    {
        return screen_w_dp;
    }
    /**获取屏幕高度dp*/
    public int getScreen_h_dp()
    {
        return screen_h_dp;
    }

    /**
     * 检测某对象的内存泄漏情况(debug模式有效)
     * @param obj
     */
    public void addToRefWatcher( Object obj )
    {
        if ( SettingMgr.getInstance().isDebug() && refWatcher != null )
        {
            refWatcher.watch(obj);
        }
    }

    /**
     * 添加业务管理器
     * @param baseMgr
     */
    public void addToBaseMgrs( BaseMgr baseMgr )
    {
        baseMgrs.add( baseMgr );
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        // 关闭所有Activity
        ActivityMgr.getInstance().removeAllTask();
        // 官方Activity管理器
        try {
            AtyMgrUtil.killBgProcesses();
            System.exit(0);
        }catch (Exception e){}

        // 销毁各业务管理器
        for ( BaseMgr baseMgr : baseMgrs )
        {
            baseMgr.destroy();
        }
        InitMgr.getInstance().destroy();
    }
}
