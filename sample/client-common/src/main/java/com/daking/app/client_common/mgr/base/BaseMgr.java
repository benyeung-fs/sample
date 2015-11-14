package com.daking.app.client_common.mgr.base;

import com.daking.app.client_common.mgr.event.ClientEvent;

import de.greenrobot.event.EventBus;

/**
 * 基础管理器(所有业务管理器都必须继承于TA)
 * Created by daking on 15/8/26.
 */
public class BaseMgr {
    public BaseMgr() {
        init();
    }

    /**
     * 初始化
     */
    public void init() {
        AppMgr.getInstance().addToBaseMgrs(this);

        EventBus.getDefault().register(this);
    }

    /**
     * 销毁,由AppMgr调用
     */
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * 处理全局事件
     * (派发者与接受者处于同一线程)
     */
    public void onEvent(ClientEvent e) {

    }

    /**
     * 处理全局事件
     * (始终创建新的子线程来接收此事件)
     */
    public void onEventAsync(ClientEvent e) {

    }
}
