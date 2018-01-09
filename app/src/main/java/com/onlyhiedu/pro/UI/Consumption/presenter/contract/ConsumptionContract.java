package com.onlyhiedu.pro.UI.Consumption.presenter.contract;

import com.onlyhiedu.pro.Base.BasePresenter;
import com.onlyhiedu.pro.Base.BaseView;
import com.onlyhiedu.pro.Model.bean.ConsumptionData;

import java.util.List;

/**
 * Created by pengpeng on 2017/4/15.
 */

public interface ConsumptionContract {
    interface View extends BaseView {
        void showSuccess(List<ConsumptionData> data);
    }

    interface Presenter extends BasePresenter<ConsumptionContract.View> {
        void getClassTimeInfo();
    }
}
