package com.gezbox.library.utils.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by albert on 8/1/14.
 */

public class BitmapUtils {
    private static final int DEFAULT_TARGET_WIDTH = 300;
    private static final int DEFAULT_TARGET_HEIGHT = 300;

    /**
     * 计算最接近的inSampleSize值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize1(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * 计算需要的缩小比率
     *
     * @param originalWidth
     * @param originalHeight
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static double calScaleRatio(int originalWidth, int originalHeight, int targetWidth, int targetHeight) {
        if (originalWidth == 0 || originalHeight == 0) {
            return 1;
        } else {
            double ratio = Math.sqrt((targetWidth * targetHeight * 1.0) / (originalWidth * originalHeight));
            if (ratio > 1) {
                return 1;
            } else {
                return ratio;
            }
        }
    }

    /**
     * 计算需要的缩小比率
     *
     * @param originalWidth
     * @param originalHeight
     * @return
     */
    public static double calScaleRatio(int originalWidth, int originalHeight) {
        return calScaleRatio(originalWidth, originalHeight, DEFAULT_TARGET_WIDTH, DEFAULT_TARGET_HEIGHT);
    }

    /**
     * 缩小图片
     *
     * @param filename
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap scaleBitmap(String filename, int targetWidth, int targetHeight, boolean needPortrait) {
        if (TextUtils.isEmpty(filename)) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(filename, options);
        }
        catch (OutOfMemoryError e)
        {
            return null;
        }
        catch(Exception ee)
        {
            return null;
        }

        return scaleBitmap(bitmap, targetWidth, targetHeight, needPortrait);
    }

    //20150616修改 尽量解决OOM问题
//    public  static Bitmap scaleBitmap(String filename, int targetWidth, int targetHeight, boolean needPortrait)
//    {
//        return getCompressedBitmap(filename,100,targetWidth,targetHeight);
//    }

    /**
     * 缩小图片
     *
     * @param filename
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap scaleBitmap(String filename, int targetWidth, int targetHeight) {
        return scaleBitmap(filename, targetWidth, targetHeight, false);
    }

    /**
     * 缩小图片
     *
     * @param filename
     * @return
     */
    public static Bitmap scaleBitmap(String filename) {
        return scaleBitmap(filename, DEFAULT_TARGET_WIDTH, DEFAULT_TARGET_HEIGHT);
    }

    /**
     * 缩小图片
     *
     * @param bitmap
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int targetWidth, int targetHeight, boolean needPortrait) {
        if (bitmap == null) {
            return null;
        }

        float ratio = (float) calScaleRatio(bitmap.getWidth(), bitmap.getHeight(), targetWidth, targetHeight);
        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio);
        if (needPortrait) {
            if (bitmap.getWidth() > bitmap.getHeight()) {
                matrix.postRotate(90);
            }
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 缩小图片
     *
     * @param bitmap
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int targetWidth, int targetHeight) {
        return scaleBitmap(bitmap, targetWidth, targetHeight, false);
    }


    /**
     * 缩小图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmap) {
        return scaleBitmap(bitmap, DEFAULT_TARGET_WIDTH, DEFAULT_TARGET_HEIGHT);
    }


    /**
     * Bitmap 转成文件
     *
     * @param bitmap
     * @return
     */
    public static File bitmapToFile(Bitmap bitmap) {
        File sdRoot = Environment.getExternalStorageDirectory();
        File file = new File(sdRoot, "header.jpg");
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();

            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(byteArray);

            return file;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


    public static Bitmap getMatrixBitmap(Uri mImageCaptureUri, Context context) {

        // 不管是拍照还是选择图片每张图片都有在数据中存储也存储有对应旋转角度orientation值
        // 所以我们在取出图片是把角度值取出以便能正确的显示图片,没有旋转时的效果观看

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(mImageCaptureUri, null, null, null, null);// 根据Uri从数据库中找
        if (cursor != null) {
            cursor.moveToFirst();
            String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路
            String orientation = cursor.getString(cursor
                    .getColumnIndex("orientation"));// 获取旋转的角度
            cursor.close();
            if (filePath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);//根据Path读取资源图片
                int angle = 0;
                if (orientation != null && !"".equals(orientation)) {
                    angle = Integer.parseInt(orientation);
                }
                if (angle != 0) {
                    // 下面的方法主要作用是把图片转一个角度，也可以放大缩小等
                    Matrix m = new Matrix();
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    m.setRotate(angle); // 旋转angle度
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                            m, true);// 从新生成图片

                }
                return bitmap;
            }
            else
                return null;
        }
        else
            return null;
    }



    /**
     * auto fix the imageOrientation
     * @param bm source
     * @param uri image Uri if null user path
     * @param path image path if null use uri
     */
    public static Bitmap autoFixOrientation(Bitmap bm, Uri uri,String path) {
        int deg = 0;
        try {
            ExifInterface exif = null;
            if (uri == null) {
                exif = new ExifInterface(path);
            }
            else if (path == null) {
                exif = new ExifInterface(uri.getPath());
            }

            if (exif == null) {
                return bm;
            }

            String rotate = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            int rotateValue = Integer.parseInt(rotate);
//            System.out.println("orientetion : " + rotateValue);
            switch (rotateValue) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    deg = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    deg = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    deg = 270;
                    break;
                default:
                    deg = 0;
                    break;
            }
        } catch (Exception ee) {
            return bm;
        }
        Matrix m = new Matrix();
        m.preRotate(deg);
        bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

        return bm;
    }







    private static Bitmap getCompressedBitmap(String filePath, int reqSize, int reqWidth, int reqHeight) {

        android.util.Log.d("czh","before reqSize = " + reqSize);
        android.util.Log.d("czh","before reqWidth = " + reqWidth);
        android.util.Log.d("czh","before reqHeight = " + reqHeight);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap result = null;

        try {
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
            result = compressImage(bmp, reqSize, options);

            android.util.Log.d("czh","after reqSize = " + result.getByteCount()/1024);
            android.util.Log.d("czh","after reqWidth = " + result.getWidth());
            android.util.Log.d("czh","after reqHeight = " + result.getHeight());

            bmp.recycle();
            bmp = null;
        } catch (OutOfMemoryError e) {
            result = null;
        } catch (Exception e) {
            result = null;
        }
        return result;

    }


    //计算图片的缩放值
    private static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }



    /**
     *
     * @param image    待处理图片
     * @param reqSize  图片目标内存大小（单位KB）
     * @return
     */
    private static Bitmap compressImage(Bitmap image, int reqSize, BitmapFactory.Options opt) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        while (baos.toByteArray().length / 1024 > reqSize) {
            baos.reset();  //清空baos
            image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10; //递减图片质量
        }
        ByteArrayInputStream stream = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(stream, null, opt);
    }


    /**
     * 获取圆角位图的方法
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
