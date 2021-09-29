package com.krunal.camgal_library.Frag;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.krunal.camgal_library.Adapter.ImageAdapter_Native;
import com.krunal.camgal_library.GalleryActivity;
import com.krunal.camgal_library.Intermediate.OnImageListner;
import com.krunal.camgal_library.Model.GalleryModel;
import com.krunal.camgal_library.Model.ImageModel;
import com.krunal.camgal_library.R;
import com.krunal.camgal_library.Utils.Alana_GridSpacing;
import com.krunal.camgal_library.Utils.BookStore;
import com.krunal.camgal_library.Utils.HelperResizer;
import com.krunal.camgal_library.Utils.Utility;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class Gallery_Fragment_Native extends Fragment implements ImageAdapter_Native.ImageGridAdapterListener {

    GalleryModel model;
    Context context;
    public RecyclerView recyclerViewImage;
    public ArrayList<ImageModel> gridList;
    public ImageAdapter_Native gridImageViewAdapter;
    private int ImgCount = 5;
    private GalleryActivity mainFragment;


    OnImageListner imageListner;


    public Gallery_Fragment_Native(Context context, GalleryModel model, OnImageListner onImageListner) {
        // Required empty public constructor
        this.context = context;
        this.imageListner = onImageListner;
        this.model = model;
        gridList = model.getListImage();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        recyclerViewImage = view.findViewById(R.id.songRecycler);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        recyclerViewImage.setLayoutParams(layoutParams);
        HelperResizer.getInstance(context);
        HelperResizer.setMargin(recyclerViewImage, 0, 0, 0, 10);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainFragment = (GalleryActivity) getActivity();
        ImgCount = new BookStore(getContext()).get_Image_Selection_Count();

    }

    public void setImageAdapater() {
        gridImageViewAdapter = new ImageAdapter_Native(this, gridList, this, mainFragment.list_new_pic_selected);
        recyclerViewImage.addItemDecoration(new Alana_GridSpacing(3, 35, true));
        recyclerViewImage.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerViewImage.setAdapter(gridImageViewAdapter);
        gridImageViewAdapter.notifyDataSetChanged();
    }

    public boolean checkSlot_AreFull() {
        if (mainFragment.list_new_pic_selected == null || mainFragment.list_new_pic_selected.size() == 0)
            return false;
        int count = 0;
        for (ImageModel imageModel : mainFragment.list_new_pic_selected) {
            if (imageModel != null && !TextUtils.isEmpty(imageModel.getfPath())) {
                count++;
            }
        }

        if (count == 0 || count < mainFragment.list_new_pic_selected.size()) {
            return false;
        } else
            return true;
    }

    @Override
    public void onUserImageClick(ImageModel iteam, int position) {

        if (checkSlot_AreFull()) {
            Utility.displayToast(context, getResources().getString(R.string.all_image_selected));
            return;
        } else if (!TextUtils.isEmpty(mainFragment.list_new_pic_selected.get(mainFragment.selectedPosition).getfPath())) {
            Utility.displayToast(context, getResources().getString(R.string.already_image_selected));
            return;
        } else if (checkItemAlreadyinSelectedList(iteam, mainFragment.list_new_pic_selected)) {
            Utility.displayToast(context, getResources().getString(R.string.image_already_selecdte));
            return;
        }

        iteam.setCounter(gridList.get(position).getCounter() + 1);
        gridList.set(position, iteam);
        gridImageViewAdapter.notifyDataSetChanged();
        if (mainFragment.list_new_pic_selected == null)
            mainFragment.list_new_pic_selected = new ArrayList<>();
        mainFragment.list_new_pic_selected.set(mainFragment.selectedPosition, new ImageModel(iteam.getfId(),
                iteam.getfName(), iteam.getfPath(), false));

        if (!checkSlot_AreFull()) {
            mainFragment.selectedPosition++;
            if (mainFragment.selectedPosition >= ImgCount)
                mainFragment.selectedPosition--;
        }

        String stst = findemptySlot(mainFragment.list_new_pic_selected);
        if (!TextUtils.isEmpty(stst)) {
            mainFragment.selectedPosition = Integer.parseInt(stst);
        }

        imageListner.onImageClick(mainFragment.selectedPosition);
        gridImageViewAdapter.notifyItemChanged(position);


    }

    private boolean checkItemAlreadyinSelectedList(ImageModel iteam, ArrayList<ImageModel> list_new_pic_selected) {

        for (ImageModel imageModel : list_new_pic_selected) {
            if (!TextUtils.isEmpty(imageModel.getfPath()) && imageModel.getfPath().equals(iteam.getfPath())) {
                return true;
            }

        }

        return false;
    }

    public void notifyListChange(final ArrayList<ImageModel> gridList, final int y) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gallery_Fragment_Native.this.gridList = gridList;
//                gridImageViewAdapter.notifyListChange(gridList);
                setImageAdapater();
                recyclerViewImage.scrollTo(0, y);
            }
        });
    }

    public String findemptySlot(final ArrayList<ImageModel> gridList) {


        String count = null;
        for (int i = 0; i < gridList.size(); i++) {
            ImageModel imageModel = gridList.get(i);
            if (imageModel != null && TextUtils.isEmpty(imageModel.getfPath())) {
                count = "" + i;
                break;
            }
        }
        return count;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (gridImageViewAdapter == null) {
            setImageAdapater();
        }
    }

    @Override
    public void onAdd(int finalCount, ImageModel imageModel) {
        imageListner.onAdd_Item(finalCount, imageModel);
    }
}
