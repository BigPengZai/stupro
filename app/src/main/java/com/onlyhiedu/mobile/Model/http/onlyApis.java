package com.onlyhiedu.mobile.Model.http;


import com.onlyhiedu.mobile.Model.bean.ClassConsumption;
import com.onlyhiedu.mobile.Model.bean.CourseList;
import com.onlyhiedu.mobile.Model.bean.CourseWareImageList;
import com.onlyhiedu.mobile.Model.bean.FeedBackInfo;
import com.onlyhiedu.mobile.Model.bean.RoomInfo;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;
import com.onlyhiedu.mobile.Model.bean.UpdataVersionInfo;
import com.onlyhiedu.mobile.Model.bean.UserDataBean;

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


//    String HOST = "http://192.168.1.182:8080/";

    //测试环境
    String HOST = "http://192.168.1.252:8090/";
    //公网环境
//    String HOST = "http://api.onlyeduhi.com/";
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
                                                     @Field("token") String token,
                                                     @Field("deviceId") String deviceId);

    /**
     * 获取学生信息
     *
     * @param token
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
     * @param oldPassword
     * @param timestamp
     * @param newPassword
     * @return
     */
    @FormUrlEncoded
    @POST("client/user/updatePassword")
    Flowable<onlyHttpResponse> updatePassword(@Field("oldPassword") String oldPassword, @Field("timestamp") Long timestamp, @Field("newPassword") String newPassword);


    /**
     *版本更新
     *
     * */
    @POST("client/student/getAppInfo")
    Flowable<onlyHttpResponse<UpdataVersionInfo>> updataVersion();


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
     * 课时消耗
     */
    @FormUrlEncoded
    @POST("client/course/updateEndTime")
    Flowable<onlyHttpResponse<ClassConsumption>> uploadClassConsumption(@Field("courseUuid") String courseUuid,@Field("endTime") String endTime);
}
