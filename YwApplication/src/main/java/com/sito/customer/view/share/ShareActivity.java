package com.sito.customer.view.share;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sito.customer.BuildConfig;
import com.sito.customer.R;
import com.sito.customer.app.CustomerApp;
import com.sito.customer.view.BaseActivity;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

/**
 * 分享
 * Created by zhangan on 2017/8/29.
 */

public class ShareActivity extends BaseActivity {

    private WebView mWebView;
    private ProgressDialog mProgressD;
    private Handler handler;
    private String mUrl = "/customer/invitation.json?customerId="
            + CustomerApp.getInstance().getCurrentUser().getCustomer().getCustomerId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutAndToolbar(R.layout.activity_share, "邀请TA");
        mWebView = (WebView) findViewById(R.id.web_view);
        mUrl = BuildConfig.HOST + mUrl;
        init();
        loadUrl(mWebView, mUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_share) {
            if (!isAppInstalled(this.getApplicationContext(), "com.tencent.mm")) {
                CustomerApp.getInstance().showToast("没有安装微信");
                return true;
            }
            WXWebpageObject wxWebpageObject = new WXWebpageObject();
            wxWebpageObject.webpageUrl = mUrl;
            WXMediaMessage mediaMessage = new WXMediaMessage(wxWebpageObject);
            mediaMessage.title = "优维+";
            mediaMessage.description = "";
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.message = mediaMessage;
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.scene = SendMessageToWX.Req.WXSceneSession;
            CustomerApp.iwxapi.sendReq(req);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.clearFormData();
        mWebView.getSettings().setJavaScriptEnabled(true);//可用JS
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                loadUrl(view, url);//载入网页
                return true;
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {//载入进度改变而触发
                if (progress == 100) {
                    handler.sendEmptyMessage(1);//如果全部载入,隐藏进度对话框
                }
                super.onProgressChanged(view, progress);
            }
        });
        mProgressD = new ProgressDialog(this);
        mProgressD.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressD.setMessage("数据载入中，请稍候！");
    }

    public void loadUrl(WebView view, final String url) {
        handler.sendEmptyMessage(0);
        view.loadUrl(url);//载入网页
    }

    private boolean isAppInstalled(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }


}
