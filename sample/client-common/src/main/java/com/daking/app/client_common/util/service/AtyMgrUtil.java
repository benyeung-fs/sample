package com.daking.app.client_common.util.service;

import android.app.ActivityManager;
import android.content.Context;

import com.daking.app.client_common.util.BaseUtil;

import java.util.List;

/**
 * ActivityManager工具类
 * Created by daking on 15/11/14.
 */
public class AtyMgrUtil extends BaseUtil {
    /**
     * 获取官方ActivityManager
     */
    public static ActivityManager getAtyMgr() {
        return (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * 杀死此应用的后台进程
     */
    public static void killBgProcesses() {
        getAtyMgr().killBackgroundProcesses(getContext().getPackageName());
    }

    /**
     * 检测某进程是否运行中
     *
     * @param processName
     */
    public static boolean isProcessRunning(String processName) {
        List<ActivityManager.RunningAppProcessInfo> list = getAtyMgr().getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : list) {
            if (info.processName.equals(processName)) {
                return true;
            }
        }
        return false;
    }
}
