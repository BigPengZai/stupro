package com.onlyhiedu.mobile.Model.http;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.onlyhiedu.mobile.App.Constants;
import com.onlyhiedu.mobile.BuildConfig;
import com.onlyhiedu.mobile.Model.bean.AuthCodeInfo;
import com.onlyhiedu.mobile.Model.bean.AuthUserDataBean;
import com.onlyhiedu.mobile.Model.bean.ClassConsumption;
import com.onlyhiedu.mobile.Model.bean.ConsumptionData;
import com.onlyhiedu.mobile.Model.bean.CourseList;
import com.onlyhiedu.mobile.Model.bean.CourseWareImageList;
import com.onlyhiedu.mobile.Model.bean.FeedBackInfo;
import com.onlyhiedu.mobile.Model.bean.RoomInfo;
import com.onlyhiedu.mobile.Model.bean.StudentInfo;
import com.onlyhiedu.mobile.Model.bean.UpdateVersionInfo;
import com.onlyhiedu.mobile.Model.bean.UserDataBean;
import com.onlyhiedu.mobile.Utils.SPUtil;
import com.onlyhiedu.mobile.Utils.SystemUtil;

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
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
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

    public Flowable<onlyHttpResponse<CourseList>> fetchGetNoStartCourseList(int page) {
        return sOnlyApis.getNoStartCourseList(page);
    }

    public Flowable<onlyHttpResponse<RoomInfo>> fetchGetRoomInfoList(String uuid) {
        return sOnlyApis.getRoomInfoList(uuid);
    }

    public Flowable<onlyHttpResponse<CourseList>> fetchGetEndCourseList(int page) {
        return sOnlyApis.getEndCourseList(page);
    }

    public Flowable<onlyHttpResponse> fetchUpdatePassword(String oldPassword, Long timestamp, String newPassword) {
        return sOnlyApis.updatePassword(oldPassword, timestamp, newPassword);
    }

    public Flowable<onlyHttpResponse<UpdateVersionInfo>> fetchUpdateVersion() {
        return sOnlyApis.updateVersion();
    }

    public Flowable<onlyHttpResponse<List<CourseWareImageList>>> fetchGetCourseWareImageList(String wareId) {
        return sOnlyApis.getCoursewareImageList(wareId);
    }

    public Flowable<onlyHttpResponse<FeedBackInfo>> fetchRequestFeedBackInfo(String content) {
        return sOnlyApis.requestFeedBackInfo(content);
    }

    public Flowable<onlyHttpResponse<ClassConsumption>> fetchUploadClassConsumption(String uuid) {
        return sOnlyApis.uploadClassConsumption(uuid);
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
}
