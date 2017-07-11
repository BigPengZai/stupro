package com.onlyhiedu.mobile.UI.User.activity;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.activity.MainActivity;
import com.onlyhiedu.mobile.UI.User.presenter.OpenIDPresenter;
import com.onlyhiedu.mobile.UI.User.presenter.contract.OpenIDContract;
import com.onlyhiedu.mobile.Utils.MyUMAuthListener;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.onlyhiedu.mobile.R.id.guest;

public class OpenIDActivity extends BaseActivity<OpenIDPresenter> implements OpenIDContract.View {

    private static final String TAG = OpenIDActivity.class.getSimpleName();
    public static final String cancelShow = "cancelShow";  //取消按钮是否可见
    public static final String information = "information"; //游客模式下，是否点击各人信息进入的首页

    private UMShareAPI mShareAPI;
    private boolean mBooleanExtra;
    private int mIntExtra;


    @BindView(R.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R.id.guest)
    TextView mTvGuest;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_open_id;
    }

    @Override
    protected void initView() {
        mShareAPI = UMShareAPI.get(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        boolean extra = getIntent().getBooleanExtra(cancelShow, false);
        mBooleanExtra = getIntent().getBooleanExtra(information, false);
        mIntExtra = getIntent().getIntExtra(MainActivity.showPagePosition, 0);

        if (extra) mTvCancel.setVisibility(View.VISIBLE);
        else mTvCancel.setVisibility(View.GONE);
        if (extra) mTvGuest.setVisibility(View.GONE);
        else mTvGuest.setVisibility(View.VISIBLE);
    }

    @Override
    public void isShowBingActivity(String token, String phone, String name, SHARE_MEDIA share_media, String uid) {
        Log.d(TAG, "token : " + token);
        if (token == null) {
            startActivity(new Intent(this, BindActivity.class).putExtra(BindActivity.share_media, share_media).putExtra(BindActivity.share_media_uid, uid));
        } else {
            SPUtil.setToken(token);
            SPUtil.setPhone(phone);
            SPUtil.setName(name);
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    private UMAuthListener wxAuthLister = new MyUMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            mShareAPI.doOauthVerify(OpenIDActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
        }
    };

    private UMAuthListener umAuthListener = new MyUMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            //回调成功，即登陆成功后这里返回Map<String, String> map，map里面就是用户的信息，可以拿出来使用了
//            Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
            if (map != null) {
                mShareAPI.getPlatformInfo(OpenIDActivity.this, share_media, umAuthListener2);
            }
        }
    };

    private UMAuthListener umAuthListener2 = new MyUMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            //回调成功，即登陆成功后这里返回Map<String, String> map，map里面就是用户的信息，可以拿出来使用了
            Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
            if (map != null) {
                String uid = null;
                String openid = null;
                String name = null;
                String gender = null;
                String iconurl = null;
                String city = null;
                String province = null;
                String country = null;
                switch (share_media) {
                    case WEIXIN:
                        uid = map.get("unionid");
                        openid = map.get("openid");
                        name = map.get("screen_name");
                        gender = map.get("gender");
                        iconurl = map.get("iconurl");
                        city = map.get("city");
                        province = map.get("province");
                        country = map.get("country");
                        break;
                    case QQ:
                        uid = map.get("uid");
                        openid = map.get("openid");
                        name = map.get("screen_name");
                        gender = map.get("gender");
                        iconurl = map.get("iconurl");
                        city = map.get("city");
                        province = map.get("province");
                        break;
                    case SINA:
                        uid = map.get("uid");
                        name = map.get("screen_name");
                        gender = map.get("gender");
                        iconurl = map.get("iconurl");
                        city = map.get("location");
                        break;
                }
                Log.d(TAG, "uid : " + uid + "openid : " + openid + " name : " + name + "_______name : " + name + "_______gender : " + gender
                        + "_______iconurl : " + iconurl + "_______city : " + city + "_______province : " + province + "_______country : " + country);

                mPresenter.isBindUser(share_media, uid, openid, name, gender, iconurl, city, province, country);

            }
        }
    };


    @OnClick({R.id.mobile_login, R.id.register, guest, R.id.btn_openid_wx, R.id.btn_openid_qq, R.id.btn_openid_sina,R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mobile_login:
                startActivity(new Intent(this,LoginActivity.class).putExtra(information,mBooleanExtra).putExtra(MainActivity.showPagePosition,mIntExtra));
                break;
            case R.id.register:
                startActivity(new Intent(this,RegActivity.class));
                break;
            case guest:
                App.bIsGuestLogin = true;
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.btn_openid_wx:
                mShareAPI.deleteOauth(this, SHARE_MEDIA.WEIXIN, wxAuthLister);
                break;
            case R.id.btn_openid_qq:
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.btn_openid_sina:
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.SINA, umAuthListener);
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止内存泄漏
        mShareAPI.release();
    }
}
