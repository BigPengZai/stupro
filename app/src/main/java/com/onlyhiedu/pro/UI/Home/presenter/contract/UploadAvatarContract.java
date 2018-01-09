package com.onlyhiedu.pro.UI.Home.presenter.contract;

import com.onlyhiedu.pro.Base.BasePresenter;
import com.onlyhiedu.pro.Base.BaseView;
import com.onlyhiedu.pro.Model.bean.Avatar;
import com.onlyhiedu.pro.Model.bean.StudentInfo;

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