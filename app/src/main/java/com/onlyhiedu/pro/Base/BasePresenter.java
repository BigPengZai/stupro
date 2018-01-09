package com.onlyhiedu.pro.Base;

public interface BasePresenter<T extends BaseView>{

    void attachView(T view);

    void detachView();

}