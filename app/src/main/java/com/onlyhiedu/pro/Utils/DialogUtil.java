package com.onlyhiedu.pro.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.onlyhiedu.pro.R;

/**
 * Created by pengpeng on 2017/2/22.
 */

public class DialogUtil {


    public static void showPresimissFialDialog(Context context,String msg) {
        //创建对话框创建器
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //设置对话框显示小图标
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置标题
        builder.setTitle("权限申请");
        //设置正文

        builder.setMessage("在设置-应用-"+AppUtil.getPackageInfo(context)+"-权限 中开"+msg+"权限，才能正常使用拍照或图片选择功能");

        //添加确定按钮点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里用来跳到手机设置页，方便用户开启权限
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        //添加取消按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //使用构建器创建出对话框对象
        AlertDialog dialog = builder.create();
        dialog.show();//显示对话框
    }
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
        //window.setWindowAnimations(R.style.dialog_center);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wl = window.getAttributes();
       /* wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();*/
// 以下这两句是为了保证按钮可以水平满屏
        int width = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        int height = (int) (((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() * 0.9);
        // wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        if(ScreenUtil.isScreenChange(activity)){
            wl.width = (int) (width * 0.4);
        }else{
            wl.width = (int) (width * 0.8);
        }
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;  // 一般情况下为wrapcontent,最大值为height*0.9
       /* ViewUtils.measureView(contentView);
        int meHeight = contentView.getMeasuredHeight();//height 为0,weight为1时,控件计算所得height就是0
        View textview = contentView.findViewById(R.id.tv_msg);
        ViewUtils.measureView(textview);
        int textHeight = textview.getMeasuredHeight();*/
        if (measuredHeight > height) {
            wl.height = height;
        }
        //wl.horizontalMargin= 0.2f;
// 设置显示位置
        // wl.gravity = Gravity.CENTER_HORIZONTAL;
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
