package com.krunal.camgal_library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krunal.camgal_library.Model.ImageModel;
import com.krunal.camgal_library.Utils.HelperResizer;
import com.krunal.camgal_library.databinding.ActivityPreviewBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.krunal.camgal_library.Utils.Utility.ARG_PARAM1;
import static com.krunal.camgal_library.Utils.Utility.ARG_PARAM2;

public class PreviewActivity extends AppCompatActivity {
    private @NonNull
    ActivityPreviewBinding viewBinding;
    private ArrayList<ImageModel> listImages = new ArrayList<>();

    private Type typetype = new TypeToken<ArrayList<ImageModel>>() {
    }.getType();
    private String mParam1;
    int mParam2;
    private PreviewActivity context;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_preview);
        viewBinding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        context = this;

        mParam1 = getIntent().getStringExtra(ARG_PARAM1);
        mParam2 = getIntent().getIntExtra(ARG_PARAM2, 0);
        Type type = new TypeToken<ArrayList<ImageModel>>() {
        }.getType();
        listImages.addAll(new Gson().fromJson(mParam1, type));

        final ImageModel imageModel = listImages.get(mParam2);
        Glide.with(context).load("file://" + imageModel.getfPath())
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewBinding.imgPreview);

        viewBinding.tvTitle.setText("Preview");
        HelperResizer.setHeight(context, viewBinding.actionBar, 150);
        HelperResizer.setSize(viewBinding.btnBack, 70, 70, true);
        HelperResizer.setSize(viewBinding.btnDone, 100, 100, true);
        viewBinding.layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}