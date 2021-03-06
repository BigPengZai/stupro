package com.onlyhiedu.pro.UI.User.activity;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.onlyhiedu.pro.Base.BaseActivity;
import com.onlyhiedu.pro.Model.event.MainActivityTabSelectPos;
import com.onlyhiedu.pro.R;
import com.onlyhiedu.pro.UI.Home.activity.MainActivity;
import com.onlyhiedu.pro.UI.User.presenter.OpenIDPresenter;
import com.onlyhiedu.pro.UI.User.presenter.contract.OpenIDContract;
import com.onlyhiedu.pro.Utils.MyUMAuthListener;
import com.onlyhiedu.pro.Utils.SPUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.OnClick;

import static com.onlyhiedu.pro.R.id.btn_openid_qq;
import static com.onlyhiedu.pro.R.id.btn_openid_sina;
import static com.onlyhiedu.pro.R.id.btn_openid_wx;
import static com.onlyhiedu.pro.R.id.mobile_login;
import static com.onlyhiedu.pro.R.id.register;
import static com.onlyhiedu.pro.R.id.tv_cancel;


public class OpenIDActivity extends BaseActivity<OpenIDPresenter> implements OpenIDContract.View {

    private static final String TAG = OpenIDActivity.class.getSimpleName();
    public static final String AccountEdgeOut = "AccountEdgeOut"; //账号被挤掉

    private UMShareAPI mShareAPI;
    private int mShowHomePosition;
    private boolean mAccountEdgeOut;


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
        mShowHomePosition = getIntent().getIntExtra(MainActivity.showPagePosition, 0);
        mAccountEdgeOut = getIntent().getBooleanExtra(AccountEdgeOut, false);
    }

    @Override
    public void isShowBingActivity(SHARE_MEDIA share_media, String uid) {
        startActivity(new Intent(this, BindActivity.class).putExtra(BindActivity.share_media, share_media).putExtra(BindActivity.share_media_uid, uid));
    }

    @Override
    public void showUser() {
        SPUtil.setGuest(false);
        if (mAccountEdgeOut) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            EventBus.getDefault().post(new MainActivityTabSelectPos(mShowHomePosition));
        }
        finish();
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
                        SPUtil.setAvatarUrl(iconurl);
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
                        SPUtil.setAvatarUrl(iconurl);
                        city = map.get("city");
                        province = map.get("province");
                        break;
                    case SINA:
                        uid = map.get("uid");
                        name = map.get("screen_name");
                        gender = map.get("gender");
                        iconurl = map.get("iconurl");
                        SPUtil.setAvatarUrl(iconurl);
                        city = map.get("location");
                        break;
                }
                Log.d(TAG, "uid : " + uid + "openid : " + openid + " name : " + name + "_______name : " + name + "_______gender : " + gender
                        + "_______iconurl : " + iconurl + "_______city : " + city + "_______province : " + province + "_______country : " + country);
            }
            if (map != null) {
                mShareAPI.getPlatformInfo(OpenIDActivity.this, share_media, umAuthListener2);
            }
        }
    };

    private UMAuthListener umAuthListener2 = new MyUMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            //回调成功，即登陆成功后这里返回Map<String, String> map，map里面就是用户的信息，可以拿出来使用了
//            Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
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
                        SPUtil.setAvatarUrl(iconurl);
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
                        SPUtil.setAvatarUrl(iconurl);
                        city = map.get("city");
                        province = map.get("province");
                        break;
                    case SINA:
                        uid = map.get("uid");
                        name = map.get("screen_name");
                        gender = map.get("gender");
                        iconurl = map.get("iconurl");
                        SPUtil.setAvatarUrl(iconurl);
                        city = map.get("location");
                        break;
                }
                Log.d(TAG, "uid : " + uid + "openid : " + openid + " name : " + name + "_______name : " + name + "_______gender : " + gender
                        + "_______iconurl : " + iconurl + "_______city : " + city + "_______province : " + province + "_______country : " + country);

                mPresenter.isBindUser(share_media, uid, openid, name, gender, iconurl, city, province, country);

            }
        }
    };


    @OnClick({mobile_login, register, btn_openid_wx, btn_openid_qq, btn_openid_sina, tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case mobile_login:
                startActivity(new Intent(this, LoginActivity.class).putExtra(MainActivity.showPagePosition, mShowHomePosition).putExtra(OpenIDActivity.AccountEdgeOut, mAccountEdgeOut));
                break;
            case register:
                startActivity(new Intent(this, RegActivity.class));
                break;
            case btn_openid_wx:
                mShareAPI.deleteOauth(this, SHARE_MEDIA.WEIXIN, wxAuthLister);
                break;
            case btn_openid_qq:
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case btn_openid_sina:
                mShareAPI.doOauthVerify(this, SHARE_MEDIA.SINA, umAuthListener);
                break;
            case tv_cancel:
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

    @Override
    public void IMLoginFailure(String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(OpenIDActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
