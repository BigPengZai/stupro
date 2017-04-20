package com.onlyhiedu.mobile.Model.bean.board;

/**
 * Created by Administrator on 2017/3/30.
 */

public interface MethodType {

    String POINT = "PaintPoint";
    String LINE = "PaintLine";
    String PaintSet = "PaintSet"; //画笔颜色改变
    String ViewRect = "ViewRect"; //PC拖动白板
    String EraserPoint = "EraserPoint";//橡皮擦
    String PaintEllipse = "PaintEllipse";//○
    String PaintRect = "PaintRect"; //画方
    String Destory = "Destory";  //老师退出视频
    String Create = "Create";  //创建
    String PaintText = "PaintText";
    String EraserRect = "EraserRect";//范围清除
    String ChangeDoc = "ChangeDoc";  //改变白板关联的课件
    String ClearScreen = "ClearScreen";//清除整体屏幕
}
