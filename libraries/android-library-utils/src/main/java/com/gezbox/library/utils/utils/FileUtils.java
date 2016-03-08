package com.gezbox.library.utils.utils;

import android.content.Context;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by chenzhaohua on 16/1/22.
 */
public class FileUtils {

    /**
     * 将Uri对应的内容保存到File中
     *
     * @param context
     * @param uri
     * @param filePath
     */
    public static void saveToFile(Context context, Uri uri, String filePath) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(context.getContentResolver().openInputStream(uri));
            bos = new BufferedOutputStream(new FileOutputStream(filePath, false));
            byte[] buffer = new byte[1024];

            bis.read(buffer);
            do {
                bos.write(buffer);
            } while (bis.read(buffer) != -1);
        } catch (IOException ioe) {

        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {

            }
        }
    }
}
