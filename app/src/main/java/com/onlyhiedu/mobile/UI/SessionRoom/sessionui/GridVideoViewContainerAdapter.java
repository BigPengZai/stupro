package com.onlyhiedu.mobile.UI.SessionRoom.sessionui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;


import com.onlyhiedu.mobile.UI.SessionRoom.model.ConstantApp;
import com.onlyhiedu.mobile.UI.SessionRoom.propeller.UserStatusData;

import java.lang.ref.SoftReference;
import java.util.HashMap;


public class GridVideoViewContainerAdapter extends VideoViewAdapter {
    public static final String TAG = GridVideoViewContainerAdapter.class.getSimpleName();
//    private final static Logger log = LoggerFactory.getLogger(GridVideoViewContainerAdapter.class);

    public GridVideoViewContainerAdapter(Context context, int localUid, HashMap<Integer, SoftReference<SurfaceView>> uids, VideoViewEventListener listener) {
        super(context, localUid, uids, listener);
//        log.debug("GridVideoViewContainerAdapter " + (mLocalUid & 0xFFFFFFFFL));
    }

    @Override
    protected void customizedInit(HashMap<Integer, SoftReference<SurfaceView>> uids, boolean force) {
        VideoViewAdapterUtil.composeDataItem1(mUsers, uids, mLocalUid); // local uid

        if (force || mItemWidth == 0 || mItemHeight == 0) {
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(outMetrics);

            int count = uids.size();
            int DividerX = 1;
            int DividerY = 1;
            if (count == 2) {
                DividerY = 2;
            } else if (count >= 3) {
                DividerX = 2;
                DividerY = 2;
            }

//            mItemWidth = outMetrics.widthPixels / DividerX;
//            mItemHeight = outMetrics.heightPixels / DividerY;
//            Log.d(TAG, "mItemWidth:" + mItemWidth);
//            Log.d(TAG, "mItemHeight" + mItemHeight);

            int screenHeight = ScreenUtil.getScreenHeight(mContext);
            int screenWidth = ScreenUtil.getScreenWidth(mContext);
            mItemWidth = screenWidth/3;
            mItemHeight = screenHeight/2;
            Log.d(TAG, "mItemWidth:" + mItemWidth);
            Log.d(TAG, "mItemHeight" + mItemHeight);
        }
    }

    @Override
    public void notifyUiChanged(HashMap<Integer, SoftReference<SurfaceView>> uids, int localUid, HashMap<Integer, Integer> status, HashMap<Integer, Integer> volume) {
        setLocalUid(localUid);

        VideoViewAdapterUtil.composeDataItem(mUsers, uids, localUid, status, volume, mVideoInfo);

        notifyDataSetChanged();
//        log.debug("notifyUiChanged " + (mLocalUid & 0xFFFFFFFFL) + " " + (localUid & 0xFFFFFFFFL) + " " + uids + " " + status + " " + volume);
        Log.d(TAG, "notifyUiChanged " + (mLocalUid & 0xFFFFFFFFL) + " " + (localUid & 0xFFFFFFFFL) + " " + uids + " " + status + " " + volume);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        int sizeLimit = mUsers.size();
        if (sizeLimit >= ConstantApp.MAX_PEER_COUNT + 1) {
            sizeLimit = ConstantApp.MAX_PEER_COUNT + 1;
        }
        return sizeLimit;
    }

    public UserStatusData getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        UserStatusData user = mUsers.get(position);

        SurfaceView view = user.mView.get();
        if (view == null) {
            throw new NullPointerException("SurfaceView destroyed for user " + user.mUid + " " + user.mStatus + " " + user.mVolume);
        }

        return (String.valueOf(user.mUid) + System.identityHashCode(view)).hashCode();
    }
}
