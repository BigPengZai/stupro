package com.onlyhiedu.mobile.Model.http;


import com.onlyhiedu.mobile.Model.bean.AuthCodeInfo;
import com.onlyhiedu.mobile.Model.bean.AuthUserDataBean;
import com.onlyhiedu.mobile.Model.bean.ConsumptionData;
import com.onlyhiedu.mobile.Model.bean.CourseList;
import com.onlyhiedu.mobile.Model.bean.CourseWareImageList;
import com.onlyhiedu.mobile.Model.bean.FeedBackInfo;
import com.onlyhiedu.mobile.Model.bean.RoomInfo;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;
import com.onlyhiedu.mobile.Model.bean.UpdateVersionInfo;
import com.onlyhiedu.mobile.Model.bean.UserDataBean;
import com.onlyhiedu.mobile.Model.bean.UserIsRegister;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by xuwc on 2016/11/24.
 */
public interface onlyApis {


    //测试环境
//    String HOST = "http://192.168.1.219/";
    //公网环境
    String HOST = "http://api.onlyeduhi.com/";

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
    Flowable<onlyHttpResponse<UserDataBean>> getUser(@Field("phone") String phone,
                                                     @Field("password") String password,
                                                     @Field("timestamp") Long timestamp,
                                                     @Field("deviceType") String deviceType,
                                                     @Field("userType") String userType,
                                                     @Field("deviceId") String deviceId);


    /**
     * 客户端验证码登录
     *
     * @param phone
     * @param deviceType
     * @param userType
     * @return
     */
    @FormUrlEncoded
    @POST("client/user/authLogin")
    Flowable<onlyHttpResponse<AuthUserDataBean>> authLogin(@Field("authCode") String authCode,
                                                           @Field("phone") String phone,
                                                           @Field("deviceType") String deviceType,
                                                           @Field("userType") String userType,
                                                           @Field("deviceId") String deviceId);


    /**
     * 获取学生信息
     *
     * @param
     * @return
     */
    @POST("client/user/getStudentInfo")
    Flowable<onlyHttpResponse<StudentInfo>> getStudentInfo();

    /**
     * 修改学生性别
     *
     * @param sex
     * @return
     */
    @FormUrlEncoded
    @POST("client/student/updateSex")
    Flowable<onlyHttpResponse> updateSex(@Field("sex") int sex);


    /**
     * 修改学生年级
     *
     * @param sex
     * @return
     */
    @FormUrlEncoded
    @POST("client/student/updateGrade")
    Flowable<onlyHttpResponse> updateGrade(@Field("grade") String grade);

    /**
     * 修改高考所在地
     *
     * @param examArea
     * @return
     */
    @FormUrlEncoded
    @POST("client/student/updateExamArea")
    Flowable<onlyHttpResponse> updateExamArea(@Field("examArea") String examArea);

    /**
     * 获取学生课程列表
     *
     * @param page
     * @return
     */
    @POST("client/student/getNoStartCourseList")
    Flowable<onlyHttpResponse<CourseList>> getNoStartCourseList(@Query("pageNo") int page);

    @POST("client/student/getEndCourseList")
    Flowable<onlyHttpResponse<CourseList>> getEndCourseList(@Query("pageNo") int page);

    /**
     * 获取教室信息
     *
     * @param
     * @return
     */
    @POST("client/course/getCourseRoom")
    Flowable<onlyHttpResponse<RoomInfo>> getRoomInfoList(@Query("courseUuid") String uuid);

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param timestamp
     * @param newPassword
     * @return
     */
    @FormUrlEncoded
    @POST("client/user/updatePassword")
    Flowable<onlyHttpResponse> updatePassword(@Field("oldPassword") String oldPassword, @Field("timestamp") Long timestamp, @Field("newPassword") String newPassword);


    /**
     * 版本更新
     */
    @POST("client/student/getAppInfo")
    Flowable<onlyHttpResponse<UpdateVersionInfo>> updateVersion();


    @FormUrlEncoded
    @POST("client/courseware/getCoursewareImageList")
    Flowable<onlyHttpResponse<List<CourseWareImageList>>> getCoursewareImageList(@Field("coursewareId") String courseWareId);

    /**
     * 意见反馈
     */
    @FormUrlEncoded
    @POST("client/user/userFeedback")
    Flowable<onlyHttpResponse<FeedBackInfo>> requestFeedBackInfo(@Field("content") String content);


    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("client/user/register")
    Flowable<onlyHttpResponse> registerInfo(@Field("userName") String userName, @Field("phone") String phone, @Field("password") String password,@Field("authCode") String authCode);

    /**
     * 验证码
     */
    @FormUrlEncoded
    @POST("client/user/getAuthCode")
    Flowable<onlyHttpResponse<AuthCodeInfo>> getAuthCode(@Field("phone") String phone);

    /**
     * 找回密码
     */
    @FormUrlEncoded
    @POST("client/user/retrievePassword")
    Flowable<onlyHttpResponse> retrievePassword(@Field("phone") String phone, @Field("password") String password, @Field("authCode") String authCode,@Field("deviceType") String deviceType,@Field("userType") String userType,@Field("deviceId") String deviceId);

    /**
     * 课时消耗
     */
    @POST("client/student/getClassTimeInfo")
    Flowable<onlyHttpResponse<List<ConsumptionData>>> getTimeInfo();

    /**
     * 设置推送
     */
    @FormUrlEncoded
    @POST("client/student/bingAccount")
    Flowable<onlyHttpResponse> getPushInfo(@Field("deviceToken") String deviceToken, @Field("tag") String tag);

    /**
     * 号码是否注册
     */
    @FormUrlEncoded
    @POST(" client/student/isRegister")
    Flowable<onlyHttpResponse<UserIsRegister>> getRegState(@Field("phone") String phone);

    /**
     *
     * 流统计
     * */

    @FormUrlEncoded
    @POST(" client/course/statisticsClassTime")
    Flowable<onlyHttpResponse> getStatics(@Field("classTime") String classTime, @Field("courseUuid") String uuid);


     /*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓第三方登录↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/

    /**
     * 微信登录
     */
    @FormUrlEncoded
    @POST("client/wechat/login")
    Flowable<onlyHttpResponse<AuthUserDataBean>> wechatLogin(@Field("uid") String uid,
                                                             @Field("openid") String openid,
                                                             @Field("name") String name,
                                                             @Field("gender") String gender,
                                                             @Field("iconurl") String iconurl,
                                                             @Field("city") String city,
                                                             @Field("province") String province,
                                                             @Field("country") String country,
                                                             @Field("deviceType") String deviceType,
                                                             @Field("deviceId") String deviceId);

    /**
     * 微信登录
     */
    @FormUrlEncoded
    @POST("client/qq/login")
    Flowable<onlyHttpResponse<AuthUserDataBean>> qqLogin(@Field("uid") String uid,
                                                         @Field("openid") String openid,
                                                         @Field("name") String name,
                                                         @Field("gender") String gender,
                                                         @Field("iconurl") String iconurl,
                                                         @Field("city") String city,
                                                         @Field("province") String province,
                                                         @Field("deviceType") String deviceType,
                                                         @Field("deviceId") String deviceId);

    /**
     * 微博   登录
     */
    @FormUrlEncoded
    @POST("client/sinamicroblog/login")
    Flowable<onlyHttpResponse<AuthUserDataBean>> sinaLogin(@Field("uid") String uid,
                                                           @Field("name") String name,
                                                           @Field("gender") String gender,
                                                           @Field("iconurl") String iconurl,
                                                           @Field("location") String location,
                                                           @Field("deviceType") String deviceType,
                                                           @Field("deviceId") String deviceId);


    /**
     * 微信绑定
     */
    @FormUrlEncoded
    @POST("client/wechat/bing")
    Flowable<onlyHttpResponse<AuthUserDataBean>> wechatBind(@Field("uid") String uid, @Field("phone") String phone, @Field("userName") String userName, @Field("deviceType") String deviceType, @Field("deviceId") String deviceId);

    /**
     * QQ绑定
     */
    @FormUrlEncoded
    @POST("client/qq/bing")
    Flowable<onlyHttpResponse<AuthUserDataBean>> qqBind(@Field("uid") String uid, @Field("phone") String phone, @Field("userName") String userName, @Field("deviceType") String deviceType, @Field("deviceId") String deviceId);

    /**
     * QQ绑定
     */
    @FormUrlEncoded
    @POST("client/sinamicroblog/bing")
    Flowable<onlyHttpResponse<AuthUserDataBean>> sinaBind(@Field("uid") String uid, @Field("phone") String phone, @Field("userName") String userName, @Field("deviceType") String deviceType, @Field("deviceId") String deviceId);


    /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑第三方登录↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/




     /*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓     环信IM   ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/


    /**
     * 添加好友
     */
    @FormUrlEncoded
    @POST("client/chat/addFriend")
    Flowable<onlyHttpResponse> addFriend(@Field("token") String token, @Field("ownerUserName") String ownerUserName, @Field("friendUserName") String friendUserName);

    /**
     * 删除好友
     */
    @FormUrlEncoded
    @POST("client/chat/deleteFriend")
    Flowable<onlyHttpResponse> deleteFriend(@Field("token") String token, @Field("ownerUserName") String ownerUserName, @Field("friendUserName") String friendUserName);



     /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑   环信IM   ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/




}
