package com.onlyhiedu.mobile.UI.Setting.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.UpdateVersionInfo;

/**
 * Created by pengpeng on 2017/3/24.
 */

public interface UpdateContract {
    interface View extends BaseView{
        void showUpdateSuccess(UpdateVersionInfo versionInfo);
    }
    interface Presenter extends BasePresenter<UpdateContract.View>{
        void updateVersion(boolean isMain);
    }
}
