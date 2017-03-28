package com.onlyhiedu.mobile.Utils;

import android.os.Handler;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by pengpeng on 2017/3/25.
 */

public class ProgressBody {
    /**
     * 包装响应体，用于处理提示下载进度
     *
     * Created by Seeker on 2016/6/29.
     */
    public static final class ProgressResponseBody extends ResponseBody {

        //实际待包装的响应体
        private final ResponseBody responseBody;

        //进度回调接口
        private OKHttpUICallback.ProgressCallback mListener;

        //包装完成的BufferedSource
        private BufferedSource bufferedSource;

        //传递下载进度到主线程
        private Handler mHandler;

        public ProgressResponseBody(ResponseBody responseBody, OKHttpUICallback.ProgressCallback listener, Handler handler){
            this.responseBody = responseBody;
            this.mListener = listener;
            this.mHandler = handler;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if(bufferedSource == null){
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        /**
         * 读取，回调进度接口
         * @return
         */
        private Source source(Source source){
            return new ForwardingSource(source) {
                //读取当前获取的字节数
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    final long byteRead =  super.read(sink, byteCount);
                    if(mHandler != null && mListener != null){
                        //增加当前读取的字节数，如果读取完成则返回-1
                        totalBytesRead += byteRead != -1?byteRead:0;
                        //回调，若是contentLength()不知道长度，则返回-1
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mListener.onProgress(totalBytesRead, contentLength(), byteRead == -1);
                            }
                        });
                    }
                    return byteRead;
                }
            };
        }
    }
}
