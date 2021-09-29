package com.krunal.camgal_library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.krunal.camgal_library.Intermediate.PickerListener;
import com.krunal.camgal_library.Picker.PickerBottomSheetFragment;
import com.krunal.camgal_library.Utils.Utility;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PickerListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onClickNext(View view) {
        PickerBottomSheetFragment blankFragment = new PickerBottomSheetFragment();

        blankFragment.show(getSupportFragmentManager(), blankFragment.getTag());

    }

    @Override
    public void onResultPicker(Intent data, ArrayList<String> listSaved) {

        Utility.displayToast(this, "Success: " + listSaved.size());


    }
}