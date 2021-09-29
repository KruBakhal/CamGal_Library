package com.krunal.camgal_library.Adapter;


import android.app.Activity;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.krunal.camgal_library.CameraActivity;
import com.krunal.camgal_library.GalleryActivity;
import com.krunal.camgal_library.Intermediate.SelectedAdapterListener;
import com.krunal.camgal_library.Model.ImageModel;
import com.krunal.camgal_library.R;
import com.krunal.camgal_library.Utils.HelperResizer;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;


public class SelectedImageAdapter_Native extends RecyclerView.Adapter<SelectedImageAdapter_Native.UserViewHolder> {
    Activity context;
    ArrayList<ImageModel> list;
    private long mLastClickTime = 0;

    public void notifyListChange(ArrayList<ImageModel> list_new_pic_selected, int position) {
        if (list.size() <= position && list_new_pic_selected.size() <= position)
            this.list.set(position, list_new_pic_selected.get(position));
        notifyItemChanged(position);
    }



    SelectedAdapterListener selectedAdapterListener;

    public SelectedImageAdapter_Native(Activity context, ArrayList<ImageModel> list, SelectedAdapterListener listener) {
        this.context = context;
        this.list = list;
        this.selectedAdapterListener = listener;

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lay_selected_image_iteam, parent, false);
        UserViewHolder viewHolder = new UserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, final int i) {
        //userViewHolder.setIsRecyclable(true);
        final ImageModel imageModel = list.get(i);
//        userViewHolder.imageView.setCornerRadius(15);

        if (context instanceof GalleryActivity) {
            if (i == ((GalleryActivity) context).selectedPosition) {
                userViewHolder.layFrame.setBackgroundResource(R.drawable.fram);
            } else {
                userViewHolder.layFrame.setBackground(null);
            }
        } else if (context instanceof CameraActivity) {
            if (i == ((CameraActivity) context).selectedPosition) {
                userViewHolder.layFrame.setBackgroundResource(R.drawable.fram);
            } else {
                userViewHolder.layFrame.setBackground(null);
            }
        }


        if (imageModel.getfPath() != null && imageModel.getfPath().length() != 0) {
            Glide.with(context).load("file://" + imageModel.getfPath()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(userViewHolder.imageView);
            userViewHolder.layClose.setVisibility(View.VISIBLE);
        } else {
            userViewHolder.layClose.setVisibility(View.GONE);
            userViewHolder.imageView.setImageResource(R.drawable.add_image);
        }

        userViewHolder.lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (context instanceof GalleryActivity) {
                    ((GalleryActivity) context).selectedPosition = i;
                } else {
                    ((CameraActivity) context).selectedPosition = i;
                }
                notifyDataSetChanged();
            }
        });
        userViewHolder.imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                selectedAdapterListener.onRemove_From_Selected_list(imageModel, i);
                if (context instanceof GalleryActivity) {
                    ((GalleryActivity) context).selectedPosition = i;
                } else {
                    ((CameraActivity) context).selectedPosition = i;
                }
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        if (list == null || list.size() == 0)
            return 0;
        else return list.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layImageList, lay1, layFrame;
        ImageView imageViewClose;
        RoundedImageView imageView;
        View view;
        LinearLayout lay;
        RelativeLayout layClose;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            lay = itemView.findViewById(R.id.lay);
            layImageList = itemView.findViewById(R.id.layImageList);
            lay1 = itemView.findViewById(R.id.lay1);
            layFrame = itemView.findViewById(R.id.layFrame);
            layClose = itemView.findViewById(R.id.layClose);
            imageView = itemView.findViewById(R.id.imageViewIteam);
            imageViewClose = itemView.findViewById(R.id.imageViewCancel);
            layClose.setVisibility(View.VISIBLE);

            FrameLayout.LayoutParams params =
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.MATCH_PARENT);
            lay.setGravity(Gravity.CENTER);
            lay.setLayoutParams(params);
            HelperResizer.getInstance(context);
            imageView.setCornerRadius(context.getResources().getDisplayMetrics().widthPixels * 6 / 1080);

            HelperResizer.setSize(layImageList, 190, 190, true);
            HelperResizer.setSize(layClose, 190, 190, true);
            HelperResizer.setSize(layFrame, 150, 160, true);
            HelperResizer.setSize(lay1, 150, 160, true);
            HelperResizer.setSize(imageViewClose, 40, 40, true);
            HelperResizer.setMargin(layImageList, 15, 0, 15, 0);
//            imageViewClose.setVisibility(View.GONE);

        }
    }
}

