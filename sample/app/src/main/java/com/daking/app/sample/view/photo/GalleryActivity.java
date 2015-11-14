package com.daking.app.sample.view.photo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.sample.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

/**
 * 图像选择
 */
public class GalleryActivity extends BaseActivity {
    public static final int REQUEST_CODE_PICTURE = 1;

    private Button btnPick;
    private TextView tvUri;
    private ImageView ivPhoto;

    @Override
    public int bindLayout() {
        return R.layout.activity_gallery;
    }

    @Override
    public String bindTitle() {
        return (String) getApplicationContext().getResources().getText(R.string.act_gallery_name);
    }

    @Override
    public void initView(View view) {
        btnPick = (Button) view.findViewById(R.id.btn_gallery_pick);
        tvUri = (TextView) view.findViewById(R.id.tv_gallery_uri);
        ivPhoto = (ImageView) view.findViewById(R.id.iv_gallery_photo);

        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                if (Build.VERSION.SDK_INT < 19) {
                    i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("image/*");
                } else {
                    i = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                }
                startActivityForResult(i, REQUEST_CODE_PICTURE);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICTURE) {
                Uri imgFileUri = data.getData(); // 此Uri可以为ContentProvider或File路径
                //Log.i("DAKING", imgFileUri.toString());
                String imgPath = null; // 图片文件路径

                String[] columns = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(imgFileUri, columns, null, null, null);
                // 区别Uri是来自于ContentProvider还是File,最终都转为File路径
                if (cursor != null) {
                    cursor.moveToFirst();
                    int dataColIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    imgPath = cursor.getString(dataColIndex);
                    if (imgPath == null || imgPath.equals("null")) {
                        showToast(getResources().getText(R.string.act_gallery_tip).toString());
                    }
                    cursor.close();
                    cursor = null;
                } else {
                    File imgFile = new File(imgFileUri.getPath());
                    if (!imgFile.exists()) {
                        showToast(getResources().getText(R.string.act_gallery_tip).toString());
                    } else {
                        imgPath = imgFile.getAbsolutePath();
                    }
                }
                if (imgPath != null) {
                    imgPath = ImageDownloader.Scheme.FILE.wrap(imgPath);
                    tvUri.setText(imgPath);
                    ivPhoto.setTag(imgPath);
                    ImageLoader.getInstance().displayImage(imgPath, ivPhoto,
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
    }
}
