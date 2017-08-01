package com.onlyhiedu.mobile.UI.Info.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;

/**
 * Created by pengpeng on 2017/4/6.
 */

public interface InfoContract {

    interface View extends BaseView {

        void showStudentInfo(StudentInfo data);
    }

    interface Presenter extends BasePresenter<InfoContract.View> {

        void getStudentInfo();
        void updateSex(int sex);
        void updateGrade(String grade);
        void updateExamArea(String examArea);

        void updateSubject(String subject);
    }
}