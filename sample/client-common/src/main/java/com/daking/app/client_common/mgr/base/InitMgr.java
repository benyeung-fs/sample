package com.daking.app.client_common.mgr.base;

import android.content.Context;
import android.graphics.Bitmap;

import com.daking.app.client_common.R;
import com.daking.app.client_common.mgr.http.HttpProxy;
import com.daking.app.client_common.mgr.location.BDLoactionMgr;
import com.daking.app.client_common.mgr.setting.AppConfig;
import com.daking.app.client_common.mgr.setting.SettingMgr;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * 初始化管理器
 * Created by daking on 15/8/19.
 */
public class InitMgr {
    public static String TAG = "Client-common";

    private volatile static InitMgr initMgr;

    private Context context;
    private boolean isInit; // 是否初始化

    public static InitMgr getInstance()
    {
        if( initMgr==null ) {
            synchronized ( InitMgr.class ){
                if ( initMgr==null ) {
                    initMgr = new InitMgr();
                }
            }
        }
        return initMgr;
    }

    /**
     * 初始化
     * @param context 传入activity.getApplicationContext
     */
    public void init( Context context )
    {
        if ( isInit ) return;

        this.context = context;

        SettingMgr.getInstance().init( context );
        AppMgr.getInstance().init( context );
        // 设置http请求地址
        String httpServerURL = SettingMgr.getInstance().getHttpServiceURL();
        if ( httpServerURL !=null ) HttpProxy.getInstance().setServerURL( httpServerURL );
        // 设置ImageLoader
        initImageLoader();
        // 初始化百度地图
        BDLoactionMgr.getInstance().init(context);

        this.isInit = true;
    }

    public void destroy(){
        BDLoactionMgr.getInstance().destroy();
        this.isInit = false;
    }

    /**获取应用的上下文*/
    public Context getContext()
    {
        return context;
    }

    /**设置ImageLoader*/
    private void initImageLoader()
    {
        // 显示图片的参数
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_image) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_image) //设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_image) //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED) //设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
                .displayer(new FadeInBitmapDisplayer(100)) //是否图片加载好后渐入的动画时间
                .build();

        // 缓存路径
        File cacheDir = StorageUtils.getOwnCacheDirectory( context, AppConfig.IMAGELOADER_CACHE_URL );

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context);
        if ( SettingMgr.getInstance().isDebug() )
        {
            builder.writeDebugLogs();
        }
        builder.denyCacheImageMultipleSizesInMemory(); //禁止缓存多张图片(同一个Uri获取不同大小的图片时,只缓存一个到内存)
        builder.memoryCacheExtraOptions(480, 800); //即保存的每个缓存文件的最大长宽
        builder.memoryCache(new LRULimitedMemoryCache(50 * 1024 * 1024)); //缓存策略
        builder.memoryCacheSize(50 * 1024 * 1024); //设置内存缓存的大小
        builder.diskCacheFileNameGenerator(new Md5FileNameGenerator()); //缓存文件名的保存方式,采用MD5加密
        builder.diskCacheSize(200 * 1024 * 1024); //磁盘缓存大小
        builder.diskCacheFileCount(200); //缓存的文件数量
        builder.diskCache(new UnlimitedDiskCache(cacheDir)); //自定义缓存路径

        builder.threadPoolSize(5); // 线程
        builder.threadPriority(Thread.NORM_PRIORITY - 2);
        builder.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)); // 下载连接超时5s,读取超时30s
        builder.tasksProcessingOrder(QueueProcessingType.LIFO); // 图片下载和显示的工作队列
        builder.defaultDisplayImageOptions(defaultOptions); //显示图片的参数

        ImageLoaderConfiguration configuration = builder.build();
        ImageLoader.getInstance().init( configuration );
    }
}
