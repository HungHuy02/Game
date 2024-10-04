package com.huy.game.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.huy.game.R;
import com.huy.game.android.base.BaseActivity;
import com.huy.game.android.globalstate.UserState;
import com.huy.game.android.utils.Constants;
import com.huy.game.android.utils.StorageUtils;
import com.huy.game.databinding.ActivitySettingBinding;
import com.huy.game.databinding.BottomSheetLayoutBinding;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySettingBinding binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.languageImage.setImageResource(UserState.getInstance().getLanguage().equals("vi") ? R.drawable.icons8_vietnam_48 : R.drawable.icons8_usa_48);

        binding.btnChangeLanguage.setOnClickListener((v) -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SettingActivity.this);
            BottomSheetLayoutBinding bottomSheetLayoutBinding = BottomSheetLayoutBinding.inflate(getLayoutInflater());
            bottomSheetDialog.setContentView(bottomSheetLayoutBinding.getRoot());
            bottomSheetDialog.show();

            View.OnClickListener listener = view -> {
                String language;
                if (view == bottomSheetLayoutBinding.btnVn) {
                    language = "vi";
                }else {
                    language = "en";
                }
                if(!language.equals(UserState.getInstance().getLanguage())) {
                    UserState.getInstance().setLanguage(language);
                    StorageUtils.getInstance(view.getContext()).setStringValue(Constants.DATASTORE_LANGUAGE, language);
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            };

            bottomSheetLayoutBinding.btnVn.setOnClickListener(listener);
            bottomSheetLayoutBinding.btnEn.setOnClickListener(listener);
        });
    }
}
