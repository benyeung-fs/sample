package com.daking.app.client_common.util.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.daking.app.client_common.mgr.base.ActivityMgr;
import com.daking.app.client_common.mgr.base.AppMgr;
import com.daking.app.client_common.util.BaseUtil;

import java.util.List;

/**
 * 窗体工具类
 * Created by daking on 15/8/31.
 */
public class ActivityUtil extends BaseUtil {
    /**
     * 提示语(支持html文本)
     */
    public static void toast(String tip) {
        ActivityMgr.getInstance().showToast(tip);
    }

    /**
     * 打电话
     *
     * @param phone    电话号码
     * @param activity 发起请求的Activity,若为null则取当前栈顶Activity
     */
    public static void tel(String phone, Activity activity) {
        if (activity == null) activity = getCurActivity();
        if (activity == null) return;

        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        activity.startActivity(intent);
    }

    /**
     * 发短信
     *
     * @param phone    电话号码
     * @param content  短信内容
     * @param activity 发起请求的Activity,若为null则取当前栈顶Activity
     */
    public static void sms(String phone, String content, Activity activity) {
        if (activity == null) activity = getCurActivity();
        if (activity == null) return;

        Uri uri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        activity.startActivity(intent);
    }

    /**
     * 打开网址
     *
     * @param url      网址
     * @param activity 发起请求的Activity,若为null则取当前栈顶Activity
     */
    public static void web(String url, Activity activity) {
        if (activity == null) activity = getCurActivity();
        if (activity == null) return;

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

    /**
     * 打开市场应用进行评分
     */
    public static void market() {
        Activity activity = getCurActivity();
        if (activity == null) return;

        Uri uri = Uri.parse("market://details?id=" + AppMgr.getInstance().getContext().getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * 卸载APK
     */
    public static void uninstallApk(String packageName) {
        Activity activity = getCurActivity();
        if (activity == null) return;

        Uri uri = Uri.fromParts("package", packageName, null);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        activity.startActivity(intent);
    }

    /**
     * 安装APK
     */
    public static void installApk(String packageName) {
        Activity activity = getCurActivity();
        if (activity == null) return;

        Uri uri = Uri.fromParts("package", packageName, null);
        Intent intent = new Intent(Intent.ACTION_PACKAGE_ADDED, uri);
        activity.startActivity(intent);
    }

    /**
     * 打开Apk
     */
    public static void openApk(String packageName) {
        Activity activity = getCurActivity();
        if (activity == null) return;

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageName);
        // 打开指定APK,需要其包名和启动Activity的名字
        List<ResolveInfo> resolveInfoList = activity.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        if (resolveInfoList != null && resolveInfoList.size() > 0) {
            ResolveInfo resolveInfo = resolveInfoList.get(0);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName componentName = new ComponentName(
                    resolveInfo.activityInfo.packageName,
                    resolveInfo.activityInfo.name
            );
            intent.setComponent(componentName);
            activity.startActivity(intent);
        }
    }

    /**
     * 回到桌面
     */
    public static void backToHome(){
        Activity activity = getCurActivity();
        if (activity == null) return;

        Intent it = new Intent();
        it.setAction(Intent.ACTION_MAIN);
        it.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(it);
    }
}
