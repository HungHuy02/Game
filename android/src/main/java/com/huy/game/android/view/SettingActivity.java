package com.huy.game.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.huy.game.R;
import com.huy.game.android.base.BaseActivity;
import com.huy.game.android.utils.Constants;
import com.huy.game.android.utils.StorageUtils;
import com.huy.game.android.viewmodel.SettingActivityViewModel;
import com.huy.game.chess.manager.GameSetting;
import com.huy.game.databinding.ActivitySettingBinding;
import com.huy.game.databinding.BottomSheetLayoutBinding;

public class SettingActivity extends BaseActivity {

    private ActivitySettingBinding binding;
    private SettingActivityViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setupViewModel();
        setContentView(binding.getRoot());
        setupLanguageImage();
        setupSound();
        backButton();
        changeLanguageButton();
        changeSound();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(SettingActivityViewModel.class);
        viewModel.getMute().observe(this, mute -> {
            binding.tvSound.setText(getText(mute ? R.string.volume_up_text : R.string.volume_off_text));
            binding.soundImage.setImageResource(mute ? R.drawable.volume_up_24px : R.drawable.volume_off_24px);
        });
    }

    private void setupLanguageImage() {
        binding.languageImage.setImageResource(GameSetting.getInstance().getLanguage().equals("vi-VN") ? R.drawable.icons8_vietnam_48 : R.drawable.icons8_usa_48);
    }

    private void backButton() {
        binding.backBtn.setOnClickListener((v) -> finish());
    }

    private void changeLanguageButton() {
        binding.btnChangeLanguage.setOnClickListener((v) -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SettingActivity.this);
            BottomSheetLayoutBinding bottomSheetLayoutBinding = BottomSheetLayoutBinding.inflate(getLayoutInflater());
            bottomSheetDialog.setContentView(bottomSheetLayoutBinding.getRoot());
            bottomSheetDialog.show();

            View.OnClickListener listener = view -> {
                String language;
                if (view == bottomSheetLayoutBinding.btnVn) {
                    language = "vi-VN";
                }else {
                    language = "en";
                }
                if(!language.equals(GameSetting.getInstance().getLanguage())) {
                    GameSetting.getInstance().setLanguage(language);
                    StorageUtils.getInstance(view.getContext()).setStringValue(Constants.DATASTORE_LANGUAGE, language);
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            };

            bottomSheetLayoutBinding.btnVn.setOnClickListener(listener);
            bottomSheetLayoutBinding.btnEn.setOnClickListener(listener);
        });
    }

    private void setupSound() {
        viewModel.setMute(GameSetting.getInstance().isMute());
    }

    private void changeSound() {
        binding.btnSound.setOnClickListener((v) -> {
            GameSetting gameSetting = GameSetting.getInstance();
            gameSetting.setMute(!gameSetting.isMute());
            viewModel.setMute(gameSetting.isMute());
        });
    }
}
