package com.daking.app.client_common.view.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daking.app.client_common.mgr.base.AppMgr;
import com.daking.app.client_common.mgr.event.ClientEvent;

import de.greenrobot.event.EventBus;

/**
 * 项目Fragment基类(支持V4包)
 * Created by daking on 15/11/3.
 */
public abstract class BaseFragmentV4 extends Fragment implements IBaseFragment{
    /**当前Fragment渲染的视图View*/
    private View rootView = null;
    /**是否关注全局事件*/
    private boolean isRegClientEvent = false;

    /**日志输出标志*/
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, TAG + "-->onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG+"-->onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, TAG+"-->onCreateView()");

        //渲染视图View(防止切换时重绘View)
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView( rootView );
            }
        } else {
            rootView = inflater.inflate(bindLayout(), container, false);
            // 控件初始化
            initView( rootView );
        }

        //业务处理
        initData(getActivity());

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, TAG+"-->onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, TAG+"-->onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, TAG+"-->onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, TAG+"-->onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, TAG+"-->onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, TAG+"-->onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, TAG+"-->onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, TAG+"-->onDestroy()");
        super.onDestroy();

        unregisterClientEvent();
        AppMgr.getInstance().addToRefWatcher(this);
    }

    @Override
    public void onDetach() {
        Log.d(TAG, TAG+"-->onDetach()");
        super.onDetach();
    }

    /**
     * 获取当前Fragment依附在的Activity
     * @return
     */
    protected Activity getContext() {
        return getActivity();
    }



    /*********************************全局事件相关(开始)*********************************/
    /**
     * 监听全局事件ClientEvent
     * <br> 事件派发时会自动调用onEventMainThread,子类需重写此方法
     */
    protected void registerClientEvent(){
        if(!isRegClientEvent){
            isRegClientEvent = true;
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 取消监听全局事件ClientEvent
     */
    protected void unregisterClientEvent(){
        if(isRegClientEvent){
            isRegClientEvent = false;
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 接受到全局事件更新UI
     * <br> 子类需重写此方法
     */
    public void onEventMainThread(ClientEvent event){
        if (isRegClientEvent){
            throw new Error("BaseActivity监听全局事件,但没有重写onEventMainThread");
        }
    }
    /*********************************全局事件相关(开始)*********************************/
}
