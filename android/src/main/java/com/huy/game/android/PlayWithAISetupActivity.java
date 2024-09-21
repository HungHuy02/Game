package com.huy.game.android;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.huy.game.R;
import com.huy.game.android.utils.StorageUtils;
import com.huy.game.android.viewmodel.PlayWithAISetupViewModel;
import com.huy.game.databinding.ActivityPlayWithAiSetUpBinding;

public class PlayWithAISetupActivity extends AppCompatActivity implements View.OnClickListener {

    private PlayWithAISetupViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPlayWithAiSetUpBinding binding = ActivityPlayWithAiSetUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(PlayWithAISetupViewModel.class);

        viewModel.getPositionColor().observe(this, position -> {
            clearButtonsBorder(binding);
            switch (position) {
                case 1:
                    binding.btnWhite.setBackground(getDrawable(R.drawable.button_stroke));
                    break;
                case 2:
                    binding.btnWhiteBlack.setBackground(getDrawable(R.drawable.button_stroke));
                    break;
                case 3:
                    binding.btnBlack.setBackground(getDrawable(R.drawable.button_stroke));
                    break;
            }
        });

        viewModel.getPosition().observe(this, position -> {
            clearButtonsStroke(binding);
            switch (position) {
                case 1:
                    updateButton(binding.btn1m);
                    binding.tvTime.setText(getText(R.string.one_minute_text));
                    break;
                case 2:
                    updateButton(binding.btn3m);
                    binding.tvTime.setText(getText(R.string.three_minute_text));
                    break;
                case 3:
                    updateButton(binding.btn5m);
                    binding.tvTime.setText(getText(R.string.five_minute_text));
                    break;
                case 4:
                    updateButton(binding.btn2mp1);
                    binding.tvTime.setText(getText(R.string.two_minute_plus_one_text));
                    break;
                case 5:
                    updateButton(binding.btn3mp2);
                    binding.tvTime.setText(getText(R.string.three_minute_plus_two_text));
                    break;
                case 6:
                    updateButton(binding.btn5mp5);
                    binding.tvTime.setText(getText(R.string.five_minute_plus_five_text));
                    break;
                case 7:
                    updateButton(binding.btn10m);
                    binding.tvTime.setText(getText(R.string.ten_minute_text));
                    break;
                case 8:
                    updateButton(binding.btn15mp10);
                    binding.tvTime.setText(getText(R.string.fifteen_minute_plus_ten));
                    break;
                case 9:
                    updateButton(binding.btn30m);
                    binding.tvTime.setText(getText(R.string.thirty_minute));
                    break;
                case 10:
                    updateButton(binding.btnNone);
                    binding.tvTime.setText(getText(R.string.none_text));
                    break;
            }
        });

        StorageUtils storageUtils = StorageUtils.getInstance(this);
        int position = storageUtils.getIntValue("position-ai");
        viewModel.setPosition(position);

        binding.btnNone.setOnClickListener(this);
        binding.btn1m.setOnClickListener(this);
        binding.btn3m.setOnClickListener(this);
        binding.btn5m.setOnClickListener(this);
        binding.btn2mp1.setOnClickListener(this);
        binding.btn3mp2.setOnClickListener(this);
        binding.btn5mp5.setOnClickListener(this);
        binding.btn10m.setOnClickListener(this);
        binding.btn15mp10.setOnClickListener(this);
        binding.btn30m.setOnClickListener(this);
        binding.btnBlack.setOnClickListener((v) -> viewModel.setPositionColor(3));
        binding.btnWhite.setOnClickListener((v) -> viewModel.setPositionColor(1));
        binding.btnWhiteBlack.setOnClickListener((v) -> viewModel.setPositionColor(2));

        viewModel.showButtons().observe(this, showButtons -> binding.btns.setVisibility(showButtons ? View.VISIBLE : View.GONE));

        binding.backBtn.setOnClickListener((v) -> finish());

        binding.btnControlTime.setOnClickListener((v) -> viewModel.setShowButtons(Boolean.FALSE.equals(viewModel.showButtons().getValue())));

    }

    @Override
    public void onClick(View view) {
        StorageUtils storageUtils = StorageUtils.getInstance(this);
        int id = view.getId();
        updateButton((MaterialButton) view);
        if(id == R.id.btn_none) {
            handleData(storageUtils, 10);
        }else if(id == R.id.btn_1m) {
            handleData(storageUtils, 1);
        }else if(id == R.id.btn_3m) {
            handleData(storageUtils, 2);
        }else if(id == R.id.btn_5m){
            handleData(storageUtils, 3);
        }else if(id == R.id.btn_2mp1) {
            handleData(storageUtils, 4);
        }else if(id == R.id.btn_3mp2) {
            handleData(storageUtils, 5);
        }else if(id == R.id.btn_5mp5) {
            handleData(storageUtils, 6);
        }else if(id == R.id.btn_10m) {
            handleData(storageUtils, 7);
        }else if(id == R.id.btn_15mp10) {
            handleData(storageUtils, 8);
        }else if(id == R.id.btn_30m) {
            handleData(storageUtils, 9);
        }
    }

    private void handleData(StorageUtils utils ,int position) {
        utils.setIntValue("position-ai", position);
        viewModel.setPosition(position);
    }

    private void updateButton(MaterialButton button) {
        int strokeWidth = 3;
        ColorStateList strokeColor = getResources().getColorStateList(R.color.light_green_700, getTheme());
        ColorStateList buttonColor = getResources().getColorStateList(R.color.grey_700, getTheme());
        button.setStrokeWidth(strokeWidth);
        button.setStrokeColor(strokeColor);
        button.setBackgroundTintList(buttonColor);
    }

    private void clearButtonsBorder(ActivityPlayWithAiSetUpBinding binding) {
        clearButtonBorder(binding.btnBlack);
        clearButtonBorder(binding.btnWhite);
        clearButtonBorder(binding.btnWhiteBlack);
    }

    private void clearButtonBorder(ImageButton button) {
        button.setBackground(getDrawable(R.drawable.transparent_button));
    }

    private void clearButtonsStroke(ActivityPlayWithAiSetUpBinding binding) {
        clearButtonStroke(binding.btnNone);
        clearButtonStroke(binding.btn1m);
        clearButtonStroke(binding.btn3m);
        clearButtonStroke(binding.btn5m);
        clearButtonStroke(binding.btn2mp1);
        clearButtonStroke(binding.btn3mp2);
        clearButtonStroke(binding.btn5mp5);
        clearButtonStroke(binding.btn10m);
        clearButtonStroke(binding.btn15mp10);
        clearButtonStroke(binding.btn30m);
    }

    private void clearButtonStroke(MaterialButton button) {
        button.setStrokeWidth(0);
        button.setBackgroundTintList(getResources().getColorStateList(R.color.grey_800, getTheme()));
    }
}
