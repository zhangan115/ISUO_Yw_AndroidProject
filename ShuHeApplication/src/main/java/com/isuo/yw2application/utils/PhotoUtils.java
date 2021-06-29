package com.isuo.yw2application.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.isuo.yw2application.app.Yw2Application;
import com.sito.library.luban.Luban;
import com.sito.library.utils.DataUtil;
import com.sito.library.utils.DisplayUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 照片处理
 * Created by pingan on 2017/7/11.
 */

public class PhotoUtils {


    public interface PhotoListener {
        void onSuccess(File file);
    }

    public static void cropPhoto(final Context context, File photoFile, final PhotoListener listener) {
        cropPhoto(context, photoFile,false, "",false, listener);
    }

    public static void cropPhoto(final Context context, File photoFile, String mark, final PhotoListener listener) {
        cropPhoto(context, photoFile, false, mark, true,listener);
    }

    public static void cropPhoto(final Context context, File photoFile, final boolean cleanFile, final String mark,final boolean showMark, final PhotoListener listener) {
        Observable.just(photoFile)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        File file1 = null;
                        File file2 = null;
                        try {
                            file1 = Luban.with(context).load(file).get().get(0);
                            if (!showMark){
                                file.delete();
                                return Observable.just(file1);
                            }
                            file2 = new File(file1.getParent(), System.currentTimeMillis() + ".jpg");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (file1 != null && file.exists() && !cleanFile) {
                            try {
                                FileInputStream fi = new FileInputStream(file1);
                                Bitmap bitmap = BitmapFactory.decodeStream(fi);
                                Bitmap newBitmap = createWatermark(bitmap, mark);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                newBitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                                newBitmap.recycle();
                                FileOutputStream fos = new FileOutputStream(file2);
                                fos.write(stream.toByteArray());
                                fos.flush();
                                fos.close();
                                stream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            file.delete();
                            file1.delete();
                        }
                        return Observable.just(file2);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(File file) {
                        if (listener == null) {
                            return;
                        }
                        listener.onSuccess(file);
                    }
                });

    }

    static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    // 为图片target添加水印文字
    // Bitmap target：被添加水印的图片
    // String mark：水印文章
    private static Bitmap createWatermark(Bitmap target, @Nullable String mark) {
        int w = target.getWidth();
        int h = target.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint p = new Paint();
        // 水印的颜色
        p.setColor(Color.WHITE);
        // 水印的字体大小
        p.setTextSize(DisplayUtil.sp2px(Yw2Application.getInstance(), 14));
        p.setAntiAlias(true);// 去锯齿
        canvas.drawBitmap(target, 0, 0, p);
        float textHeight = p.descent() - p.ascent();
        String appName = "小梭优维+";
        String appContent = "运维管理系统";
        canvas.drawText(appName, 20, 20 + textHeight, p);
        canvas.drawText(appContent, 20, 30 + 2 * textHeight, p);
        if (!TextUtils.isEmpty(mark)) {
            float markWidth = p.measureText(mark);
            canvas.drawText(mark, w - 20 - markWidth, 20 + textHeight, p);
        }
        String customerName = Yw2Application.getInstance().getCurrentUser().getCustomer().getCustomerName();
        canvas.drawText(customerName, 20, h - 20 - textHeight, p);
        String userName = Yw2Application.getInstance().getCurrentUser().getRealName();
        float nameWidth = p.measureText(userName);
        canvas.drawText(userName, w - 50 - nameWidth, h - 20 - 2 * textHeight, p);
        String date = DataUtil.timeFormat(System.currentTimeMillis(), null);
        float dateWidth = p.measureText(date);
        canvas.drawText(date, w - 50 - dateWidth, h - 20 - textHeight, p);
//        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.save();
        canvas.restore();
        return bmp;
    }


    public static File from(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        String fileName = getFileName(context, uri);
        String[] splitName = splitFileName(fileName);
        File tempFile = File.createTempFile(splitName[0], splitName[1]);
        tempFile = rename(tempFile, fileName);
        tempFile.deleteOnExit();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            copy(inputStream, out);
            inputStream.close();
        }

        if (out != null) {
            out.close();
        }
        return tempFile;
    }

    static File rename(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (!newFile.equals(file)) {
            if (newFile.exists()) {
                if (newFile.delete()) {
                    Log.d("FileUtil", "Delete old " + newName + " file");
                }
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to " + newName);
            }
        }
        return newFile;
    }

    static String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        return copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    }

    static long copyLarge(InputStream input, OutputStream output, byte[] buffer)
            throws IOException {
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final int EOF = -1;


}