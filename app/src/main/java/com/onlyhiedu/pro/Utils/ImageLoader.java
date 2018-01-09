package com.onlyhiedu.pro.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.onlyhiedu.pro.R;

import java.security.MessageDigest;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Glide 图片加载辅助类
 * 适配圆形图片加载情况
 */

public class ImageLoader {

    private ImageLoader() {
    }

    public static void loadImage(Context context,RequestManager loader, ImageView view, String url) {
        loadImage(context,loader, view, url, 0);
    }

    public static void loadImage(Context context,RequestManager loader, ImageView view, String url, int placeholder) {
        boolean isCenterCrop = false;
        if (view instanceof CircleImageView)
            isCenterCrop = true;
        loadImage(context,loader, view, url, placeholder, placeholder, isCenterCrop);
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
    public static void loadImage(Context context,RequestManager loader, ImageView view, String url, int placeholder, int error, boolean isCenterCrop) {
        if (TextUtils.isEmpty(url)) {
            view.setImageResource(placeholder);
        } else {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .priority(Priority.HIGH);

            Glide.with(context).load(url).apply(options).into(view);

           /* if (view instanceof CircleImageView) {
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
            }*/
        }
    }

    public static void loadCircleImage(Context context, ImageView view, String url){
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.head_portrait)
                .error(R.mipmap.head_portrait)
                .priority(Priority.HIGH)
                .transform(new GlideCircleTransform());
        Glide.with(context).load(url).apply(options).into(view);
       /* Glide.with(context).load(url).centerCrop().placeholder(R.mipmap.head_portrait)
                .transform(new GlideCircleTransform(context,2,context.getResources().getColor(R.color.white)))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                .into(view);*/
    }

      public static class GlideCircleTransform extends BitmapTransformation {

          public GlideCircleTransform() {
              super();
          }

          @Override
          public Bitmap transform(BitmapPool pool, Bitmap toTransform,
                                  int outWidth, int outHeight) {
              return circleCrop(pool, toTransform);
          }

          private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
              if (source == null) return null;
              int size = Math.min(source.getWidth(), source.getHeight());
              int x = (source.getWidth() - size) / 2;
              int y = (source.getHeight() - size) / 2;
              // TODO this could be acquired from the pool too
              Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
              Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
              if (result == null) {
                  result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
              }
              Canvas canvas = new Canvas(result);
              Paint paint = new Paint();
              paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
              paint.setAntiAlias(true);
              float r = size / 2f;
              canvas.drawCircle(r, r, r, paint);
              return result;
          }

          @Override
          public void updateDiskCacheKey(MessageDigest messageDigest) {

          }
      }
}
