package com.onlyhiedu.pro.Utils;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.onlyhiedu.pro.R;

public class SnackBarUtils {

    public static void show(View v, String msg) {
        Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void show(View v, String msg, int color) {
        Snackbar make = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
        View view = make.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
        make.show();
    }
}
