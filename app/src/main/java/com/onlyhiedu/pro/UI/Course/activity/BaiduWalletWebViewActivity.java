package com.onlyhiedu.pro.UI.Course.activity;

import android.text.TextUtils;
import android.webkit.WebView;

import com.onlyhiedu.pro.Base.SimpleActivity;
import com.onlyhiedu.pro.R;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/8.
 */

public class BaiduWalletWebViewActivity extends SimpleActivity {


    public static final String URL = "url";
    public static final String TITLE = "title";

    @BindView(R.id.web_view)
    WebView mWebView;

    @Override
    protected int getLayout() {
        return R.layout.activity_baiduwallet_webview;
    }

    @Override
    protected void initEventAndData() {

        String title = getIntent().getStringExtra(TITLE);
        if(!TextUtils.isEmpty(title)){
            setToolBar(title);
        }

        String url = getIntent().getStringExtra(URL);
        if(!TextUtils.isEmpty(url)){
            mWebView.loadUrl(url);
        }
    }
}
