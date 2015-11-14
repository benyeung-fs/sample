package com.daking.app.client_common.util.view;

import com.daking.app.client_common.util.BaseUtil;

/**
 * 显示工具类
 * Created by daking on 15/8/21.
 */
public class DisplayUtil extends BaseUtil{
    /**
     * dp转为像素
     */
    public static int dip2px(float dipValue){
        final float scale = getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
    /**
     * 像素转为dp
     */
    public static int px2dip(float pxValue){
        final float scale = getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    /**
     * 像素转sp
     * */
    public static int px2sp(float pxValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp转像素
     * */
    public static int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
