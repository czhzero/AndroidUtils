package com.gezbox.library.utils.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenzhaohua on 16/1/20.
 */
public class StringUtils {

    /**
     * convert input stream to string
     *
     * @param inputStream
     * @param encodings
     * @return
     * @throws IOException
     */
    public static String convertStreamToString(InputStream inputStream, String... encodings) throws IOException {
        String encoding;
        if (encodings.length == 0) {
            encoding = "UTF-8";
        } else {
            encoding = encodings[0];
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return new String(baos.toByteArray(), encoding);
    }

    /**
     * 判断是否中文
     * @param str
     * @return boolean
     */
    public static boolean isOnlyChinaese(String str) {

        if (str == null) {
            return false;
        }

        String regex = "([\u4e00-\u9fa5]+)";

        Matcher matcher = Pattern.compile(regex).matcher(str);
        String result = "";
        while (matcher.find()) {
            result += matcher.group();
        }

        if (result.length() == str.length()) {
            return true;
        } else {
            return false;
        }

    }

}
