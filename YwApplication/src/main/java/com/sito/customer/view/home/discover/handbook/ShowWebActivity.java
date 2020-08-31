package com.sito.customer.view.home.discover.handbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.sito.customer.R;
import com.sito.customer.common.ConstantStr;
import com.sito.customer.view.BaseActivity;


public class ShowWebActivity extends BaseActivity {

    private Button mCall;
    private WebView mWebview;
    private ProgressDialog mProgressD;
    private Handler handler;

    private int mType = 0;
    private String mUrl;
    private String mTitle;
    private int titleId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bData = getIntent().getExtras();
        String url = bData.getString(ConstantStr.KEY_URL);
        mTitle = bData.getString(ConstantStr.KEY_TITLE);
        if (url != null && url.length() > 0) {
            mUrl = url.trim();
            if (!mUrl.startsWith("http") && !mUrl.startsWith("file")) {
                mUrl = "http://" + mUrl;
            }
        } else {
//            finish();
//            return;
        }
        setLayoutAndToolbar(R.layout.showweb, mTitle);
        init();
//            String urls = "file:///android_asset/instrument/productList.html";
        loadurl(mWebview, mUrl);
    }

    @Override
    protected void onResume() {
        if (mType == 1) {//landscape
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mProgressD != null) {
            mProgressD.dismiss();
        }

        System.gc();
    }

    private void init() {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                try {
                    if (!Thread.currentThread().isInterrupted()) {
                        switch (msg.what) {
                            case 0:
                                mProgressD.show();//显示进度对话框
                                break;
                            case 1:
                                mProgressD.hide();//隐藏进度对话框，不可使用dismiss()、cancel(),否则再次调用show()时，显示的对话框小圆圈不会动。
                                break;
                        }
                    }
                    super.handleMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mWebview = (WebView) findViewById(R.id.webview);
        mWebview.clearCache(true);
        mWebview.clearHistory();
        mWebview.clearFormData();
        mWebview.getSettings().setJavaScriptEnabled(true);//可用JS
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
        mWebview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                loadurl(view, url);//载入网页
                return true;
            }

        });
        mWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {//载入进度改变而触发
                if (progress == 100) {
                    handler.sendEmptyMessage(1);//如果全部载入,隐藏进度对话框
                }
                super.onProgressChanged(view, progress);
            }
        });

        mWebview.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url.contains(".apk")) {
                    /*Bundle bundle =new Bundle();
                    bundle.putString(ConstantUtil.KEY_URL,url);
        			bundle.putString(ConstantUtil.KEY_NAME,"ddy");
            		Intent intent = new Intent(ShowWebActivity.this,DownloadService.class);
            		intent.putExtras(bundle);
                    startService(intent);*/
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
        mProgressD = new ProgressDialog(this);
        mProgressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressD.setMessage("数据载入中，请稍候！");
    }

    public void loadurl(final WebView view, final String url) {
        handler.sendEmptyMessage(0);
        mWebview.loadUrl(url);//载入网页
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回键
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
