package com.onlyhiedu.pro.UI.Home.presenter.contract;

import com.onlyhiedu.pro.Base.BasePresenter;
import com.onlyhiedu.pro.Base.BaseView;
import com.onlyhiedu.pro.Model.bean.StudentInfo;

/**
 * Created by Administrator on 2017/3/17.
 */

public interface MeContract {

    interface View extends BaseView {

        void showStudentInfo(StudentInfo data);
    }

    interface Presenter extends BasePresenter<MeContract.View> {

        void getStudentInfo();
        void updateSex(int sex);
        void updateGrade(String grade);
        void updateExamArea(String examArea);
    }
}
