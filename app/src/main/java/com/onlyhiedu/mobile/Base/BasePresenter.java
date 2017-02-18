package com.onlyhiedu.mobile.Base;

public interface BasePresenter<T extends BaseView>{

    void attachView(T view);

    void detachView();

}