package com.huy.game.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.huy.game.R;
import com.huy.game.android.utils.StorageUtils;
import com.huy.game.android.viewmodel.PlayFragmentViewModel;
import com.huy.game.databinding.FragmentPlayBinding;

public class PlayFragment extends Fragment {

    private FragmentPlayBinding fragmentPlayBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPlayBinding = FragmentPlayBinding.inflate(inflater, container, false);
        return fragmentPlayBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PlayFragmentViewModel viewModel = new ViewModelProvider(this).get(PlayFragmentViewModel.class);
        Drawable bulletDrawable = AppCompatResources.getDrawable(view.getContext(), R.drawable.icons8_bullet_39);
        Drawable flashDrawable = AppCompatResources.getDrawable(view.getContext(), R.drawable.flash_on_24px);
        Drawable timerDrawable = AppCompatResources.getDrawable(view.getContext(), R.drawable.timer_24px);

        ColorStateList flashColor = getResources().getColorStateList(R.color.flash_color, requireActivity().getTheme());
        ColorStateList timerColor = getResources().getColorStateList(R.color.timer_color, requireActivity().getTheme());

        viewModel.getSelectedTime().observe(getViewLifecycleOwner(), timeSelected -> fragmentPlayBinding.btnTime.setText(timeSelected));

        viewModel.getSelectedIcon().observe(getViewLifecycleOwner(), iconSelected -> {
            Drawable drawable;
            ColorStateList tintColor = switch (iconSelected) {
                case "1" -> {
                    drawable = bulletDrawable;
                    yield flashColor;
                }
                case "2" -> {
                    drawable = flashDrawable;
                    yield flashColor;
                }
                default -> {
                    drawable = timerDrawable;
                    yield timerColor;
                }
            };

            fragmentPlayBinding.btnTime.setIcon(drawable);
            fragmentPlayBinding.btnTime.setIconTint(tintColor);
        });
        StorageUtils storageUtils = StorageUtils.getInstance(getContext());
        String time = storageUtils.getStringValue("time");
        String icon = storageUtils.getStringValue("icon");
        viewModel.setSelectedTime(time.equals("null") ? getString(R.string.ten_minute_text) : time);
        viewModel.setSelectedIcon(icon.equals("null") ? "3" : icon);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String timeSelected = data.getStringExtra("time");
                        String iconSelected = data.getStringExtra("icon");
                        viewModel.setSelectedTime(timeSelected);
                        viewModel.setSelectedIcon(iconSelected);
                    }
                }
            }
        );

        fragmentPlayBinding.btnTime.setOnClickListener((v) -> {
            Intent intent = new Intent(fragmentPlayBinding.getRoot().getContext(), ChangeTimeActivity.class);
            int position = storageUtils.getIntValue("position");
            intent.putExtra("position", position == -1 ? 7 : position);
            launcher.launch(intent);
        });

        fragmentPlayBinding.btnPlayTwo.setOnClickListener((v) -> {

        });

        fragmentPlayBinding.btnPlayAi.setOnClickListener((v) -> {
            Intent intent = new Intent(this.getContext(), AndroidLauncher.class);
            startActivity(intent);
        });

        fragmentPlayBinding.btnNew.setOnClickListener((v) -> {

        });
    }
}
