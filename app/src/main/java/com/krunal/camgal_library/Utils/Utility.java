package com.krunal.camgal_library.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.krunal.camgal_library.Model.GalleryModel;
import com.krunal.camgal_library.Model.ImageModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Utility {
    private static Toast toast;
    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static void forUriSecurity() {
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            if (Build.VERSION.SDK_INT >= 18) {
                builder.detectFileUriExposure();
            }
        } catch (Exception e) {
            e.toString();
        }
    }

    public static void onClickEvent(final View v) {
        v.setEnabled(false);
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setEnabled(true);
            }
        }, 150);
    }

    public static void displayToast(Context context, String txt) {
        if (toast != null && toast.getView() != null && toast.getView().isShown()) {
            toast.cancel();
            toast = null;
        }
        if (!TextUtils.isEmpty(txt)) {
            toast = Toast.makeText(context, txt, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /*-----------------------------------for Gallery and AUdio ---------------------------------------------------------------------*/
    public static ArrayList<ImageModel> getImageBucketsFolder(Context mContext) {
        ArrayList<ImageModel> buckets = new ArrayList<>();
        ArrayList<String> blist = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.SIZE};
        String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null,
                null, orderBy);
        try {
            if (cursor != null) {
                File file;
                while (cursor.moveToNext()) {
                    String fName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    String fPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String fId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                    boolean status = getImageBucketsCount(mContext, fId);
                    file = new File(fPath);

                    if (file.exists() && !blist.contains(fId) && status) {
                        buckets.add(new ImageModel(fId, fName, fPath));
                        blist.add(fId);
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.d("bucket", e.toString());
        }
        return buckets;
    }

    public static boolean getImageBucketsCount(Context mContext, String id) {
        ArrayList<String> blist = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        String selection = MediaStore.Images.Media.BUCKET_ID + "=?";
        String[] selectionArgs = {id};
        String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
        Cursor cursor = mContext.getContentResolver().query(uri, projection, selection, selectionArgs, orderBy);

        if (cursor != null) {
            if (cursor.getCount() > 0) {


                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public static ArrayList<ImageModel> get_Image_BucketsList(Context mContext, String id) {
        ArrayList<ImageModel> buckets = new ArrayList<>();
        ArrayList<String> blist = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        String selection = MediaStore.Images.Media.BUCKET_ID + "=?";
        String[] selectionArgs = {id};
        String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";

        Cursor cursor = mContext.getContentResolver().query(uri, projection, selection, selectionArgs, orderBy);

        if (cursor != null) {
            File file;
            while (cursor.moveToNext()) {
                String fName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String fPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                file = new File(fPath);

                if (file.exists() && !blist.contains(fPath) && !file.getAbsolutePath().endsWith(".gif")) {
                    buckets.add(new ImageModel(id, fName, fPath));
                    blist.add(fPath);
                }
            }
            cursor.close();
        }
        return buckets;
    }

    public static ArrayList<ImageModel> getAllFolder_Image(Context mContext) {

        ArrayList<ImageModel> all_Images = new ArrayList<>();

        ArrayList<ImageModel> all_folder = getImageBucketsFolder(mContext);
        for (ImageModel imageModel : all_folder) {
            ArrayList<ImageModel> list = get_Image_BucketsList(mContext, imageModel.getfId());
            all_Images.addAll(list);
        }
        return all_Images;
    }

    public static ArrayList<ImageModel> get_All_Audio_Files(Context mContext) {
        ArrayList<ImageModel> buckets = new ArrayList<>();
        ArrayList<GalleryModel> all_Folder = new ArrayList<>();
        ArrayList<String> blist = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE};
        String orderBy = MediaStore.Audio.Media.DATE_ADDED + " DESC";
        Cursor cursor = mContext.getContentResolver().query(uri,
                projection, null, null, orderBy);

        if (cursor != null) {
            File file = null;
            while (cursor.moveToNext()) {
                String fName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String fPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String fId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String fSize = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));

                file = new File(fPath);
                if (file.exists() && !blist.contains(fId)) {
                    ArrayList<ImageModel> listImage;
                    listImage = get_Audio_BucketsList(mContext, fId);
                    if (listImage != null && listImage.size() > 0) {
                        all_Folder.add(new GalleryModel(new ImageModel(fId, fName, fPath), listImage));
                        blist.add(fId);
                        buckets.addAll(listImage);
                    }
                }
            }
            cursor.close();
        }


        return buckets;
    }

    public static ArrayList<ImageModel> get_Audio_BucketsList(Context mContext, String id) {
        ArrayList<ImageModel> buckets = new ArrayList<>();
        ArrayList<String> blist = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                MediaStore.Audio.AudioColumns.SIZE};
        String selection = MediaStore.Audio.Media.DATA + " like ? ";
        String[] selectionArgs = new String[]{"%" + id + "%"};
        String orderBy = MediaStore.Audio.Media.DATE_ADDED + " DESC";

        Cursor cursor = mContext.getContentResolver().query(uri, projection, selection, selectionArgs, orderBy);

        if (cursor != null) {
            File file;
            while (cursor.moveToNext()) {
                String fName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String fPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String fsize = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                file = new File(fPath);

                if (file.exists() && !blist.contains(fPath) && file.getAbsolutePath().endsWith(".mp3")) {
                    buckets.add(new ImageModel(id, fName, fPath, fsize));
                    blist.add(fPath);
                }
            }
            cursor.close();
        }
        return buckets;
    }

    public static ArrayList<GalleryModel> getAudio_Folder_Image(Context mContext) {


        ArrayList<GalleryModel> all_Folder = new ArrayList<>();
        ArrayList<String> blist = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE};
        String orderBy = MediaStore.Audio.Media.DATE_ADDED + " DESC";
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, orderBy);

        if (cursor != null) {
            File file = null;
            while (cursor.moveToNext()) {
                String fName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String fPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String fId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String fSize = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));

                file = new File(fPath);
                if (file.exists() && !blist.contains(fId)) {
                    ArrayList<ImageModel> listImage = new ArrayList<>();
                    try {
                        listImage.addAll(get_Audio_BucketsList(mContext, fId));
                    } catch (Exception e) {
                        Log.d("msgOffline ", e.getMessage());
                    }

                    if (listImage != null && listImage.size() > 0) {
                        all_Folder.add(new GalleryModel(new ImageModel(fId, fName, fPath), listImage));
                        blist.add(fId);
                    }
                }
            }
            cursor.close();
        }

        return all_Folder;
    }

    public static ArrayList<GalleryModel> getImages_Folder_Image(Context mContext) {

        ArrayList<GalleryModel> all_Folder = new ArrayList<>();
        ArrayList<String> blist = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.SIZE};
        String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, orderBy);

        if (cursor != null) {
            File file = null;
            while (cursor.moveToNext()) {
                String fName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String fPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String fId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                String fSize = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                String fModified = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));


                file = new File(fPath);

                if (file.exists() && !blist.contains(fId)) {

                    ArrayList<ImageModel> listImage;
                    listImage = get_Image_BucketsList(mContext, fId);
                    if (listImage != null && listImage.size() > 0) {

                        all_Folder.add(new GalleryModel(new ImageModel(fId, fName, fPath), listImage));
                        blist.add(fId);
                    }


                }


            }
            cursor.close();
        }

        return all_Folder;
    }
    /*-------------------------------------------------------------------------------------------*/


    public static boolean checkConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return checkNet();
                // connected to wifi
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return checkNet();
            }
        }
        return false;
    }

    private static boolean checkNet() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }


    public static ArrayList<ImageModel> getList_ByFolderName(String path) {
        ArrayList<ImageModel> buckets = new ArrayList<>();
        if (new File(path).exists()) {
            File[] files = new File(path).listFiles();
            if (files != null) {
                Arrays.sort(files, new Comparator() {
                    public int compare(Object o1, Object o2) {

                        if (((File)o1).lastModified() > ((File)o2).lastModified()) {
                            return -1;
                        } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
                            return +1;
                        } else {
                            return 0;
                        }
                    }

                });
                for (File file : files) {
                    buckets.add(new ImageModel("", file.getName(), file.getAbsolutePath(), false));
                }
            }
        }

        return buckets;
    }

    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public static Uri scanMedia(Context context, String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(scanFileIntent);
        return uri;
    }

    public static String getPicNameByPosition(int i) {

        String _time = "";
        _time = "img_" + i + ".png";

        return _time;


    }

    public static String saveImageToCache(String folder, Bitmap bmp, String name) {
        String fileName = "";
        fileName = name;
        OutputStream fOut;
        String file_path = null;
        try {
            File dir = new File(folder);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(dir, fileName);
            file.deleteOnExit();
            fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            bmp.recycle();
            file_path = file.getAbsolutePath();
            //  scanMedia(context, file_path);

        } catch (Exception e) {
            Log.e("error in saving image", e.getMessage());
        }
        return file_path;
    }

    public static String getGalleryTypeString(int pos) {
        String type = null;
        switch (pos) {
            case 1:
                type = "image/png";
                break;
            case 2:
                type = "image/jpg";
                break;
            case 3:
                type = "image/jpeg";
                break;
        }

        return type;
    }

    public static boolean checkCameraFront(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkCameraRear() {
        int numCamera = Camera.getNumberOfCameras();
        if (numCamera > 0) {
            return true;
        } else {
            return false;
        }
    }
}
