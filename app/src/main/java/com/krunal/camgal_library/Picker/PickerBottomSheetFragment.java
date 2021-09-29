package com.krunal.camgal_library.Picker;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.krunal.camgal_library.CameraActivity;
import com.krunal.camgal_library.CreationActivity;
import com.krunal.camgal_library.GalleryActivity;
import com.krunal.camgal_library.Intermediate.PickerListener;
import com.krunal.camgal_library.R;
import com.krunal.camgal_library.Utils.Constant;
import com.krunal.camgal_library.Utils.Utility;
import com.krunal.camgal_library.databinding.FragmentPickerBottomSheetBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PickerBottomSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PickerBottomSheetFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentPickerBottomSheetBinding viewBinding;
    PickerListener pickerListener;

    public PickerBottomSheetFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PickerBottomSheetFragment newInstance(String param1, String param2) {
        PickerBottomSheetFragment fragment = new PickerBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewBinding = FragmentPickerBottomSheetBinding.inflate(getLayoutInflater(), container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pickerListener = (PickerListener) getActivity();

        viewBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        viewBinding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        // do operation
                        openCamera();
                    } else {
                        // request Permission in Activity
//                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 300);
                        mPermissionResultCamera.launch(Manifest.permission.CAMERA);
                    }
                } else {
                    openCamera();
                }
            }
        });
        viewBinding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        // do operation
                        openGallery();
                    } else {
                        mPermissionResultGallery.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                } else {
                    openGallery();
                }
            }
        });
        viewBinding.creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), CreationActivity.class));
            }

        });
        viewBinding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private ActivityResultLauncher<String> mPermissionResultCamera = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), result -> {

                if (result.booleanValue()) {
                    openCamera();
                } else {
                    Utility.displayToast(getContext(), getResources().getString(R.string.permission_denied));

                }
            });
    private ActivityResultLauncher<String> mPermissionResultGallery = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), result -> {

                if (result.booleanValue()) {
                    openGallery();
                } else {
                    Utility.displayToast(getContext(), getResources().getString(R.string.permission_denied));
                }
            });


    private void openGallery() {
        resultLauncher.launch(new Intent(getActivity(), GalleryActivity.class)
                .putExtra(Constant.GALLERY_TYPE, 0)// 0-ALL,1-PNG,2-JPG,3-JPEG
                .putExtra(Constant.IMAGE_Selection_COUNT, 5)//
                .putExtra(Constant.COMPRESS_STATUS, true)// active or inactive
                .putExtra(Constant.COMPRESSION_SIZE, Constant.DEFAULT_COMRESSION_SIZE)// (int) long
                .putExtra(Constant.COMPRESSION_SIZE_Type, "MB")// KB,MB
        );
    }

    private void openCamera() {
        resultLauncher.launch(new Intent(getActivity(), CameraActivity.class)
                .putExtra(Constant.FACING_MODE, 0)// active or inactive
                .putExtra(Constant.IMAGE_Selection_COUNT, 5)// active or inactive
                .putExtra(Constant.COMPRESS_STATUS, true)// active or inactive
                .putExtra(Constant.COMPRESSION_SIZE, Constant.DEFAULT_COMRESSION_SIZE)//
                .putExtra(Constant.COMPRESSION_SIZE_Type, "MB")
        );
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == RESULT_OK) {
                        ArrayList<String> listSaved = result.getData().getStringArrayListExtra("selectedImages");
                        pickerListener.onResultPicker(result.getData(), listSaved);
                    } else if (result.getResultCode() == Activity.RESULT_FIRST_USER || result.getResultCode() == Activity.RESULT_CANCELED) {
                        pickerListener.onResultPicker(result.getData(), new ArrayList<>());
                        Utility.displayToast(getContext(), "No result");
                    }
                    dismiss();

                }
            });

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
//        getDialog().setCancelable(false);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCancel(@NonNull @NotNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                // do Operation
                openGallery();
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 300) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                // do Operation
                openCamera();
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}