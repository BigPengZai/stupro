package com.onlyhiedu.mobile.Model.bean.board;

import java.util.List;

/**
 * 画板相关信息
 * Created by Administrator on 2017/3/29.
 */

public class ResponseWhiteboardList {


    /**
     * AccountID : 1111
     * ActionType : Response_WhiteboardList
     * ChannelID : DebugChannel1
     * Keyword : HKT
     * ResponseParam : {"WhiteboardList":[{"Active":1,"WhiteboardDocID":"","WhiteboardDocPageID":"","WhiteboardEraseSize":5,"WhiteboardFontType":"宋体#Bold,Italic,Underline,Strikeout,#20.00","WhiteboardHeight":708,"WhiteboardID":"729EB6760AE04A94AA9A38AED417E415","WhiteboardName":"whiteboard1","WhiteboardPenColor":"FFFF0000","WhiteboardPenSize":5,"WhiteboardViewRect":"0,0,520,520","WhiteboardWidth":800}]}
     * Result : 1
     * ResultDesc : SUCCEED
     * Timestamp : 1490773884
     */

    public String AccountID;
    public String ActionType;
    public String ChannelID;
    public String Keyword;
    public ResponseParamBean ResponseParam;
    public int Result;
    public String ResultDesc;
    public String Timestamp;

    public static class ResponseParamBean {

        public List<WhiteboardListBean> WhiteboardList;

        public static class WhiteboardListBean {
            /**
             * Active : 1
             * WhiteboardDocID :
             * WhiteboardDocPageID :
             * WhiteboardEraseSize : 5
             * WhiteboardFontType : 宋体#Bold,Italic,Underline,Strikeout,#20.00
             * WhiteboardHeight : 708
             * WhiteboardID : 729EB6760AE04A94AA9A38AED417E415
             * WhiteboardName : whiteboard1
             * WhiteboardPenColor : FFFF0000
             * WhiteboardPenSize : 5
             * WhiteboardViewRect : 0,0,520,520
             * WhiteboardWidth : 800
             */

            public int Active;
            public String WhiteboardDocID;
            public String WhiteboardDocPageID;
            public int WhiteboardEraseSize;
            public String WhiteboardFontType;
            public int WhiteboardHeight;
            public String WhiteboardID;
            public String WhiteboardName;
            public String WhiteboardPenColor;
            public int WhiteboardPenSize;
            public String WhiteboardViewRect;
            public int WhiteboardWidth;
        }
    }
}
