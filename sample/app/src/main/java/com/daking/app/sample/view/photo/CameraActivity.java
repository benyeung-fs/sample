package com.daking.app.sample.view.photo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.daking.app.client_common.util.bitmap.BitmapUtil;
import com.daking.app.client_common.util.time.TimeUtil;
import com.daking.app.client_common.view.base.BaseActivity;
import com.daking.app.sample.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 内置Camera
 */
public class CameraActivity extends BaseActivity {
    public static final int REQUEST_CODE_CAMERA = 1;

    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button btnCamera;
    private TextView tvUri;
    private ImageView imgPhoto;

    private File outputImgFile = null; // 照片输出文件
    private Uri outputImgUri = null; // 照片输出文件路径

    @Override
    public int bindLayout() {
        return R.layout.activity_camera;
    }

    @Override
    public String bindTitle() {
        return (String) getApplicationContext().getResources().getText(R.string.act_camera_name);
    }

    @Override
    public void initView(View view) {
        rb1 = (RadioButton) view.findViewById(R.id.rb1);
        rb2 = (RadioButton) view.findViewById(R.id.rb2);
        rb3 = (RadioButton) view.findViewById(R.id.rb3);
        btnCamera = (Button) view.findViewById(R.id.btn_camera);
        tvUri = (TextView) view.findViewById(R.id.tv_uri);
        imgPhoto = (ImageView) view.findViewById(R.id.img_Photo);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputImgFile = null;
                outputImgUri = null;

                if (rb1.isChecked()) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, REQUEST_CODE_CAMERA);
                    tvUri.setText("");
                } else if (rb2.isChecked() || rb3.isChecked()) {
                    // 判断sd卡是否加载
                    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        showToast("sd卡不存在!");
                        return;
                    }
                    // 计算输出路径
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (rb2.isChecked()) {
                        String outputImgPath = Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + "/daking_testCamera" + TimeUtil.getCurrentTimeInString() + ".jpg";
                        outputImgFile = new File(outputImgPath);
                        outputImgFile.getParentFile().mkdirs();
                        outputImgUri = Uri.fromFile(outputImgFile);
                    } else {
                        ContentValues values = new ContentValues(3);
                        values.put(MediaStore.Images.Media.DISPLAY_NAME, "daking_testCamera" + TimeUtil.getCurrentTimeInString());
                        values.put(MediaStore.Images.Media.DESCRIPTION, "Test Desc");
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        outputImgUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                values);
                    }
                    tvUri.setText("路径:" + outputImgUri.toString());
                    i.putExtra(MediaStore.EXTRA_OUTPUT, outputImgUri);
                    startActivityForResult(i, REQUEST_CODE_CAMERA);
                }
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
            if (requestCode == REQUEST_CODE_CAMERA) {
                if (rb1.isChecked()) {
                    // 缩略图
                    imgPhoto.setTag("");
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    imgPhoto.setImageBitmap(bitmap);
                } else if (rb2.isChecked() || rb3.isChecked()) {
                    // 方法1:利用ImageLoader去处理缩放,并加载显示图片
                    String imgUrl = null;
                    if (rb2.isChecked()) {
                        imgUrl = ImageDownloader.Scheme.FILE.wrap(outputImgFile.getAbsolutePath());
                    } else {
                        imgUrl = outputImgUri.toString();
                    }
                    imgPhoto.setTag(imgUrl);
                    ImageLoader.getInstance().displayImage(imgUrl, imgPhoto, new SimpleImageLoadingListener() {
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

                    /**
                     // 方法2:利用BitmapFactory.Options的inJustDecodeBounds和inSampleSize性质得到的缩放图片
                     if (rb2.isChecked()) {
                     // 文件路径
                     Bitmap bitmap = BitmapUtil.getBitmapThumbnail(outputImgFile.getAbsolutePath(),
                     imgPhoto.getMeasuredWidth(), imgPhoto.getMeasuredHeight());
                     imgPhoto.setImageBitmap(bitmap);
                     }else{
                     // 内容解析器获取Uri对应的输入流
                     try {
                     Bitmap bitmap = null;
                     InputStream is = getContentResolver().openInputStream(outputImgUri);
                     bitmap = BitmapUtil.getBitmapThumbnail(is,
                     imgPhoto.getMeasuredWidth(), imgPhoto.getMeasuredHeight());
                     imgPhoto.setImageBitmap(bitmap);
                     } catch (FileNotFoundException e) {
                     e.printStackTrace();
                     }
                     }
                     */
                }
            }
        }
    }
}
