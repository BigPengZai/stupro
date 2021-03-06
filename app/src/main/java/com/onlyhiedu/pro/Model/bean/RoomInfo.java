package com.onlyhiedu.pro.Model.bean;


import java.io.Serializable;

/**
 * Created by pengpeng on 2017/3/22.
 */

public class RoomInfo implements Serializable {

    /**
     * hasError : false
     * code : 0
     * message : 成功
     * data : {"commChannelId":"DC8497B8-A5EF-4E58-8940-3981E4511A7D10","signallingChannelId":"DC8497B8-A5EF-4E58-8940-3981E4511A7D1012207187","channelTeacherId":1511698262,"channelStudentId":1951267069}
     */

    private String commChannelId;
    private String signallingChannelId;

    public String getCommChannelId() {
        return commChannelId;
    }

    public void setCommChannelId(String commChannelId) {
        this.commChannelId = commChannelId;
    }

    public String getSignallingChannelId() {
        return signallingChannelId;
    }

    public void setSignallingChannelId(String signallingChannelId) {
        this.signallingChannelId = signallingChannelId;
    }




}
