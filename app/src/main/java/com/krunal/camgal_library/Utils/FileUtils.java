package com.krunal.camgal_library.Utils;

import android.content.Context;
import android.os.Environment;


import com.krunal.camgal_library.R;

import java.io.File;
import java.io.IOException;

public class FileUtils {


    private final String STORY_MAKER_FOLDER = "StoryMaker";
    public String strTemp_Image = "images"; // cancelled
    public String strTemp_CroppedImage = "cropped";
    public String strTemp_CompressedImage = "Compressed";
    public String APPDIRECTORY = "";


    public String getTempImageFolder(Context context) {
        String path = context.getFilesDir().getPath() + File.separator + strTemp_Image;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public void setAppDir(Context mContext) {

        APPDIRECTORY = getOurAppFolder(mContext);
    }

    public String getTemp_OriginalImage_Path(Context mContext) {
        String path = getTempFolderPath(mContext);
        File file = new File(path);
        if (file.exists()) {
            path = path + File.separator + strTemp_Image;
            file = new File(path);
            if (!file.exists())
                file.mkdir();
            return file.getAbsolutePath();
        }
        return path;
    }

    public String getTemp_CroppedImagePath(Context mContext) {
        String path = getTempFolderPath(mContext);
        File file = new File(path);
        if (file.exists()) {
            path = path + File.separator + strTemp_CroppedImage;
            file = new File(path);
            if (!file.exists())
                file.mkdir();
            return file.getAbsolutePath();
        }
        return path;
    }

    public String getTemp_Compressed_ImagePath(Context mContext) {
        String path = getTempFolderPath(mContext);
        File file = new File(path);
        if (file.exists()) {
            path = path + File.separator + strTemp_CompressedImage;
            file = new File(path);
            if (!file.exists())
                file.mkdir();
            return file.getAbsolutePath();
        }
        return path;
    }

    public String getTempFolderPath(Context mContext) {

        String path = mContext.getFilesDir().getPath();

        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

    public String getOurAppFolder(Context mContext) {
        String path;

        path = Environment.getExternalStorageDirectory() + File.separator + getAppName(mContext);

        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

    public String get_StoryMaker_CreationPath(Context mContext) {
        setAppDir(mContext);
        String path = getOurAppFolder(mContext) + File.separator + "MyCreation" + File.separator + STORY_MAKER_FOLDER;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public String getAppName(Context mContext) {
        if (mContext == null)
            return null;
        return mContext.getString(R.string.app_name);
    }


    public void emptyTempCompressedImages(Context context) {
        File file = new File(getTemp_Compressed_ImagePath(context));
        if (!file.exists())
            return;
        File[] list = file.listFiles();
        if (list == null || list.length <= 0) {
            return;
        }
        try {
            org.apache.commons.io.FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void emptyTempCroppedImages(Context context) {
        File file = new File(getTemp_CroppedImagePath(context));
        if (!file.exists())
            return;
        File[] list = file.listFiles();
        if (list == null || list.length <= 0) {
            return;
        }
        try {
            org.apache.commons.io.FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cacheFolder(Context context) {
        emptyTempCompressedImages(context);
        emptyTempCroppedImages(context);
    }
}
