package com.onlyhiedu.pro.UI.Setting.presenter.contract;

import com.onlyhiedu.pro.Base.BasePresenter;
import com.onlyhiedu.pro.Base.BaseView;
import com.onlyhiedu.pro.Model.bean.UpdateVersionInfo;

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
