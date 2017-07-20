package com.onlyhiedu.mobile.Model.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public class HomeTeacher {

    /**
     * total : 1
     * list : [{"name":"血神","image":null,"teachingAge":"10年","graduateClass":"7年","improveScore":"99.9","description":"十年修成血神子，称霸人间7年，只要99.9"}]
     */

    public int total;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * name : 血神
         * image : null
         * teachingAge : 10年
         * graduateClass : 7年
         * improveScore : 99.9
         * description : 十年修成血神子，称霸人间7年，只要99.9
         */

        public String name;
        public String image;
        public String teachingAge;
        public String graduateClass;
        public String improveScore;
        public String description;
    }
}
