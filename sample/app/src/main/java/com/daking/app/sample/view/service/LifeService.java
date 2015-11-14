package com.daking.app.sample.view.service;

import android.util.Log;

import com.daking.app.client_common.component.service.BaseService;
import com.daking.app.client_common.mgr.event.ClientEvent;

/**
 * 研究Service生命周期
 * Created by daking on 15/11/3.
 */
public class LifeService extends BaseService {

    @Override
    public void setServiceData(ClientEvent event) {
        Log.d(TAG, TAG + "-->setServiceData()");
    }
}
