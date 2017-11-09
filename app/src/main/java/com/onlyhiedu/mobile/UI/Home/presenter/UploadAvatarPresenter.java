package com.onlyhiedu.mobile.UI.Home.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.onlyhiedu.mobile.Base.RxPresenter;
import com.onlyhiedu.mobile.Model.bean.Avatar;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;
import com.onlyhiedu.mobile.Model.http.MyResourceSubscriber;
import com.onlyhiedu.mobile.Model.http.RetrofitHelper;
import com.onlyhiedu.mobile.Model.http.onlyHttpResponse;
import com.onlyhiedu.mobile.UI.Home.presenter.contract.UploadAvatarContract;
import com.onlyhiedu.mobile.Utils.SPUtil;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UploadAvatarPresenter extends RxPresenter<UploadAvatarContract.View> implements UploadAvatarContract.Presenter {
    private RetrofitHelper mRetrofitHelper;

    @Inject
    public UploadAvatarPresenter(RetrofitHelper mRetrofitHelper) {
        this.mRetrofitHelper = mRetrofitHelper;
    }

    public void uploadAvatar(File file) {
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Flowable<onlyHttpResponse<Avatar>> flowable = this.mRetrofitHelper.fetchUploadAvatar(body, file);
        MyResourceSubscriber<onlyHttpResponse<Avatar>> observer = new MyResourceSubscriber<onlyHttpResponse<Avatar>>() {
            public void onNextData(onlyHttpResponse<Avatar> data) {
                if (UploadAvatarPresenter.this.getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().uploadAvatarSucess(data.getData());
                        if (data.getData() != null && (data.getData()).imagePath != null) {
                            UploadAvatarPresenter.this.saveAvatar((data.getData()).imagePath, "icon");
                        }
                    } else {
                        getView().showError(data.getMessage());
                    }
                }

            }
        };
        this.addSubscription(this.mRetrofitHelper.startObservable(flowable, observer));
    }

    public void saveAvatar(String imagePath, String imgName) {
        Flowable<onlyHttpResponse> flowable = this.mRetrofitHelper.fetchSavaAvatar(imagePath, imgName);
        MyResourceSubscriber<onlyHttpResponse> observer = new MyResourceSubscriber<onlyHttpResponse>() {
            public void onNextData(onlyHttpResponse data) {
                if (UploadAvatarPresenter.this.getView() != null && data != null) {
                    if (!data.isHasError()) {
                        getView().saveAvatarSucess();
                    } else {
                        getView().showError(data.getMessage());
                    }
                }

            }
        };
        this.addSubscription(this.mRetrofitHelper.startObservable(flowable, observer));
    }

    public static String getRealFilePath(Context context, Uri uri) {
        if (null == uri) {
            return null;
        } else {
            String scheme = uri.getScheme();
            String data = null;
            if (scheme == null) {
                data = uri.getPath();
            } else if ("file".equals(scheme)) {
                data = uri.getPath();
            } else if ("content".equals(scheme)) {
                Cursor cursor = context.getContentResolver().query(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null);
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex("_data");
                        if (index > -1) {
                            data = cursor.getString(index);
                        }
                    }

                    cursor.close();
                }
            }

            return data;
        }
    }

    public void getStuInfo() {
        Flowable<onlyHttpResponse<StudentInfo>> flowable = this.mRetrofitHelper.fetchStudentInfo();
        MyResourceSubscriber observer = new MyResourceSubscriber<onlyHttpResponse<StudentInfo>>() {
            public void onNextData(onlyHttpResponse<StudentInfo> data) {
                if (UploadAvatarPresenter.this.getView() != null) {
                    if (!data.isHasError()) {
                        SPUtil.setAvatarUrl((data.getData()).iconurl);
                        getView().getInfoSucess(data.getData());
                    } else {
                        getView().showError(data.getMessage());
                    }
                }

            }
        };
        this.addSubscription(this.mRetrofitHelper.startObservable(flowable, observer));
    }
}