package com.daking.app.client_common.util.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 数据格式转换工具类
 * Created by daking on 15/8/7.
 */
public class DataFormatUtil {
    private DataFormatUtil() {
        throw new AssertionError();
    }

    /**
     * 将byte[]转换成InputStream
     * @param bytes
     * @return
     */
    public InputStream byte2InputStream( byte[] bytes ) {
        ByteArrayInputStream bais = new ByteArrayInputStream( bytes );
        return bais;
    }

    /**
     * 将InputStream转换成byte[]
     * @param is
     * @return
     */
    public byte[] inputStream2Bytes( InputStream is ) {
        String str = "";
        byte[] readByte = new byte[1024];
        int readCount = -1;
        try {
            while ((readCount = is.read(readByte, 0, 1024)) != -1) {
                str += new String(readByte).trim();
            }
            return str.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
