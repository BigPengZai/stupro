package com.onlyhiedu.mobile.Model.bean.board;

import java.util.List;

/**
 * 画板相关信息
 * Created by Administrator on 2017/3/29.
 */

public class ResponseWhiteboardList {


    /**
     * AccountID : 582471047
     * ActionType : Response_WhiteboardList
     * ChannelID : C48D14A4-FE09-44C2-99D6-17BF422622071012207187
     * Keyword : HKT
     * ResponseParam : {"WhiteboardList":[{"Active":1,"WhiteboardDocID":"","WhiteboardDocPageID":"","WhiteboardEraseSize":10,"WhiteboardFontType":"宋体#Regular,#20.00","WhiteboardHeight":803,"WhiteboardID":"4250E4A427854C2EA3023A5E1A1D803C","WhiteboardName":"default","WhiteboardPenColor":"FF000000","WhiteboardPenSize":5,"WhiteboardViewRect":"0,0,1453,803","WhiteboardWidth":1523}]}
     * Result : 1
     * ResultDesc : SUCCEED
     * Timestamp : 1492498653
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
             * WhiteboardEraseSize : 10
             * WhiteboardFontType : 宋体#Regular,#20.00
             * WhiteboardHeight : 803
             * WhiteboardID : 4250E4A427854C2EA3023A5E1A1D803C
             * WhiteboardName : default
             * WhiteboardPenColor : FF000000
             * WhiteboardPenSize : 5
             * WhiteboardViewRect : 0,0,1453,803
             * WhiteboardWidth : 1523
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
