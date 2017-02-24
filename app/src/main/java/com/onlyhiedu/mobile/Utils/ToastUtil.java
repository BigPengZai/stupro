package com.onlyhiedu.mobile.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by pengpeng on 2017/2/23.
 */

public class ToastUtil {
    private Context mContext;
    private static ToastUtil mInstance;
    private Toast mToast;



    public ToastUtil(Context ctx) {
        mContext = ctx;
    }

    public void showToast(String text) {
        if(mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
