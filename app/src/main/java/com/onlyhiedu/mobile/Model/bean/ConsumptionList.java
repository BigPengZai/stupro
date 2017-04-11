package com.onlyhiedu.mobile.Model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pengpeng on 2017/4/11.
 * 课时消耗
 */

public class ConsumptionList {
    public List<ListBean> list;
    public static class ListBean implements Serializable {
        public String teacherName;
        public String total;
        public String remaing;

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getRemaing() {
            return remaing;
        }

        public void setRemaing(String remaing) {
            this.remaing = remaing;
        }
        public int getShow_type() {
            if (  teacherName==null||teacherName.equals("")) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
