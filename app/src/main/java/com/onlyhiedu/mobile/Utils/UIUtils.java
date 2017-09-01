package com.onlyhiedu.mobile.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.App.AppManager;
import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.Base.BaseRecyclerAdapter;
import com.onlyhiedu.mobile.Model.http.onlyApis;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Emc.DemoHelper;
import com.onlyhiedu.mobile.UI.Home.activity.HomeNewsWebViewActivity;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.User.activity.OpenIDActivity;
import com.onlyhiedu.mobile.db.DemoDBManager;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.onlyhiedu.mobile.Utils.AppUtil.isMethodsCompat;

/**
 * Created by xuwc on 2017/2/21.
 */

public class UIUtils {
    public static void emcLogin() {
        String pwd = Encrypt.SHA512(SPUtil.getEmcRegName() + "&" + "123456" + ":onlyhi");
        DemoDBManager.getInstance().closeDB();
        DemoHelper.getInstance().setCurrentUserName(SPUtil.getName());
        EMClient.getInstance().login(SPUtil.getEmcRegName(), pwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().chatManager().loadAllConversations();
                EMClient.getInstance().groupManager().loadAllGroups();
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        SPUtil.getName());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }

                // get user's info (this should be get from App's server or 3rd party service)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                Log.d("tag", "登录失败:" + message);
            }
        });
    }

    public static void startLoginActivity(Context context) {
        SPUtil.removeKey(Constants.TOKEN);
        AppManager.getAppManager().AppExit();
        context.startActivity(new Intent(context, OpenIDActivity.class).putExtra(OpenIDActivity.AccountEdgeOut, true).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void startHomeNewsWebViewAct(Context context, String url, String title) {
        context.startActivity(new Intent(context, HomeNewsWebViewActivity.class).putExtra(HomeNewsWebViewActivity.URL, url).putExtra(HomeNewsWebViewActivity.TITLE, title));
    }

    public static void startGuestLoginActivity(Context cxt, int tabPosition) {
        cxt.startActivity(new Intent(cxt, OpenIDActivity.class)
                .putExtra(MainActivity.showPagePosition, tabPosition));
    }

    public static void initCursor(EditText mEdit) {
        CharSequence s = mEdit.getText();
        if (s instanceof Spannable) {
            Spannable spanText = (Spannable) s;
            Selection.setSelection(spanText, s.length());
        }
    }

    public static void setRcDecorationAndLayoutManager(Context context, RecyclerView recyclerView, BaseRecyclerAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.list_divider)
                .size(1)
                .build());
        recyclerView.setAdapter(adapter);
    }

    //横向
    public static void setHorizontalLayoutManager(Context context, RecyclerView recyclerView, BaseRecyclerAdapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
//                .colorResId(R.color.list_divider)
//                .size(1)
//                .build());
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
     *
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
        return cacheSize;
    }

    /**
     * 清楚App缓存
     *
     * @param showToast
     */
    public static void clearAppCache(boolean showToast) {
        final Handler handler = showToast ? new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Toast.makeText(App.getInstance().getApplicationContext(), "缓存清除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(App.getInstance().getApplicationContext(), "缓存清除失败", Toast.LENGTH_SHORT).show();
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


    public static boolean requestPermission(Activity context, int code, String[] permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, permission, code);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static void callLine(Activity activity, String phoneNum) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "拨打电话失败", Toast.LENGTH_SHORT).show();
        }

    }

    public static void getIMUserInfo(String name, Callback call) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("token", SPUtil.getToken())
                .add("userName", name)
                .build();
        Request request = new Request.Builder().url(onlyApis.IM_USER_INFO_URL)
                .post(formBody).build();
        client.newCall(request).enqueue(call);
    }


}