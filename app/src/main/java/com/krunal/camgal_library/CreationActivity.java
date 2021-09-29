package com.krunal.camgal_library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krunal.camgal_library.Adapter.Original_Image_Adapter;
import com.krunal.camgal_library.Model.ImageModel;
import com.krunal.camgal_library.Utils.FileUtils;
import com.krunal.camgal_library.Utils.HelperResizer;
import com.krunal.camgal_library.Utils.Utility;
import com.krunal.camgal_library.databinding.ActivityCreationBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.krunal.camgal_library.Utils.Utility.ARG_PARAM1;
import static com.krunal.camgal_library.Utils.Utility.ARG_PARAM2;

public class CreationActivity extends AppCompatActivity {
    ActivityCreationBinding viewBinding;
    private ArrayList<ImageModel> listImages = new ArrayList<>();
    private Original_Image_Adapter adpter;
    private Type typetype = new TypeToken<ArrayList<ImageModel>>() {
    }.getType();
    private Context context;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_creation);
        viewBinding = ActivityCreationBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        context = this;

        viewBinding.tvTitle.setText("Creations");
        HelperResizer.setHeight(context, viewBinding.actionBar, 150);
        HelperResizer.setSize(viewBinding.btnBack, 70, 70, true);
        HelperResizer.setSize(viewBinding.btnDone, 100, 100, true);

      /*  String list = Navigation.findNavController(viewBinding.getRoot()).getCurrentBackStackEntry().getSavedStateHandle().get("key");
        if (!TextUtils.isEmpty(list)) {
            listImages.clear();
            listImages = new ArrayList<>();
            listImages.addAll(new Gson().fromJson(list, typetype));
        } else {
      */
        ArrayList<ImageModel> tempList =
                Utility.getList_ByFolderName(new FileUtils().getTemp_Compressed_ImagePath(context));
        if (tempList != null && tempList.size() > 0) {
            listImages.addAll(tempList);
        }
//        }

        viewBinding.layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Navigation.findNavController(viewBinding.getRoot()).popBackStack();
                finish();
            }
        });
        viewBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        adpter = new Original_Image_Adapter(context, listImages, new Original_Image_Adapter.Original_ImageListener() {
            @Override
            public void onCropImageClick(ImageModel image, int position) {


            }

            @Override
            public void onIteamClick(ImageModel image, int position) {

                startActivity(new Intent(context, PreviewActivity.class)
                        .putExtra(ARG_PARAM1, new Gson().toJson(listImages)).
                                putExtra(ARG_PARAM2, position)

                );

            }
        });
        viewBinding.rvImageList.setAdapter(adpter);


    }
}