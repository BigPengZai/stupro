package com.onlyhiedu.mobile.Model.http;


import com.onlyhiedu.mobile.Model.bean.UserDataBean;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by xuwc on 2016/11/24.
 */
public interface onlyApis {


    String HOST = "http://192.168.1.182:8080/";

    /**
     * 客户端登录
     *
     * @param phone
     * @param password
     * @param deviceType
     * @param userType
     * @return
     */
    @FormUrlEncoded
    @POST("client/user/login")
    Flowable<onlyHttpResponse<UserDataBean>> getUser(@Field("phone") String phone, @Field("password") String password, @Field("deviceType") String deviceType, @Field("userType") String userType);


}
