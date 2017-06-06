package com.onlyhiedu.mobile.UI.Home.activity;

import android.text.TextUtils;
import android.webkit.WebView;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;

import butterknife.BindView;

public class HomeNewsWebViewActivity extends SimpleActivity {

    public static final String URL = "url";

    @BindView(R.id.web_view)
    WebView mWebView;

    @Override
    protected int getLayout() {
        return R.layout.activity_home_news_web_view;
    }

    @Override
    protected void initEventAndData() {
        setToolBar("精选好闻");
        String url = getIntent().getStringExtra(URL);
        if(!TextUtils.isEmpty(url)){
            mWebView.loadUrl(url);
        }
    }


}
