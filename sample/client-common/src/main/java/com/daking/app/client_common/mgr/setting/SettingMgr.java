package com.daking.app.client_common.mgr.setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;


/**
 * 设置管理器
 * Created by daking on 15/8/14.
 */
public class SettingMgr {
    private volatile static SettingMgr settingMgr;

    private Context context;
    private PackageManager curPackMgr;
    private PackageInfo curPackInfo;
    private boolean isInit; // 是否初始化

    public static SettingMgr getInstance()
    {
        if( settingMgr==null ) {
            synchronized ( SettingMgr.class ){
                if ( settingMgr==null ) {
                    settingMgr = new SettingMgr();
                }
            }
        }
        return settingMgr;
    }

    /**
     * 初始化
     * @param context 传入activity.getApplicationContext
     */
    public void init( Context context )
    {
        if ( isInit ) return;

        this.context = context;
        this.curPackMgr = context.getPackageManager();
        try {
            this.curPackInfo = this.curPackMgr.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        this.isInit = true;
    }

    /**
     * 获取当前应用的PackageManager
     * @return
     */
    public PackageManager getCurPackMgr()
    {
        if ( isInit ) {
            return this.curPackMgr;
        }
        return null;
    }

    /**
     * 获取当前应用的PackageInfo
     * @return
     */
    public PackageInfo getCurPackInfo()
    {
        if ( isInit ) {
            return this.curPackInfo;
        }
        return null;
    }

    /**
     * 获取应用名
     * @return
     */
    public String getAppName()
    {
        if ( isInit ) {
            try {
                ApplicationInfo info = this.curPackMgr.getApplicationInfo(context.getPackageName(), 0);
                return String.valueOf( info.loadLabel( curPackMgr ) );
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取应用版本
     * @return
     */
    public String getVersionName()
    {
        if ( isInit )
        {
            return curPackInfo.versionName;
        }
        return null;
    }

    /**
     * 获取应用版本号
     * @return
     */
    public int getVersionCode()
    {
        if ( isInit )
        {
            return curPackInfo.versionCode;
        }
        return 0;
    }

    /**
     * 是否开启debug模式
     */
    public boolean isDebug()
    {
        Bundle metaData = getAppMetaData();
        if ( null != metaData )
        {
            return metaData.getBoolean( "isDebug",false );
        }
        return false;
    }

    /**
     * 获取资源地址
     */
    public String getResourceURL()
    {
        Bundle metaData = getAppMetaData();
        if ( null != metaData )
        {
            return metaData.getString( "resourceURL", "http://192.168.0.251:81/justdo-develop/" );
        }
        return null;
    }

    /**
     * 获取用户数据地址
     */
    public String getUserDataURL()
    {
        Bundle metaData = getAppMetaData();
        if ( null != metaData )
        {
            return metaData.getString( "userDataURL", "http://192.168.0.251:81/justDoResource/" );
        }
        return null;
    }

    /**
     * 获取上传地址
     */
    public String getUploadURL()
    {
        Bundle metaData = getAppMetaData();
        if ( null != metaData )
        {
            return metaData.getString( "uploadURL", "http://192.168.0.251/justDoAPI/base/upload/one" );
        }
        return null;
    }

    /**
     * 获取下载地址
     */
    public String getDownloadURL()
    {
        Bundle metaData = getAppMetaData();
        if ( null != metaData )
        {
            return metaData.getString( "downloadURL", "http://192.168.0.251:81/justDoResource/user/voice/" );
        }
        return null;
    }

    /**
     * 获取http请求地址
     */
    public String getHttpServiceURL()
    {
        Bundle metaData = getAppMetaData();
        if ( null != metaData )
        {
            return metaData.getString( "httpServerURL", "http://192.168.0.251/justDoAPI/" );
        }
        return null;
    }



    /**
     * 获取<application>下的<meta-data>
     * @return
     */
    public Bundle getAppMetaData()
    {
        Bundle metaData = null;
        if ( isInit )
        {
            try {
                ApplicationInfo info = curPackMgr.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                metaData = info.metaData;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return metaData;
    }

    /**
     * 获取指定<activity>下的<meta-data>
     * @return 指定Activity名
     */
    public Bundle getActivityMetaData( ComponentName activityName )
    {
        Bundle metaData = null;
        if ( isInit ) {
            try {
                ActivityInfo info = curPackMgr.getActivityInfo(activityName, PackageManager.GET_META_DATA);
                metaData = info.metaData;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return metaData;
    }

    /**
     * 获取指定<service>下的<meta-data>
     * @return 指定Service名
     */
    public Bundle getServiceMetaData( ComponentName serviceName )
    {
        Bundle metaData = null;
        if ( isInit ) {
            try {
                ServiceInfo info = curPackMgr.getServiceInfo(serviceName, PackageManager.GET_META_DATA);
                metaData = info.metaData;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return metaData;
    }


}
