package com.daking.app.client_common.util.file;

import android.content.Context;

import com.daking.app.client_common.mgr.base.AppMgr;
import com.daking.app.client_common.util.BaseUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 内存存储工具类
 * <br> 内部存储空间位于/data/data/<应用包名>/files下
 * <br> 主要用于存储与APP密切相关的文本数据
 * Created by daking on 15/8/25.
 */
public class InternalStorageUtil extends BaseUtil {
    private InternalStorageUtil() {
        throw new AssertionError();
    }

    /**
     * 保存内容到内部存储器中
     *
     * @param filename 文件名
     * @param content  内容
     */
    public static void save(String filename, String content) {
        try {
            FileOutputStream fos = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(content);
            osw.flush();
            fos.flush();
            osw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过文件名获取内容
     *
     * @param filename 文件名
     * @return 文件内容, 若文件不存在则返回空
     */
    public static String get(String filename) {
        if (!exists(filename)) {
            return null;
        }

        FileInputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            fis = getContext().openFileInput(filename);
            byte[] data = new byte[1024];
            int len = -1;
            while ((len = fis.read(data)) != -1) {
                baos.write(data, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(baos.toByteArray());
    }

    /**
     * 以追加的方式在文件的末尾添加内容
     *
     * @param filename 文件名
     * @param content  追加的内容
     */
    public static void append(String filename, String content) {
        try {
            FileOutputStream fos = getContext().openFileOutput(filename, Context.MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(content);
            osw.flush();
            fos.flush();
            osw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param filename 文件名
     * @return 是否成功
     */
    public static boolean delete(String filename) {
        return getContext().deleteFile(filename);
    }

    /**
     * 文件是否存在
     *
     * @param filename 指定内部存储文件名
     * @return 是否存在
     */
    public static boolean exists(String filename) {
        File file = new File(getContext().getFilesDir(), filename);
        return file.exists();
    }

    /**
     * 获取内部存储路径下的所有文件名
     *
     * @return 文件名数组
     */
    public static String[] queryAllFile() {
        return getContext().fileList();
    }

}
