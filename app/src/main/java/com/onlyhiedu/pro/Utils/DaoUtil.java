package com.onlyhiedu.pro.Utils;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.onlyhiedu.pro.App.App;

/**
 * Created by pengpeng on 2017/2/22.
 */

public class DaoUtil {
    protected static final Object object = new Object();
    private static LiteOrm liteOrm;

    public static LiteOrm getInstance(Context context){
        synchronized (object) {
            if (liteOrm == null) {
                liteOrm = LiteOrm.newSingleInstance(context, "liteorm.db");
            }
            liteOrm.setDebugged(true); // open the log
        }
        return liteOrm;
    }

    public static LiteOrm getInstance(){
        return getInstance(App.getInstance());
    }
}
