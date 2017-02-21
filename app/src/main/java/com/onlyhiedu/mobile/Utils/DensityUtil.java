package com.onlyhiedu.mobile.Utils;

import android.content.Context;
import android.content.res.Resources;

public class DensityUtil {

	// 根据屏幕密度转换
	private static float mPixels = 0.0F;
	private static float density = -1.0F;

	/**
	 * 像素转化dip
	 *
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {

		final float scale = context.getResources().getDisplayMetrics().density;

		return (int) (pxValue / scale + 0.5f);

	}

	/**
	 * dip转化像素
	 *
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(float dipValue) {
		final float scale = Resources.getSystem().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);

	}




}