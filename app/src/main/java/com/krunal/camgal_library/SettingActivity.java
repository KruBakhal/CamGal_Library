package com.krunal.camgal_library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.krunal.camgal_library.Model.ImageModel;
import com.krunal.camgal_library.Utils.BookStore;
import com.krunal.camgal_library.Utils.HelperResizer;
import com.krunal.camgal_library.databinding.ActivitySettingBinding;

import java.util.ArrayList;

import static com.krunal.camgal_library.Utils.Constant.DEFAULT_COMRESSION_SIZE;
import static com.krunal.camgal_library.Utils.Utility.checkCameraFront;
import static com.krunal.camgal_library.Utils.Utility.checkCameraRear;
import static com.krunal.camgal_library.Utils.Utility.onClickEvent;

public class SettingActivity extends AppCompatActivity {
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private @NonNull
    ActivitySettingBinding viewBinding;
    String arry_image_count[];
    String arry_gallery_type[] = {"ALL", "PNG", "JPG", "JPEG"}; // 0-ALL,1-PNG,2-JPG,3-JPEG
    String arry_facing_mode[] = {"Both", "BACK", "FRONT"}; // 0-"Both", 1-"BACK", 2-"FRONT"
    String optionSizetypeList[] = {"KB", "MB"}; // 0-"Both", 1-"BACK", 2-"FRONT"
    private BookStore bookStore;
    private String width;
    private String height;
    private ArrayList<ImageModel> orginalImageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
        viewBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        bookStore = new BookStore(this);
        String asd[] = bookStore.get_RESOLUTION().split("%");
        width = asd[0];
        height = asd[1];
        viewBinding.edWidth.setText("" + width);
        viewBinding.edHeight.setText("" + height);


        resize();
        setupSpinnerImageCount();
        setupSpinnerGallertType();
        setupSpinnerFacingMode();
        setupSwitchCompress();
        setupAspectRatio();
        setupNoAspectRatio();
        setupoptionSizetype();

    }

    private void backToImageFrag() {
//        if (orginalImageList != null && orginalImageList.size() > 0) {
        setResult(RESULT_OK);
//        }
        finish();
    }

    private void setupNoAspectRatio() {
        viewBinding.edWidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                width = s.toString();
                if (!TextUtils.isEmpty(s.toString())) {
                    String sd = s.toString();
                    if (Integer.parseInt(sd) == 0) {
                        width = sd;
                        bookStore.set_SAVE_RESOLUTION(width + "%" + height);
                        return;
                    }
                }
                String smg = width + "%" + height;
                bookStore.set_SAVE_RESOLUTION(smg);


            }
        });
        viewBinding.edHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                height = s.toString();
                if (!TextUtils.isEmpty(s.toString())) {
                    String sd = s.toString();
                    if (Integer.parseInt(sd) == 0) {
                        height = sd;
                        bookStore.set_SAVE_RESOLUTION(width + "%" + height);
                        return;
                    }
                }
                String smg = width + "%" + height;
                bookStore.set_SAVE_RESOLUTION(smg);
            }
        });

    }

    private void setupSwitchCompress() {
        viewBinding.switchCompress.setChecked(bookStore.get_COMPRESS_STATUS());
        viewBinding.switchCompress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewBinding.switchCompress.setChecked(isChecked);
                bookStore.set_COMPRESS_STATUS(isChecked);
            }
        });
    }

    private void setupAspectRatio() {

        viewBinding.switchRatio.setChecked(bookStore.get_ASPECT_RATIO());
        viewBinding.seekBar.setProgress(bookStore.get_RESIZE_RATIO_Percentage());
        viewBinding.tvLabel.setText("Resize Image By Percentage:: " + bookStore.get_RESIZE_RATIO_Percentage() + "%");

        if (bookStore.get_ASPECT_RATIO()) {
            viewBinding.layResizePercentage.setVisibility(View.VISIBLE);
            viewBinding.layNoRatio.setVisibility(View.GONE);
        } else {
            viewBinding.layResizePercentage.setVisibility(View.GONE);
            viewBinding.layNoRatio.setVisibility(View.VISIBLE);
        }

        viewBinding.switchRatio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewBinding.switchRatio.setChecked(isChecked);
                bookStore.setASPECT_RATIO(isChecked);
                if (isChecked) {
                    viewBinding.layResizePercentage.setVisibility(View.VISIBLE);
                    viewBinding.layNoRatio.setVisibility(View.GONE);
                } else {
                    viewBinding.layResizePercentage.setVisibility(View.GONE);
                    viewBinding.layNoRatio.setVisibility(View.VISIBLE);
                }
            }
        });

        viewBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bookStore.set_RESIZE_RATIO_Percentage(progress);
                viewBinding.tvLabel.setText("Resize Image By Percentage:: " + progress + "%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void resize() {
        HelperResizer.getInstance(this);
        HelperResizer.setSize(viewBinding.btnBack, 70, 70, true);
        HelperResizer.setSize(viewBinding.btnDone, 80, 80, true);
        HelperResizer.setHeight(this, viewBinding.actionBar, 150);

        viewBinding.layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToImageFrag();
                onClickEvent(v);
            }
        });
    }

    private void setupoptionSizetype() {
        viewBinding.edRestrictSize.setText("" + bookStore.get_COMPRESSION_SIZE());


        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, optionSizetypeList);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewBinding.optionSizetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        bookStore.set_COMPRESSION_Type("KB");
                        break;
                    case 1:
                        bookStore.set_COMPRESSION_Type("MB");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewBinding.optionSizetype.setAdapter(ad);
        if (bookStore.get_COMPRESSION_Type().equals("KB")) {
            viewBinding.optionSizetype.setSelection(0);
        } else {
            viewBinding.optionSizetype.setSelection(optionSizetypeList.length - 1);
        }
        viewBinding.edRestrictSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String size = s.toString();
                if (!TextUtils.isEmpty(size)) {
                    String sd = s.toString();
                    if (Integer.parseInt(sd) != 0) {
                        bookStore.set_COMPRESSION_SIZE(DEFAULT_COMRESSION_SIZE);
                        return;
                    }
                }
//                bookStore.set_IMAGE_RESTRICTION(DEFAULT_Restrict_SIZE);
            }
        });
    }

    private void setupSpinnerFacingMode() {
        if (!checkCameraFront(this) || !checkCameraRear()) {
            viewBinding.layCamera.setVisibility(View.GONE);
        }

        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arry_facing_mode);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewBinding.spinnerFacingMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookStore.set_FACING_MODE(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewBinding.spinnerFacingMode.setAdapter(ad);
        viewBinding.spinnerFacingMode.setSelection(bookStore.get_FACING_MODE());
    }

    private void setupSpinnerImageCount() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            list.add(String.valueOf(i));
        }
        arry_image_count = new String[list.size()];
        for (int i = 0; i < 20; i++) {
            arry_image_count[i] = list.get(i);
        }
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arry_image_count);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewBinding.spinnerImgCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookStore.set_Image_Selection_Count(Integer.parseInt(arry_image_count[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewBinding.spinnerImgCount.setAdapter(ad);
        viewBinding.spinnerImgCount.setSelection(bookStore.get_Image_Selection_Count());
    }

    private void setupSpinnerGallertType() {
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arry_gallery_type);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewBinding.spinnerGalleryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookStore.set_GALLERY_TYPE(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewBinding.spinnerGalleryType.setAdapter(ad);
        viewBinding.spinnerGalleryType.setSelection(bookStore.get_GALLERY_TYPE());
    }


}