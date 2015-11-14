package com.daking.app.client_common.util.file;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5
 * Created by daking on 15/11/8.
 */
public class MD5 {
    /**
     * MD5加密
     */
    public static String encode(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return getHashString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 进行MD5加密N次
     *
     * @param content 原字符串
     * @param count   加密次数
     * @return
     */
    public static String encodeMulti(String content, int count) {
        if (count < 0) {
            return encode(content);
        }

        String s1 = content;
        for (int i = 0; i < count; i++) {
            s1 = encode(s1);
        }
        return s1;
    }

    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }

}
