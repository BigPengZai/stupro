package com.onlyhiedu.mobile.UI.Consumption.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.ConsumptionData;

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
