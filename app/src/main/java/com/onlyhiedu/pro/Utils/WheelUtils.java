package com.onlyhiedu.pro.Utils;

import android.content.Context;
import android.graphics.Color;

import com.bigkoo.pickerview.OptionsPickerView;
import com.onlyhiedu.pro.App.App;
import com.onlyhiedu.pro.Model.bean.AddressBean;
import com.onlyhiedu.pro.Model.bean.AddressModel;
import com.onlyhiedu.pro.Model.bean.ProvinceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/4.
 */

public class WheelUtils {

    public static OptionsPickerView getWhellView(Context context, OptionsPickerView.OnOptionsSelectListener l, ArrayList<ProvinceBean> data) {
        OptionsPickerView view = new OptionsPickerView.Builder(context, l)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)//设置滚轮文字大小
                .setSelectOptions(0, 1, 2)  //设置默认选中项
                .setOutSideCancelable(true)
                .build();
        view.setPicker(data);
        return view;
    }

    public static OptionsPickerView getWhellView2(Context context, OptionsPickerView.OnOptionsSelectListener l, ArrayList<ProvinceBean> data, ArrayList<ArrayList<String>> data2) {
        OptionsPickerView view = new OptionsPickerView.Builder(context, l)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)//设置滚轮文字大小
                .setSelectOptions(0, 1, 2)  //设置默认选中项
                .setOutSideCancelable(true)
                .build();
        view.setPicker(data, data2);
        return view;
    }


    public static ArrayList<ProvinceBean> getSexData() {
        ArrayList<ProvinceBean> data = new ArrayList<>();
        data.add(new ProvinceBean(0, "男", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "女", "描述部分", "其他数据"));
        return data;
    }

    public static ArrayList<ProvinceBean> getGrade() {
        ArrayList<ProvinceBean> data = new ArrayList<>();
        data.add(new ProvinceBean(0, "小四", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "小五", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "小六", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "初一", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "初二", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "初三", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "高一", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "高二", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "高三", "描述部分", "其他数据"));
        return data;
    }
    //学科
    public static ArrayList<ProvinceBean> getSubject(){
        //语文 数学 英语  物理 化学 历史 生物  政治 地理 科学
        ArrayList<ProvinceBean> data = new ArrayList<>();
        data.add(new ProvinceBean(0, "语文", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "数学", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "英语", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "物理", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "化学", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "历史", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "生物", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "政治", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "地理", "描述部分", "其他数据"));
        data.add(new ProvinceBean(0, "科学", "描述部分", "其他数据"));
        return data;
    }
    public static ArrayList<ProvinceBean> getAddress() {
        ArrayList<ProvinceBean> datas = new ArrayList<>();
        ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
        ArrayList<String> cityList;
        String address = AppUtil.readAssert(App.getInstance().getApplicationContext(), "address.json");
        AddressModel model = JsonUtil.parseJson(address, AddressModel.class);
        if (model != null) {
            AddressBean data = model.Result;
            if (data == null) return datas;
            if (data.ProvinceItems != null && data.ProvinceItems.Province != null) {
                for (int i = 0; i < data.ProvinceItems.Province.size(); i++) {
                    datas.add(new ProvinceBean(i, data.ProvinceItems.Province.get(i).Name, "描述部分", "其他数据"));
                    List<AddressBean.ProvinceEntity.CityEntity> city = data.ProvinceItems.Province.get(i).City;
                    cityList = new ArrayList<>();
                    for (int i1 = 0; i1 < city.size(); i1++) {
                        cityList.add(city.get(i1).Name);
                    }
                    options2Items.add(cityList);
                }
            }
        }
        return datas;
    }

}
