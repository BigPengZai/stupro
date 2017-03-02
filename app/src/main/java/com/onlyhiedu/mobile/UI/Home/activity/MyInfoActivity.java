package com.onlyhiedu.mobile.UI.Home.activity;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.view.listener.OnAddressChangeListener;
import com.onlyhiedu.mobile.UI.Home.view.wheelview.AddressDtailsEntity;
import com.onlyhiedu.mobile.UI.Home.view.wheelview.AddressModel;
import com.onlyhiedu.mobile.UI.Home.view.wheelview.ChooseAddressWheel;
import com.onlyhiedu.mobile.UI.Home.view.wheelview.ChooseGradewheel;
import com.onlyhiedu.mobile.Utils.JsonUtil;
import com.onlyhiedu.mobile.Utils.ScreenUtil;

import butterknife.BindView;
import butterknife.internal.Utils;

/**
 * Created by pengpeng on 2017/3/2.
 */

public class MyInfoActivity extends SimpleActivity implements View.OnClickListener, OnAddressChangeListener {
    @BindView(R.id.rl_location)
    RelativeLayout mRl_Location;
    @BindView(R.id.tv_location)
    TextView mTv_Location;


    @BindView(R.id.rl_grade)
    RelativeLayout mRl_Grade;
    @BindView(R.id.tv_grade)
    TextView mTv_Grade;
    private ChooseAddressWheel chooseAddressWheel = null;

    public static final String TAG = MyInfoActivity.class.getSimpleName();
    private ChooseGradewheel mChooseGradewheel;

    @Override
    protected int getLayout() {
        return R.layout.activity_info;
    }

    @Override
    protected void initEventAndData() {
        mRl_Location.setOnClickListener(this);
        mRl_Grade.setOnClickListener(this);
        chooseAddressWheel = new ChooseAddressWheel(this);
        chooseAddressWheel.setOnAddressChangeListener(this);

        mChooseGradewheel = new ChooseGradewheel(this);
        mChooseGradewheel.setOnGradeChangeListener(this);
        initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_location:
                chooseAddressWheel.show(view);
                break;
            case R.id.rl_grade:
                mChooseGradewheel.show(view);
                break;
        }
    }

    private void initData() {
        String address = ScreenUtil.readAssert(this, "address.txt");
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressDtailsEntity data = model.Result;
            if (data == null) return;
            mTv_Location.setText(data.Province + " " + data.City + " " + data.Area);
            if (data.ProvinceItems != null && data.ProvinceItems.Province != null) {
                chooseAddressWheel.setProvince(data.ProvinceItems.Province);
                chooseAddressWheel.defaultValue(data.Province, data.City, data.Area);
            }
        }
    }
    @Override
    public void onAddressChange(String province, String city, String district) {
        mTv_Location.setText(province + " " + city + " " + district);
    }

    @Override
    public void onGradeChange(String province) {
        mTv_Grade.setText(province);
    }
}
