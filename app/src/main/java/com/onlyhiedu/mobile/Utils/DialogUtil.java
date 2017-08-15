package com.onlyhiedu.mobile.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.onlyhiedu.mobile.R;

/**
 * Created by pengpeng on 2017/2/22.
 */

public class DialogUtil {

    public static Dialog showProgressDialog(Context context, String msg, boolean cancleable, boolean outsideTouchable) {
        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.setCancelable(cancleable);
        dialog.setCanceledOnTouchOutside(outsideTouchable);
        View root = View.inflate(context, R.layout.progressview_wrapconent, null);
        TextView tvMsg = (TextView) root.findViewById(R.id.message);
        tvMsg.setText(msg);
        dialog.setContentView(root);
        dialog.show();
        //根据实际view高度调整
        return dialog;
    }

    public static void dismiss(Dialog dialog) {
        if(dialog == null){
            return;
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    //竖屏
    public static Dialog showOnlyAlert(Context activity, String title, String msg,
                                       String firstTxt, String secondTxt,
                                       boolean outsideCancleable, boolean cancleable,
                                       final DialogListener listener) {
        return showAlert(activity, false, title, msg, firstTxt, secondTxt, outsideCancleable, cancleable, listener);
    }
    private static Dialog showAlert(Context context, boolean isButtonVerticle, String title, String msg,
                                    String firstTxt, String secondTxt,
                                    boolean outsideCancleable, boolean cancleable,
                                    final DialogListener listener) {
        Dialog dialog = buildDialog(context, cancleable, outsideCancleable);
        int height = assigAlertView(context, dialog, isButtonVerticle, title, msg, firstTxt, secondTxt, listener);
        setDialogStyle(context, dialog, height);
        dialog.show();
        return dialog;
    }





    private static Dialog buildDialog(Context context, boolean cancleable, boolean outsideTouchable) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(cancleable);
        dialog.setCanceledOnTouchOutside(outsideTouchable);
        return dialog;
    }

    private static int assigAlertView(Context activity, final Dialog dialog, boolean isButtonVerticle,
                                      String title, String msg, String firstTxt, String secondTxt,
                                      final DialogListener listener) {
        View root = View.inflate(activity, isButtonVerticle ? R.layout.dialog_alert_vertical : R.layout.dialog_alert, null);
        TextView tvTitle = (TextView) root.findViewById(R.id.tv_title);
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }

        TextView tvMsg = (TextView) root.findViewById(R.id.tv_msg);
        tvMsg.setText(msg);
        Button button1 = (Button) root.findViewById(R.id.btn_1);
        Button button2 = (Button) root.findViewById(R.id.btn_2);
        if (TextUtils.isEmpty(firstTxt)) {
            root.findViewById(R.id.ll_container).setVisibility(View.GONE);
            root.findViewById(R.id.line).setVisibility(View.GONE);
        } else {
            button1.setVisibility(View.VISIBLE);
            button1.setText(firstTxt);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPositive(dialog);
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            //btn2
            if (TextUtils.isEmpty(secondTxt)) {
                root.findViewById(R.id.line_btn2).setVisibility(View.GONE);
                button2.setVisibility(View.GONE);
            } else {
                root.findViewById(R.id.line_btn2).setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button2.setText(secondTxt);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onNegative(dialog);
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
            }
        }
        dialog.setContentView(root);
        int height = mesureHeight(root, R.id.tv_msg);
        return height;
    }

    private static void setDialogStyle(Context activity, Dialog dialog, int measuredHeight) {
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wl = window.getAttributes();
        int width = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        int height = (int) (((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() * 0.9);
        if (ScreenUtil.isScreenChange(activity)) {
            wl.width = (int) (width * 0.54);
        } else {
            wl.width = (int) (width * 0.94);
        }
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        if (measuredHeight > height) {
            wl.height = height;
        }
        if (!(activity instanceof Activity)) {
            wl.type = WindowManager.LayoutParams.TYPE_TOAST;
        }
        dialog.onWindowAttributesChanged(wl);
    }

    /**
     * @param root
     * @param id   height为0,weight为1的scrollview包裹的view的id,如果没有,传0或负数即可
     * @return
     */
    private static int mesureHeight(View root, int id) {
        measureView(root);
        int height = root.getMeasuredHeight();
        int heightExtra = 0;
        if (id > 0) {
            View view = root.findViewById(id);
            if (view != null) {
                measureView(view);
                heightExtra = view.getMeasuredHeight();
            }
        }
        return height + heightExtra;
    }

    private static void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    ,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int lpHeight = p.height;
        int lpWidth = p.width;
        int childHeightSpec;
        int childWidthSpec;
        if (lpHeight > 0) {   //如果Height是一个定值，那么我们测量的时候就使用这个定值
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight,
                    View.MeasureSpec.EXACTLY);
        } else {  // 否则，我们将mode设置为不指定，size设置为0
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
        }
        if (lpWidth > 0) {
            childWidthSpec = View.MeasureSpec.makeMeasureSpec(lpHeight,
                    View.MeasureSpec.EXACTLY);
        } else {
            childWidthSpec = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }
}
