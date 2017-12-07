package com.onlyhiedu.mobile.Model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pengpeng on 2017/12/5.
 */

public class AgoraUidBean implements Serializable{


    /**
     * teaAgoraUid : 2049289033
     * stuAgoraUid : 2129058124
     * ccAgoraUid : null
     * crAgoraUid : null
     * tsAgoraUidList : [1501568200,2070404552]
     * qcAgoraUidList : [447648724]
     */

    private int teaAgoraUid;
    private int stuAgoraUid;
    private int ccAgoraUid;
    private int crAgoraUid;
    private List<Integer> tsAgoraUidList;
    private List<Integer> qcAgoraUidList;

    public int getTeaAgoraUid() {
        return teaAgoraUid;
    }

    public void setTeaAgoraUid(int teaAgoraUid) {
        this.teaAgoraUid = teaAgoraUid;
    }

    public int getStuAgoraUid() {
        return stuAgoraUid;
    }

    public void setStuAgoraUid(int stuAgoraUid) {
        this.stuAgoraUid = stuAgoraUid;
    }

    public int getCcAgoraUid() {
        return ccAgoraUid;
    }

    public void setCcAgoraUid(int ccAgoraUid) {
        this.ccAgoraUid = ccAgoraUid;
    }

    public int getCrAgoraUid() {
        return crAgoraUid;
    }

    public void setCrAgoraUid(int crAgoraUid) {
        this.crAgoraUid = crAgoraUid;
    }

    public List<Integer> getTsAgoraUidList() {
        return tsAgoraUidList;
    }

    public void setTsAgoraUidList(List<Integer> tsAgoraUidList) {
        this.tsAgoraUidList = tsAgoraUidList;
    }

    public List<Integer> getQcAgoraUidList() {
        return qcAgoraUidList;
    }

    public void setQcAgoraUidList(List<Integer> qcAgoraUidList) {
        this.qcAgoraUidList = qcAgoraUidList;
    }

}
