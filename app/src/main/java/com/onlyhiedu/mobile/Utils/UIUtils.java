package com.onlyhiedu.mobile.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.App.AppManager;
import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.User.activity.LoginActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.onlyhiedu.mobile.Utils.AppUtil.isMethodsCompat;

/**
 * Created by xuwc on 2017/2/21.
 */

public class UIUtils {

    public static void startLoginActivity(Context context) {
        SPUtil.removeKey(Constants.TOKEN);
        AppManager.getAppManager().AppExit();
        context.startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    public static void setRcDecorationAndLayoutManager(Context context, RecyclerView recyclerView, BaseRecyclerAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.list_divider)
                .size(1)
                .build());
        recyclerView.setAdapter(adapter);
    }

    public static void setRecycleAdapter(Context context, RecyclerView recyclerView, BaseRecyclerAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    public static int StatusBarLightMode(Activity activity) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }


    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout ll_tab = null;

        try {
            ll_tab = (LinearLayout) tabStrip.get(tabs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int left = ScreenUtil.dip2px(leftDip);
        int right = ScreenUtil.dip2px(rightDip);

        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);

            if ((i + 1) % 2 == 0) {
                params.leftMargin = left / 2;
                params.rightMargin = right;

            } else {
                params.leftMargin = left;
                params.rightMargin = right / 2;
            }
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    /**
     * 加密后的密码
     *
     * @param phone
     * @param pwd
     * @return
     */
    public static String sha512(String phone, String pwd) {
        return Encrypt.SHA512(phone + "&" + pwd + ":onlyhi");
    }

    /**
     * 获取项目Cache大小
     * @param context
     */
    public static String calculateCacheSize(Context context) {
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = context.getFilesDir();
        File cacheDir = context.getCacheDir();

        fileSize += AppFilesUtil.getDirSize(filesDir);
        fileSize += AppFilesUtil.getDirSize(cacheDir);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            File externalCacheDir = AppUtil
                    .getExternalCacheDir(context);
            fileSize += AppFilesUtil.getDirSize(externalCacheDir);
        }
        if (fileSize > 0)
            cacheSize = AppFilesUtil.formatFileSize(fileSize);
        return  cacheSize;
    }

    /**
     * 清楚App缓存
     * @param showToast
     */
    public static void clearAppCache(boolean showToast) {
        final Handler handler = showToast ? new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Toast.makeText(App.getInstance().getApplicationContext(),"缓存清除成功",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(App.getInstance().getApplicationContext(),"缓存清除失败",Toast.LENGTH_SHORT).show();
                }
            }
        } : null;
        AppOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    AppFilesUtil.cleanInternalCache(App.getInstance().getApplicationContext());
                    // 2.2版本才有将应用缓存转移到sd卡的功能
                    if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
                        AppFilesUtil.deleteFilesByDirectory(AppUtil
                                .getExternalCacheDir(App.getInstance().getApplicationContext()));
                    }
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                if (handler != null)
                    handler.sendMessage(msg);
            }
        });
    }


}