package com.huy.game.android.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.huy.game.R;
import com.huy.game.android.globalstate.UserState;
import com.huy.game.android.models.response.ScalarBooleanResponse;
import com.huy.game.android.utils.Constants;
import com.huy.game.android.utils.StorageUtils;
import com.huy.game.android.viewmodel.PlayFragmentViewModel;
import com.huy.game.android.viewmodel.apiservice.UserServiceViewModel;
import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.enums.TimeType;
import com.huy.game.databinding.AccountMenuLayoutBinding;
import com.huy.game.databinding.FragmentPlayBinding;

import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayFragment extends Fragment {

    private FragmentPlayBinding fragmentPlayBinding;
    private PlayFragmentViewModel viewModel;
    private ActivityResultLauncher<Intent> launcher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPlayBinding = FragmentPlayBinding.inflate(inflater, container, false);
        return fragmentPlayBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel(view);
        setupLauncher();
        timeButton();
        playTwoButton();
        playWithAIButton();
        newButton();
        userImage();
        settingButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupUserImage();
    }

    private void setupViewModel(View view) {
        viewModel = new ViewModelProvider(this).get(PlayFragmentViewModel.class);
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
    }

    private void setupLauncher() {
        launcher = registerForActivityResult(
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
    }

    private void timeButton() {
        StorageUtils storageUtils = StorageUtils.getInstance(getContext());
        int position = storageUtils.getIntValue(Constants.DATASTORE_POSITION);
        viewModel.setPosition(position);
        fragmentPlayBinding.btnTime.setOnClickListener((v) -> {
            Intent intent = new Intent(fragmentPlayBinding.getRoot().getContext(), ChangeTimeActivity.class);
            intent.putExtra(Constants.BUNDLE_POSITION, position == -1 ? 7 : position);
            launcher.launch(intent);
        });
    }

    private void playTwoButton() {
        fragmentPlayBinding.btnPlayTwo.setOnClickListener((v) -> {
            Intent intent = new Intent(this.getContext(), TwoPersonsPlaySetupActivity.class);
            startActivity(intent);
        });
    }

    private void playWithAIButton() {
        fragmentPlayBinding.btnPlayAi.setOnClickListener((v) -> {
            Intent intent = new Intent(this.getContext(), PlayWithAISetupActivity.class);
            startActivity(intent);
        });
    }

    private void newButton() {
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
            intent.putExtra(Constants.BUNDLE_PLAYER1_NAME, UserState.getInstance().getName());
            startActivity(intent);
        });
    }

    private void setupUserImage() {
        if(UserState.getInstance().getImageUrl() != null) {
            Glide.with(this)
                .load(UserState.getInstance().getImageUrl())
                .into(fragmentPlayBinding.userImage);
        }
    }

    private void userImage() {
        setupUserImage();
        fragmentPlayBinding.userImage.setOnClickListener((v) -> {
            PopupWindow popupWindow = new PopupWindow(v.getContext());
            AccountMenuLayoutBinding binding = AccountMenuLayoutBinding.inflate(getLayoutInflater());
            binding.tvName.setText(UserState.getInstance().getName());
            binding.btnAccount.setOnClickListener((V) -> {
                popupWindow.dismiss();
                Intent intent = new Intent(V.getContext(), ProfileActivity.class);
                startActivity(intent);
            });
            binding.btnLogout.setOnClickListener((V) -> {
                UserServiceViewModel userServiceViewModel = new ViewModelProvider(PlayFragment.this).get(UserServiceViewModel.class);
                userServiceViewModel.logout(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ScalarBooleanResponse> call, @NonNull Response<ScalarBooleanResponse> response) {
                        if(response.isSuccessful()) {
                            popupWindow.dismiss();
                            Toast.makeText(getContext(), "Đăng xuât thành công", Toast.LENGTH_SHORT).show();
                            StorageUtils.getInstance(V.getContext()).setStringValue(Constants.DATASTORE_ACCESS_TOKEN, "null");
                            Intent intent = new Intent(V.getContext(), LoginWayActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ScalarBooleanResponse> call, @NonNull Throwable throwable) {
                        Toast.makeText(V.getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
            popupWindow.setContentView(binding.getRoot());
            popupWindow.setOutsideTouchable(true);
            popupWindow.showAsDropDown(v, -30, 0);
            dimBehind(popupWindow);
        });
    }

    private void settingButton() {
        fragmentPlayBinding.settingBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(this.getContext(), SettingActivity.class);
            startActivity(intent);
        });
    }


    private void dimBehind(PopupWindow popupWindow) {
        View container = popupWindow.getContentView().getRootView();
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);
    }
}
