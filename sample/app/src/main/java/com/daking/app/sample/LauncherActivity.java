package com.daking.app.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.daking.app.client_common.mgr.base.ActivityMgr;
import com.daking.app.client_common.mgr.base.AppMgr;
import com.daking.app.client_common.mgr.event.AppEventName;
import com.daking.app.client_common.mgr.event.ClientEvent;
import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.sample.mgr.sql.DBOpenHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import de.greenrobot.event.EventBus;

public class LauncherActivity extends BaseActivity {

    private ImageView imgBg; // 背景图

    @Override
    public int bindLayout() {
        return R.layout.activity_launcher;
    }

    @Override
    public void initView(View view) {
        this.showTitle(false);
        this.enterFullScreen();
        // 创建数据库
        DBOpenHelper helper = new DBOpenHelper(getApplicationContext());
        helper.getReadableDatabase();

        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // 设置背景图
                imgBg = (ImageView) findViewById(R.id.imgBg);
                String imgUrl = ImageDownloader.Scheme.DRAWABLE.wrap(String.valueOf(R.drawable.launcher_bg));
                final ImageSize imgSize = new ImageSize(AppMgr.getInstance().getScreen_w_px(), AppMgr.getInstance().getScreen_h_px());
                ImageLoader.getInstance().loadImage(imgUrl, imgSize, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        imgBg.setImageBitmap(loadedImage);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                // 异步处理业务初始化工作
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        // 初始化各业务管理器

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        // 派发业务初始化完成事件
                        ClientEvent event = new ClientEvent(AppEventName.FINISH_APP_INIT);
                        EventBus.getDefault().post(event);

                        finish();
                        ActivityMgr.getInstance().startActivity(LauncherActivity.this,
                                new Intent(LauncherActivity.this, MainActivity.class),
                                ActivityMgr.ANIM_METHOD_ALPHA);
                    }
                }.execute();
            }
        });
        view.startAnimation(animation);
    }

    @Override
    public void initData(Context mContext) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

}
