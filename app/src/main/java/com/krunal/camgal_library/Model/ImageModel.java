package com.krunal.camgal_library.Model;

public class ImageModel implements Cloneable {
    String fId, fSize;
    String fName, fPath;
    String fModified;
    int counter = 0;
    boolean isCroped = false;
    String croppedPath = null;

    @Override
    public String toString() {
        return "ImageModel{" +
                "fId='" + fId + '\'' +
                ", fSize='" + fSize + '\'' +
                ", fName='" + fName + '\'' +
                ", fPath='" + fPath + '\'' +
                ", fModified='" + fModified + '\'' +
                ", counter=" + counter +
                ", isCroped=" + isCroped +
                ", croppedPath='" + croppedPath + '\'' +
                '}';
    }

    public String getCroppedPath() {
        return croppedPath;
    }

    public void setCroppedPath(String croppedPath) {
        this.croppedPath = croppedPath;
    }

    public ImageModel(String fId, String fName, String fPath, boolean isCroped) {
        this.fId = fId;

        this.fName = fName;
        this.fPath = fPath;
        this.isCroped = isCroped;
    }

    public ImageModel(String fName, String fPath, boolean isCroped) {
        this.fName = fName;
        this.fPath = fPath;
        this.isCroped = isCroped;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public ImageModel(String fId, String fName, String fPath, String fSize) {
        this.fId = fId;
        this.fName = fName;
        this.fPath = fPath;
        this.fSize = fSize;
    }

    public ImageModel() {

    }

    public ImageModel(String fName) {
        this.fName = fName;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getfModified() {
        return fModified;
    }

    public void setfModified(String fModified) {
        this.fModified = fModified;
    }

    public ImageModel(String fId, String fName, String fPath, String fSize, String croppedPath) {
        this.fId = fId;
        this.fName = fName;
        this.fPath = fPath;
        this.fSize = fSize;
        this.croppedPath = croppedPath;
    }

    public void setCroped(boolean croped) {
        isCroped = croped;
    }

    public boolean isCroped() {
        return isCroped;
    }

    public ImageModel(String fId, String fName, String fPath) {
        this.fId = fId;
        this.fName = fName;
        this.fPath = fPath;
    }

    public String getfId() {
        return fId;
    }


    public String getfName() {
        return fName;
    }


    public String getfPath() {
        return fPath;
    }

}
