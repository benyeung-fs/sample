package com.daking.app.client_common.mgr.base;

import android.app.Activity;
import android.content.Intent;

import com.daking.app.client_common.R;
import com.daking.app.client_common.view.base.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.Vector;

/**
 * Activity管理器
 * Created by daking on 15/8/17.
 */
public class ActivityMgr {
    public static final int ANIM_METHOD_NONE = 0; // 无动画
    public static final int ANIM_METHOD_PUSH = 1; // 左出右入
    public static final int ANIM_METHOD_ALPHA = 2; // 淡出淡入

    private volatile static ActivityMgr activityMgr;

    /**
     * 寄存整个应用Activity
     **/
    private Vector<WeakReference<Activity>> activitys;

    public static ActivityMgr getInstance() {
        if (activityMgr == null) {
            synchronized (ActivityMgr.class) {
                if (activityMgr == null) {
                    activityMgr = new ActivityMgr();
                }
            }
        }
        return activityMgr;
    }

    public ActivityMgr() {
        activitys = new Vector<WeakReference<Activity>>();
    }

    /**
     * 获取Activity总数
     *
     * @return
     */
    public int getActivityNum() {
        return activitys.size();
    }

    /**
     * 获取当前Activity
     *
     * @return
     */
    public Activity getCurActivity() {
        return getActivityByIndex(activitys.size() - 1);
    }

    /**
     * 根据索引获取Activity
     *
     * @param index
     * @return
     */
    public Activity getActivityByIndex(int index) {
        WeakReference<Activity> task = getTask(index);
        if (task != null) {
            return task.get();
        }
        return null;
    }

    /**
     * 打开新Activity
     *
     * @param intent
     * @param animethod 切换动画方式
     */
    public void startActivity(Intent intent, int animethod) {
        Activity curAct = getCurActivity();
        startActivity(curAct, intent, animethod);
    }

    /**
     * 打开新Activity
     *
     * @param curAct    旧Activity
     * @param intent
     * @param animethod 切换动画方式
     */
    public void startActivity(Activity curAct, Intent intent, int animethod) {
        if (curAct != null) {
            curAct.startActivity(intent);
            switch (animethod) {
                case ANIM_METHOD_NONE:
                    break;
                case ANIM_METHOD_PUSH:
                    curAct.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    break;
                case ANIM_METHOD_ALPHA:
                    curAct.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
    }

    /**
     * 提示语(支持html文本)
     */
    public void showToast(String tip){
        Activity activity = getCurActivity();
        if (activity != null){
            ((BaseActivity)activity).showToast(tip);
        }
    }



    /*******************************************Activity队列操作API（开始）*****************************************/

    /**
     * 将Activity添加到队列中
     *
     * @param task 添加Activity
     */
    public void addTask(WeakReference<Activity> task) {
        int index = -1;
        if ((index = hasTask(task)) != -1) {
            removeTask(index);
        }
        activitys.add(task);
    }

    /**
     * 将Activity添加到队列中
     *
     * @param task 添加Activity
     * @param i    添加索引
     */
    public void addTask(WeakReference<Activity> task, int i) {
        int index = -1;
        if ((index = hasTask(task)) != -1) {
            removeTask(index);
        }
        if (i > activitys.size()) i = activitys.size();
        else if (i < 0) i = 0;
        activitys.add(i, task);
    }

    /**
     * 将传入的Activity对象从栈中移除
     *
     * @param task
     */
    public void removeTask(WeakReference<Activity> task) {
        activitys.remove(task);
    }

    /**
     * 根据指定位置从栈中移除Activity
     *
     * @param taskIndex Activity栈索引
     */
    public void removeTask(int taskIndex) {
        if (activitys.size() > taskIndex && taskIndex >= 0)
            activitys.remove(taskIndex);
    }

    /**
     * 获取指定位置的Activity弱引用
     *
     * @param index
     * @return
     */
    public WeakReference<Activity> getTask(int index) {
        if (index >= 0 && index < activitys.size()) {
            return activitys.get(index);
        }
        return null;
    }

    /**
     * 返回传入的Activity所在的索引
     *
     * @param activity
     * @return -1即不存在
     */
    public int hasTask(Activity activity) {
        String act_name = activity.getClass().getName();
        int end = activitys.size();
        for (int i = 0; i < end; i++) {
            Activity act = activitys.get(i).get();
            if (act_name == act.getClass().getName()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 返回传入的Activity所在的索引
     *
     * @param task
     * @return -1即不存在
     */
    public int hasTask(WeakReference<Activity> task) {
        int end = activitys.size();
        for (int i = 0; i < end; i++) {
            if (task == activitys.get(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 删除最后一个Activity(最顶上)
     */
    public void removeTailTask() {
        int size = activitys.size();
        if (size > 0) {
            if (!activitys.get(size - 1).get().isFinishing()) {
                activitys.get(size - 1).get().finish();
            }
        }
    }

    /**
     * 将队列中Activity移除至队尾(剩下最顶上)
     */
    public void removeToTailTask() {
        int end = activitys.size() - 1;
        int start = 0;
        for (int i = start; i < end; i++) {
            if (!activitys.get(i).get().isFinishing()) {
                activitys.get(i).get().finish();
            }
        }
    }

    /**
     * 将队列中Activity移除至队头(剩下最底)
     */
    public void removeToHeadTask() {
        int end = activitys.size();
        int start = 1;
        for (int i = end - 1; i >= start; i--) {
            if (!activitys.get(i).get().isFinishing()) {
                activitys.get(i).get().finish();
            }
        }
    }

    /**
     * 移除全部（用于整个应用退出）
     */
    public void removeAllTask() {
        //finish所有的Activity
        for (WeakReference<Activity> task : activitys) {
            if (!task.get().isFinishing()) {
                task.get().finish();
            }
        }
    }

    /*******************************************Activity队列操作API（压栈/出栈）API（结束）*****************************************/
}
