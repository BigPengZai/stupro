package com.onlyhiedu.mobile.Model;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */

public class OrderList {


    /**
     * total : 1
     * list : [{"orderUuid":"1bc004b87b2e460692a86586d75689ac","orderNo":"20170725194442100001","type":"小学","length":"80","money":"14080","createDate":1500954486000,"payTime":null}]
     */

    public int total;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * orderUuid : 1bc004b87b2e460692a86586d75689ac
         * orderNo : 20170725194442100001
         * type : 小学
         * length : 80
         * money : 14080
         * createDate : 1500954486000
         * payTime : null
         */

        public String orderUuid;
        public String orderNo;
        public String type;
        public String length;
        public String money;
        public long createDate;
        public Object payTime;
    }
}
