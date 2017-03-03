package com.onlyhiedu.mobile.Widget.wheelview;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.UI.Home.adapter.ProvinceWheelAdapter;
import com.onlyhiedu.mobile.Utils.ScreenUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pengpeng on 2017/3/2.
 */

public class ChooseGradewheel implements MyOnWheelChangedListener {
    @BindView(R.id.province_wheel)
    MyWheelView provinceWheel;
    private Activity context;
    private View parentView;
    private PopupWindow popupWindow = null;
    private WindowManager.LayoutParams layoutParams = null;
    private LayoutInflater layoutInflater = null;

    private List<AddressDtailsEntity.ProvinceEntity> province = null;

    private OnAddressChangeListener onAddressChangeListener = null;

    public ChooseGradewheel(Activity context) {
        this.context = context;
        init();
    }

    private void init() {
        layoutParams = context.getWindow().getAttributes();
        layoutInflater = context.getLayoutInflater();
        initView();
        initPopupWindow();
    }

    private void initView() {
        parentView = layoutInflater.inflate(R.layout.choose_grade_layout, null);
        ButterKnife.bind(this, parentView);
        provinceWheel.setVisibleItems(5);
        provinceWheel.addChangingListener(this);
    }

    private void initPopupWindow() {
        popupWindow = new PopupWindow(parentView, WindowManager.LayoutParams.MATCH_PARENT, (int) (ScreenUtil.getScreenHeight(context) * (2.0 / 5)));
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setAnimationStyle(R.style.anim_push_bottom);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                layoutParams.alpha = 1.0f;
                context.getWindow().setAttributes(layoutParams);
                popupWindow.dismiss();
            }
        });
    }
    public void setProvince(List<AddressDtailsEntity.ProvinceEntity> province) {
        this.province = province;
        bindData();
    }
    private void bindData() {
        provinceWheel.setViewAdapter(new ProvinceWheelAdapter(context, province));
    }
    public void show(View v) {
        layoutParams.alpha = 0.6f;
        context.getWindow().setAttributes(layoutParams);
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    @OnClick(R.id.confirm_button)
    public void confirm() {
        if (onAddressChangeListener != null) {
            int provinceIndex = provinceWheel.getCurrentItem();
            String provinceName = null;
            List<AddressDtailsEntity.ProvinceEntity.CityEntity> citys = null;
            if (province != null && province.size() > provinceIndex) {
                AddressDtailsEntity.ProvinceEntity provinceEntity = province.get(provinceIndex);
                provinceName = provinceEntity.Name;
            }
            onAddressChangeListener.onGradeChange(provinceName);
        }
        cancel();
    }

    @OnClick(R.id.cancel_button)
    public void cancel() {
        popupWindow.dismiss();
    }

    public void setOnGradeChangeListener(OnAddressChangeListener onAddressChangeListener) {
        this.onAddressChangeListener = onAddressChangeListener;
    }

    @Override
    public void onChanged(MyWheelView wheel, int oldValue, int newValue) {
        if (wheel == provinceWheel) {
            updateCitiy();//省份改变后城市和地区联动
        }
    }
    private void updateCitiy() {
//        int index = provinceWheel.getCurrentItem();
//        List<AddressDtailsEntity.ProvinceEntity.CityEntity> citys = province.get(index).City;
//        if (citys != null && citys.size() > 0) {
//            provinceWheel.setViewAdapter(new ProvinceWheelAdapter(context, province));
//            provinceWheel.setCurrentItem(0);
//        }
    }

}
