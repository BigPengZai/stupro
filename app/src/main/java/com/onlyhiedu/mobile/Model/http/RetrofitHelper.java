package com.onlyhiedu.mobile.Model.http;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.BuildConfig;
import com.onlyhiedu.mobile.Model.bean.AgoraUidBean;
import com.onlyhiedu.mobile.Model.bean.AuthCodeInfo;
import com.onlyhiedu.mobile.Model.bean.AuthUserDataBean;
import com.onlyhiedu.mobile.Model.bean.Avatar;
import com.onlyhiedu.mobile.Model.bean.ConsumptionData;
import com.onlyhiedu.mobile.Model.bean.CourseList;
import com.onlyhiedu.mobile.Model.bean.CoursePriceList;
import com.onlyhiedu.mobile.Model.bean.CoursePriceTypeInfo;
import com.onlyhiedu.mobile.Model.bean.CourseWareImageList;
import com.onlyhiedu.mobile.Model.bean.FeedBackInfo;
import com.onlyhiedu.mobile.Model.bean.HomeBannerBean;
import com.onlyhiedu.mobile.Model.bean.HomeTeacher;
import com.onlyhiedu.mobile.Model.bean.IMAllUserInfo;
import com.onlyhiedu.mobile.Model.bean.IMUserInfo;
import com.onlyhiedu.mobile.Model.bean.OrderList;
import com.onlyhiedu.mobile.Model.bean.PingPayStatus;
import com.onlyhiedu.mobile.Model.bean.PingPaySucessInfo;
import com.onlyhiedu.mobile.Model.bean.PingPaySucessInfoAliPay;
import com.onlyhiedu.mobile.Model.bean.RoomInfo;
import com.onlyhiedu.mobile.Model.bean.StarContentList;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;
import com.onlyhiedu.mobile.Model.bean.TypeListInfo;
import com.onlyhiedu.mobile.Model.bean.UpdateVersionInfo;
import com.onlyhiedu.mobile.Model.bean.UserDataBean;
import com.onlyhiedu.mobile.Model.bean.UserIsRegister;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.SystemUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xuwc on 2016/11/24.
 */
public class RetrofitHelper {


    private static OkHttpClient sOkHttpClient = null;
    private static onlyApis sOnlyApis = null;

    public RetrofitHelper() {
        init();
    }

    private void init() {
        initOkHttp();
        sOnlyApis = getOnlyApiService();
    }

    private static void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            // https://drakeet.me/retrofit-2-0-okhttp-3-0-config
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }
        // http://www.jianshu.com/p/93153b34310e
        File cacheFile = new File(Constants.PATH_CACHE);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!SystemUtil.isNetworkConnected()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (SystemUtil.isNetworkConnected()) {
                    int maxAge = 0;
                    // 正常访问同一请求接口（多次访问同一接口），给10秒缓存，超过时间重新发送请求，否则取缓存数据
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
        Interceptor parameters = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();
                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("token", SPUtil.getToken())
                        .build();
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(parameters);
        builder.cache(cache).addInterceptor(cacheInterceptor);
        //设置超时
        builder.connectTimeout(6, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        sOkHttpClient = builder.build();
    }

    private static onlyApis getOnlyApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(sOnlyApis.HOST)
                .client(sOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(onlyApis.class);
    }


    /**
     * 初始化通用的观察者
     *
     * @param observable 观察者
     */
    public ResourceSubscriber startObservable(Flowable observable, ResourceSubscriber subscriber) {
        return (ResourceSubscriber) observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber);
    }


    public Flowable<onlyHttpResponse<UserDataBean>> fetchUser(String z, String m, Long time, String deviceid) {
        return sOnlyApis.getUser(z, m, time, "Android", "student", deviceid);
    }

    public Flowable<onlyHttpResponse<StudentInfo>> fetchStudentInfo() {
        return sOnlyApis.getStudentInfo();
    }

    public Flowable<onlyHttpResponse> fetchUpdateSex(int sex) {
        return sOnlyApis.updateSex(sex);
    }

    public Flowable<onlyHttpResponse> fetchUpdateGrade(String grade) {
        return sOnlyApis.updateGrade(grade);
    }

    public Flowable<onlyHttpResponse> fetchUpdateExamArea(String grade) {
        return sOnlyApis.updateExamArea(grade);
    }

    //1V1 未完成课程列表
    public Flowable<onlyHttpResponse<CourseList>> fetchGetNoStartCourseList(int page) {
        return sOnlyApis.getNoStartCourseList(page);
    }

    //获得监课的声网 uid 列表
    public Flowable<onlyHttpResponse<AgoraUidBean>> fetchGetMonitorAgoraUidList(String courseUuid) {
        return sOnlyApis.getGetMonitorAgoraUidList(courseUuid);
    }

    //1VN 未完成课程列表
    public Flowable<onlyHttpResponse<CourseList>> fetchGetNoEndCourseList(int page) {
        return sOnlyApis.getNoEndCourseList(page);
    }

    // 1V1 已完成课程列表
    public Flowable<onlyHttpResponse<CourseList>> fetchGetEndCourseList(int page) {
        return sOnlyApis.getEndCourseList(page);
    }
    // 1VN 已完成课程列表
    public Flowable<onlyHttpResponse<CourseList>> fetchGetCourseRecordList(int page) {
        return sOnlyApis.getCourseRecordList(page);
    }

    public Flowable<onlyHttpResponse<RoomInfo>> fetchGetRoomInfoList(String uuid) {
        return sOnlyApis.getRoomInfoList(uuid);
    }



    public Flowable<onlyHttpResponse> fetchUpdatePassword(String oldPassword, Long timestamp, String newPassword) {
        return sOnlyApis.updatePassword(oldPassword, timestamp, newPassword);
    }

    public Flowable<onlyHttpResponse<UpdateVersionInfo>> fetchUpdateVersion(String deviceType) {
        return sOnlyApis.updateVersion(deviceType);
    }

    public Flowable<onlyHttpResponse<List<CourseWareImageList>>> fetchGetCourseWareImageList(String wareId) {
        return sOnlyApis.getCoursewareImageList(wareId);
    }

    public Flowable<onlyHttpResponse<FeedBackInfo>> fetchRequestFeedBackInfo(String content) {
        return sOnlyApis.requestFeedBackInfo(content);
    }


    //注册
    public Flowable<onlyHttpResponse> fetchRegisterInfo(String username, String phone, String pwd, String authCode) {
        return sOnlyApis.registerInfo(username, phone, pwd, authCode);
    }

    //验证码
    public Flowable<onlyHttpResponse<AuthCodeInfo>> fetchAuthCode(String phone) {
        return sOnlyApis.getAuthCode(phone);
    }

    //验证码登录
    public Flowable<onlyHttpResponse<AuthUserDataBean>> fetchAuthLogin(String authCode, String phone, String deviceId) {
        return sOnlyApis.authLogin(authCode, phone, "Android", "student", deviceId);
    }

    //找回密码
    public Flowable<onlyHttpResponse> fetchRetrieve(String phone, String pwd, String authCode, String deviceId) {
        return sOnlyApis.retrievePassword(phone, pwd, authCode, "Android", "student", deviceId);
    }

    //课时消耗
    public Flowable<onlyHttpResponse<List<ConsumptionData>>> fetchClassTimeInfo() {
        return sOnlyApis.getTimeInfo();
    }

    //设置 推送token
    public Flowable<onlyHttpResponse> fetchPushToken(String deviceToken, String tag) {
        return sOnlyApis.getPushInfo(deviceToken, tag);
    }

    //号码是否注册
    public Flowable<onlyHttpResponse<UserIsRegister>> fetchIsReg(String phone) {
        return sOnlyApis.getRegState(phone);
    }

    //流统计
    public Flowable<onlyHttpResponse> fetchStatics(String classTime, String uuid) {
        return sOnlyApis.getStatics(classTime, uuid);
    }

    //下课
    public Flowable<onlyHttpResponse> fetchUpdateEndTime(String courseUuid) {
        return sOnlyApis.getUpdateEndTime(courseUuid);
    }


    //停止录制
    public Flowable<onlyHttpResponse> fetchStopRecord(String courseUuid) {
        return sOnlyApis.getStopRecord(courseUuid);
    }
    public Flowable<onlyHttpResponse<AuthUserDataBean>> fetchIsBindUser(SHARE_MEDIA share_media, String uid, String openid, String name, String gender, String iconurl, String city, String province, String country, String deviceId) {

        if (share_media == SHARE_MEDIA.WEIXIN) {
            return sOnlyApis.wechatLogin(uid, openid, name, gender, iconurl, city, province, country, "Android", deviceId);
        } else if (share_media == SHARE_MEDIA.QQ) {
            return sOnlyApis.qqLogin(uid, openid, name, gender, iconurl, city, province, "Android", deviceId);
        } else {
            return sOnlyApis.sinaLogin(uid, name, gender, iconurl, city, "Android", deviceId);
        }
    }

    public Flowable<onlyHttpResponse<AuthUserDataBean>> fetchBindUser(SHARE_MEDIA share_media, String uid, String phone, String username, String code, String deviceId) {
        if (share_media == SHARE_MEDIA.WEIXIN) {
            return sOnlyApis.wechatBind(uid, phone, username, code, "Android", deviceId);
        } else if (share_media == SHARE_MEDIA.QQ) {
            return sOnlyApis.qqBind(uid, phone, username, code, "Android", deviceId);
        } else {
            return sOnlyApis.sinaBind(uid, phone, username, code, "Android", deviceId);
        }
    }

    public Flowable<onlyHttpResponse> fetchaddFriends(String username) {
        return sOnlyApis.addFriend(SPUtil.getPhone(), username);
    }

    public Flowable<onlyHttpResponse> fetchDeleteFriend(String username) {
        return sOnlyApis.deleteFriend(SPUtil.getPhone(), username);
    }

    //环信注册
    public Flowable<onlyHttpResponse> fetchEmcRegister(String name, String pwd) {
        return sOnlyApis.emcRegister(name, pwd);
    }

    //环信登录
    public Flowable<onlyHttpResponse> fetchEmcLogin(String name, String password) {
        return sOnlyApis.emcLogin(name, password);
    }

    //上传图片
    public Flowable<onlyHttpResponse<Avatar>> fetchUploadAvatar(RequestBody body, File file) {
        return sOnlyApis.uploadAvatar(body, file);
    }

    //保存图片
    public Flowable<onlyHttpResponse> fetchSavaAvatar(String url, String imgname) {
        return sOnlyApis.saveAvatar(url, imgname);
    }

    //保存图片
    public Flowable<onlyHttpResponse<IMUserInfo>> fetchQueryIMUserInfo(String phone) {
        return sOnlyApis.queryIMUserInfo(phone);
    }

    public Flowable<onlyHttpResponse<IMAllUserInfo>> fetchGetIMUserFriendList() {
        return sOnlyApis.getIMUserFriendList(Integer.MAX_VALUE, SPUtil.getEmcRegName());
    }

    public Flowable<onlyHttpResponse<IMAllUserInfo>> fetchGetIMUserList(List<String> names) {
        return sOnlyApis.getIMUserList(names);
    }

    public Flowable<onlyHttpResponse<HomeBannerBean>> fetchGetListBanner() {
        return sOnlyApis.getListBanner("Android");
    }


    public Flowable<onlyHttpResponse<HomeTeacher>> fetchGetListTeacher() {
        return sOnlyApis.getListTeacher("Android");
    }

    public Flowable<onlyHttpResponse<HomeBannerBean>> fetchGetListArticle() {
        return sOnlyApis.getListArticle("Android");
    }

    public Flowable<onlyHttpResponse<List<TypeListInfo>>> fetchGetTypeList() {
        return sOnlyApis.getTypeList();
    }

    public Flowable<onlyHttpResponse<List<CoursePriceTypeInfo>>> fetchGetCorsePriceList(String type) {
        return sOnlyApis.getCorsePriceList(type);
    }

    public Flowable<onlyHttpResponse<List<CoursePriceList>>> fetchGetPriceList(String activityType, String type) {
        return sOnlyApis.getPriceList(activityType, type);
    }

    public Flowable<onlyHttpResponse> fetchGetPayMoney(String coursePriceUuid, String code) {
        return sOnlyApis.getPayMoney(coursePriceUuid, code);
    }
    //微信
    public Flowable<onlyHttpResponse<PingPaySucessInfo>> fetchGetPingPay(String coursePriceUuid, String channel, String code) {
        return sOnlyApis.getPingPay(coursePriceUuid, channel, code);
    }

    //支付宝
    public Flowable<onlyHttpResponse<PingPaySucessInfoAliPay>> fetchGetPingPayAliPay(String coursePriceUuid, String channel, String code) {
        return sOnlyApis.getPingPayAliPay(coursePriceUuid, channel, code);
    }


    public Flowable<onlyHttpResponse<String>> fetchGetBaiduPay(String coursePriceUuid, String code, String name, String phone) {
        return sOnlyApis.getBaiduPay(coursePriceUuid, code, name, phone);
    }

    //科目
    public Flowable<onlyHttpResponse> fetchUpdateSubject(String subject) {
        return sOnlyApis.updateSubject(subject);
    }

    //PIng++ 订单支付状态查询
    public Flowable<onlyHttpResponse<PingPayStatus>> fetchPingPayStatus(String id) {
        return sOnlyApis.getPingPayStatus(id);
    }


    public Flowable<onlyHttpResponse<OrderList>> fetchGetOrderList(String state, int page) {
        return sOnlyApis.getOrderList(state, page);
    }

    //订单 wx 支付
    public Flowable<onlyHttpResponse<PingPaySucessInfo>> fetchOrderGetPingPay(String coursePriceUuid, String channel, String code) {
        return sOnlyApis.getOrderPingPay(coursePriceUuid, channel, code);
    }

    //订单 alipay 支付
    public Flowable<onlyHttpResponse<PingPaySucessInfoAliPay>> fetchOrderGetPingPayAliPay(String coursePriceUuid, String channel, String code) {
        return sOnlyApis.getOrderPingPayAliPay(coursePriceUuid, channel, code);
    }


    public Flowable<onlyHttpResponse<String>> fetchOrderGetBaiduPay(String coursePriceUuid, String code) {
        return sOnlyApis.getOrderBaiduPay(coursePriceUuid, code);
    }

    public Flowable<onlyHttpResponse<List<StarContentList>>> fetchGetStarContentList(int num) {
        return sOnlyApis.getStarContentList(num);
    }

    public Flowable<onlyHttpResponse> fetchSaveAppraise(int num, String classAppraiseStarUuids, String remark, String courseUuid) {
        return sOnlyApis.saveAppraise(num, classAppraiseStarUuids, remark, courseUuid);
    }

}
