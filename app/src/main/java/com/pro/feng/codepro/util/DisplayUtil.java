package com.pro.feng.codepro.util;


import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.pro.feng.codepro.ProApplication;

/**
 * 单位转化
 *
 */
public class DisplayUtil {

    /**
     * Don't let anyone instantiate this class.
     */
    private DisplayUtil() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * dip转px
     */
    public static final int DIP2PX = 0;
    /**
     * PX转DIP
     */
    public static final int PX2DIP = 1;
    /**
     * px转sp
     */
    public static final int PX2SP = 2;
    /**
     * sp转px
     */
    public static final int SP2PX = 3;

    public static DisplayMetrics metrics;

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / getDensity() + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变  (废弃原因：无需传Context)
     *
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    @Deprecated
    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue * getDensity() + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(float dipValue) {
        return (int) (dipValue * getDensity() + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue) {
        return (int) (pxValue / getScaledDensity() + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue) {
        return (int) (spValue * getScaledDensity() + 0.5f);
    }

    /**
     * 单位转换
     *
     * @param metrics 屏幕密度和缩放信息
     * @param value   需转换的值
     * @param type    转换类型
     * @return 返回转换后的值
     */
    public static int applyDimension(DisplayMetrics metrics, float value, int type) {
        switch (type) {
            case DIP2PX:
                return dip2px(value);
            case PX2DIP:
                return px2dip(value);
            case PX2SP:
                return px2sp(value);
            case SP2PX:
                return sp2px(value);
            default:
                break;
        }
        return 0;
    }

    /**
     * 单位转换
     *
     * @param value 需转换的值
     * @param type  转换类型
     * @return 返回转换后的值
     */
    public static int applyDimension(float value, int type) {
        return applyDimension(getDisplayMetrics(), value, type);
    }

    /**
     * 获取DisplayMetrics对象
     *
     * @return
     */
    public static DisplayMetrics getDisplayMetrics() {
        if (metrics == null) {
            metrics = ProApplication.getInstance().getResources().getDisplayMetrics();
        }
        return metrics;
    }

    /**
     * 获取scaledDensity属性
     *
     * @return
     */
    public static float getScaledDensity() {
        return getDisplayMetrics().scaledDensity;
    }

    /**
     * 得到density属性
     *
     * @return
     */
    public static float getDensity() {
        return getDisplayMetrics().density;
    }
}
