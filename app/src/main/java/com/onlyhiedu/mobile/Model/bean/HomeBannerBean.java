package com.onlyhiedu.mobile.Model.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public class HomeBannerBean {


    /**
     * total : 1
     * list : [{"image":"http://192.168.1.219/clientupload/1499242520992.jpg","link":"http://www.baidu.com"}]
     */

    public List<ListBean> list;

    public static class ListBean {
        /**
         * image : http://192.168.1.219/clientupload/1499242520992.jpg
         * link : http://www.baidu.com
         */
        public String title;
        public String image;
        public String link;
    }
}
