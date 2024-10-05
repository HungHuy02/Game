package com.huy.game.android.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.huy.game.R;
import com.huy.game.android.base.BaseActivity;
import com.huy.game.android.utils.Constants;
import com.huy.game.android.utils.StorageUtils;
import com.huy.game.databinding.ActivityChangeTimeBinding;

public class ChangeTimeActivity extends BaseActivity implements View.OnClickListener {

    private ActivityChangeTimeBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeTimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        backButton();
        setupButtons();
    }

    private void setupButtons() {
        int position = getIntent().getIntExtra(Constants.BUNDLE_POSITION, 7);
        switch (position) {
            case 1:
                updateButton(binding.btn1m);
                break;
            case 2:
                updateButton(binding.btn1mp1);
                break;
            case 3:
                updateButton(binding.btn2mp1);
                break;
            case 4:
                updateButton(binding.btn3m);
                break;
            case 5:
                updateButton(binding.btn3mp2);
                break;
            case 6:
                updateButton(binding.btn5m);
                break;
            case 7:
                updateButton(binding.btn10m);
                break;
            case 8:
                updateButton(binding.btn15mp10);
                break;
            case 9:
                updateButton(binding.btn30m);
                break;
        }
        binding.btn1m.setOnClickListener(this);
        binding.btn1mp1.setOnClickListener(this);
        binding.btn2mp1.setOnClickListener(this);
        binding.btn3m.setOnClickListener(this);
        binding.btn3mp2.setOnClickListener(this);
        binding.btn5m.setOnClickListener(this);
        binding.btn10m.setOnClickListener(this);
        binding.btn15mp10.setOnClickListener(this);
        binding.btn30m.setOnClickListener(this);
    }

    private void backButton() {
        binding.backBtn.setOnClickListener((v) -> finish());
    }

    @Override
    public void onClick(View view) {
        StorageUtils storageUtils = StorageUtils.getInstance(view.getContext());
        Intent resultIntent = new Intent();
        int id = view.getId();
        if(id == R.id.btn_1m) {
            handleResult(resultIntent, storageUtils, 1);
        }else if(id == R.id.btn_1mp1) {
            handleResult(resultIntent, storageUtils, 2);
        }else if(id == R.id.btn_2mp1) {
            handleResult(resultIntent, storageUtils, 3);
        }else if(id == R.id.btn_3m){
            handleResult(resultIntent, storageUtils, 4);
        }else if(id == R.id.btn_3mp2) {
            handleResult(resultIntent, storageUtils, 5);
        }else if(id == R.id.btn_5m) {
            handleResult(resultIntent, storageUtils, 6);
        }else if(id == R.id.btn_10m) {
            handleResult(resultIntent, storageUtils, 7);
        }else if(id == R.id.btn_15mp10) {
            handleResult(resultIntent, storageUtils, 8);
        }else if(id == R.id.btn_30m) {
            handleResult(resultIntent, storageUtils, 9);
        }
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void updateButton(MaterialButton button) {
        int strokeWidth = 3;
        ColorStateList strokeColor = getResources().getColorStateList(R.color.light_green_700, getTheme());
        ColorStateList buttonColor = getResources().getColorStateList(R.color.grey_700, getTheme());
        button.setStrokeWidth(strokeWidth);
        button.setStrokeColor(strokeColor);
        button.setBackgroundTintList(buttonColor);
    }

    private void handleResult(Intent resultIntent, StorageUtils storageUtils, int position) {
        resultIntent.putExtra(Constants.BUNDLE_POSITION, position);
        storageUtils.setIntValue(Constants.DATASTORE_POSITION, position);
    }
}


