package com.daking.app.client_common.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import com.daking.app.client_common.mgr.base.ActivityMgr;
import com.daking.app.client_common.mgr.base.AppMgr;

/**
 * 工具类基类
 * Created by daking on 15/10/22.
 */
public class BaseUtil {
    /**获取当前Activity*/
    public static Activity getCurActivity() {
        return ActivityMgr.getInstance().getCurActivity();
    }

    /**获取全局上下文*/
    public static Context getContext()
    {
        return AppMgr.getInstance().getContext();
    }

    /**获取资源*/
    public static Resources getResources() {
        Context context = getContext();
        if (null != context){
            return context.getResources();
        }
        return null;
    }
}
