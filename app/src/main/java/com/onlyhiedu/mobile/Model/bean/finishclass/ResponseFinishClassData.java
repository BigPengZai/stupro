package com.onlyhiedu.mobile.Model.bean.finishclass;


/**
 * Created by pengpeng on 2017/4/6.
 */

public class ResponseFinishClassData {
    public String AccountID;
    public String ActionType;
    public String ChannelID;
    public String Keyword;
    public ResponParamBean mResponParamBean;
    public static class ResponParamBean {
        public String Confirm;
        public String FinishTime;
    }
}
