package com.daking.app.client_common.util.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

import com.daking.app.client_common.util.BaseUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 位图工具类
 * Created by daking on 15/8/5.
 */
public class BitmapUtil extends BaseUtil {
    private static final int DEFAULT_COLOR = 0xff424242;

    private BitmapUtil() {
        throw new AssertionError();
    }


    /**************************BitmapFactory.Options解决OOM(开始)**************************/
    /**
     * 通过资源id获取图片的缩略图
     *
     * @param resId     图片资源id
     * @param reqWidth  缩放到的宽度
     * @param reqHeight 缩放到的高度
     * @return
     */
    public static Bitmap getBitmapThumbnail(int resId, int reqWidth, int reqHeight) {
        Resources res = getResources();
        // 检测图片的尺寸信息
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // 计算inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // 解析出缩放后的图片
        options.inJustDecodeBounds = false;
        Bitmap output = BitmapFactory.decodeResource(res, resId, options);

        return output;
    }

    /**
     * 通过文件路径获取图片的缩略图
     *
     * @param fileName  图片路径
     * @param reqWidth  缩放到的宽度
     * @param reqHeight 缩放到的高度
     * @return
     */
    public static Bitmap getBitmapThumbnail(String fileName, int reqWidth, int reqHeight) {
        // 检测图片的尺寸信息
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);

        // 计算inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // 解析出缩放后的图片
        options.inJustDecodeBounds = false;
        Bitmap output = BitmapFactory.decodeFile(fileName, options);

        return output;
    }

    /**
     * 通过输入流获取图片的缩略图
     *
     * @param fileStream 图片输入流
     * @param reqWidth   缩放到的宽度
     * @param reqHeight  缩放到的高度
     * @return
     */
    public static Bitmap getBitmapThumbnail(InputStream fileStream, int reqWidth, int reqHeight) {
        // 给输入流外套一层BufferedInputStream,为了解析完尺寸后,重置输入流,再接着解码图片.
        InputStream is = new BufferedInputStream(fileStream);
        try {
            is.mark(is.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 检测图片的尺寸信息
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);

        // 计算inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        try {
            is.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 解析出缩放后的图片
        options.inJustDecodeBounds = false;
        Bitmap output = BitmapFactory.decodeStream(is, null, options);

        return output;
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth  缩放到的宽度
     * @param reqHeight 缩放到的高度
     * @return 表示几分之一, 若返回3则表示3分之一
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        if (reqWidth == -1) reqWidth = width;
        if (reqHeight == -1) reqHeight = height;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 计算sampleSize(安卓官方方法)
     *
     * @param options
     * @param minSideLength  最小边长度,一般传-1即可
     * @param maxNumOfPixels 最大像素数,如100*200
     */
    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
    /**************************BitmapFactory.Options解决OOM(结束)**************************/



    /**
     * 将图片缩放到指定宽高(以图片中心为轴心)
     *
     * @param bitmap 原图片
     * @param finalW 最终宽度
     * @param finalH 最终高度
     * @return 缩放后的图片
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int finalW, int finalH) {
        final int w = bitmap.getWidth();
        final int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        float sx = finalW / (float) w;
        float sy = finalH / (float) h;
        float bestScale = sx > sy ? sx : sy;
        float subX = (finalW - w * bestScale) / 2;
        float subY = (finalH - h * bestScale) / 2;
        matrix.postScale(bestScale, bestScale);
        matrix.postTranslate(subX, subY);

        Bitmap output = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return output;
    }


    /**
     * 圆角图片
     *
     * @param bitmap 原图片
     * @param pixels 圆角的弧度
     * @param color  填充颜色
     * @return
     */
    public static Bitmap drawToRoundCorner(Bitmap bitmap, int pixels, int color) {
        if (color == -1) color = DEFAULT_COLOR;

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        // 圆角边框
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        canvas.drawARGB(0, 0, 0, 0); // 清屏
        canvas.drawRoundRect(rectF, pixels, pixels, paint);
        // 填充图片
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 椭圆图片
     *
     * @param bitmap 原图片
     * @param color  填充颜色
     * @return
     */
    public static Bitmap drawToOval(Bitmap bitmap, int color) {
        if (color == -1) color = DEFAULT_COLOR;

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);

        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        // 清屏
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 正圆形图片(使用Xfermode实现)
     *
     * @param bitmap 原图片
     * @param color  填充颜色
     * @return
     */
    public static Bitmap drawToCircle(Bitmap bitmap, int color) {
        if (color == -1) color = DEFAULT_COLOR;

        final int w = bitmap.getWidth();
        final int h = bitmap.getHeight();
        final int r = w < h ? w >> 1 : h >> 1; // 半径
        final int d = r << 1; // 直径
        Bitmap output = Bitmap.createBitmap(d, d, bitmap.getConfig());
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);

        // 清屏
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, new Rect((w >> 1) - r, (h >> 1) - r, d, d), new Rect(0, 0, d, d), paint);

        return output;
    }

    /**
     * 正圆形图片(使用canvas.clipPath实现)
     *
     * @param bitmap 原图片
     * @param color  填充颜色
     * @return
     */
    public static Bitmap drawToCircle2(Bitmap bitmap, int color) {
        if (color == -1) color = DEFAULT_COLOR;

        final int w = bitmap.getWidth();
        final int h = bitmap.getHeight();
        final int r = w < h ? w >> 1 : h >> 1; // 半径
        final int d = r << 1; // 直径

        Bitmap output = Bitmap.createBitmap(d, d, bitmap.getConfig());
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);

        Path path = new Path();
        path.addCircle(r, r, r, Path.Direction.CW);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, new Rect((w >> 1) - r, (h >> 1) - r, d, d), new Rect(0, 0, d, d), paint);

        return output;
    }

    /**
     * 正圆形图片(使用BitmapShader实现)
     *
     * @param bitmap 原图片
     * @return
     */
    public static Bitmap drawToCircle3(Bitmap bitmap) {
        final int w = bitmap.getWidth();
        final int h = bitmap.getHeight();
        final int r = w < h ? w >> 1 : h >> 1; // 半径

        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        Bitmap output = Bitmap.createBitmap(w, h, bitmap.getConfig());

        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);
        canvas.drawCircle(w >> 1, h >> 1, r, paint);

        return output;
    }


}
