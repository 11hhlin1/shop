package com.gjj.applibrary.util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;


import com.gjj.applibrary.log.L;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;


/**
 * 图片压缩工具类
 * 
 */
public class ImageCompress {

    public static final int DEFAULT_WIDTH = 1500;
    public static final int DEFAULT_HEIGHT = 1500;
    public static final int DEFAULT_SIZE = 200 * 1024;
    public static final int DEFAULT_QUALITY = 70;
    private static final String TAG_UPLOAD = "upload";
    private static float testPixels = 0;
    private static int testDefaultSize = 0;

    /**
     * 图片压缩参数
     * 
     */
    public static class CompressOptions {

        public int maxWidth = DEFAULT_WIDTH;
        public int maxHeight = DEFAULT_HEIGHT;
        public int maxSize = DEFAULT_SIZE;

        /**
         * 压缩后图片字节
         */
        public byte[] compressBytes;
        /**
         * 压缩后图片保存的文件
         */
        public File destFile;
        /**
         * 图片压缩格式,默认为jpg格式
         */
        public CompressFormat imgFormat = CompressFormat.JPEG;

        public String uri;

        public int fileKey;

        public int oriFileSize;
        /**
         * 图片原始宽
         */
        public int oriW;
        /**
         * 图片原始高
         */
        public int oriH;
        /**
         * 图片压缩后宽
         */
        public int newW;
        /**
         * 图片压缩后高
         */
        public int newH;
        /**
         * 图片压缩质量
         */
        public int quality;
        /**
         * 缩放
         */
        public float scale;

        public boolean isUploadTest;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void compressImageFile(CompressOptions compressOptions) {

        String filePath = compressOptions.uri;


        if (null == filePath) {
            return;
        }

        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            compressOptions.oriFileSize = fis.available();
        } catch (Exception e) {
            return;
        } finally {
            if (null != fis) {
                Util.closeCloseable(fis);
            }
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        compressOptions.oriW = options.outWidth;
        compressOptions.oriH = options.outHeight;

        int agree = ImageUtil.readPictureDegree(filePath);
        int actualWidth = options.outWidth;
        int actualHeight = options.outHeight;
        if (agree % 180 == 90) {
            actualWidth = options.outHeight;
            actualHeight = options.outWidth;
        }

//        UploadTaskStatistics.markFileOriDimension(compressOptions.fileKey, actualWidth, actualHeight);

        int desiredWidth = getResizedDimension(compressOptions.maxWidth, compressOptions.maxHeight, actualWidth, actualHeight);
        int desiredHeight = getResizedDimension(compressOptions.maxHeight, compressOptions.maxWidth, actualHeight, actualWidth);

        options.inJustDecodeBounds = false;
        options.inSampleSize = findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
        options.inMutable = true;
        // options.inPreferredConfig = Config.RGB_565;

        L.i("%s calculate compress options: file[%s], actualWidth[%s], actualHeight[%s], desiredWidth[%s], desiredHeight[%s], inSampleSize[%s]",
                TAG_UPLOAD, filePath, actualWidth, actualHeight, desiredWidth, desiredHeight, options.inSampleSize);

        Bitmap bitmap = null;
        Bitmap destBitmap = null;
        try {
            destBitmap = BitmapFactory.decodeFile(filePath, options);
//            UploadTaskStatistics.addFileCPU(compressOptions.fileKey);
//            UploadTaskStatistics.addFileMem(compressOptions.fileKey);
            if (null == destBitmap) {
                L.i("%s decode file null, file[%s]", TAG_UPLOAD, filePath);
                return;
            }
            Matrix matrix = null;
            int destWith = destBitmap.getWidth();
            int destHeight = destBitmap.getHeight();
            // 旋转
            if (agree % 360 != 0) {
                L.i("%s image rotation[%s], file[%s]", TAG_UPLOAD, agree, filePath);
                if (null == matrix) {
                    matrix = new Matrix();
                }
                matrix.setRotate(agree);
            }
            // 压大小
            if (agree % 180 == 90) {
                destWith = destBitmap.getHeight();
                destHeight = destBitmap.getWidth();
            }
            if (destWith > desiredWidth || destHeight > desiredHeight) {
                L.i("%s scale by size: desiredWidth[%s], desiredHeight[%s], file[%s]", TAG_UPLOAD, desiredWidth, desiredHeight, filePath);
                if (null == matrix) {
                    matrix = new Matrix();
                }
                final float sx = desiredWidth / (float) destWith;
                final float sy = desiredHeight / (float) destHeight;
                matrix.postScale(sx, sy);
            }

            if (null != matrix) {
                bitmap = Bitmap.createBitmap(destBitmap, 0, 0, destBitmap.getWidth(), destBitmap.getHeight(), matrix, true);
                if (!destBitmap.isRecycled()) {
                    destBitmap.recycle();
                }
                destBitmap = null;
            } else {
                bitmap = destBitmap;
            }
            if (null == bitmap) {
                L.i("%s create bitmap null, file[%s]", TAG_UPLOAD, filePath);
                return;
            }
//            UploadTaskStatistics.addFileCPU(compressOptions.fileKey);
//            UploadTaskStatistics.addFileMem(compressOptions.fileKey);
            compressOptions.newW = bitmap.getWidth();
            compressOptions.newH = bitmap.getHeight();
            compressOptions.scale = (float) (Math.round((float) compressOptions.newW / compressOptions.oriW * 100)) / 100;

//            UploadTaskStatistics.markFileAfterScaleDimension(compressOptions.fileKey, compressOptions.newW, compressOptions.newH);
            // compress file if need
            compressFile(compressOptions, bitmap);
            L.i("%s compress complete, file[%s]", TAG_UPLOAD, filePath);
        } catch (OutOfMemoryError e) {
            L.e(e);
            L.e("%s OutOfMemoryError[%s], file[%s]", TAG_UPLOAD, e, filePath);
        } finally {
            if (null != bitmap && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            System.gc();
        }
    }

    /**
     * compress file from bitmap with compressOptions
     * 
     * @param compressOptions
     * @param bitmap
     */
    private void compressFile(CompressOptions compressOptions, Bitmap bitmap) {
        String filePath = compressOptions.uri;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (compressOptions.isUploadTest) {
            // TODO 配合专项测试
            int size = 0;
            if (testPixels <= 0) {
                testPixels = bitmap.getWidth() * bitmap.getHeight();
                bitmap.compress(CompressFormat.JPEG, 100, baos);
                size = baos.size();
                testDefaultSize = size;
                baos.reset();
            } else {
                size = (int) (bitmap.getWidth() * bitmap.getHeight() / testPixels * testDefaultSize);
            }
//            UploadTaskStatistics.markAfterScaleFileSize(compressOptions.fileKey, size);
            int cul = (int) System.currentTimeMillis();
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    bitmap.setPixel(bitmap.getWidth() / 2 + i, bitmap.getHeight() / 2 + j, cul);
                }
            }
        }
        int quality = DEFAULT_QUALITY;
        L.i("%s scale by quality[%s], file[%s]", TAG_UPLOAD, quality, filePath);
        bitmap.compress(CompressFormat.JPEG, quality, baos);
        while (baos.size() > compressOptions.maxSize) {
            quality = quality - 10;
            L.i("%s scale by quality[%s], file[%s] ", TAG_UPLOAD, quality, filePath);
            baos.reset();
            if (quality <= 0) {
                break;
            }
            bitmap.compress(CompressFormat.JPEG, quality, baos);
//            UploadTaskStatistics.addFileCPU(compressOptions.fileKey);
//            UploadTaskStatistics.addFileMem(compressOptions.fileKey);
        }
        compressOptions.quality = quality;
        compressOptions.compressBytes = baos.toByteArray();

        if (null != compressOptions.destFile) {
            OutputStream stream = null;
            try {
                stream = new FileOutputStream(compressOptions.destFile);
                baos.writeTo(stream);
                baos.flush();

            } catch (Exception e) {
                L.e(e);
            } finally {
                if (stream != null) {
                    Util.closeCloseable(stream);
                }
            }
        }
        baos.reset();
        Util.closeCloseable(baos);
    }

    private static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary) {

        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }

        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }
}