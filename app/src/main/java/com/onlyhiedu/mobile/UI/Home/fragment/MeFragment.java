package com.onlyhiedu.mobile.UI.Home.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.onlyhiedu.mobile.Base.BaseFragment;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Consumption.activity.ConsumeActivity;
import com.onlyhiedu.mobile.UI.Home.activity.KnowActivity;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.Home.presenter.MePresenter;
import com.onlyhiedu.mobile.UI.Home.presenter.contract.MeContract;
import com.onlyhiedu.mobile.UI.Info.activity.MyInfoActivity;
import com.onlyhiedu.mobile.UI.Setting.activity.SettingActivity;
import com.onlyhiedu.mobile.Widget.SettingItemView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.social.tool.UMImageMark;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuwc on 2017/2/18.
 */

public class MeFragment extends BaseFragment<MePresenter> implements MeContract.View {
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
    private String name = "";
    private final int SHARE_REQUEST_CODE = 1;

    private UMImage mUmImage;

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
    }

    @Override
    protected void initData() {
        mPresenter.getStudentInfo();
    }


    @Override
    public void showStudentInfo(StudentInfo data) {
        if (data != null && data.name != null) {
            name = data.name;
            Log.d(TAG, "姓名:" + name);
            mTvName.setText(name);
        }
    }

    @OnClick({ R.id.iv_setting, R.id.setting_me, R.id.setting_consumption, R.id.setting_share, R.id.setting_know})
    public void onClick(View view) {
        switch (view.getId()) {
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
    private void requestSharePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SHARE_REQUEST_CODE);
            } else {
                shareUrl();
            }
        } else {
            shareUrl();
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
                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA)
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

    @Override
    public void showError(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }
}
