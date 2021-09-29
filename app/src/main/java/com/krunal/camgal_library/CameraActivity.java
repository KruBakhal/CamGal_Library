package com.krunal.camgal_library;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.krunal.camgal_library.Adapter.SelectedImageAdapter_Native;
import com.krunal.camgal_library.Intermediate.SelectedAdapterListener;
import com.krunal.camgal_library.Model.ImageModel;
import com.krunal.camgal_library.Utils.BookStore;
import com.krunal.camgal_library.Utils.Constant;
import com.krunal.camgal_library.Utils.FileUtils;
import com.krunal.camgal_library.Utils.HelperResizer;
import com.krunal.camgal_library.databinding.ActivityCameraBinding;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraLogger;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Facing;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.krunal.camgal_library.Utils.Utility.ARG_PARAM1;
import static com.krunal.camgal_library.Utils.Utility.checkCameraFront;
import static com.krunal.camgal_library.Utils.Utility.checkCameraRear;
import static com.krunal.camgal_library.Utils.Utility.displayToast;
import static com.krunal.camgal_library.Utils.Utility.getPicNameByPosition;
import static com.krunal.camgal_library.Utils.Utility.onClickEvent;

public class CameraActivity extends AppCompatActivity implements SelectedAdapterListener {

    public int selectedPosition = 0;
    private ActivityCameraBinding viewBinding;
    private SelectedImageAdapter_Native imageSelectedAdapter;
    private ArrayList<ImageModel> list_new_pic_selected;
    private FileUtils utilsStorage;
    private BookStore bookStore;
    private int ImgCount = 5;
    private int Facing_MODE = 0;
    Context context;
    private boolean compressStatus;
    private int compressionSize;
    private String compression_Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
        viewBinding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        context = this;
        new FileUtils().cacheFolder(context);
        setupCamera();
        initData();
    }

    private void initData() {
        utilsStorage = new FileUtils();
        utilsStorage.cacheFolder(context);
        bookStore = new BookStore(context);

        ImgCount = getIntent().getIntExtra(Constant.IMAGE_Selection_COUNT, bookStore.get_Image_Selection_Count());
        Facing_MODE = getIntent().getIntExtra(Constant.FACING_MODE, bookStore.get_FACING_MODE());
        compressStatus = getIntent().getBooleanExtra(Constant.COMPRESS_STATUS, bookStore.get_COMPRESS_STATUS());
        compressionSize = getIntent().getIntExtra(Constant.COMPRESSION_SIZE, bookStore.get_COMPRESSION_SIZE());

        String c_type = getIntent().getStringExtra(Constant.COMPRESSION_SIZE_Type);
        if (!TextUtils.isEmpty(c_type)) {
            if (c_type.equals("MB")) {
                compression_Type = "MB";
            } else if (c_type.equals("KB")) {
                compression_Type = "KB";
            }
        }
        if (TextUtils.isEmpty(compression_Type)) {
            compression_Type = bookStore.get_COMPRESSION_Type();
        }

        switch (Facing_MODE) {
            case 0:
                viewBinding.switchCamera.setVisibility(View.VISIBLE);
                break;
            case 1:
                viewBinding.switchCamera.setVisibility(View.GONE);
                viewBinding.camera.setFacing(Facing.BACK);
                if (checkCameraFront(context)) {
                    viewBinding.camera.setFacing(Facing.BACK);
                } else if (checkCameraRear()) {
                    viewBinding.camera.setFacing(Facing.FRONT);
                }
                break;
            case 2:
                viewBinding.switchCamera.setVisibility(View.GONE);
                viewBinding.camera.setFacing(Facing.FRONT);
                if (checkCameraRear()) {
                    viewBinding.camera.setFacing(Facing.FRONT);
                } else if (checkCameraFront(context)) {
                    viewBinding.camera.setFacing(Facing.BACK);
                }
                break;
        }

        /*if (!checkCameraFront(context) || !checkCameraRear()) {
            viewBinding.switchCamera.setVisibility(View.GONE);
            if (!checkCameraFront(context)) {
                viewBinding.camera.setFacing(Facing.BACK);
            } else if (!checkCameraRear()) {
                viewBinding.camera.setFacing(Facing.FRONT);
            }
        }*/

        setSelectedImageList();
        resize();
        click();

    }

    private void click() {

        viewBinding.layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
                onClickEvent(v);
            }
        });

        viewBinding.switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Facing_MODE == 0)
                    if (viewBinding.camera.getFacing() == Facing.BACK) {
                        viewBinding.camera.setFacing(Facing.FRONT);
                    } else
                        viewBinding.camera.setFacing(Facing.BACK);

                onClickEvent(v);
            }
        });

        viewBinding.capturePictureSnapshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSlot_AreFull()) {
                    displayToast(context, getResources().getString(R.string.all_image_selected));
                    return;
                } else if (!TextUtils.isEmpty(list_new_pic_selected.get(selectedPosition).getfPath())) {
                    displayToast(context, getResources().getString(R.string.str_all_selected_images));
                    return;
                }
                viewBinding.camera.takePictureSnapshot();
                onClickEvent(v);
            }
        });

        viewBinding.layDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_new_pic_selected != null && list_new_pic_selected.size() != 0) {

                    ArrayList<ImageModel> picList1 = new ArrayList<>();
                    for (int i = 0; i < list_new_pic_selected.size(); i++) {
                        if (!TextUtils.isEmpty(list_new_pic_selected.get(i).getfPath())) {
                            picList1.add(list_new_pic_selected.get(i));
                        }
                    }

                    if (picList1 == null || picList1.size() <= 0) {
                        displayToast(context, getResources().getString(R.string.capture_image_first));
                        return;
                    }

//                    Bundle bundle = new Bundle();
//                    bundle.putString(ARG_PARAM1, new Gson().toJson(picList1));

                 /*   startActivity(new Intent(context, CompressionResizerActivity.class)
                            .putExtra(ARG_PARAM1, new Gson().toJson(picList1)));*/

                    someActivityResultLauncher.launch(new Intent(context, CompressionResizerActivity.class)
                            .putExtra(ARG_PARAM1, new Gson().toJson(picList1))
                            .putExtra(Constant.COMPRESS_STATUS, compressStatus)
                            .putExtra(Constant.COMPRESSION_SIZE_Type, compression_Type)
                            .putExtra(Constant.COMPRESSION_SIZE, compressionSize)
                    );

                }
                onClickEvent(v);
            }
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        ArrayList<String> listSaved = result.getData().getStringArrayListExtra("selectedImages");
                        setResult(RESULT_OK, new Intent().putStringArrayListExtra("selectedImages", listSaved));
                        finish();
                    } else if (result.getResultCode() == Activity.RESULT_FIRST_USER) {

                    }

                }
            });

    private void resize() {
        HelperResizer.getInstance(context);
        HelperResizer.setSize(viewBinding.btnBack, 70, 70, true);
        HelperResizer.setSize(viewBinding.btnDone, 80, 80, true);


        HelperResizer.setHeight(context, viewBinding.innerLay, 250);
        HelperResizer.setHeight(context, viewBinding.actionBar, 150);
        HelperResizer.setHeight(context, viewBinding.layText, 120);


    }

    private void setupCamera() {
        //camera
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE);
        viewBinding.camera.addCameraListener(new Listener());

    }


    private void setSelectedImageList() {

        viewBinding.textView.setText("You can take upto" + ImgCount + " images");
        list_new_pic_selected = new ArrayList<>();
        for (int i = 0; i < ImgCount; i++) {
            list_new_pic_selected.add(new ImageModel());
        }
        imageSelectedAdapter = new SelectedImageAdapter_Native(CameraActivity.this, list_new_pic_selected, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        viewBinding.rvSelectedImageList.setLayoutManager(layoutManager);
        viewBinding.rvSelectedImageList.setAdapter(imageSelectedAdapter);
        viewBinding.rvSelectedImageList.scrollToPosition(selectedPosition);
        viewBinding.rvSelectedImageList.setScrollX(viewBinding.rvSelectedImageList.computeHorizontalScrollOffset());
        setBottomText();
    }

    private void setBottomText() {
        int count = 0;
        if (list_new_pic_selected != null && list_new_pic_selected.size() != 0)
            for (int i = 0; i < list_new_pic_selected.size(); i++) {
                if (list_new_pic_selected.get(i).getfPath() != null && list_new_pic_selected.get(i).getfPath().length() != 0) {
                    count++;
                }
            }

        viewBinding.tv1.setText("Please Choose 1-" + list_new_pic_selected.size() + " Photos");
        viewBinding.tv2.setText("(" + count + "/" + ImgCount + ")");
        if (count != 0) {
            viewBinding.btnDone.setSelected(true);
        } else {
            viewBinding.btnDone.setSelected(false);
        }
    }

    public boolean checkSlot_AreFull() {
        if (list_new_pic_selected == null || list_new_pic_selected.size() == 0)
            return false;
        int count = 0;
        for (ImageModel imageModel : list_new_pic_selected) {
            if (imageModel != null && !TextUtils.isEmpty(imageModel.getfPath())) {
                count++;
            }
        }
        if (count == 0 || count < list_new_pic_selected.size()) {
            return false;
        } else
            return true;
    }

    @Override
    public void onRemove_From_Selected_list(ImageModel imageModel, int position) {
        if (!TextUtils.isEmpty(imageModel.getfPath()) && new File(imageModel.getfPath()).exists()) {
            try {
                org.apache.commons.io.FileUtils.forceDeleteOnExit(new File(imageModel.getfPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        list_new_pic_selected.set(position, new ImageModel());
        imageSelectedAdapter.notifyItemChanged(position);
        setBottomText();
    }

    class Listener extends CameraListener {

        @Override
        public void onPictureTaken(@NonNull @NotNull PictureResult result) {
            super.onPictureTaken(result);


            // If planning to save a file on a background thread,
            // just use toFile. Ensure you have permissions.

            File file = new File(getFilesDir(), "images");
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(file, getPicNameByPosition(selectedPosition));

            result.toFile(file, new FileCallback() {
                @Override
                public void onFileReady(@Nullable @org.jetbrains.annotations.Nullable File file) {

                    ImageModel iteam = new ImageModel(file.getName(), file.getAbsolutePath(), false);
                    if (list_new_pic_selected == null)
                        list_new_pic_selected = new ArrayList<>();

                    list_new_pic_selected.set(selectedPosition, iteam);


                    imageSelectedAdapter.notifyListChange(list_new_pic_selected, selectedPosition - 1);

                    if (!checkSlot_AreFull())
                        selectedPosition = getEmptySlotPosition(list_new_pic_selected);

                    imageSelectedAdapter.notifyDataSetChanged();
                    setBottomText();

                    viewBinding.rvSelectedImageList.smoothScrollToPosition(selectedPosition);

                }
            });
        }

        private int getEmptySlotPosition(ArrayList<ImageModel> selected_originalImageList) {
            for (int i = 0; i < selected_originalImageList.size(); i++) {
                ImageModel imageModel = selected_originalImageList.get(i);
                if (imageModel != null && TextUtils.isEmpty(imageModel.getfPath())) {
                    return i;
                }
            }
            return 0;
        }

        public boolean checkSlot_AreFull() {
            if (list_new_pic_selected == null || list_new_pic_selected.size() == 0)
                return false;
            int count = 0;
            for (ImageModel imageModel : list_new_pic_selected) {
                if (imageModel != null && !TextUtils.isEmpty(imageModel.getfPath())) {
                    count++;
                }
            }

            if (count == 0 || count < list_new_pic_selected.size()) {
                return false;
            } else
                return true;
        }

        @Override
        public void onPictureShutter() {
            super.onPictureShutter();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewBinding.camera.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewBinding.camera.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewBinding.camera.destroy();
    }


}