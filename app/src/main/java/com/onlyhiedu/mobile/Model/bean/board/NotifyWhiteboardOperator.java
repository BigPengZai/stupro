package com.onlyhiedu.mobile.Model.bean.board;

/**
 * 画板动作
 * Created by Administrator on 2017/3/29.
 */

public class NotifyWhiteboardOperator {


    /**
     * AccountID : 1111
     * ActionType : Notify_WhiteboardOperator
     * ChannelID : DebugChannel1
     * Keyword : HKT
     * NotifyParam : {"MethodParam":"171,39|145,75|130,152|128,195|129,216|130,232|131,239|135,248|138,253|139,255|138,255|","MethodType":"PaintPoint","WhiteboardID":"2A7D987712AE42E9946BE13B9C8A2612"}
     * Timestamp : 1490770793
     */

    public String AccountID;
    public String ActionType;
    public String ChannelID;
    public String Keyword;
    public NotifyParamBean NotifyParam;
    public String Timestamp;

    public static class NotifyParamBean {
        /**
         * MethodParam : 171,39|145,75|130,152|128,195|129,216|130,232|131,239|135,248|138,253|139,255|138,255|
         * MethodType : PaintPoint
         * WhiteboardID : 2A7D987712AE42E9946BE13B9C8A2612
         */

        public String MethodParam;
        public String MethodType;
        public String WhiteboardID;
    }
}


