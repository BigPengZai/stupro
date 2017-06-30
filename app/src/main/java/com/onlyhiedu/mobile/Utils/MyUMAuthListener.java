package com.onlyhiedu.mobile.Utils;

import android.widget.Toast;

import com.onlyhiedu.mobile.App.App;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by Administrator on 2017/6/30.
 */

public abstract class MyUMAuthListener implements UMAuthListener {
    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        Toast.makeText(App.getInstance().getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        Toast.makeText(App.getInstance().getApplicationContext(), "授权取消", Toast.LENGTH_SHORT).show();
    }
}
