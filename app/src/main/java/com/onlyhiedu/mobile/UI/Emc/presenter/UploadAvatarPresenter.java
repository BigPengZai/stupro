package com.onlyhiedu.mobile.UI.Emc.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.onlyhiedu.mobile.Base.IMBaseView;
import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.Avatar;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Emc.presenter.contract.UploadAvatarContract;
import com.onlyhiedu.mobile.Utils.SPUtil;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by pengpeng on 2017/7/10.
 */

public class UploadAvatarPresenter extends RxPresenter<UploadAvatarContract.View> implements UploadAvatarContract.Presenter {


    private RetrofitHelper mRetrofitHelper;

    @Inject
    public UploadAvatarPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    @Override
    public void uploadAvatar(File file) {
        RequestBody body =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Flowable<onlyHttpResponse<Avatar>> flowable = mRetrofitHelper.fetchUploadAvatar(body, file);
        MyResourceSubscriber<onlyHttpResponse<Avatar>> observer = new MyResourceSubscriber<onlyHttpResponse<Avatar>>() {
            @Override
            public void onNextData(onlyHttpResponse<Avatar> data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().uploadAvatarSucess(data.getData());
                        if (data.getData() != null && data.getData().imagePath != null) {
                            saveAvatar(data.getData().imagePath, "icon");
                        }
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };

        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    @Override
    public void saveAvatar(String imagePath, String imgName) {
        Flowable<onlyHttpResponse> flowable = mRetrofitHelper.fetchSavaAvatar(imagePath, imgName);
        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            @Override
            public void onNextData(onlyHttpResponse data) {
                if (getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().saveAvatarSucess();
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }



    //获得学生信息
    @Override
    public void getStuInfo() {
        Flowable<onlyHttpResponse<StudentInfo>> flowable = mRetrofitHelper.fetchStudentInfo();

        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<StudentInfo>>() {
            @Override
            public void onNextData(onlyHttpResponse<StudentInfo> data) {
                if (getView() != null ) {
                    if (!data.isHasError()) {
                        SPUtil.setAvatarUrl(data.getData().iconurl);
                        getView().getInfoSucess();
                    } else {
                        getView().showError(data.getMessage());
                    }
                }
            }
        };
        addSubscription(mRetrofitHelper.startObservable(flowable, observer));
    }
}
