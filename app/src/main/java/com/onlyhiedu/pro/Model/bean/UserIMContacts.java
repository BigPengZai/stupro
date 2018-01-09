package com.onlyhiedu.pro.Model.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */

public class UserIMContacts {


    /**
     * hasError : false
     * code : 0
     * message : 成功
     * data : {"total":2,"list":[{"imUserName":"13916593205","iconurl":null,"userName":"stt","phone":"13916593205"},{"imUserName":"13994413502","iconurl":null,"userName":"杨澜","phone":"13994413502"}]}
     */

    public boolean hasError;
    public int code;
    public String message;
    public DataBean data;

    public static class DataBean {
        /**
         * total : 2
         * list : [{"imUserName":"13916593205","iconurl":null,"userName":"stt","phone":"13916593205"},{"imUserName":"13994413502","iconurl":null,"userName":"杨澜","phone":"13994413502"}]
         */

        public int total;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * imUserName : 13916593205
             * iconurl : null
             * userName : stt
             * phone : 13916593205
             */

            public String imUserName;
            public String iconurl;
            public String userName;
            public String phone;
        }
    }
}
