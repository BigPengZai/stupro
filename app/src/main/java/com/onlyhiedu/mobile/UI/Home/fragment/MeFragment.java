package com.onlyhiedu.mobile.UI.Home.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Base.BaseFragment;
import com.onlyhiedu.mobile.Model.bean.Avatar;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Consumption.activity.ConsumeActivity;
import com.onlyhiedu.mobile.UI.Emc.DemoHelper;
import com.onlyhiedu.mobile.UI.Emc.presenter.UploadAvatarPresenter;
import com.onlyhiedu.mobile.UI.Emc.presenter.contract.UploadAvatarContract;
import com.onlyhiedu.mobile.UI.Home.activity.KnowActivity;
import com.onlyhiedu.mobile.UI.Info.activity.MyInfoActivity;
import com.onlyhiedu.mobile.UI.Setting.activity.SettingActivity;
import com.onlyhiedu.mobile.Utils.ImageLoader;
import com.onlyhiedu.mobile.Utils.PhotoUtil;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.ScreenUtil;
import com.onlyhiedu.mobile.Utils.UIUtils;
import com.onlyhiedu.mobile.Widget.SettingItemView;
import com.onlyhiedu.mobile.Widget.TakePhotoPopWin;
import com.umeng.analytics.MobclickAgent;
import com.umeng.social.tool.UMImageMark;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuwc on 2017/2/18.
 */

public class MeFragment extends BaseFragment<UploadAvatarPresenter> implements UploadAvatarContract.View {
    public static final String TAG = MeFragment.class.getSimpleName();
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.setting_me)
    SettingItemView mSettingInfo;
    @BindView(R.id.setting_consumption)
    SettingItemView mSettingConsumption;
    @BindView(R.id.setting_share)
    SettingItemView mSettingItemShare;
    @BindView(R.id.setting_know)
    SettingItemView mSettingItemKnow;
    @BindView(R.id.iv_portrait)
    ImageView mAvatar;
    @BindView(R.id.ll_me)
    LinearLayout mLl;

    private String name = "";

    private final int SHARE_REQUEST_CODE = 1;
    private UMImage mUmImage;

    private File mFileOut;
    private ProgressDialog dialog;
    public final static int ALBUM_REQUEST_CODE = 1;
    public final static int CAMERA_REQUEST_CODE = 3;
    // 拍照路径
    public static String SAVED_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/Onlyhi/camera/";
    private String // 指定相机拍摄照片保存地址
            cameraPath = SAVED_IMAGE_DIR_PATH +
            System.currentTimeMillis() + ".png";
    private TakePhotoPopWin mTakePhotoPopWin;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fr_me;
    }

    @Override
    protected void initView() {
        if (!App.bIsGuestLogin) {
            mPresenter.getStuInfo();
        }
    }

    @Override
    protected void initData() {
        if (App.bIsGuestLogin) {
            SPUtil.setAvatarUrl("");
            mTvName.setText("登录/注册");
            mTvName.setTextColor(getResources().getColor(R.color.c_F42440));
            mTvName.setBackgroundResource(R.drawable.radius5);
            mTvName.setGravity(Gravity.CENTER);
            mTvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtil.dip2px(95), ScreenUtil.dip2px(28));
            params.topMargin = ScreenUtil.dip2px(10);
            mTvName.setLayoutParams(params);
        } else {
            mTvName.setText(SPUtil.getName());
        }
        ImageLoader.loadCircleImage(getActivity(), mAvatar, SPUtil.getAvatarUrl());

    }

    public void setTextStyle() {
        mTvName.setText(SPUtil.getName());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.topMargin = ScreenUtil.dip2px(10);
        mTvName.setLayoutParams(params);
        mTvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        mTvName.setTextColor(getResources().getColor(R.color.white));
        mTvName.setBackgroundResource(R.drawable.transparent);
    }


    @OnClick({R.id.iv_portrait, R.id.iv_setting, R.id.setting_me, R.id.setting_consumption, R.id.setting_share, R.id.setting_know, R.id.tv_name})
    public void onClick(View view) {
        if (App.bIsGuestLogin) {
            if (view.getId() != R.id.setting_know) {
                UIUtils.startGuestLoginActivity(mContext, 2);
            } else {
                startActivity(new Intent(getContext(), KnowActivity.class));
            }
        } else {
            switch (view.getId()) {
                case R.id.iv_portrait:
//                    mPresenter.uploadHeadPhoto((MainActivity) getActivity());
                    uploadHeadPhoto();
                    break;
                case R.id.iv_setting:
                    startActivity(new Intent(getContext(), SettingActivity.class));
                    MobclickAgent.onEvent(mContext, "me_setting");
                    break;
                case R.id.setting_me:
                    startActivity(new Intent(getContext(), MyInfoActivity.class));
                    break;
                case R.id.setting_consumption:
                    startActivity(new Intent(getContext(), ConsumeActivity.class));
                    break;
                case R.id.setting_share:
                    //分享file 文件
//                requestSharePermission();
                    shareUrl();
                    break;
                case R.id.setting_know:
                    startActivity(new Intent(getContext(), KnowActivity.class));
                    break;
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SHARE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) shareUrl();
                else Toast.makeText(mContext, "权限未授权", Toast.LENGTH_SHORT).show();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //分享
    private void shareUrl() {
        UMImageMark umImageMark = new UMImageMark();
        umImageMark.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
        umImageMark.setMarkBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.logoicon));
        UMWeb web = new UMWeb("http://www.onlyhi.cn/");
        web.setTitle(name + "邀您体验[嗨课堂]");
        mUmImage = new UMImage(mContext, R.mipmap.logoicon);
        mUmImage.compressFormat = Bitmap.CompressFormat.PNG;
        web.setThumb(mUmImage);
        web.setDescription("嗨，快去体验嗨课堂一对一辅导吧！");
        new ShareAction(getActivity()).withMedia(web)
                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA)
                .setCallback(umShareListener).open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            String plat = initPlatform(platform);
            Toast.makeText(mContext, plat + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            String plat = initPlatform(platform);
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
            Toast.makeText(mContext, plat + " 分享失败啦,请检查是否安装应用哦。", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            String plat = initPlatform(platform);
//            Toast.makeText(mContext, plat + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    private String initPlatform(SHARE_MEDIA platform) {
        switch (platform) {
            case QZONE:
                return "QQ空间";
            case QQ:
                return "QQ";
            case WEIXIN:
                return "微信";
            case WEIXIN_CIRCLE:
                return "微信朋友圈";
        }
        return "";
    }


    public void uploadHeadPhoto() {
        mTakePhotoPopWin = new TakePhotoPopWin(getActivity(), onClickListener);
        //showAtLocation(View parent, int gravity, int x, int y)
        mTakePhotoPopWin.showAtLocation(mLl, Gravity.CENTER, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_take_photo:
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        Intent intent = new Intent();
                        // 指定开启系统相机的Action
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        String out_file_path = SAVED_IMAGE_DIR_PATH;
                        File dir = new File(out_file_path);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        // 把文件地址转换成Uri格式
                        Uri uri = Uri.fromFile(new File(cameraPath));
                        // 设置系统相机拍摄照片完成后图片文件的存放地址
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, CAMERA_REQUEST_CODE);
                    } else {
                        Toast.makeText(getActivity(), "请确认已经插入SD卡",
                                Toast.LENGTH_LONG).show();
                    }
                    mTakePhotoPopWin.dismiss();
                    break;
                case R.id.btn_pick_photo:
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, ALBUM_REQUEST_CODE);
                    mTakePhotoPopWin.dismiss();
                    break;
            }
        }
    };


    //上传头像
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                mFileOut = PhotoUtil.startUCrop(getActivity(), MeFragment.this, cameraPath, 200, 200);
                break;
            case ALBUM_REQUEST_CODE:
                try {
                    Uri uri = data.getData();
                    String absolutePath =
                            PhotoUtil.getAbsolutePath(getActivity(), uri);
                    Log.d(TAG, "path=" + absolutePath);
                    mFileOut = PhotoUtil.startUCrop(getActivity(), MeFragment.this, absolutePath, 200, 200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case UCrop.REQUEST_CROP:
                if (data != null && UCrop.getOutput(data) != null && mFileOut != null) {
                    Log.d(TAG, "mFileOut: " + mFileOut.getAbsolutePath());
                    dialog = ProgressDialog.show(getActivity(), getString(R.string.dl_update_photo), getString(R.string.dl_waiting));
                    dialog.show();
                    mPresenter.uploadAvatar(mFileOut);
                }
                break;
            case UCrop.RESULT_ERROR:
                Toast.makeText(getActivity(), "更新头像失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void uploadAvatarSucess(Avatar data) {
        if (data != null && data.imagePath != null) {
            Log.d("", data.imagePath);
            SPUtil.setAvatarUrl(data.imagePath);
            DemoHelper.getInstance().getUserProfileManager().uploadUserAvatar(data.imagePath);
        }
    }

    @Override
    public void saveAvatarSucess() {
        dialog.dismiss();
        if (mFileOut != null) {
            ImageLoader.loadCircleImage(getActivity(), mAvatar, SPUtil.getAvatarUrl());
            Toast.makeText(getActivity(), getString(R.string.toast_updatephoto_success),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getString(R.string.toast_updatephoto_fail),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getInfoSucess(StudentInfo info) {
        if (info != null && info.iconurl != null && !"".equals(info.iconurl)) {
            ImageLoader.loadCircleImage(getActivity(), mAvatar, SPUtil.getAvatarUrl());
        }
    }
}
