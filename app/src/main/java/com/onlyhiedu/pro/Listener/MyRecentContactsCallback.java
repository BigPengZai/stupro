package com.onlyhiedu.pro.Listener;

import android.content.Context;

import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.onlyhiedu.pro.IM.reminder.ReminderManager;
import com.onlyhiedu.pro.IM.session.SessionHelper;
import com.onlyhiedu.pro.IM.session.extension.GuessAttachment;
import com.onlyhiedu.pro.IM.session.extension.RTSAttachment;
import com.onlyhiedu.pro.IM.session.extension.RedPacketAttachment;
import com.onlyhiedu.pro.IM.session.extension.RedPacketOpenedAttachment;
import com.onlyhiedu.pro.IM.session.extension.SnapChatAttachment;
import com.onlyhiedu.pro.IM.session.extension.StickerAttachment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/12.
 */

public class MyRecentContactsCallback implements RecentContactsCallback {

    private Context mContext;

    public MyRecentContactsCallback(Context context) {
        mContext = context;
    }

    @Override
    public void onRecentContactsLoaded() {
        // 最近联系人列表加载完毕
    }

    @Override
    public void onUnreadCountChange(int unreadCount) {
        ReminderManager.getInstance().updateSessionUnreadNum(unreadCount);
    }

    @Override
    public void onItemClick(RecentContact recent) {
        // 回调函数，以供打开会话窗口时传入定制化参数，或者做其他动作
        switch (recent.getSessionType()) {
            case P2P:
                SessionHelper.startP2PSession(mContext, recent.getContactId());
                break;
            case Team:
                SessionHelper.startTeamSession(mContext, recent.getContactId());
                break;
            default:
                break;
        }
    }

    @Override
    public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
        // 设置自定义消息的摘要消息，展示在最近联系人列表的消息缩略栏上
        // 当然，你也可以自定义一些内建消息的缩略语，例如图片，语音，音视频会话等，自定义的缩略语会被优先使用。
        if (attachment instanceof GuessAttachment) {
            GuessAttachment guess = (GuessAttachment) attachment;
            return guess.getValue().getDesc();
        } else if (attachment instanceof RTSAttachment) {
            return "[白板]";
        } else if (attachment instanceof StickerAttachment) {
            return "[贴图]";
        } else if (attachment instanceof SnapChatAttachment) {
            return "[阅后即焚]";
        } else if (attachment instanceof RedPacketAttachment) {
            return "[红包]";
        } else if (attachment instanceof RedPacketOpenedAttachment) {
            return ((RedPacketOpenedAttachment) attachment).getDesc(recentContact.getSessionType(), recentContact.getContactId());
        }

        return null;
    }

    @Override
    public String getDigestOfTipMsg(RecentContact recent) {
        String msgId = recent.getRecentMessageId();
        List<String> uuids = new ArrayList<>(1);
        uuids.add(msgId);
        List<IMMessage> msgs = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
        if (msgs != null && !msgs.isEmpty()) {
            IMMessage msg = msgs.get(0);
            Map<String, Object> content = msg.getRemoteExtension();
            if (content != null && !content.isEmpty()) {
                return (String) content.get("content");
            }
        }

        return null;
    }
}
