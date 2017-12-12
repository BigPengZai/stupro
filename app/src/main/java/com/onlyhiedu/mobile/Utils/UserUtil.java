package com.onlyhiedu.mobile.Utils;

import com.onlyhiedu.mobile.Model.bean.CourseList;
import com.onlyhiedu.mobile.Model.bean.UserDataBean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pengpeng on 2017/2/22.
 */

public class UserUtil {

    static public Boolean isLogin() {
        ArrayList<UserDataBean> userInfoData = DaoUtil.getInstance().query(UserDataBean.class);
        if (userInfoData != null && userInfoData.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    static public void logout() {
        DaoUtil.getInstance().deleteAll(UserDataBean.class);
    }

    static public void saveUserData(UserDataBean userData) {
        DaoUtil.getInstance().save(userData);
    }

    /**
     * 请在调用前使用 isLogin() 进行判断。数据可能返回空。
     */
    static public UserDataBean getUserData() {
        ArrayList<UserDataBean> list = DaoUtil.getInstance().query(UserDataBean.class);
        if (list != null && list.size() > 0) {
            return list.get(list.size() - 1);
        } else {
            return new UserDataBean();
        }
    }

    static public void clearUserData() {
        DaoUtil.getInstance().deleteAll(UserDataBean.class);
    }


    public static int isClassIn(CourseList.ListBean item) {
//        String startTime = "16:07:00";
//        String courseDate = "2017-04-14";
//        String endTime_start = "16:20:00";
        String startTime = item.getStartTime() + ":00";
        String courseDate = item.getCourseDate();
        String endTime_start = item.getEndTime() + ":00";
        String mTime = DateUtil.getTime(courseDate + " " + startTime);
        String mEndtime1 = DateUtil.getTime(courseDate + " " + endTime_start);
        String mRoomStartTime = DateUtil.getStrTime(mTime);
        String mRoomEndTime = DateUtil.getStrTime(mEndtime1);
        String nowTime = DateUtil.formatDate(new Date(System.currentTimeMillis()), DateUtil.yyyyMMddHHmmss);
        DateFormat df = new SimpleDateFormat(DateUtil.yyyyMMddHHmmss);
        try {
            Date room_start = df.parse(mRoomStartTime);
            Date room_end = df.parse(mRoomEndTime);
            Date now = df.parse(nowTime);
            //上课中
            if (now.getTime() > room_start.getTime() && now.getTime() < room_end.getTime()) {
                return 1;
            }
            //即将开始
            if (now.getTime() < room_start.getTime() && now.getTime() > (room_start.getTime() - 10 * 60 * 1000)) {
                return 2;
            }
            //已经结束
            if (now.getTime() > room_start.getTime() && now.getTime() > room_end.getTime()) {
                return 3;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
