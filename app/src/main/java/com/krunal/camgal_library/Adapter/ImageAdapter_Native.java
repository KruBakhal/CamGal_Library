package com.krunal.camgal_library.Adapter;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.krunal.camgal_library.Frag.Gallery_Fragment_Native;
import com.krunal.camgal_library.Model.ImageModel;
import com.krunal.camgal_library.R;
import com.krunal.camgal_library.Utils.HelperResizer;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;


public class ImageAdapter_Native extends RecyclerView.Adapter<ImageAdapter_Native.UserViewHolder> {
    private final ArrayList<ImageModel> list_new_pic_selected;
    private Gallery_Fragment_Native type;
    ArrayList<ImageModel> list;
    Gallery_Fragment_Native context;
    private long mLastClickTime = 0;

    synchronized public void addItem(ImageModel imageModel) {
        if (list == null)
            list = new ArrayList<>();
        list.add(imageModel);
        //notifyDataSetChanged();

        notifyItemInserted(list.size() - 1);
    }

    public void notifyListChange(ArrayList<ImageModel> gridList) {
        this.list = gridList;
        notifyDataSetChanged();
    }


    public interface ImageGridAdapterListener {
        void onUserImageClick(ImageModel image, int position);

        void onAdd(int finalCount, ImageModel imageModel);
    }

    ImageGridAdapterListener listener;


    public ImageAdapter_Native(Gallery_Fragment_Native context,
                               ArrayList<ImageModel> gridList,
                               Gallery_Fragment_Native single,
                               ArrayList<ImageModel> list_new_pic_selected) {
        this.context = context;
        this.listener = (ImageGridAdapterListener) context;
        this.list = gridList;
        this.type = single;
        this.list_new_pic_selected = list_new_pic_selected;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View convertView = inflater.inflate(R.layout.lay_middle_image, parent, false);
        UserViewHolder userViewHolder = new UserViewHolder(convertView);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
        final ImageModel imageModel = list.get(position);

        Glide.with(context).load("file://" + imageModel.getfPath()).into(holder.imageViewIteam);
        int count = getImageCount(imageModel.getfPath());
        holder.tvCount.setText("" + count);
        if (count == 0) {
            holder.layFrame.setBackground(null);
            holder.layFrame1.setBackground(null);
            holder.layCount.setVisibility(View.GONE);
        } else if (count <= 1) {
            holder.layCount.setVisibility(View.VISIBLE);
            holder.layFrame.setBackgroundResource(R.drawable.fram);
            holder.layFrame1.setBackgroundResource(R.drawable.selected_img_opacity);
        } else {
            holder.layCount.setVisibility(View.VISIBLE);
            holder.layFrame.setBackgroundResource(R.drawable.fram);
            holder.layFrame1.setBackgroundResource(R.drawable.selected_img_opacity);
        }
        holder.imageViewIteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 300) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                listener.onUserImageClick(imageModel, position);
            }
        });


    }

    public int getImageCount(String s) {
        int counter = 0;
        for (int j = 0; j < list_new_pic_selected.size(); j++) {
            if (s.equals(list_new_pic_selected.get(j).getfPath())) {
                counter++;
            }
        }
        return counter;
    }

    @Override
    public int getItemCount() {
        if (list == null || list.size() == 0)
            return 0;
        else return list.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvCount;
        RoundedImageView imageViewIteam;

        RelativeLayout layImageList, layCount, layFrame, layFrame1;

        public UserViewHolder(@NonNull View convertView) {
            super(convertView);
            tvCount = convertView.findViewById(R.id.tvCount);
            layImageList = convertView.findViewById(R.id.layImageList);
            imageViewIteam = convertView.findViewById(R.id.imageViewIteam);

            layCount = convertView.findViewById(R.id.layCount);
            layFrame = convertView.findViewById(R.id.layFrame);
            layFrame1 = convertView.findViewById(R.id.layFrame1);

            imageViewIteam.setCornerRadius(context.getResources().getDisplayMetrics().widthPixels * 25 / 1080);
            HelperResizer.getInstance(context.getContext());
            HelperResizer.setSize(layImageList, 300, 300, true);
            HelperResizer.setSize(imageViewIteam, 300, 300, true);
            HelperResizer.setSize(layCount, 100, 100, true);


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
