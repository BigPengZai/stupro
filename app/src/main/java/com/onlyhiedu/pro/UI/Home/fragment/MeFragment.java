package com.onlyhiedu.pro.UI.Home.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.business.session.actions.PickImageAction;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.nos.NosService;
import com.netease.nimlib.sdk.nos.model.NosThumbParam;
import com.netease.nimlib.sdk.nos.util.NosThumbImageUtil;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.onlyhiedu.pro.Base.BaseFragment;
import com.onlyhiedu.pro.IM.contact.activity.UserProfileSettingActivity;
import com.onlyhiedu.pro.IM.contact.helper.UserUpdateHelper;
import com.onlyhiedu.pro.Model.bean.Avatar;
import com.onlyhiedu.pro.Model.bean.StudentInfo;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.UI.Consumption.activity.ConsumeActivity;
import com.onlyhiedu.pro.UI.Home.activity.KnowActivity;
import com.onlyhiedu.pro.UI.Home.activity.MineOrdersActivity;
import com.onlyhiedu.pro.UI.Home.presenter.UploadAvatarPresenter;
import com.onlyhiedu.pro.UI.Home.presenter.contract.UploadAvatarContract;
import com.onlyhiedu.pro.UI.Info.activity.MyInfoActivity;
import com.onlyhiedu.pro.UI.Setting.activity.SettingActivity;
import com.onlyhiedu.pro.Utils.DialogUtil;
import com.onlyhiedu.pro.Utils.ImageLoader;
import com.onlyhiedu.pro.Utils.PhotoUtil;
import com.onlyhiedu.pro.Utils.SPUtil;
import com.onlyhiedu.pro.Utils.ScreenUtil;
import com.onlyhiedu.pro.Utils.UIUtils;
import com.onlyhiedu.pro.Widget.SettingItemView;
import com.onlyhiedu.pro.Widget.TakePhotoPopWin;
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
    //我的订单
    @BindView(R.id.setting_orders)
    SettingItemView mSettingOrders;

    private final int SHARE_REQUEST_CODE = 1;
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 2;
    private UMImage mUmImage;

    private File mFileOut;
    private ProgressDialog dialog;
    public final static int ALBUM_REQUEST_CODE = 1;
    public final static int CAMERA_REQUEST_CODE = 3;
    // 拍照路径
    public static String SAVED_IMAGE_DIR_PATH = Environment.getExternalStorageDirectory()
            + "/Onlyhi/camera/";
    private String // 指定相机拍摄照片保存地址
            cameraPath = SAVED_IMAGE_DIR_PATH +
            System.currentTimeMillis() + ".png";
    private TakePhotoPopWin mTakePhotoPopWin;


    // data
    AbortableFuture<String> uploadAvatarFuture;
    private NimUserInfo userInfo;

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
        if (!SPUtil.getGuest()) {
            mPresenter.getStuInfo();
        }
    }

    @Override
    protected void initData() {
        if (SPUtil.getGuest()) {
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
        mPresenter.getStuInfo();
    }


    public void showGuestUI() {
        SPUtil.setAvatarUrl("");
        mTvName.setText("登录/注册");
        mTvName.setTextColor(getResources().getColor(R.color.c_F42440));
        mTvName.setBackgroundResource(R.drawable.radius5);
        mTvName.setGravity(Gravity.CENTER);
        mTvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtil.dip2px(95), ScreenUtil.dip2px(28));
        params.topMargin = ScreenUtil.dip2px(10);
        mTvName.setLayoutParams(params);
        ImageLoader.loadCircleImage(getActivity(), mAvatar, SPUtil.getAvatarUrl());
    }


    @OnClick({R.id.iv_portrait, R.id.iv_setting, R.id.setting_me, R.id.setting_consumption, R.id.setting_share, R.id.setting_know, R.id.tv_name, R.id.setting_orders})
    public void onClick(View view) {
        if (SPUtil.getGuest()) {
            if (view.getId() == R.id.setting_know) {
                startActivity(new Intent(getContext(), KnowActivity.class));
            } else if (view.getId() == R.id.iv_setting) {
                startActivity(new Intent(getContext(), SettingActivity.class));
            }else if(view.getId() == R.id.setting_share){
                shareUrl();
            } else {
                UIUtils.startGuestLoginActivity(mContext, 2);
            }

        } else {
            switch (view.getId()) {
                case R.id.iv_portrait:
//                    mPresenter.uploadHeadPhoto((MainActivity) getActivity());
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE2);

                    } else {
                        uploadHeadPhoto();
                    }
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
                case R.id.setting_orders:
                    startActivity(new Intent(getContext(), MineOrdersActivity.class));
                    break;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SHARE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) shareUrl();
                else Toast.makeText(mContext, "权限未授权", Toast.LENGTH_SHORT).show();
                break;
            case MY_PERMISSIONS_REQUEST_CALL_PHONE2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uploadHeadPhoto();
                } else {
                    // Permission Denied
                    DialogUtil.showPresimissFialDialog(getContext(), "SD卡存储");
                }
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

        if (SPUtil.getGuest()) {
            web.setTitle("您的好友邀您体验[嗨课堂]");
        } else {

            web.setTitle(SPUtil.getName() + "邀您体验[嗨课堂]");
        }
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
                    //从拍照
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
//                        Uri uri = Uri.fromFile(new File(cameraPath));
                        Uri uri;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                            uri = FileProvider.getUriForFile(getActivity(),
                                    "com.onlyhiedu.mobile.fileprovider",
                                    new File(cameraPath));
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            uri = Uri.fromFile(new File(cameraPath));
                        }
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
                    //从系统相册
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
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadAvatarSucess(Avatar data) {
        if (data != null && data.imagePath != null) {
            Log.d(TAG, data.imagePath);
            SPUtil.setAvatarUrl(data.imagePath);
            //更新IM 个人头像
//            DemoHelper.getInstance().getUserProfileManager().uploadUserAvatar(data.imagePath);
        }
    }

    @Override
    public void saveAvatarSucess() {
        dialog.dismiss();
        if (mFileOut != null) {
            initUikitAvatar();

        } else {
            Toast.makeText(getActivity(), getString(R.string.toast_updatephoto_fail),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getInfoSucess(StudentInfo info) {
        if (info != null && info.iconurl != null && !"".equals(info.iconurl)) {
            initUikitAvatar();
        }
    }

    private void initUikitAvatar() {
//mFileOut
        if (mFileOut == null) {
            return;
        }
        LogUtil.i(TAG, "start upload avatar, local file path=" + mFileOut.getAbsolutePath());
        new Handler().postDelayed(outimeTask, 3000);
        uploadAvatarFuture = NIMClient.getService(NosService.class).upload(mFileOut, PickImageAction.MIME_JPEG);
        uploadAvatarFuture.setCallback(new RequestCallbackWrapper<String>() {
            @Override
            public void onResult(int code, String url, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && !TextUtils.isEmpty(url)) {
                    LogUtil.i(TAG, "upload avatar success, url =" + url);

                    UserUpdateHelper.update(UserInfoFieldEnum.AVATAR, url, new RequestCallbackWrapper<Void>() {
                        @Override
                        public void onResult(int code, Void result, Throwable exception) {
                            if (code == ResponseCode.RES_SUCCESS) {
                                ImageLoader.loadCircleImage(getActivity(), mAvatar, SPUtil.getAvatarUrl());
                                onUpdateDone();
                                Toast.makeText(getActivity(), getString(R.string.toast_updatephoto_success),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), R.string.head_update_failed, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }); // 更新资料
                } else {
                    Toast.makeText(getActivity(), R.string.user_info_update_failed, Toast
                            .LENGTH_SHORT).show();
                    onUpdateDone();
                }
            }
        });


/*
        UserUpdateHelper.update(UserInfoFieldEnum.AVATAR, SPUtil.getAvatarUrl(), new RequestCallbackWrapper<Void>() {
            @Override
            public void onResult(int code, Void result, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS) {
                    ImageLoader.loadCircleImage(getActivity(), mAvatar, SPUtil.getAvatarUrl());
                    onUpdateDone();
                } else {
                    Toast.makeText(getActivity(), R.string.head_update_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }
    private Runnable outimeTask = new Runnable() {
        @Override
        public void run() {
            cancelUpload(R.string.user_info_update_failed);
        }
    };

    private void cancelUpload(int resId) {
        if (uploadAvatarFuture != null) {
            uploadAvatarFuture.abort();
            Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
            onUpdateDone();
        }
    }
    private void onUpdateDone() {
        uploadAvatarFuture = null;
        getUserInfo();
    }
    private void getUserInfo() {
        userInfo = (NimUserInfo) NimUIKit.getUserInfoProvider().getUserInfo(SPUtil.getUikitAccid());
        if (userInfo == null) {
            NimUIKit.getUserInfoProvider().getUserInfoAsync(SPUtil.getUikitAccid(), new SimpleCallback<NimUserInfo>() {

                @Override
                public void onResult(boolean success, NimUserInfo result, int code) {
                    if (success) {
                        userInfo = result;
                        updateUI();
                    } else {
                        Toast.makeText(getActivity(), "getUserInfoFromRemote failed:" + code, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            updateUI();
        }
    }

    private void updateUI() {
        NimUIKit.getUserInfoProvider().getUserInfo(SPUtil.getUikitAccid());
    }
}
