package com.onlyhiedu.mobile.Model.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by pengpeng on 2017/2/22.
 */
@Table("user")
public class UserDataBean {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    public int _id;

    @Column("error")
    private boolean error;


    public void setError(boolean error){
        this.error = error;
    }
    public boolean getError(){
        return this.error;
    }

}
