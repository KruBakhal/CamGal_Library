package com.krunal.camgal_library.Adapter;


import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krunal.camgal_library.GalleryActivity;
import com.krunal.camgal_library.Intermediate.onAddItemListner;
import com.krunal.camgal_library.Model.GalleryModel;
import com.krunal.camgal_library.Model.ImageModel;
import com.krunal.camgal_library.R;
import com.krunal.camgal_library.Utils.HelperResizer;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;


public class FolderAdapter_Native extends RecyclerView.Adapter<FolderAdapter_Native.UserViewHolder> {

    private int type = 1;
    GalleryActivity galleryFragment;
    ArrayList<GalleryModel> list;

    private long mLastClickTime = 0;




    onAddItemListner onAddItemListner;

    synchronized public void addItem(GalleryModel galleryModel) {
        if (list == null)
            list = new ArrayList<>();
        list.add(galleryModel);
        onAddItemListner.onAdd(galleryModel);
        notifyItemInserted(list.size() - 1);
    }


    public interface FolderAdapterListener {
        void onUserFolderClick(ImageModel image, int i);

    }

    FolderAdapterListener listener;

    public FolderAdapter_Native(GalleryActivity context, ArrayList<GalleryModel> list) {
        this.galleryFragment = context;
        onAddItemListner = (onAddItemListner) context;
        this.list = list;
        listener = (FolderAdapterListener) context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_folder, parent, false);
        UserViewHolder viewHolder = new UserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, final int i) {

        bind(userViewHolder, i);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void bind(UserViewHolder userViewHolder, final int i) {
        HelperResizer.getInstance(galleryFragment);


        final ImageModel imageModel = (ImageModel) list.get(i).getListFolder();


        userViewHolder.txt_folder.setText(imageModel.getfName());


        if (galleryFragment.folder_position_native != null && !TextUtils.isEmpty(galleryFragment.folder_position_native)) {
            if (Integer.parseInt(galleryFragment.folder_position_native) == i) {

                userViewHolder.frame_folder.setBackgroundResource(R.drawable.select_folder);


            } else {
                userViewHolder.frame_folder.setBackgroundResource(R.drawable.folder);

            }
        } else {
            if (i == 0) {

                userViewHolder.frame_folder.setBackgroundResource(R.drawable.select_folder);


            } else {
                userViewHolder.frame_folder.setBackgroundResource(R.drawable.folder);

            }
        }

        userViewHolder.flmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                listener.onUserFolderClick(imageModel, i);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list == null || list.size() == 0)
            return 0;
        else return list.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout frame_folder;
        View view;
        RelativeLayout flmain, llborder;
        RoundedImageView iv_firstImage;
        TextView txt_folder;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            flmain = itemView.findViewById(R.id.flmain);
            frame_folder = itemView.findViewById(R.id.frame_folder);
            iv_firstImage = itemView.findViewById(R.id.iv_firstImage);
            llborder = itemView.findViewById(R.id.llborder);
            txt_folder = itemView.findViewById(R.id.txt_folder);
            iv_firstImage.setCornerRadius(galleryFragment.getResources().getDisplayMetrics().widthPixels * 10 / 1080);

            HelperResizer.setHeightWidthAsWidth(galleryFragment, flmain, 380, 290);
            HelperResizer.setHeightWidthAsWidth(galleryFragment, frame_folder, 380, 290);
            HelperResizer.setHeightWidthAsWidth(galleryFragment, iv_firstImage, 380, 290);
            HelperResizer.setHeightWidthAsWidth(galleryFragment, llborder, 380, 70);
            HelperResizer.setMarginLeft(galleryFragment, flmain, 15);

        }
    }
}

