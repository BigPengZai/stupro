package com.onlyhiedu.mobile.UI.Home.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;

/**
 * Created by Administrator on 2017/3/17.
 */

public interface MeContract {

    interface View extends BaseView {

        void showStudentInfo(StudentInfo data);
    }

    interface Presenter extends BasePresenter<MeContract.View> {

        void getStudentInfo();
    }
}
