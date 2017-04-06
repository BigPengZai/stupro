package com.onlyhiedu.mobile.Model.bean.finishclass;

/**
 * Created by pengpeng on 2017/4/6.
 * 请求下课
 */

public class RequestFinishClass {
    public String AccountID;
    public String ActionType = "Request_FinishClass";
    public String ChannelID = "DebugChannel1";
    public String Keyword = "HKT";
    public String Timestamp = System.currentTimeMillis()+"";
}
