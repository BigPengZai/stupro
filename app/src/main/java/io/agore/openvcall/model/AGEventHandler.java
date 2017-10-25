package io.agore.openvcall.model;

import io.agora.rtc.IRtcEngineEventHandler;

public interface AGEventHandler {
    void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed);

    void onJoinChannelSuccess(String channel, int uid, int elapsed);

    void onUserOffline(int uid, int reason);

    void onUserJoined(int uid, int elapsed);
    void onExtraCallback(int type, Object... data);

    void onUserMuteVideo(int uid,boolean muted);
    void onUserMuteAudio(int uid,boolean muted);
    int EVENT_TYPE_ON_DATA_CHANNEL_MSG = 3;

    int EVENT_TYPE_ON_USER_VIDEO_MUTED = 6;

    int EVENT_TYPE_ON_USER_AUDIO_MUTED = 7;

    int EVENT_TYPE_ON_SPEAKER_STATS = 8;

    int EVENT_TYPE_ON_AGORA_MEDIA_ERROR = 9;

    int EVENT_TYPE_ON_USER_VIDEO_STATS = 10;

    int EVENT_TYPE_ON_APP_ERROR = 13;

    void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats);
}
