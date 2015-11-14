package com.daking.app.client_common.view.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.daking.app.client_common.R;
import com.daking.app.client_common.mgr.base.ActivityMgr;

/**
 * 等待弹窗
 * Created by daking on 15/8/26.
 */
public class WaitDialog extends Dialog {
    private static WaitDialog waitDialog = null;
    private static int count;
    private static Context curContext = null;

    public WaitDialog( Context context ) {
        super(context);
    }

    public WaitDialog( Context context, int theme ) {
        super(context, theme);
    }

    public static int getCount()
    {
        return count;
    }

    /**打开弹窗*/
    public static void showWait()
    {
        synchronized ( WaitDialog.class ) {
            count++;
            createDialog(ActivityMgr.getInstance().getCurActivity());
            if (waitDialog != null) {
                if (!waitDialog.isShowing()) {
                    waitDialog.show();
                }
            }
        }
    }

    /**关闭等待*/
    public static void closeWait()
    {
        synchronized ( WaitDialog.class ) {
            count--;
            if (count < 0) count = 0;
            if (count == 0 && waitDialog != null && waitDialog.isShowing()) {
                waitDialog.dismiss();
            }
        }
    }

    /**是否需要显示等待(用于Activity切换时仍然保持等待弹窗)*/
    public static void whetherShowWait(){
        synchronized ( WaitDialog.class ) {
            if (count > 0) {
                count--;
                WaitDialog.showWait();
            }
        }
    }

    private static WaitDialog createDialog(Context context) {
        if( context == null ) return null;
        // 原先不存在,或更换context时,重新创建WaitDialog
        if ( waitDialog == null || curContext != context )
        {
            curContext = context;

            destoryDialog();

            waitDialog = new WaitDialog(context, R.style.CustomProgressDialog);
            waitDialog.setContentView(R.layout.dialog_wait);
            waitDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
            waitDialog.setCanceledOnTouchOutside(false);
            waitDialog.setCancelable(false);
        }
        return waitDialog;
    }

    private static void destoryDialog()
    {
        if ( waitDialog != null ) {
            waitDialog.dismiss();
            waitDialog = null;
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if ( waitDialog == null ) {
            return;
        }

        if( hasFocus ) {
            ImageView imageView = (ImageView) waitDialog
                    .findViewById(R.id.imgLoading);
            AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                    .getBackground();
            animationDrawable.start();
        }else{
            waitDialog.dismiss();
        }
    }

    public WaitDialog setTitile( String strTitle ){
        return waitDialog;
    }

    public WaitDialog setMessage(String strMessage){
        TextView tvMsg = (TextView)waitDialog.findViewById( R.id.tvMsg );

        if (tvMsg != null){
            tvMsg.setText(strMessage);
        }

        return waitDialog;
    }
}
