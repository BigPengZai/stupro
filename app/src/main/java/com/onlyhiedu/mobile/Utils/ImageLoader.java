package com.onlyhiedu.mobile.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.onlyhiedu.mobile.R;

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

    public static void loadCircleImage(Context context, ImageView view, String url){
        Glide.with(context).load(url).centerCrop().placeholder(R.mipmap.head_portrait)
                .transform(new GlideCircleTransform(context,2,context.getResources().getColor(R.color.white)))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                .into(view);
    }

      static  class GlideCircleTransform extends BitmapTransformation {

         private Paint mBorderPaint;
         private float mBorderWidth;

         public GlideCircleTransform(Context context) {
             super(context);
         }

         public GlideCircleTransform(Context context, int borderWidth, int borderColor) {
             super(context);
             mBorderWidth = Resources.getSystem().getDisplayMetrics().density * borderWidth;

             mBorderPaint = new Paint();
             mBorderPaint.setDither(true);
             mBorderPaint.setAntiAlias(true);
             mBorderPaint.setColor(borderColor);
             mBorderPaint.setStyle(Paint.Style.STROKE);
             mBorderPaint.setStrokeWidth(mBorderWidth);
         }


         protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
             return circleCrop(pool, toTransform);
         }

         private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
             if (source == null) return null;

             int size = (int) (Math.min(source.getWidth(), source.getHeight()) - (mBorderWidth / 2));
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
             if (mBorderPaint != null) {
                 float borderRadius = r - mBorderWidth / 2;
                 canvas.drawCircle(r, r, borderRadius, mBorderPaint);
             }
             return result;
         }
          @Override public String getId() {
              return getClass().getName();
          }
     }
}
