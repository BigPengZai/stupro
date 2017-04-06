package com.onlyhiedu.mobile.UI.Home.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.BaseFragment;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Consumption.activity.ConsumptionActivity;
import com.onlyhiedu.mobile.UI.Home.presenter.MePresenter;
import com.onlyhiedu.mobile.UI.Home.presenter.contract.MeContract;
import com.onlyhiedu.mobile.UI.Info.activity.MyInfoActivity;
import com.onlyhiedu.mobile.UI.Setting.activity.AboutActivity;
import com.onlyhiedu.mobile.UI.Setting.activity.SettingActivity;
import com.onlyhiedu.mobile.Widget.SettingItemView;
import com.umeng.analytics.MobclickAgent;

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

    @BindView(R.id.setting)
    SettingItemView mSetting;
    @BindView(R.id.setting_about)
    SettingItemView mSettingAbout;
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

    }


    @Override
    public void showStudentInfo(StudentInfo data) {
    }

    @OnClick({R.id.setting,R.id.setting_me,R.id.setting_about,R.id.setting_consumption})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                MobclickAgent.onEvent(mContext,"me_setting");
                break;
            case R.id.setting_me:
                startActivity(new Intent(getContext(), MyInfoActivity.class));
                break;
            case R.id.setting_about:
                startActivity(new Intent(getContext(), AboutActivity.class));
                break;
            case R.id.setting_consumption:
                startActivity(new Intent(getContext(),ConsumptionActivity.class));
                break;
        }
    }


    @Override
    public void showError(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }
}
