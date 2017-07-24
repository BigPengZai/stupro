package com.onlyhiedu.mobile.Model.bean;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.onlyhiedu.mobile.Utils.IMUserInfoUtil;

/**
 * Created by Administrator on 2017/7/12.
 */
@DatabaseTable(tableName="IMUserInfo")
public class IMUserInfo {

    /**
     * imUserName : 13052509803
     * iconurl : http://192.168.1.219:20027/upload/1499242520992.jpg
     * userName : 许维诚
     * phone : 13052509803
     */
    @DatabaseField(generatedId=true)
    public int id;
    @DatabaseField(index = true)
    public String imUserName;
    @DatabaseField
    public String iconurl;
    @DatabaseField
    public String userName;
    @DatabaseField
    public String phone;
    /**
     * initial letter for nickname
     */
    @DatabaseField
    protected String initialLetter;
    public String getInitialLetter() {
        if(initialLetter == null){
            IMUserInfoUtil.setUserInitialLetter(this);
        }
        return initialLetter;
    }

    public void setInitialLetter(String initialLetter) {
        this.initialLetter = initialLetter;
    }



    public IMUserInfo( String imUserName, String iconurl, String userName, String phone) {
        this.imUserName = imUserName.toLowerCase();
        this.iconurl = iconurl;
        this.userName = userName;
        this.phone = phone;
    }

    public IMUserInfo() {
    }
}
