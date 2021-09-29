package com.krunal.camgal_library.Utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class HelperResizer {
    public static int height, width;
    public static int SCALE_WIDTH = 1080; // scale width of ui
    public static int SCALE_HEIGHT = 1920; // scale height of ui

    public static void getInstance(Context context) {

        getHeight(context);
        getwidth(context);
    }

    public static int getwidth(Context context) {
        width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    public static int getHeight(Context context) {
        height = context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    public static void setHeight(Context mContext, View view, int v_height) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int height = dm.heightPixels * v_height / SCALE_HEIGHT;
        view.getLayoutParams().height = height;
    }

    public static void setWidth(Context mContext, View view, int v_Width) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int width = dm.widthPixels * v_Width / SCALE_WIDTH;
        view.getLayoutParams().width = width;
    }

    public static int setHeight(int h) {

        return (height * h) / 1920;

    }

    public static int setWidth(int w) {
        return (width * w) / 1080;

    }

    public static void setSize(View view, int width, int height) {

        view.getLayoutParams().height = setHeight(height);
        view.getLayoutParams().width = setWidth(width);

    }

    public static void setHeightByWidth(Context mContext, View view, int v_height) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int height = dm.widthPixels * v_height / SCALE_WIDTH;
        view.getLayoutParams().height = height;
    }

    public static void setSize(View view, int width, int height, boolean sameheightandwidth) {

        if (sameheightandwidth) {
            view.getLayoutParams().height = setWidth(height);
            view.getLayoutParams().width = setWidth(width);
        } else {
            view.getLayoutParams().height = setHeight(height);
            view.getLayoutParams().width = setHeight(width);
        }

    }

    public static void setMargin(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.setMargins(setWidth(left), setHeight(top), setWidth(right), setHeight(bottom));
    }

    public static void setPadding(View view, int left, int top, int right, int bottom) {
        view.setPadding(left, top, right, bottom);
    }

    public static void setHeightWidth(Context mContext, View view, int v_width, int v_height) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int width = dm.widthPixels * v_width / SCALE_WIDTH;
        int height = dm.heightPixels * v_height / SCALE_HEIGHT;
        view.getLayoutParams().width = width;
        view.getLayoutParams().height = height;
    }


    public static void setHeightWidthAsWidth(Context mContext, View view, int v_width, int v_height) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int width = dm.widthPixels * v_width / SCALE_WIDTH;
        int height = dm.widthPixels * v_height / SCALE_WIDTH;
        view.getLayoutParams().width = width;
        view.getLayoutParams().height = height;
    }

    public static void setHeightByWidth(Context mContext, View view, int width, int height) {
        view.getLayoutParams().height = setWidth(height);
        view.getLayoutParams().width = setWidth(width);
    }

    public static void setMarginBottom(Context mContext, View view, int m_bottom) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        // margin
        int bottom = dm.heightPixels * m_bottom / SCALE_HEIGHT;

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(0, 0, 0, bottom);
            view.requestLayout();
        }
    }

    public static void setMarginTop(Context mContext, View view, int m_top) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        // margin
        int top = dm.heightPixels * m_top / SCALE_HEIGHT;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(0, top, 0, 0);
            view.requestLayout();
        }
    }


    /**
     * ToDo.. Set width and height of view
     *
     * @param mContext The context
     * @param view     The view whose width and height are to be set
     * @param v_width  The width to be set
     */


    /**
     * ToDo.. Set height of view
     *
     * @param mContext The context
     * @param view     The view whose width and height are to be set
     * @param v_height The height to be set
     */


    public static void setHeightAsWidth(Context mContext, View view, int v_height) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int height = dm.widthPixels * v_height / SCALE_WIDTH;
        view.getLayoutParams().height = height;
    }

    /**
     * ToDo.. Set padding to view
     *
     * @param mContext The context
     * @param view     The view whose width and height are to be set
     * @param p_left   The magicleft padding to be set
     * @param p_top    The top padding to be set
     * @param p_right  The magicalright padding to be set
     * @param p_bottom The bottom padding to be set
     */
    public static void setPadding(Context mContext, View view, int p_left, int p_top, int
            p_right, int p_bottom) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int left = dm.widthPixels * p_left / SCALE_WIDTH;
        int top = dm.heightPixels * p_top / SCALE_HEIGHT;
        int right = dm.widthPixels * p_right / SCALE_WIDTH;
        int bottom = dm.heightPixels * p_bottom / SCALE_HEIGHT;
        view.setPadding(left, top, right, bottom);
    }

    /**
     * ToDo.. Set padding to view
     *
     * @param mContext The context
     * @param view     The view whose width and height are to be set
     * @param padding  The padding to be set
     */
    public static void setPadding(Context mContext, View view, int padding) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int left_right = dm.widthPixels * padding / SCALE_WIDTH;
        int top_bottom = dm.heightPixels * padding / SCALE_HEIGHT;
        view.setPadding(left_right, top_bottom, left_right, top_bottom);
    }


    /**
     * ToDo.. Set magicleft padding to view
     *
     * @param mContext The context
     * @param view     The view whose width and height are to be set
     * @param p_left   The magicleft padding to be set
     */
    public static void setPaddingLeft(Context mContext, View view, int p_left) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int left = dm.widthPixels * p_left / SCALE_WIDTH;
        view.setPadding(left, 0, 0, 0);
    }

    /**
     * ToDo.. Set top padding to view
     *
     * @param mContext The context
     * @param view     The view whose width and height are to be set
     * @param p_top    The top padding to be set
     */
    public static void setPaddingTop(Context mContext, View view, int p_top) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int top = dm.heightPixels * p_top / SCALE_HEIGHT;
        view.setPadding(0, top, 0, 0);
    }


    /**
     * ToDo.. Set magicalright padding to view
     *
     * @param mContext The context
     * @param view     The view whose width and height are to be set
     * @param p_right  The magicalright padding to be set
     */
    public static void setPaddingRight(Context mContext, View view, int p_right) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int right = dm.widthPixels * p_right / SCALE_WIDTH;
        view.setPadding(0, 0, right, 0);
    }

    /**
     * ToDo.. Set bottom padding to view
     *
     * @param mContext The context
     * @param view     The view whose width and height are to be set
     * @param p_bottom The bottom padding to be set
     */
    public static void setPaddingBottom(Context mContext, View view, int p_bottom) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int bottom = dm.heightPixels * p_bottom / SCALE_HEIGHT;
        view.setPadding(0, 0, 0, bottom);
    }


    /**
     * ToDo.. Set margin to view
     *
     * @param mContext The context
     * @param view     The view whose width and height are to be set
     * @param m_left   The magicleft margin to be set
     * @param m_top    The top margin to be set
     * @param m_right  The magicalright margin to be set
     * @param m_bottom The bottom margin to be set
     */
    public static void setMargins(Context mContext, View view, int m_left, int m_top, int
            m_right, int m_bottom) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        // margin
        int left = dm.widthPixels * m_left / SCALE_WIDTH;
        int top = dm.heightPixels * m_top / SCALE_HEIGHT;
        int right = dm.widthPixels * m_right / SCALE_WIDTH;
        int bottom = dm.heightPixels * m_bottom / SCALE_HEIGHT;

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    /**
     * ToDo.. Set margin to view
     *
     * @param mContext The context
     * @param view     The view whose width and height are to be set
     * @param margin   The margin to be set
     */
    public static void setMargins(Context mContext, View view, int margin) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        // margin
        int left_right = dm.widthPixels * margin / SCALE_WIDTH;
        int top_bottom = dm.widthPixels * margin / SCALE_WIDTH;

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left_right, top_bottom, left_right, top_bottom);
            view.requestLayout();
        }
    }


    /**
     * ToDo.. Set magicleft margin to view
     *
     * @param mContext The context
     * @param view     The view whose width and height are to be set
     * @param m_left   The magicleft margin to be set
     */
    public static void setMarginLeft(Context mContext, View view, int m_left) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        // margin
        int left = dm.widthPixels * m_left / SCALE_WIDTH;

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, 0, 0, 0);
            view.requestLayout();
        }
    }


    /**
     * ToDo.. Set top margin to view
     *
     * @param mContext The context
     * @param view     The view whose width and height are to be set
     * @param m_top    The top margin to be set
     */


    /**
     * ToDo.. Set magicalright margin to view
     *
     * @param mContext The context
     * @param view     The view whose width and height are to be set
     * @param m_right  The magicalright margin to be set
     */
    public static void setMarginRight(Context mContext, View view, int m_right) {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        // margin
        int right = dm.widthPixels * m_right / SCALE_WIDTH;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(0, 0, right, 0);
            view.requestLayout();
        }
    }


    /**
     * ToDo.. Set bottom margin to view
     *
     * @param mContext The context
     * @param view     The view whose width and height are to be set
     * @param m_bottom The bottom margin to be set
     */


}
