package com.daking.app.client_common.util.view;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.daking.app.client_common.util.BaseUtil;

/**
 * 输入设备工具类
 * Created by daking on 15/10/20.
 */
public class InputUtil extends BaseUtil{
    /**
     * 显示和隐藏软键盘
     *
     * @param view EditText、TextView
     * @param isShow 是否显示软键盘
     */
    public static void popSoftKeyboard(View view, boolean isShow) {
        if (isShow) {
            InputUtil.showSoftKeyboard(view);
        } else {
            InputUtil.hideSoftKeyboard(view);
        }
    }

    /**
     * 显示软键盘
     *
     * @param view
     */
    public static void showSoftKeyboard(View view) {
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public static void hideSoftKeyboard(View view) {
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),  0);
    }
}

