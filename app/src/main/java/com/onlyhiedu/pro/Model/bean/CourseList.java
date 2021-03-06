package com.onlyhiedu.pro.Model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */

public class CourseList {


    /**
     * total : 1
     * list : [{"uuid":"C48D14A4-FE09-44C2-99D6-17BF42262179","leadsUuid":"3BD05F2F-17A5-4854-8A26-654DFEA55998","teacherUuid":null,"courseDate":null,"startTime":"10:00","endTime":null,"subject":null,"teacherName":null}]
     */

    public int total;
    public List<ListBean> list;

    public static class ListBean implements Serializable{
        /**
         * uuid : C48D14A4-FE09-44C2-99D6-17BF42262179
         * leadsUuid : 3BD05F2F-17A5-4854-8A26-654DFEA55998
         * teacherUuid : null
         * courseDate : null
         * startTime : 10:00
         * endTime : null
         * subject : null
         * teacherName : null
         */


        public String courseUuid;
        public String teacherUuid;
        public String courseDate;
        public String startTime;
        public String endTime;
        public String subject;
        public String teacherName;
        //家长声网uid null：未绑定家长
        public String channelPatriarchId;

        public int channelTeacherId;
//课程类型 0:测评课;1:正式课;2:调试课
        public int courseType;
        //增加字段 是否点击
        public boolean isClickAble=false;

        public boolean isClickAble() {
            return isClickAble;
        }

        //增加字段 是否 结束
        public boolean isFinish = false;


        public void setClickAble(boolean clickAble) {
            isClickAble = clickAble;
        }

        public String getUuid() {
            return courseUuid;
        }

        public void setUuid(String uuid) {
            this.courseUuid = uuid;
        }

        public String getTeacherUuid() {
            return teacherUuid;
        }

        public void setTeacherUuid(String teacherUuid) {
            this.teacherUuid = teacherUuid;
        }

        public String getCourseDate() {
            return courseDate;
        }

        public void setCourseDate(String courseDate) {
            this.courseDate = courseDate;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }
    }
}
