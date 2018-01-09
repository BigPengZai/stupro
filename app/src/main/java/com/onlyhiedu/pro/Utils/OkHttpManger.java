package com.onlyhiedu.pro.Utils;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by pengpeng on 2017/3/25.
 */

public class OkHttpManger {
    private static final String TAG = "OkHttpManger";

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    private Handler okHttpHandler;
    private OkHttpClient mOkHttpClient;
    private OkHttpManger(){
        this.mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)//连接超时限制
                .writeTimeout(30, TimeUnit.SECONDS)//写入超时
                .readTimeout(30, TimeUnit.SECONDS)//读取超时
                .build();
        this.okHttpHandler = new Handler(Looper.getMainLooper());
    }

    public static final OkHttpManger getInstance(){
        return SingleFactory.manger;
    }
    private static final class SingleFactory{
        private static final OkHttpManger manger = new OkHttpManger();
    }


    /**
     * 异步下载文件,实现了下载进度的提示
     *
     * @param url
     * @param destFileDir 文件存储根路径
     * @param downListener
     */
    public Call downloadAsync(final String url, final String destFileDir,
                              final OKHttpUICallback.ProgressCallback downListener) throws IOException {
        File file = new File(destFileDir,getFileName(url));
        if(!file.exists()){
            file.createNewFile();
        }
        Call call = downloadAsync(url, file, downListener);
        return call;
    }
    /**
     * 获取文件名
     *
     * @param
     */
    private String getFileName(String url){
        int lastSeparaorIndex = url.lastIndexOf("/");
        return (lastSeparaorIndex < 0) ? url : url.substring(lastSeparaorIndex + 1, url.length());
    }
    /**
     * 异步下载文件,实现了下载进度的提示
     * @param url
     * @param downFile:存储文件(文件存储绝对路径文件)
     * @param downListener
     * @throws IOException
     */
    public Call downloadAsync(final String url, final File downFile,
                              final OKHttpUICallback.ProgressCallback downListener) throws IOException {
        OkHttpClient downLoadClient = mOkHttpClient.newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //拦截
                        Response origin = chain.proceed(chain.request());
                        //包装响应体
                        return origin.newBuilder()
                                .body(new ProgressBody.ProgressResponseBody(origin.body(), downListener, okHttpHandler))
                                .build();
                    }
                })
                .build();
        final Request request = new Request.Builder().url(url).build();
        Call call = downLoadClient.newCall(request);
        call.enqueue(new OkHttpThreadCallback(okHttpHandler, downListener, true)
                        .setFile(downFile));
        return call;
    }
}
