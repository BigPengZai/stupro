package com.onlyhiedu.pro.UI.Home.activity;

import android.app.ProgressDialog;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.onlyhiedu.pro.Base.SimpleActivity;
import com.onlyhiedu.pro.R;

import butterknife.BindView;

/**
 * Created by pengpeng on 2017/5/19.
 */

public class KnowTitleActivity extends SimpleActivity {
    @BindView(R.id.web_view)
    WebView mWb_Service;
    ProgressDialog dialog;

    @Override
    protected int getLayout() {
        return R.layout.activity_know_tile;
    }

    @Override
    protected void initEventAndData() {
        String position = getIntent().getStringExtra("position");
        if (position != null) {
            setToolBar(position + "/6");
        }

        mWb_Service.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWb_Service.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
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
        dialog = ProgressDialog.show(this, null, "页面加载中，请稍后..");
        //http://www.onlyeduhi.com/app/problem5.html
        mWb_Service.loadUrl("http://www.onlyeduhi.cn/app/problem" + position + ".html");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWb_Service != null) {
            mWb_Service.removeAllViews();
            mWb_Service.destroy();
        }
    }
}
