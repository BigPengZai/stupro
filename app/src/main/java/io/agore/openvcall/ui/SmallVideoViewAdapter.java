package io.agore.openvcall.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.onlyhiedu.mobile.Utils.ScreenUtil;

import java.lang.ref.SoftReference;
import java.util.HashMap;

public class SmallVideoViewAdapter extends VideoViewAdapter {
//    private final static Logger log = LoggerFactory.getLogger(SmallVideoViewAdapter.class);

    private int mExceptedUid;

    public SmallVideoViewAdapter(Context context, int localUid, int exceptedUid, HashMap<Integer, SoftReference<SurfaceView>> uids, VideoViewEventListener listener) {
        super(context, localUid, uids, listener);
        mExceptedUid = exceptedUid;
//        log.debug("SmallVideoViewAdapter " + (mLocalUid & 0xFFFFFFFFL) + " " + (mExceptedUid & 0xFFFFFFFFL));
    }

    @Override
    protected void customizedInit(HashMap<Integer, SoftReference<SurfaceView>> uids, boolean force) {
        VideoViewAdapterUtil.composeDataItem(mUsers, uids, mLocalUid, null, null, mVideoInfo, mExceptedUid);

        if (force || mItemWidth == 0 || mItemHeight == 0) {
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(outMetrics);
//            mItemWidth = outMetrics.widthPixels / 4;
//            mItemHeight = outMetrics.heightPixels / 4;

            int screenHeight = ScreenUtil.getScreenHeight(mContext);
            int screenWidth = ScreenUtil.getScreenWidth(mContext);
            mItemWidth = screenWidth/10*3;
            mItemHeight = screenHeight/2;
        }
    }

    @Override
    public void notifyUiChanged(HashMap<Integer, SoftReference<SurfaceView>> uids, int uidExcepted, HashMap<Integer, Integer> status, HashMap<Integer, Integer> volume) {
        mUsers.clear();

        mExceptedUid = uidExcepted;

//        log.debug("notifyUiChanged " + (mLocalUid & 0xFFFFFFFFL) + " " + (uidExcepted & 0xFFFFFFFFL) + " " + uids + " " + status + " " + volume);
        VideoViewAdapterUtil.composeDataItem(mUsers, uids, mLocalUid, status, volume, mVideoInfo, uidExcepted);

        notifyDataSetChanged();
    }

    public int getExceptedUid() {
        return mExceptedUid;
    }
}
