package com.daking.app.client_common.mgr.event;

import android.os.Bundle;


/**
 * 客户端事件
 * Created by daking on 15/8/13.
 */
public class ClientEvent{
    private int type; // 事件类型
    private Bundle data; // 携带数据

    /**
     * 自定义事件
     * @param type 类型
     */
    public ClientEvent( int type )
    {
        this.type = type;
    }

    /**
     * 自定义事件
     * @param type 类型
     * @param data 数据
     */
    public ClientEvent( int type, Bundle data )
    {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }
}
