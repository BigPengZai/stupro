package com.onlyhiedu.mobile.Model.bean;

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

        public String uuid;
        public String leadsUuid;
        public String teacherUuid;
        public String courseDate;
        public String startTime;
        public String endTime;
        public String subject;
        public String teacherName;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getLeadsUuid() {
            return leadsUuid;
        }

        public void setLeadsUuid(String leadsUuid) {
            this.leadsUuid = leadsUuid;
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

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }
    }
}
