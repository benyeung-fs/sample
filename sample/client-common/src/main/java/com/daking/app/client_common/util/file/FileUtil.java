package com.daking.app.client_common.util.file;

import com.daking.app.client_common.util.BaseUtil;

import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * 文件工具类
 * Created by daking on 15/8/31.
 */
public class FileUtil extends BaseUtil{
    /**
     * 获取raw文本
     * @param rawId
     * @return
     */
    public static String getRaw(int rawId){
        try {
            InputStream is = getContext().getResources().openRawResource(rawId);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String content = EncodingUtils.getString(buffer, "utf-8");
            is.close();
            return content;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
