package com.onlyhiedu.mobile.UI.Setting.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.UpdataVersionInfo;

/**
 * Created by pengpeng on 2017/3/24.
 */

public interface UpdataContract {
    interface View extends BaseView{
        void showUpdataSuccess(UpdataVersionInfo versionInfo);
    }
    interface Presenter extends BasePresenter<UpdataContract.View>{
        void updataVersion();
    }
}
