package com.onlyhiedu.mobile.Model.bean.board;

/**
 * Created by Administrator on 2017/8/25.
 */

public class MyBoardData {

    public String type;     //类型
    public String XYData;
    public int color;
    public float lineWidth;
    public float fullRate;  //全屏缩放比例
    public float halfRate;  //半屏缩放比例


    public MyBoardData(String type, String XYData, int color, float lineWidth, float fullRate, float halfRate) {
        this.type = type;
        this.XYData = XYData;
        this.color = color;
        this.lineWidth = lineWidth;
        this.fullRate = fullRate;
        this.halfRate = halfRate;
    }
}
