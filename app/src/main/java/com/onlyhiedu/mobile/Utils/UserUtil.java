package com.onlyhiedu.mobile.Utils;

import com.onlyhiedu.mobile.Model.bean.UserDataBean;

import java.util.ArrayList;

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

}
