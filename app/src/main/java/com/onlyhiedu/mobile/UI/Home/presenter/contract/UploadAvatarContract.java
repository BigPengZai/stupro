package com.onlyhiedu.mobile.UI.Home.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.Avatar;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;

import java.io.File;

public interface UploadAvatarContract {
    public interface Presenter extends BasePresenter<View> {
        void uploadAvatar(File var1);

        void saveAvatar(String var1, String var2);

        void getStuInfo();
    }

    public interface View extends BaseView {
        void uploadAvatarSucess(Avatar var1);

        void saveAvatarSucess();

        void getInfoSucess(StudentInfo var1);
    }
}