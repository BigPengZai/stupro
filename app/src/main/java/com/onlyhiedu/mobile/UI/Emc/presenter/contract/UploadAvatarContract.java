package com.onlyhiedu.mobile.UI.Emc.presenter.contract;

import com.onlyhiedu.mobile.Base.BasePresenter;
import com.onlyhiedu.mobile.Base.BaseView;
import com.onlyhiedu.mobile.Model.bean.Avatar;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;

import java.io.File;

/**
 * Created by pengpeng on 2017/7/10.
 */

public interface UploadAvatarContract {
    interface View extends BaseView {
        void uploadAvatarSucess(Avatar data);

        void saveAvatarSucess();

        void getInfoSucess(StudentInfo info);
    }

    interface Presenter extends BasePresenter<UploadAvatarContract.View> {
        void uploadAvatar(File file);

        void saveAvatar(String imagePath,String imgName);

        void getStuInfo();
    }
}
