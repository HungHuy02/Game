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
import com.huy.game.android.utils.Constants;
import com.huy.game.android.utils.StorageUtils;
import com.huy.game.android.viewmodel.PlayFragmentViewModel;
import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.enums.TimeType;
import com.huy.game.databinding.FragmentPlayBinding;

import java.util.Optional;

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
                case 1 -> {
                    drawable = bulletDrawable;
                    yield flashColor;
                }
                case 2 -> {
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



        viewModel.getPosition().observe(getViewLifecycleOwner(), position -> {
            String time = getString(R.string.ten_minute_text);
            int icon = switch (position) {
                case 1 -> {
                    time = getString(R.string.one_minute_text);
                    yield 1;
                }
                case 2 -> {
                    time = getString(R.string.one_minute_plus_one_text);
                    yield 1;
                }
                case 3 -> {
                    time = getString(R.string.two_minute_plus_one_text);
                    yield 1;
                }
                case 4 -> {
                    time = getString(R.string.three_minute_text);
                    yield 2;
                }
                case 5 -> {
                    time = getString(R.string.three_minute_plus_two_text);
                    yield 2;
                }
                case 6 -> {
                    time = getString(R.string.five_minute_text);
                    yield 2;
                }
                case 7 -> {
                    time = getString(R.string.ten_minute_text);
                    yield 3;
                }
                case 8 -> {
                    time = getString(R.string.fifteen_minute_plus_ten);
                    yield 3;
                }
                case 9 -> {
                    time = getString(R.string.thirty_minute);
                    yield 3;
                }
                default -> 3;
            };
            viewModel.setSelectedTime(time);
            viewModel.setSelectedIcon(icon);
        });

        StorageUtils storageUtils = StorageUtils.getInstance(getContext());
        int position = storageUtils.getIntValue(Constants.DATASTORE_POSITION);
        viewModel.setPosition(position);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        viewModel.setPosition(data.getIntExtra(Constants.BUNDLE_POSITION, 3));
                    }
                }
            }
        );

        fragmentPlayBinding.btnTime.setOnClickListener((v) -> {
            Intent intent = new Intent(fragmentPlayBinding.getRoot().getContext(), ChangeTimeActivity.class);
            intent.putExtra(Constants.BUNDLE_POSITION, position == -1 ? 7 : position);
            launcher.launch(intent);
        });

        fragmentPlayBinding.btnPlayTwo.setOnClickListener((v) -> {
            Intent intent = new Intent(this.getContext(), TwoPersonsPlaySetupActivity.class);
            startActivity(intent);
        });

        fragmentPlayBinding.btnPlayAi.setOnClickListener((v) -> {
            Intent intent = new Intent(this.getContext(), PlayWithAISetupActivity.class);
            startActivity(intent);
        });

        fragmentPlayBinding.btnNew.setOnClickListener((v) -> {
            Intent intent = new Intent(this.getContext(), AndroidLauncher.class);
            intent.putExtra(Constants.BUNDLE_MODE, ChessMode.ONLINE.toString());
            int positionTime = Optional.ofNullable(viewModel.getPosition().getValue()).orElse(0);
            TimeType type = switch (positionTime) {
                case 1 -> TimeType.ONE_MINUTE;
                case 2 -> TimeType.ONE_MINUTE_PLUS_ONE;
                case 3 -> TimeType.TWO_MINUTE_PLUS_ONE;
                case 4 -> TimeType.THREE_MINUTE;
                case 5 -> TimeType.THREE_MINUTE_PLUS_TWO;
                case 6 -> TimeType.FIVE_MINUTE;
                case 7 -> TimeType.TEN_MINUTE;
                case 8 -> TimeType.FIFTEEN_MINUTE_PLUS_TEN;
                case 9 -> TimeType.THIRTY_MINUTE;
                default -> TimeType.NO_TIME;
            };
            intent.putExtra(Constants.BUNDLE_TIME, type.toString());
            intent.putExtra(Constants.BUNDLE_PLAYER1_NAME, "test");
            startActivity(intent);
        });
    }
}
