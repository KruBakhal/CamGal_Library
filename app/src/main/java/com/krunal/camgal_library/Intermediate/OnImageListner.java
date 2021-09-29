package com.krunal.camgal_library.Intermediate;

import com.krunal.camgal_library.Model.ImageModel;

public interface OnImageListner {
        void onImageClick(int position);

        void onAdd_Item(int finalCount, ImageModel imageModel);
    }