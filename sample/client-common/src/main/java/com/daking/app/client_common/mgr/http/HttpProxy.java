package com.daking.app.client_common.mgr.http;

import android.util.Log;

import com.daking.app.client_common.view.popup.WaitDialog;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * http通信代理
 * Created by daking on 15/8/17.
 */
public class HttpProxy {
    public static int ERROR_JSON = -99999; // 后端返回的json字符串解析出错
    public static String TAG = "Http";

    private volatile static HttpProxy httpProxy;

    private String _serverURL = ""; // 服务器地址,如http://192.168.0.245/moyiAPI/

    /**
     * Http代理回调
     */
    public interface CallBack {
        public void completeHandler(JSONObject result);

        //        public void dataErrHandler( JSONObject result );
        public void errorHandler(int errorCode);

        public void deferHandler();
    }

    public static HttpProxy getInstance() {
        if (httpProxy == null) {
            synchronized (HttpProxy.class) {
                if (httpProxy == null) {
                    httpProxy = new HttpProxy();
                }
            }
        }
        return httpProxy;
    }

    /**
     * 设置服务器地址
     *
     * @param url
     */
    public void setServerURL(String url) {
        _serverURL = url;
    }

    public String getServerURL() {
        return _serverURL;
    }

    /**
     * get请求
     *
     * @param interfaceName 接口名
     * @param params        参数
     * @param callBack      回调
     * @param showWait      是否显示等待弹窗
     */
    public void getJSON(String interfaceName, RequestParams params, final HttpProxy.CallBack callBack,
                        final boolean showWait) {
        String url = _serverURL + interfaceName;
        Log.i(TAG, "http的get请求:" + url + "?" + params.toString());
        // 显示等待
        if (showWait){
            WaitDialog.showWait();
        }

        HttpService.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // 将后端返回字符串转换成JSONObject
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new String(responseBody));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.errorHandler(HttpProxy.ERROR_JSON);
                    return;
                }
                // 关闭等待
                if (showWait){
                    WaitDialog.closeWait();
                }
                // 根据status值决定回调哪个方法
//                try {
//                    int status = jsonObject.getInt("status");
//                    if (status == 0) {
                callBack.completeHandler(jsonObject);
//                    } else {
//                        callBack.dataErrHandler(jsonObject);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // 关闭等待
                if (showWait){
                    WaitDialog.closeWait();
                }
                callBack.deferHandler();
                callBack.errorHandler(statusCode);
            }
        });
    }

    /**
     * post请求
     *
     * @param interfaceName 接口名
     * @param params        参数
     * @param callBack      回调
     * @param showWait      是否显示等待弹窗
     */
    public void postJSON(String interfaceName, RequestParams params, final HttpProxy.CallBack callBack,
                         final boolean showWait) {
        String url = _serverURL + interfaceName;
        Log.i(TAG, "http的post请求:" + url);
        // 显示等待
        if (showWait){
            WaitDialog.showWait();
        }

        HttpService.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // 将后端返回字符串转换成JSONObject
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new String(responseBody));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.errorHandler(HttpProxy.ERROR_JSON);
                    return;
                }
                // 关闭等待
                if (showWait){
                    WaitDialog.closeWait();
                }
                // 根据status值决定回调哪个方法
//                try {
//                    int status = jsonObject.getInt("status");
//                    if (status == 0) {
                callBack.completeHandler(jsonObject);
//                    } else {
//                        callBack.dataErrHandler(jsonObject);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // 关闭等待
                if (showWait) {
                    WaitDialog.closeWait();
                }
                callBack.deferHandler();
                callBack.errorHandler(statusCode);
            }
        });
    }
}
