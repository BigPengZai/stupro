package com.onlyhiedu.mobile.Model.bean.finishclass;


/**
 * Created by pengpeng on 2017/4/6.
 */

public class ResponseFinishClassData {
    public String AccountID;
    public String ActionType;
    public String ChannelID;
    public String Keyword;
    public ResponseParamBean ResponseParam;
    public boolean Result = true;
    public String Timestamp = (System.currentTimeMillis() / 1000) + "";

    public static class ResponseParamBean {
        public String Confirm;
        public String FinishTime ;

    }
}
