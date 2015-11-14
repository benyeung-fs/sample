package com.daking.app.client_common.util.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Bitmap与其他数据格式的互转
 * <br> 如:byte[],InputStream,Drawable
 * Created by daking on 15/8/6.
 */
public class BitmapFormatUtil {
    private BitmapFormatUtil()
    {
        throw new AssertionError();
    }

    /**
     * Bitmap转换成输出流
     * @param bitmap
     * @param format Bitmap.CompressFormat.PNG或JPEG
     * @param quality 质量,0~100
     * @return ByteArrayOutputStream
     */
    public static OutputStream bitmap2OutputStream( Bitmap bitmap, Bitmap.CompressFormat format, int quality )
    {
        if ( bitmap == null ) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress( format, quality, baos );
        return baos;
    }

    /**
     * Bitmap转换成输入流
     * @param bitmap
     * @param format Bitmap.CompressFormat.PNG或JPEG
     * @param quality 质量,0~100
     * @return ByteArrayInputStream
     */
    public static InputStream bitmap2InputStream( Bitmap bitmap, Bitmap.CompressFormat format, int quality )
    {
        if ( bitmap == null ) return null;
        ByteArrayOutputStream baos = (ByteArrayOutputStream) bitmap2OutputStream( bitmap, format, quality );
        ByteArrayInputStream bais = new ByteArrayInputStream( baos.toByteArray() );
        return bais;
    }

    /**
     * 流入流转换成Bitmap
     * @param is
     * @return
     */
    public static Bitmap inputStream2Bitmap( InputStream is )
    {
        return BitmapFactory.decodeStream( is );
    }

    /**
     * Bitmap转换成byte[]
     * @param bitmap
     * @param format Bitmap.CompressFormat.PNG或JPEG
     * @param quality 质量,0~100
     * @return byte[]
     */
    public static byte[] bitmap2Bytes( Bitmap bitmap, Bitmap.CompressFormat format, int quality )
    {
        if ( bitmap == null ) return null;
        ByteArrayOutputStream baos = (ByteArrayOutputStream) bitmap2OutputStream( bitmap, format, quality );
        return baos.toByteArray();
    }

    /**
     * byte[]转换成Bitmap
     * @param bytes 图片的二进制格式
     * @return
     */
    public static Bitmap bytes2Bitmap( byte[] bytes )  {
        return (bytes == null || bytes.length == 0) ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Bitmap转换成Drawable
     * @param b
     * @return
     */
    public static Drawable bitmap2Drawable( Bitmap b )
    {
        return b == null ? null : new BitmapDrawable(b);
    }

    /**
     * Drawable转换成Bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap( Drawable drawable ) {
        if ( drawable == null ) return null;
        if ( drawable instanceof BitmapDrawable ){
            return ((BitmapDrawable)drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ?
                        Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw( canvas );
        return bitmap;
    }
}
