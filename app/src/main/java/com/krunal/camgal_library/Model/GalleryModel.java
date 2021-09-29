package com.krunal.camgal_library.Model;

import java.util.ArrayList;

public class GalleryModel {

    Object listFolder;
    ArrayList<ImageModel> listImage;

    public Object getListFolder() {
        return listFolder;
    }

    public ArrayList<ImageModel> getListImage() {
        return listImage;
    }

    public void setListFolder(Object listFolder) {
        this.listFolder = listFolder;
    }

    public void setListImage(ArrayList<ImageModel> listImage) {
        this.listImage = listImage;
    }

    public GalleryModel(Object listFolder, ArrayList<ImageModel> listImage) {
        this.listFolder = listFolder;
        this.listImage = listImage;
    }
}
