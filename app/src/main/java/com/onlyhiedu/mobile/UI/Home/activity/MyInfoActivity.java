package com.onlyhiedu.mobile.UI.Home.activity;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.model.IPickerViewData;
import com.bigkoo.pickerview.utils.PickerViewAnimateUtil;
import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.Model.bean.AddressBean;
import com.onlyhiedu.mobile.Model.bean.AddressModel;
import com.onlyhiedu.mobile.Model.bean.PickerViewData;
import com.onlyhiedu.mobile.Model.bean.ProvinceBean;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.AppUtil;
import com.onlyhiedu.mobile.Utils.JsonUtil;
import com.onlyhiedu.mobile.Widget.SexView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by pengpeng on 2017/3/2.
 */

public class MyInfoActivity extends SimpleActivity implements View.OnClickListener {
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



    public static final String TAG = MyInfoActivity.class.getSimpleName();

    private SexView mSexView;

    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ProvinceBean> gradeItems = new ArrayList<>();
    private ArrayList<ProvinceBean> sexItems = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<IPickerViewData>>> options3Items = new ArrayList<>();
    private OptionsPickerView pvOptions;
    private OptionsPickerView pvOptionsGrade;
    private OptionsPickerView pvOptionsSex;

    private ArrayList<String> cityList;
    @Override
    protected int getLayout() {
        return R.layout.activity_info;
    }

    @Override
    protected void initEventAndData() {
        mRl_Location.setOnClickListener(this);
        mRl_Grade.setOnClickListener(this);
        mRl_Sex.setOnClickListener(this);
        initData();
        initOptionGradeData();
        initSexData();

        initOptionPicker();
        initOptionGradePicker();
        initOptionSexPicker();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_location:
                pvOptions.show();
                break;
            case R.id.rl_grade:
                pvOptionsGrade.show();
                break;
            case R.id.rl_sex:
                pvOptionsSex.show();
//                mSexView = new SexView(this,itemsOnClick);
//                mSexView.showAtLocation(this.findViewById(R.id.ll_act), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
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
    private void initOptionSexPicker() {
        pvOptionsSex = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                String tx = sexItems.get(options1).getPickerViewText();
                mTv_Sex.setText(tx);
            }
        })
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)//设置滚轮文字大小
                .setSelectOptions(0,1,2)  //设置默认选中项
                .setOutSideCancelable(true)
                .build();
        pvOptionsSex.setPicker(sexItems);
    }



    private void initOptionGradePicker() {
        pvOptionsGrade = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                String tx = gradeItems.get(options1).getPickerViewText();
                mTv_Grade.setText(tx);
            }
        })
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)//设置滚轮文字大小
                .setSelectOptions(0,1,2)  //设置默认选中项
                .setOutSideCancelable(true)
                .build();
        pvOptionsGrade.setPicker(gradeItems);//三级选择器
    }


    private void initOptionPicker() {//条件选择器初始化
        pvOptions = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2);
                mTv_Location.setText(tx);
            }
        })
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)//设置滚轮文字大小
                .setSelectOptions(0,1,2)  //设置默认选中项
                .setOutSideCancelable(true)
                .build();
        pvOptions.setPicker(options1Items, options2Items);//三级选择器

    }
    private void initData() {
        String address = AppUtil.readAssert(this, "address.txt");
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressBean data = model.Result;
            if (data == null) return;
            if (data.ProvinceItems != null && data.ProvinceItems.Province != null) {
                for (int i = 0; i < data.ProvinceItems.Province.size(); i++) {
                    options1Items.add(new ProvinceBean(i,data.ProvinceItems.Province.get(i).Name,"描述部分","其他数据"));
                    List<AddressBean.ProvinceEntity.CityEntity> city = data.ProvinceItems.Province.get(i).City;
                    cityList=new ArrayList<>();
                    for (int i1 = 0; i1 < city.size(); i1++) {
                        cityList.add(city.get(i1).Name);
                    }
                    options2Items.add(cityList);
                }
            }
        }
    }

    private void initOptionGradeData() {
        //选项1
        gradeItems.add(new ProvinceBean(0,"小一","描述部分","其他数据"));
        gradeItems.add(new ProvinceBean(0,"小二","描述部分","其他数据"));
        gradeItems.add(new ProvinceBean(0,"小三","描述部分","其他数据"));
        gradeItems.add(new ProvinceBean(0,"小四","描述部分","其他数据"));
        gradeItems.add(new ProvinceBean(0,"小五","描述部分","其他数据"));
        gradeItems.add(new ProvinceBean(0,"小六","描述部分","其他数据"));
        gradeItems.add(new ProvinceBean(0,"初一","描述部分","其他数据"));
        gradeItems.add(new ProvinceBean(0,"初二","描述部分","其他数据"));
        gradeItems.add(new ProvinceBean(0,"初三","描述部分","其他数据"));
        gradeItems.add(new ProvinceBean(0,"高一","描述部分","其他数据"));
        gradeItems.add(new ProvinceBean(0,"高二","描述部分","其他数据"));
        gradeItems.add(new ProvinceBean(0,"高三","描述部分","其他数据"));

    }
    private void initSexData() {
        sexItems.add(new ProvinceBean(0,"男","描述部分","其他数据"));
        sexItems.add(new ProvinceBean(0,"女","描述部分","其他数据"));
        sexItems.add(new ProvinceBean(0,"其他","描述部分","其他数据"));
    }
}
