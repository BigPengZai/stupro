package com.onlyhiedu.mobile.Utils;

import android.content.DialogInterface;

/**
 * Created by pengpeng on 2017/2/22.
 */

public abstract  class DialogListener {
    public abstract void onPositive(DialogInterface dialog);
    public abstract void onNegative(DialogInterface dialog);
    public void onCancle(){}
}
