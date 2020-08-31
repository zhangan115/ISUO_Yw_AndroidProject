package com.sito.customer.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateManager {
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int DOWN_ERROR = 3;
    /* 下载包安装路径 */
    private String savePath = "";
    private String saveFileName = "";
    private String mUrl = "";
    private boolean interceptFlag = false;
    private int progress;
    private AlertDialog downloadDialog;
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    private Context mContext;

    private Thread downLoadThread;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE: {
                    mProgress.setProgress(progress);
                    downloadTv.setText(String.format("%d%s", progress, "%"));
                    break;
                }
                case DOWN_OVER: {
                    installApk();
                    break;
                }
                case DOWN_ERROR:
                    if (downloadTv != null) {
                        downloadTv.setText("下载失败,请重试!");
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };
    private TextView downloadTv;
    private String fileName;//下载文件的名称
    private DownLoadCancelListener cancelListener;

    public UpdateManager(Context context, String url, @Nullable DownLoadCancelListener cancelListener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        this.cancelListener = cancelListener;
        this.mContext = context;
        this.mUrl = url;
        int index = url.lastIndexOf("/") + 1;
        if (index < url.length()) {
            fileName = mUrl.substring(index);
            if (!fileName.endsWith(".apk")) {
                fileName = fileName + ".apk";
            }
        } else {
            fileName = "sito.apk";
        }
    }

    public interface DownLoadCancelListener {
        void onCancelUpload();
    }

    public void updateApp() {
        showDownloadDialog();
    }

    private void showDownloadDialog() {
        long freeDiskSize = 0;
        if (externalMemoryAvailable()) {
            freeDiskSize = getAvailableExternalMemorySize();
        } else {
            freeDiskSize = getAvailableInternalMemorySize();
        }
        if (freeDiskSize < 52428800) {//50 * 1024 * 1024
            CustomerApp.getInstance().showToast(mContext.getString(R.string.no_more_free_disk));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.download_prompt);
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    interceptFlag = true;
                }
            });
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            View v = inflater.inflate(R.layout.progress, null);
            mProgress = (ProgressBar) v.findViewById(R.id.progress);

            downloadTv = (TextView) v.findViewById(R.id.download_noteTv);
            builder.setView(v);
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (cancelListener != null) {
                        cancelListener.onCancelUpload();
                    }
                }
            });
            builder.setPositiveButton(R.string.cancel,
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            interceptFlag = true;
                            if (cancelListener != null) {
                                cancelListener.onCancelUpload();
                            }
                        }
                    });
            downloadDialog = builder.create();
            downloadDialog.show();
            downloadApk();
        }
    }

    private Runnable mDownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(mUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);
                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numRead = is.read(buf);
                    count += numRead;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numRead <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numRead);
                } while (!interceptFlag);// 点击取消就停止下载.

                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(DOWN_ERROR);
            }
        }
    };

    /**
     * @return SDCARD is available?
     */
    private boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * @return Get free size in SDCARD
     */
    private long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            savePath = path.getPath() + "/download/";
            saveFileName = savePath + fileName;
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return 0;
        }
    }

    /**
     * @return Get free size in internal SDCARD
     */
    private long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        savePath = path.getPath() + "/download/";
        saveFileName = savePath + fileName;
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 下载apk
     */
    private void downloadApk() {
        downLoadThread = new Thread(mDownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     */
    private void installApk() {
        File apkFile = new File(saveFileName);
        if (!apkFile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".fileprovider", apkFile);
            i.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        }
        try {
            mContext.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
            CustomerApp.getInstance().showToast("App已经下载完毕,请在下载目录下安装");
        }
    }
}