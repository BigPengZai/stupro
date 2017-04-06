package com.onlyhiedu.mobile.UI.Info.activity;


import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Base.BaseActivity;
import com.onlyhiedu.mobile.Model.bean.AddressBean;
import com.onlyhiedu.mobile.Model.bean.AddressModel;
import com.onlyhiedu.mobile.Model.bean.ProvinceBean;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Info.presenter.InfoPresenter;
import com.onlyhiedu.mobile.UI.Info.presenter.contract.InfoContract;
import com.onlyhiedu.mobile.Utils.AppUtil;
import com.onlyhiedu.mobile.Utils.JsonUtil;
import com.onlyhiedu.mobile.Utils.WheelUtils;
import com.onlyhiedu.mobile.Widget.SettingItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pengpeng on 2017/4/6.
 */

public class MyInfoActivity extends BaseActivity<InfoPresenter> implements InfoContract.View {
    private boolean showAddress = false;
    private Integer mSex; //性别
    private OptionsPickerView mSexWheel;
    private OptionsPickerView mGradeWheel;
    private OptionsPickerView mAddressWheel;
    private ArrayList<ProvinceBean> mSexData = WheelUtils.getSexData();
    private ArrayList<ProvinceBean> mGradeData = WheelUtils.getGrade();
    private ArrayList<ProvinceBean> mAddressData = new ArrayList<>();
    private ArrayList<ArrayList<String>> mAddressData2 = new ArrayList<>();
    public static final String TAG = MyInfoActivity.class.getSimpleName();
    @BindView(R.id.setting_name)
    SettingItemView mSettingName;
    @BindView(R.id.setting_sex)
    SettingItemView mSettingSex;
    @BindView(R.id.setting_grade)
    SettingItemView mSettingGrade;
    @BindView(R.id.setting_address)
    SettingItemView mSettingAddress;
    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_info;
    }

    @Override
    protected void initView() {
        setToolBar("我的信息");
        thread.start();
    }
    @Override
    protected void initData() {
        mPresenter.getStudentInfo();
    }
    @Override
    public void showStudentInfo(StudentInfo data) {
        if (data != null&&data.name!=null) {
            mSettingName.setDetailText(data.name);
//            mTvName.setText(data.name);
            mSex = data.sex;
            if (data.sex!=null&&data.sex == 0) {
                mSettingSex.setDetailText("男");
            }
            if (data.sex!=null&&data.sex == 1) {
                mSettingSex.setDetailText("女");
            }
            if (data.sex == null) {
                mSettingSex.setDetailText(null);
            }
            mSettingGrade.setDetailText(data.grade);
            mSettingAddress.setDetailText(data.examArea);
        }
    }
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            initAddressData();
            showAddress = true;
        }
    });
    private void initAddressData() {

        ArrayList<String> cityList;
        String address = AppUtil.readAssert(App.getInstance().getApplicationContext(), "address.txt");
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressBean data = model.Result;
            if (data == null) return;
            if (data.ProvinceItems != null && data.ProvinceItems.Province != null) {
                for (int i = 0; i < data.ProvinceItems.Province.size(); i++) {
                    mAddressData.add(new ProvinceBean(i, data.ProvinceItems.Province.get(i).Name, "描述部分", "其他数据"));
                    List<AddressBean.ProvinceEntity.CityEntity> city = data.ProvinceItems.Province.get(i).City;
                    cityList = new ArrayList<>();
                    for (int i1 = 0; i1 < city.size(); i1++) {
                        cityList.add(city.get(i1).Name);
                    }
                    mAddressData2.add(cityList);
                }
            }
        }
    }
    OptionsPickerView.OnOptionsSelectListener sexL = new OptionsPickerView.OnOptionsSelectListener() {
        @Override
        public void onOptionsSelect(int options1, int option2, int options3, View v) {

            if (mSex == null || mSex != options1) {
                mSex = options1;
                mPresenter.updateSex(mSex);
                mSettingSex.setDetailText(mSexData.get(options1).getPickerViewText());
            }

        }
    };
    OptionsPickerView.OnOptionsSelectListener gradeL = new OptionsPickerView.OnOptionsSelectListener() {
        @Override
        public void onOptionsSelect(int options1, int option2, int options3, View v) {
            String grade = mGradeData.get(options1).getPickerViewText();
            if (TextUtils.isEmpty(mSettingGrade.getDetailText()) || !grade.equals(mSettingGrade.getDetailText())) {
                mPresenter.updateGrade(grade);
                mSettingGrade.setDetailText(grade);
            }
        }
    };
    OptionsPickerView.OnOptionsSelectListener addressL = new OptionsPickerView.OnOptionsSelectListener() {
        @Override
        public void onOptionsSelect(int options1, int option2, int options3, View v) {
            String address = mAddressData.get(options1).getPickerViewText() + "," + mAddressData2.get(options1).get(option2);
            if (TextUtils.isEmpty(mSettingAddress.getDetailText()) || !address.equals(mSettingAddress.getDetailText())) {
                mPresenter.updateExamArea(address);
                mSettingAddress.setDetailText(address);
            }
        }
    };
    @OnClick({R.id.setting_sex, R.id.setting_grade, R.id.setting_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_sex:
                if (mSexWheel == null) {
                    mSexWheel = WheelUtils.getWhellView(mContext, sexL, mSexData);
                }
                mSexWheel.show();
                break;
            case R.id.setting_grade:
                if (mGradeWheel == null) {
                    mGradeWheel = WheelUtils.getWhellView(mContext, gradeL, mGradeData);
                }
                mGradeWheel.show();
                break;
            case R.id.setting_address:
                if (mAddressWheel == null) {
                    mAddressWheel = WheelUtils.getWhellView2(mContext, addressL, mAddressData, mAddressData2);
                }
                if (showAddress) {
                    mAddressWheel.show();
                }
                break;
        }
    }
    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
