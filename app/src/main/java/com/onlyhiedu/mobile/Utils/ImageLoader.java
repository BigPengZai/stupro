package com.onlyhiedu.mobile.Utils;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Glide 图片加载辅助类
 * 适配圆形图片加载情况
 */

public class ImageLoader {

    private ImageLoader() {
    }

    public static void loadImage(RequestManager loader, ImageView view, String url) {
        loadImage(loader, view, url, 0);
    }

    public static void loadImage(RequestManager loader, ImageView view, String url, int placeholder) {
        boolean isCenterCrop = false;
        if (view instanceof CircleImageView)
            isCenterCrop = true;
        loadImage(loader, view, url, placeholder, placeholder, isCenterCrop);
    }

    /**
     *
     * @param loader
     * @param view
     * @param url
     * @param placeholder   加载中的图片
     * @param error         加载失败显示的图片
     * @param isCenterCrop  是否需要剪裁CenterCrop
     */
    public static void loadImage(RequestManager loader, ImageView view, String url, int placeholder, int error, boolean isCenterCrop) {
        if (TextUtils.isEmpty(url)) {
            view.setImageResource(placeholder);
        } else {
            if (view instanceof CircleImageView) {
                BitmapRequestBuilder builder = loader.load(url).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(placeholder)
                        .error(error);
                if (isCenterCrop)
                    builder.centerCrop();

                builder.into(
                        new BitmapImageViewTarget(view) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(view.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                view.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } else {
                DrawableRequestBuilder builder = loader.load(url).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(placeholder).error(error);
                if (isCenterCrop)
                    builder.centerCrop();
                builder.into(view);
            }
        }
    }

    public static void loadImage(RequestManager loader, ImageView view, String url,int w,int h){
        DrawableRequestBuilder builder = loader.load(url).diskCacheStrategy(DiskCacheStrategy.ALL).override(w,h);
        builder.into(view);
    }

}
