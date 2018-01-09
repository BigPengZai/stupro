package com.onlyhiedu.pro.Model.bean.board;

/**
 * Created by Administrator on 2017/12/14.
 */

public class SendIMMsg {
    public String msg;
    public int role;
    public Long time;

    public SendIMMsg(String msg, int role, Long time) {
        this.msg = msg;
        this.role = role;
        this.time = time;
    }
}
