package com.onlyhiedu.mobile.Widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.AppUtil;
import com.onlyhiedu.mobile.Utils.ScreenUtil;


/**
 * @author http://blog.csdn.net/finddreams
 * @Description:带进度条的WebView
 */
@SuppressWarnings("deprecation")
public class ProgressWebView2 extends WebView {

    private ProgressBar progressbar;
    private ProgressDialog dialog;


    public ProgressWebView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                ScreenUtil.dip2px(3), 0, 0));
        Drawable drawable = context.getResources().getDrawable(R.drawable.progress_bar_states);
        progressbar.setProgressDrawable(drawable);
        addView(progressbar);

        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new MyWebViewClient());
//        setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
//                Uri uri = Uri.parse(s);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                getContext().startActivity(intent);
//            }
//        });
        dialog = ProgressDialog.show(context, null, "页面加载中，请稍后..");


        getSettings().setJavaScriptEnabled(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        //是否可以缩放
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
        getSettings().setDisplayZoomControls(false);

        //设置 缓存模式
        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        getSettings().setDomStorageEnabled(true);

    }

    public class WebChromeClient extends android.webkit.WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }


    public class MyWebViewClient extends WebViewClient {

        private Uri mStartWalletUri;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri parse = Uri.parse(url);
            if (parse.getScheme().equals("baiduwallet")) {  //唤醒百度钱包
                mStartWalletUri = parse;
                //已安装百度钱包
                if (AppUtil.isPkgInstalled("com.baidu.wallet")) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } else {
                    Toast.makeText(getContext(), "请先安装百度钱包", Toast.LENGTH_SHORT).show();
                }
            } else if (url.endsWith(".apk")) {
                if (AppUtil.isPkgInstalled("com.baidu.wallet")) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, mStartWalletUri));
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, parse);
                    getContext().startActivity(intent);
                }
            } else {
                view.loadUrl(url);
            }
            return false;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}   