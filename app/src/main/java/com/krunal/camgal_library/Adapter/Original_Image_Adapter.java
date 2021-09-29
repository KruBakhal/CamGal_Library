package com.krunal.camgal_library.Adapter;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.krunal.camgal_library.Model.ImageModel;
import com.krunal.camgal_library.R;
import com.krunal.camgal_library.Utils.HelperResizer;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;


public class Original_Image_Adapter extends RecyclerView.Adapter<Original_Image_Adapter.UserViewHolder> {
    ArrayList<ImageModel> list;
    Context context;
    private long mLastClickTime = 0;


    public interface Original_ImageListener {
        void onCropImageClick(ImageModel image, int position);

        void onIteamClick(ImageModel image, int position);

    }

    Original_ImageListener listener;


    public Original_Image_Adapter(Context context, ArrayList<ImageModel> gridList,
                                  Original_ImageListener single) {
        this.context = context;
        this.listener = single;
        this.list = gridList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View convertView = inflater.inflate(R.layout.lay_original_iteam, parent, false);
        UserViewHolder userViewHolder = new UserViewHolder(convertView);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
        final ImageModel imageModel = list.get(position);
        if (imageModel.isCroped()) {
            Glide.with(context).load("file://" + imageModel.getCroppedPath()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.imageViewIteam);
        } else
            Glide.with(context).load("file://" + imageModel.getfPath()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.imageViewIteam);

        holder.cropClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                listener.onCropImageClick(imageModel, position);
            }
        });
        holder.imageViewIteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                listener.onIteamClick(imageModel, position);
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
        RoundedImageView imageViewIteam;
        ImageView cropClick;

        RelativeLayout layImageList;

        public UserViewHolder(@NonNull View convertView) {
            super(convertView);
            layImageList = convertView.findViewById(R.id.layImageList);
            imageViewIteam = convertView.findViewById(R.id.imageViewIteam);
            cropClick = convertView.findViewById(R.id.cropClick);


            imageViewIteam.setCornerRadius(context.getResources().getDisplayMetrics().widthPixels * 25 / 1080);
            HelperResizer.getInstance(context);
            HelperResizer.setSize(layImageList, 500, 500, true);
            HelperResizer.setSize(imageViewIteam, 500, 500, true);
            HelperResizer.setSize(cropClick, 100, 100, true);


        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
