package com.onlyhiedu.pro.UI.Setting.presenter.contract;

import com.onlyhiedu.pro.Base.BasePresenter;
import com.onlyhiedu.pro.Base.BaseView;

/**
 * Created by Administrator on 2017/3/22.
 */

public interface ModifyPwContract {

    interface View extends BaseView {

        void showUpdate(String msg);
    }

    interface Presenter extends BasePresenter<ModifyPwContract.View> {

        void updatePassword(String oldPassword,Long timestamp,String newPassword);
    }

}
