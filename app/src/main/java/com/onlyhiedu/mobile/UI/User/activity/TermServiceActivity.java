package com.onlyhiedu.mobile.UI.User.activity;

import android.app.ProgressDialog;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;

import butterknife.BindView;

/**
 * Created by pengpeng on 2017/4/21.
 */

public class TermServiceActivity extends SimpleActivity {
    @BindView(R.id.wb_service)
    WebView mWb_Service;
    ProgressDialog dialog = null;
    @Override
    protected int getLayout() {
        return R.layout.activity_service;
    }

    @Override
    protected void initEventAndData() {
        setToolBar("服务条款");
        mWb_Service.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWb_Service.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
            }
        });
        WebSettings wSet = mWb_Service.getSettings();
        wSet.setDefaultTextEncodingName("gbk");
        wSet.setJavaScriptEnabled(true);
        wSet.setSupportZoom(true);
        wSet.setBuiltInZoomControls(true);
        wSet.setUseWideViewPort(true);
        wSet.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wSet.setLoadWithOverviewMode(true);
        dialog = ProgressDialog.show(this,null,"页面加载中，请稍后..");
        mWb_Service.loadUrl("file:///android_asset/termservice.html");
    }
}
