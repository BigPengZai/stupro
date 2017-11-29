package com.onlyhiedu.mobile.UI.Setting.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyhiedu.mobile.Base.SimpleActivity;
import com.onlyhiedu.mobile.Model.http.onlyApis;
import com.onlyhiedu.mobile.R;
import com.onlyhiedu.mobile.Utils.audio.AudioPlayManager;
import com.onlyhiedu.mobile.Utils.audio.AudioRecordManager;
import com.onlyhiedu.mobile.Utils.audio.IAudioPlayListener;
import com.onlyhiedu.mobile.Utils.audio.IAudioRecordListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/10/25.
 */

public class DeviceTestActivity extends SimpleActivity implements SurfaceHolder.Callback {

    public int PMS_RECORD_AUDIO = 1;
    public int PMS_CAMERA = 2;

    @BindView(R.id.btn_audio)
    Button mBtnAudio;
    @BindView(R.id.btn_network)
    Button mBtnNetwork;
    @BindView(R.id.btn_video)
    Button mBtnVideo;
    @BindView(R.id.root)
    LinearLayout mRoot;

    private Process p = null;
    private SurfaceView surface;
    private SurfaceHolder holder;
    private Camera camera;//声明相机
    private File mAudioDir;


    @Override
    protected int getLayout() {
        return R.layout.activity_device_text;
    }

    @Override
    protected void initEventAndData() {
        setToolBar("设备检测");

        AudioRecordManager.getInstance(this).setMaxVoiceDuration(20);
        mAudioDir = new File(getCacheDir(), "AUDIO");
        if (!mAudioDir.exists()) {
            mAudioDir.mkdirs();
        }
        AudioRecordManager.getInstance(this).setAudioSavePath(mAudioDir.getAbsolutePath());


        initListener();
    }

    private void initListener() {

        mBtnAudio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (ContextCompat.checkSelfPermission(DeviceTestActivity.this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    //进入到这里代表没有权限.
                    ActivityCompat.requestPermissions(DeviceTestActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, PMS_RECORD_AUDIO);
                } else {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            AudioRecordManager.getInstance(DeviceTestActivity.this).startRecord();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (isCancelled(v, event)) {
                                AudioRecordManager.getInstance(DeviceTestActivity.this).willCancelRecord();
                            } else {
                                AudioRecordManager.getInstance(DeviceTestActivity.this).continueRecord();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            AudioRecordManager.getInstance(DeviceTestActivity.this).stopRecord();
                            AudioRecordManager.getInstance(DeviceTestActivity.this).destroyRecord();
                            break;

                    }
                }
                return false;
            }
        });

        AudioRecordManager.getInstance(this).setAudioRecordListener(new IAudioRecordListener() {

            private TextView mTimerTV;
            private TextView mStateTV;
            private ImageView mStateIV;
            private PopupWindow mRecordWindow;

            @Override
            public void initTipView() {
                View view = View.inflate(DeviceTestActivity.this, R.layout.popup_audio_wi_vo, null);
                mStateIV = (ImageView) view.findViewById(R.id.rc_audio_state_image);
                mStateTV = (TextView) view.findViewById(R.id.rc_audio_state_text);
                mTimerTV = (TextView) view.findViewById(R.id.rc_audio_timer);
                mRecordWindow = new PopupWindow(view, -1, -1);
                mRecordWindow.showAtLocation(mRoot, 17, 0, 0);
                mRecordWindow.setFocusable(true);
                mRecordWindow.setOutsideTouchable(false);
                mRecordWindow.setTouchable(false);
            }

            @Override
            public void setTimeoutTipView(int counter) {
                if (this.mRecordWindow != null) {
//                    this.mStateIV.setVisibility(View.GONE);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_rec);
                    this.mStateTV.setBackgroundResource(R.drawable.bg_voice_popup);
//                    this.mTimerTV.setText(String.format("%s", new Object[]{Integer.valueOf(counter)}));
//                    this.mTimerTV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void setRecordingTipView() {
                if (this.mRecordWindow != null) {
                    this.mStateIV.setVisibility(View.VISIBLE);
                    this.mStateIV.setImageResource(R.mipmap.ic_volume_1);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_rec);
                    this.mStateTV.setBackgroundResource(R.drawable.bg_voice_popup);
                    this.mTimerTV.setVisibility(View.GONE);
                }
            }

            @Override
            public void setAudioShortTipView() {
                if (this.mRecordWindow != null) {
                    mStateIV.setImageResource(R.mipmap.ic_volume_wraning);
                    mStateTV.setText(R.string.voice_short);
                }
            }

            @Override
            public void setCancelTipView() {
                if (this.mRecordWindow != null) {
                    this.mTimerTV.setVisibility(View.GONE);
                    this.mStateIV.setVisibility(View.VISIBLE);
                    this.mStateIV.setImageResource(R.mipmap.ic_volume_cancel);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_cancel);
                    this.mStateTV.setBackgroundResource(R.drawable.corner_voice_style);
                }
            }

            @Override
            public void destroyTipView() {
                if (this.mRecordWindow != null) {
                    this.mRecordWindow.dismiss();
                    this.mRecordWindow = null;
                    this.mStateIV = null;
                    this.mStateTV = null;
                    this.mTimerTV = null;
                }
            }

            @Override
            public void onStartRecord() {
                //开始录制
            }

            @Override
            public void onFinish(Uri audioPath, int duration) {
                //发送文件
                File file = new File(audioPath.getPath());
                if (file.exists()) {
                    AudioPlayManager.getInstance().startPlay(DeviceTestActivity.this, audioPath, new IAudioPlayListener() {

                        @Override
                        public void onStart(Uri var1) {
                            mBtnAudio.setEnabled(false);
                        }

                        @Override
                        public void onStop(Uri var1) {
                        }

                        @Override
                        public void onComplete(Uri var1) {
                            mBtnAudio.setEnabled(true);
                        }
                    });
                }
            }

            @Override
            public void onAudioDBChanged(int db) {
                switch (db / 5) {
                    case 0:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_1);
                        break;
                    case 1:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_2);
                        break;
                    case 2:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_3);
                        break;
                    case 3:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_4);
                        break;
                    case 4:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_5);
                        break;
                    case 5:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_6);
                        break;
                    case 6:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_7);
                        break;
                    default:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_8);
                }
            }
        });
    }

    private boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        if (event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth() || event.getRawY() < location[1] - 40) {
            return true;
        }
        return false;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(DeviceTestActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @OnClick({R.id.btn_network, R.id.btn_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_network:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            p = Runtime.getRuntime().exec("/system/bin/ping -c 2 " + onlyApis.IP);
                            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
                            String str = new String();
                            if (buf.readLine() == null) {
                                Message message = new Message();
                                message.obj = "无网络连接";
                                mHandler.sendMessage(message);
                                return;
                            }
                            while ((str = buf.readLine()) != null) {
                                if (str.contains("avg")) {
                                    String substring = str.substring(str.indexOf("mdev =") + 7, str.length());
                                    String[] split = substring.split("/");
                                    int minDelay = (int) (Double.valueOf(split[0]) + 0.5);
                                    int maxDelay = (int) (Double.valueOf(split[2]) + 0.5);
                                    if (maxDelay < 100) {
                                        Message message = new Message();
                                        message.obj = "网络延迟： " + minDelay + "ms-" + maxDelay + "ms" + "网络状态良好";
                                        mHandler.sendMessage(message);
                                    } else if (maxDelay >= 100 && maxDelay < 200) {
                                        Message message = new Message();
                                        message.obj = "网络延迟： " + minDelay + "ms-" + maxDelay + "ms" + "网络状况良好，但是不太稳定";
                                        mHandler.sendMessage(message);
                                    } else if (maxDelay >= 200 && maxDelay < 500) {
                                        Message message = new Message();
                                        message.obj = "网络延迟： " + minDelay + "ms-" + maxDelay + "ms" + "网络状况一般，并且不太稳定";
                                        mHandler.sendMessage(message);
                                    } else if (maxDelay >= 500) {
                                        Message message = new Message();
                                        message.obj = "网络延迟： " + minDelay + "ms-" + maxDelay + "ms" + "网络状况较差，且不太稳定";
                                        mHandler.sendMessage(message);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.btn_video:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PMS_CAMERA);
                } else {
                    final Dialog dialog = new Dialog(this, R.style.dialog);
                    View v = View.inflate(this, R.layout.layout_surfaceview, null);
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            dialog.cancel();
                        }
                    });
                    dialog.setContentView(v);
                    surface = (SurfaceView) v.findViewById(R.id.camera_surface);
                    holder = surface.getHolder();//获得句柄
                    holder.addCallback(this);//添加回调
                    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//surfaceview不维护自己的缓冲区，等待屏幕渲染引擎将内容推送到用户面前
                    dialog.show();
                    break;
                }
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //当surfaceview创建时开启相机
        if (camera == null) {
            camera = Camera.open(1);
            camera.setDisplayOrientation(90);

            try {
                camera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
                camera.startPreview();//开始预览
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //当surfaceview关闭时，关闭预览并释放资源
        camera.stopPreview();
        camera.release();
        camera = null;
        holder = null;
        surface = null;
    }

}
