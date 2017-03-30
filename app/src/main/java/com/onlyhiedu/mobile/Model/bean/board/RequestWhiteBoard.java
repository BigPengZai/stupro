package com.onlyhiedu.mobile.Model.bean.board;

/**
 * 请求白板相关参数
 * Created by Administrator on 2017/3/29.
 */
public class RequestWhiteBoard {

    /**
     * AccountID : 2222
     * ActionType : Request_WhiteboardList
     * ChannelID : DebugChannel_4
     * Keyword : HKT
     * Timestamp : 1490758924
     */

    public String AccountID;
    public String ActionType = "Request_WhiteboardList";
    public String ChannelID = "DebugChannel1";
    public String Keyword = "HKT";
    public String Timestamp = System.currentTimeMillis()+"";


}
