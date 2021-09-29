package com.krunal.camgal_library.Utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.krunal.camgal_library.R;

import static android.content.Context.MODE_PRIVATE;
import static com.krunal.camgal_library.Utils.Constant.*;

public class BookStore {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;




    public BookStore(Context context) {
        sharedPreferences = (SharedPreferences) context.getSharedPreferences("" + R.string.app_name, MODE_PRIVATE);
    }

    public int get_Image_Selection_Count() {
        return sharedPreferences.getInt(IMAGE_Selection_COUNT, 5);
    }

    public void set_Image_Selection_Count(int count) {
        editor = sharedPreferences.edit();
        editor.putInt(IMAGE_Selection_COUNT, count);
        editor.commit();

    }

    public String get_RESOLUTION() {
        return sharedPreferences.getString(SAVE_RESOLUTION, "720%720");
    }

    public void set_SAVE_RESOLUTION(String count) {
        editor = sharedPreferences.edit();
        editor.putString(SAVE_RESOLUTION, count);
        editor.commit();

    }

    // 0-Back, 1-Front
    public int get_FACING_MODE() {
        return sharedPreferences.getInt(FACING_MODE, 0);
    }

    public void set_FACING_MODE(int count) {
        editor = sharedPreferences.edit();
        editor.putInt(FACING_MODE, count);
        editor.commit();

    }

    public int get_GALLERY_TYPE() {
        return sharedPreferences.getInt(GALLERY_TYPE, 0);
    }

    public void set_GALLERY_TYPE(int count) {
        editor = sharedPreferences.edit();
        editor.putInt(GALLERY_TYPE, count);
        editor.commit();

    }

    public boolean get_COMPRESS_STATUS() {
        return sharedPreferences.getBoolean(COMPRESS_STATUS, true);
    }

    public void set_COMPRESS_STATUS(boolean count) {
        editor = sharedPreferences.edit();
        editor.putBoolean(COMPRESS_STATUS, count);
        editor.commit();
    }

    public boolean get_ASPECT_RATIO() {
        return sharedPreferences.getBoolean(ASPECT_RATIO, true);
    }

    public void setASPECT_RATIO(boolean count) {
        editor.putBoolean(ASPECT_RATIO, count);
        editor.commit();
    }

    public int get_RESIZE_RATIO_Percentage() {
        return sharedPreferences.getInt(RESIZE_RATIO_Percentage, 10);
    }

    public void set_RESIZE_RATIO_Percentage(int count) {
        editor = sharedPreferences.edit();
        editor.putInt(RESIZE_RATIO_Percentage, count);
        editor.commit();
    }

    public int get_COMPRESSION_SIZE() {
        return sharedPreferences.getInt(COMPRESSION_SIZE, 5);
    }

    public void set_COMPRESSION_SIZE(int count) {
        editor = sharedPreferences.edit();
        editor.putInt(COMPRESSION_SIZE, count);
        editor.commit();
    }

    public String get_COMPRESSION_Type() {
        return sharedPreferences.getString(COMPRESSION_SIZE_Type, "MB");
    }

    public void set_COMPRESSION_Type(String count) {
        editor = sharedPreferences.edit();
        editor.putString(COMPRESSION_SIZE_Type, count);
        editor.commit();
    }

}
