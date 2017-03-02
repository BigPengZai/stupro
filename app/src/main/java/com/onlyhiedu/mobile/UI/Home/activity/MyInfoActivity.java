package com.onlyhiedu.mobile.UI.Home.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Widget.SexView;
import com.onlyhiedu.mobile.UI.Home.view.listener.OnAddressChangeListener;
import com.onlyhiedu.mobile.Widget.wheelview.AddressDtailsEntity;
import com.onlyhiedu.mobile.Widget.wheelview.AddressModel;
import com.onlyhiedu.mobile.Widget.wheelview.ChooseAddressWheel;
import com.onlyhiedu.mobile.Widget.wheelview.ChooseGradewheel;
import com.onlyhiedu.mobile.Utils.JsonUtil;
import com.onlyhiedu.mobile.Utils.ScreenUtil;

import butterknife.BindView;

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

    @BindView(R.id.rl_sex)
    RelativeLayout mRl_Sex;
    @BindView(R.id.tv_sex)
    TextView mTv_Sex;
    private ChooseAddressWheel chooseAddressWheel = null;


    public static final String TAG = MyInfoActivity.class.getSimpleName();
    private ChooseGradewheel mChooseGradewheel;
    private SexView mSexView;

    @Override
    protected int getLayout() {
        return R.layout.activity_info;
    }

    @Override
    protected void initEventAndData() {
        mRl_Location.setOnClickListener(this);
        mRl_Grade.setOnClickListener(this);
        mRl_Sex.setOnClickListener(this);
        chooseAddressWheel = new ChooseAddressWheel(this);
        chooseAddressWheel.setOnAddressChangeListener(this);

        mChooseGradewheel = new ChooseGradewheel(this);
        mChooseGradewheel.setOnGradeChangeListener(this);
        initData();
        initGradeData();
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
            case R.id.rl_sex:
                mSexView = new SexView(this,itemsOnClick);
                mSexView.showAtLocation(this.findViewById(R.id.ll_act), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                break;
        }
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            mSexView.dismiss();
            switch (v.getId()) {
                case R.id.tv_male:
                    mTv_Sex.setText("男");
                    break;
                case R.id.tv_female:
                    mTv_Sex.setText("女");
                    break;
                default:
                    break;
            }


        }

    };
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

    private void initGradeData() {
        String address = ScreenUtil.readAssert(this, "grade.txt");
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressDtailsEntity data = model.Result;
            if (data == null) return;
            mTv_Location.setText(data.Province);
            if (data.ProvinceItems != null && data.ProvinceItems.Province != null) {
                mChooseGradewheel.setProvince(data.ProvinceItems.Province);
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
