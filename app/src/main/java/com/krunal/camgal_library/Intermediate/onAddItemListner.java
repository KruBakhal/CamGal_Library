package com.krunal.camgal_library.Intermediate;

import com.krunal.camgal_library.Model.GalleryModel;

public interface onAddItemListner {
        void onAdd(GalleryModel galleryModel);

        void onRemove();

        void onUpdate(int finalCount, GalleryModel galleryModel);
    }