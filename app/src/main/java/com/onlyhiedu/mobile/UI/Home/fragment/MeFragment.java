package com.onlyhiedu.mobile.UI.Home.fragment;

import android.content.Intent;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.onlyhiedu.mobile.App.App;
import com.onlyhiedu.mobile.Base.SimpleFragment;
import com.onlyhiedu.mobile.Model.bean.AddressBean;
import com.onlyhiedu.mobile.Model.bean.AddressModel;
import com.onlyhiedu.mobile.Model.bean.ProvinceBean;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Setting.activity.SettingActivity;
import com.onlyhiedu.mobile.Utils.AppUtil;
import com.onlyhiedu.mobile.Utils.JsonUtil;
import com.onlyhiedu.mobile.Utils.WheelUtils;
import com.onlyhiedu.mobile.Widget.SettingItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuwc on 2017/2/18.
 */

public class MeFragment extends SimpleFragment implements View.OnClickListener {

    private OptionsPickerView mSexWheel;
    private OptionsPickerView mGradeWheel;
    private OptionsPickerView mAddressWheel;


    private ArrayList<ProvinceBean> mSexData = WheelUtils.getSexData();
    private ArrayList<ProvinceBean> mGradeData = WheelUtils.getGrade();
    private ArrayList<ProvinceBean> mAddressData = new ArrayList<>();
    private ArrayList<ArrayList<String>> mAddressData2 = new ArrayList<>();

    @BindView(R.id.setting_name)
    SettingItemView mSettingName;
    @BindView(R.id.setting_sex)
    SettingItemView mSettingSex;
    @BindView(R.id.setting_grade)
    SettingItemView mSettingGrade;
    @BindView(R.id.setting_address)
    SettingItemView mSettingAddress;


    @Override
    protected int getLayoutId() {
        return R.layout.fr_me;
    }

    @Override
    protected void initEventAndData() {
        initAddressData();
        mSexWheel = WheelUtils.getWhellView(mContext, sexL, mSexData);
        mGradeWheel = WheelUtils.getWhellView(mContext, gradeL, mGradeData);
        mAddressWheel = WheelUtils.getWhellView2(mContext, addressL, mAddressData, mAddressData2);

        mSettingName.hintRightImage();

    }


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
            mSettingSex.setDetailText(mSexData.get(options1).getPickerViewText());
        }
    };
    OptionsPickerView.OnOptionsSelectListener gradeL = new OptionsPickerView.OnOptionsSelectListener() {
        @Override
        public void onOptionsSelect(int options1, int option2, int options3, View v) {
            mSettingGrade.setDetailText(mGradeData.get(options1).getPickerViewText());
        }
    };
    OptionsPickerView.OnOptionsSelectListener addressL = new OptionsPickerView.OnOptionsSelectListener() {
        @Override
        public void onOptionsSelect(int options1, int option2, int options3, View v) {
            mSettingAddress.setDetailText(mAddressData.get(options1).getPickerViewText() + mAddressData2.get(options1).get(option2));
        }
    };


    @OnClick({R.id.setting_sex, R.id.setting_grade, R.id.setting_address, R.id.setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_sex:

                mSexWheel.show();
                break;
            case R.id.setting_grade:

                mGradeWheel.show();
                break;
            case R.id.setting_address:

                mAddressWheel.show();
                break;
            case R.id.setting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
        }
    }

}
