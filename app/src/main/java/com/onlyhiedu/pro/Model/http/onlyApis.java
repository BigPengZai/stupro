package com.onlyhiedu.pro.Model.http;


import com.onlyhiedu.pro.Model.bean.AgoraUidBean;
import com.onlyhiedu.pro.Model.bean.AuthUserDataBean;
import com.onlyhiedu.pro.Model.bean.Avatar;
import com.onlyhiedu.pro.Model.bean.ConsumptionData;
import com.onlyhiedu.pro.Model.bean.CourseList;
import com.onlyhiedu.pro.Model.bean.CoursePriceList;
import com.onlyhiedu.pro.Model.bean.CoursePriceTypeInfo;
import com.onlyhiedu.pro.Model.bean.CourseWareImageList;
import com.onlyhiedu.pro.Model.bean.FeedBackInfo;
import com.onlyhiedu.pro.Model.bean.HomeBannerBean;
import com.onlyhiedu.pro.Model.bean.HomeTeacher;
import com.onlyhiedu.pro.Model.bean.IMAllUserInfo;
import com.onlyhiedu.pro.Model.bean.IMUserInfo;
import com.onlyhiedu.pro.Model.bean.OrderList;
import com.onlyhiedu.pro.Model.bean.PingPayStatus;
import com.onlyhiedu.pro.Model.bean.PingPaySucessInfo;
import com.onlyhiedu.pro.Model.bean.PingPaySucessInfoAliPay;
import com.onlyhiedu.pro.Model.bean.RoomInfo;
import com.onlyhiedu.pro.Model.bean.StarContentList;
import com.onlyhiedu.pro.Model.bean.StudentInfo;
import com.onlyhiedu.pro.Model.bean.TypeListInfo;
import com.onlyhiedu.pro.Model.bean.UikitDate;
import com.onlyhiedu.pro.Model.bean.UpdateVersionInfo;
import com.onlyhiedu.pro.Model.bean.UserDataBean;
import com.onlyhiedu.pro.Model.bean.UserIsRegister;
import com.onlyhiedu.pro.Utils.SPUtil;

import java.io.File;
import java.util.List;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by xuwc on 2016/11/24.
 */
public interface onlyApis {


    //测试环境
    String HOST = "http://clienttest.haiketang.net/";
    String IP = "clienttest.haiketang.net/";
    String coursePlayback = "http://frontendtest.haiketang.net/static/play.html?token=" + SPUtil.getToken() + "&uuid=";


    //    //公网环境
//    String HOST = "http://client.onlyhi.cn/";
//    String IP = "client.onlyhi.cn";
//    String coursePlayback =  "http://frontend.onlyhi.cn/static/play.html?token=" + SPUtil.getToken() + "&uuid=";

    String IM_USER_INFO_URL = HOST + "client/chat/getIMUserInfo";

    /**
     * 客户端登录
     *
     * @param phone
     * @param password
     * @param deviceType
     * @param
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
     * @param
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
     * 获取学生课程列表 1v1
     *
     * @param page
     * @return
     */
    @GET("client/student/noEndCourseV1List")
    Flowable<onlyHttpResponse<CourseList>> getNoStartCourseList(@Query("pageNo") int page);



    /**
     *获得监课的声网uid列表
     *
     * */
    @GET("client/course/getAgoraUidList")
    Flowable<onlyHttpResponse<AgoraUidBean>> getGetMonitorAgoraUidList(@Query("courseUuid") String courseUuid);



    /**
     * 获取学生课程列表1VN
     *
     * @param page
     * @return
     */
    @GET("client/student/noEndCourseList")
    Flowable<onlyHttpResponse<CourseList>> getNoEndCourseList(@Query("pageNo") int page);


    //课程记录 已经完成
    @GET("client/student/getCourseRecordV1List")
    Flowable<onlyHttpResponse<CourseList>> getEndCourseList(@Query("pageNo") int page);

    //下课接口
    @POST("client/course/updateEndTime")
    Flowable<onlyHttpResponse> getUpdateEndTime(@Query("courseUuid") String courseUuid);




    /**
     * 一对多课程记录 已经完成
     *
     * @param page
     * @return
     */
    @GET("client/student/getCourseRecordList")
    Flowable<onlyHttpResponse<CourseList>> getCourseRecordList(@Query("pageNo") int page);

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
    @FormUrlEncoded
    @POST("client/student/getAppInfo")
    Flowable<onlyHttpResponse<UpdateVersionInfo>> updateVersion(@Field("deviceType") String deviceType);


    @FormUrlEncoded
    @POST("client/courseware/getCoursewareImageList")
    Flowable<onlyHttpResponse<List<CourseWareImageList>>> getCoursewareImageList(@Field("coursewareUuid") String courseWareId);

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
    Flowable<onlyHttpResponse> registerInfo(@Field("userName") String userName, @Field("phone") String phone, @Field("password") String password, @Field("authCode") String authCode);

    /**
     * 验证码
     */
    @FormUrlEncoded
    @POST("client/user/getAuthCode")
    Flowable<onlyHttpResponse> getAuthCode(@Field("phone") String phone);

    /**
     * 找回密码
     */
    @FormUrlEncoded
    @POST("client/user/retrievePassword")
    Flowable<onlyHttpResponse> retrievePassword(@Field("phone") String phone, @Field("password") String password, @Field("authCode") String authCode, @Field("deviceType") String deviceType, @Field("userType") String userType, @Field("deviceId") String deviceId);

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
     * 流统计
     */

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
    Flowable<onlyHttpResponse<AuthUserDataBean>> wechatBind(@Field("uid") String uid, @Field("phone") String phone, @Field("userName") String userName, @Field("authCode") String code, @Field("deviceType") String deviceType, @Field("deviceId") String deviceId);

    /**
     * QQ绑定
     */
    @FormUrlEncoded
    @POST("client/qq/bing")
    Flowable<onlyHttpResponse<AuthUserDataBean>> qqBind(@Field("uid") String uid, @Field("phone") String phone, @Field("userName") String userName, @Field("authCode") String code, @Field("deviceType") String deviceType, @Field("deviceId") String deviceId);

    /**
     * QQ绑定
     */
    @FormUrlEncoded
    @POST("client/sinamicroblog/bing")
    Flowable<onlyHttpResponse<AuthUserDataBean>> sinaBind(@Field("uid") String uid, @Field("phone") String phone, @Field("userName") String userName, @Field("authCode") String code, @Field("deviceType") String deviceType, @Field("deviceId") String deviceId);


    /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑第三方登录↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/




     /*↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓     环信IM   ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("client/chat/register")
    Flowable<onlyHttpResponse> emcRegister(@Field("userName") String userName, @Field("password") String password);

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("lient/chat/imLoginByJson")
    Flowable<onlyHttpResponse> emcLogin(@Field("userName") String userName, @Field("password") String password);

    /**
     * 添加好友
     */
    @FormUrlEncoded
    @POST("client/chat/addFriend")
    Flowable<onlyHttpResponse> addFriend(@Field("ownerUserName") String ownerUserName, @Field("friendUserName") String friendUserName);

    /**
     * 删除好友
     */
    @FormUrlEncoded
    @POST("client/chat/deleteFriend")
    Flowable<onlyHttpResponse> deleteFriend(@Field("ownerUserName") String ownerUserName, @Field("friendUserName") String friendUserName);


    /**
     * 获取所有好友
     */
    @FormUrlEncoded
    @POST("client/chat/getIMUserFriendList")
    Flowable<onlyHttpResponse<IMAllUserInfo>> getIMUserFriendList(@Field("pageSize") int pageSize, @Field("userName") String userName);


    /**
     * 查询好友
     */
    @FormUrlEncoded
    @POST("client/chat/queryIMUserInfo")
    Flowable<onlyHttpResponse<IMUserInfo>> queryIMUserInfo(@Field("phone") String phone);


    /**
     * 根据多个username获取用户信息
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("client/chat/getIMUserList")
    Flowable<onlyHttpResponse<IMAllUserInfo>> getIMUserList(@Field("userNames") List<String> userNames);

     /*↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑   环信IM   ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/


    /**
     * 上传图片
     */
    @Multipart
    @POST("client/upload/upload")
    Flowable<onlyHttpResponse<Avatar>> uploadAvatar(@Part("file\"; filename=\"image.jpg") RequestBody imgs, @Part("file") File file);

    /**
     * 保存图片
     */
    @FormUrlEncoded
    @POST("client/student/saveAvatar")
    Flowable<onlyHttpResponse> saveAvatar(@Field("imagePath") String imagePath, @Field("imageName") String imageName);


    /**
     * 首页banner
     */
    @GET("client/home/listBanner")
    Flowable<onlyHttpResponse<HomeBannerBean>> getListBanner(@Query("deviceType") String deviceType);


    /**
     * 老师
     */
    @GET("client/home/listTeacherRecommend")
    Flowable<onlyHttpResponse<HomeTeacher>> getListTeacher(@Query("deviceType") String deviceType);

    /**
     * 教育头条
     *
     * @return
     */
    @GET("client/home/listArticle")
    Flowable<onlyHttpResponse<HomeBannerBean>> getListArticle(@Query("deviceType") String deviceType);


    /**
     * 活动列表
     *
     * @return
     */
    @GET("client/coursepay/getActivityTypeList")
    Flowable<onlyHttpResponse<List<TypeListInfo>>> getTypeList();

    /**
     * 课时包类型列表
     *
     * @return
     */
    @GET("client/coursepay/getCoursePriceTypeList")
    Flowable<onlyHttpResponse<List<CoursePriceTypeInfo>>> getCorsePriceList(@Query("activityType") String type);

    /**
     * 课时包列表
     *
     * @return
     */
    @GET("client/coursepay/getCoursePriceList")
    Flowable<onlyHttpResponse<List<CoursePriceList>>> getPriceList(@Query("activityType") String activityType, @Query("type") String type);

    /**
     * 根据课时包和优惠码
     *
     * @return
     */
    @GET("client/coursepay/getPayMoney")
    Flowable<onlyHttpResponse> getPayMoney(@Query("coursePriceUuid") String coursePriceUuid, @Query("code") String code);

    /**
     * 课时包 Ping++直接支付
     *
     * @return
     */
    @FormUrlEncoded
    @POST("client/coursepay/directPingppPayment")
    Flowable<onlyHttpResponse<PingPaySucessInfo>> getPingPay(@Field("coursePriceUuid") String coursePriceUuid, @Field("channel") String channel, @Field("code") String code);


    /**
     * 课时包 Ping++直接支付
     * 支付宝
     *
     * @return
     */
    @FormUrlEncoded
    @POST("client/coursepay/directPingppPayment")
    Flowable<onlyHttpResponse<PingPaySucessInfoAliPay>> getPingPayAliPay(@Field("coursePriceUuid") String coursePriceUuid, @Field("channel") String channel, @Field("code") String code);


    /**
     * 课时包 百度分期直接支付
     *
     * @return
     */
    @FormUrlEncoded
    @POST("client/coursepay/directBaiduStagingPayment")
    Flowable<onlyHttpResponse<String>> getBaiduPay(@Field("coursePriceUuid") String coursePriceUuid, @Field("code") String code, @Field("patriarchName") String patriarchName, @Field("patriarchPhone") String patriarchPhone);

    /**
     * 科目
     */
    @FormUrlEncoded
    @POST("client/student/updateSubject")
    Flowable<onlyHttpResponse> updateSubject(@Field("subject") String subject);

    /**
     * Ping++ 订单状态查询
     */
    @GET("client/coursepay/getPingppOrderPayStatus")
    Flowable<onlyHttpResponse<PingPayStatus>> getPingPayStatus(@Query("chargeId") String id);

    /**
     * 课程订单列表
     */
    @GET(" client/coursepay/getOrderList")
    Flowable<onlyHttpResponse<OrderList>> getOrderList(@Query("payStatus") String payStatus, @Query("pageNo") int pageNo);

    /**
     * 订单 Ping++支付 wx
     */
    @FormUrlEncoded
    @POST("client/coursepay/orderPingppPay")
    Flowable<onlyHttpResponse<PingPaySucessInfo>> getOrderPingPay(@Field("orderUuid") String coursePriceUuid, @Field("channel") String channel, @Field("code") String code);


    /**
     * 订单 Ping++支付 alipay
     */
    @FormUrlEncoded
    @POST("client/coursepay/orderPingppPay")
    Flowable<onlyHttpResponse<PingPaySucessInfoAliPay>> getOrderPingPayAliPay(@Field("orderUuid") String coursePriceUuid, @Field("channel") String channel, @Field("code") String code);


    /**
     * 订单 百度分期直接支付
     *
     * @return
     */
    @FormUrlEncoded
    @POST("client/coursepay/orderBaiduStagingPay")
    Flowable<onlyHttpResponse<String>> getOrderBaiduPay(@Field("orderUuid") String coursePriceUuid, @Field("code") String code);

    /**
     * 获取星级评价选项列表
     *
     * @param num
     * @return
     */
    @GET("client/course/getStarContentList")
    Flowable<onlyHttpResponse<List<StarContentList>>> getStarContentList(@Query("star") int num);

    /**
     * 保存评价
     *
     * @param num
     * @param classAppraiseStarUuids
     * @param remark
     * @param courseUuid
     * @return
     */
    @FormUrlEncoded
    @POST("client/course/saveAppraise")
    Flowable<onlyHttpResponse> saveAppraise(@Field("star") int num, @Field("classAppraiseStarUuids") String classAppraiseStarUuids, @Field("remark") String remark, @Field("courseUuid") String courseUuid);


    /**
     *
     * 用户注册云信
     *
     * */
    @POST("client/netease/register")
    Flowable<onlyHttpResponse<UikitDate>> registerUikit();

}
