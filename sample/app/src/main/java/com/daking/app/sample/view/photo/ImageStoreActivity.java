package com.daking.app.sample.view.photo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.sample.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * MediaStore存储图像部分
 */
public class ImageStoreActivity extends BaseActivity {
    private TextView tvTitle;
    private ImageButton ibPhoto;

    private Cursor cursor;
    private int titleCol;
    private int fileCol;

    private static final String[] COLUMNS = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_image_store;
    }

    @Override
    public String bindTitle() {
        return (String) getApplicationContext().getResources().getText(R.string.act_imageStore_name);
    }

    @Override
    public void initView(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tv_img_title);
        ibPhoto = (ImageButton) view.findViewById(R.id.ib_photo);
        ibPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cursor.moveToNext()) {
                    ImageStoreActivity.this.updateView();
                }
            }
        });
    }

    @Override
    public void initData(Context mContext) {

    }

    @Override
    public void resume() {
        closeCursor();
        // 查询所有标准存储图像
        cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                COLUMNS, null, null, null);
        titleCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
        fileCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        // 将第一行数据填充界面
        if (cursor.moveToFirst()) {
            this.updateView();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        closeCursor();
    }

    private void closeCursor(){
        if (cursor != null){
            cursor.close();
            cursor = null;
        }
    }

    public void updateView() {
        String title = cursor.getString(titleCol);
        if (null != title) {
            tvTitle.setText(title);
        }

        String urlImg = cursor.getString(fileCol);
        if (null != urlImg) {
            urlImg = ImageDownloader.Scheme.FILE.wrap(urlImg);
            ibPhoto.setTag(urlImg);
            ImageLoader.getInstance().displayImage(urlImg, ibPhoto,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);

                            String url = (String) view.getTag();
                            if (url.equals(imageUri)) {
                                ImageView load_iv = (ImageView) view;
                                load_iv.setImageBitmap(loadedImage);
                            }
                        }
                    });
        }
    }

}
