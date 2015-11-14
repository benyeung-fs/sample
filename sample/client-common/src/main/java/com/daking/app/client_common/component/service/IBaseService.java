package com.daking.app.client_common.component.service;

import com.daking.app.client_common.mgr.event.ClientEvent;

/**
 * Service接口
 * Created by daking on 15/11/3.
 */
public abstract interface IBaseService {
    /**
     * 客户端修改绑定服务的数据
     * @param event
     */
    public void setServiceData(ClientEvent event);
}
