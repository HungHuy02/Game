package com.huy.game.android.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.huy.game.R;
import com.huy.game.android.base.BaseActivity;
import com.huy.game.android.globalstate.UserState;
import com.huy.game.android.utils.Constants;
import com.huy.game.android.utils.StorageUtils;
import com.huy.game.android.viewmodel.PlayWithAISetupViewModel;
import com.huy.game.chess.enums.ChessMode;
import com.huy.game.chess.enums.Difficulty;
import com.huy.game.chess.enums.PieceColor;
import com.huy.game.chess.enums.TimeType;
import com.huy.game.databinding.ActivityPlayWithAiSetUpBinding;

import java.util.Optional;
import java.util.Random;

public class PlayWithAISetupActivity extends BaseActivity implements View.OnClickListener {

    private PlayWithAISetupViewModel viewModel;
    private ActivityPlayWithAiSetUpBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayWithAiSetUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupViewModel();
        setupLevel();
        setupLevelButtons();
        setupButtons();
        backButton();
        controlTimeButton();
        suggestButton();
        takebackButton();
        playButton();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(PlayWithAISetupViewModel.class);

        viewModel.getPositionColor().observe(this, position -> {
            clearButtonsBorder(binding);
            switch (position) {
                case 1:
                    binding.btnWhite.setBackground(AppCompatResources.getDrawable(this ,R.drawable.button_stroke));
                    break;
                case 2:
                    binding.btnWhiteBlack.setBackground(AppCompatResources.getDrawable(this ,R.drawable.button_stroke));
                    break;
                case 3:
                    binding.btnBlack.setBackground(AppCompatResources.getDrawable(this ,R.drawable.button_stroke));
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
        int position = storageUtils.getIntValue(Constants.DATASTORE_POSITION_AI);
        viewModel.setPosition(position);
        viewModel.showButtons().observe(this, showButtons -> binding.btns.setVisibility(showButtons ? View.VISIBLE : View.GONE));
    }

    private void setupLevel() {
        viewModel.getLevel().observe(this, level -> {
            switch (level) {
                case 1 -> changeColor(binding.btnLvOne);
                case 2 -> changeColor(binding.btnLvTwo);
                case 3 -> changeColor(binding.btnLvThree);
                case 4 -> changeColor(binding.btnLvFour);
                case 5 -> changeColor(binding.btnLvFive);
                case 6 -> changeColor(binding.btnLvSix);
                case 7 -> changeColor(binding.btnLvSeven);
                case 8 -> changeColor(binding.btnLvEight);
            }
        });
        int level = StorageUtils.getInstance(this).getIntValue(Constants.DATASTORE_AI_LEVEL);
        viewModel.setLevel(level == -1 ? 1 : level);
    }

    private void changeColor(MaterialTextView tv) {
        binding.btnLvOne.setBackgroundTintList(getResources().getColorStateList(R.color.black, getTheme()));
        binding.btnLvTwo.setBackgroundTintList(getResources().getColorStateList(R.color.black, getTheme()));
        binding.btnLvThree.setBackgroundTintList(getResources().getColorStateList(R.color.black, getTheme()));
        binding.btnLvFour.setBackgroundTintList(getResources().getColorStateList(R.color.black, getTheme()));
        binding.btnLvFive.setBackgroundTintList(getResources().getColorStateList(R.color.black, getTheme()));
        binding.btnLvSix.setBackgroundTintList(getResources().getColorStateList(R.color.black, getTheme()));
        binding.btnLvSeven.setBackgroundTintList(getResources().getColorStateList(R.color.black, getTheme()));
        binding.btnLvEight.setBackgroundTintList(getResources().getColorStateList(R.color.black, getTheme()));
        tv.setBackgroundTintList(getResources().getColorStateList(R.color.light_green_700, getTheme()));
    }

    private void setupLevelButtons() {
        binding.btnLvOne.setOnClickListener(levelButtonClickListener);
        binding.btnLvTwo.setOnClickListener(levelButtonClickListener);
        binding.btnLvThree.setOnClickListener(levelButtonClickListener);
        binding.btnLvFour.setOnClickListener(levelButtonClickListener);
        binding.btnLvFive.setOnClickListener(levelButtonClickListener);
        binding.btnLvSix.setOnClickListener(levelButtonClickListener);
        binding.btnLvSeven.setOnClickListener(levelButtonClickListener);
        binding.btnLvEight.setOnClickListener(levelButtonClickListener);
    }

    private final View.OnClickListener levelButtonClickListener = (v) -> {
        StorageUtils utils = StorageUtils.getInstance(v.getContext());
        int id = v.getId();
        if (id == R.id.btn_lv_one) {
            handleChangeLevel(utils, 1);
        }else if (id == R.id.btn_lv_two) {
            handleChangeLevel(utils, 2);
        }else if (id == R.id.btn_lv_three) {
            handleChangeLevel(utils, 3);
        }else if (id == R.id.btn_lv_four) {
            handleChangeLevel(utils, 4);
        }else if (id == R.id.btn_lv_five) {
            handleChangeLevel(utils, 5);
        }else if (id == R.id.btn_lv_six) {
            handleChangeLevel(utils, 6);
        }else if (id == R.id.btn_lv_seven) {
            handleChangeLevel(utils, 7);
        }else {
            handleChangeLevel(utils, 8);
        }
    };

    private void handleChangeLevel(StorageUtils utils, int level) {
        utils.setIntValue(Constants.DATASTORE_AI_LEVEL, level);
        viewModel.setLevel(level);
    }

    private void setupButtons() {
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
    }

    private void backButton() {
        binding.backBtn.setOnClickListener((v) -> finish());
    }

    private void controlTimeButton() {
        binding.btnControlTime.setOnClickListener((v) -> viewModel.setShowButtons(Boolean.FALSE.equals(viewModel.showButtons().getValue())));
    }

    private void suggestButton() {
        binding.btnSuggest.setOnClickListener((v) -> binding.switchEnableSuggest.setChecked(!binding.switchEnableSuggest.isChecked()));
    }

    private void takebackButton() {
        binding.btnTackback.setOnClickListener((v) -> binding.switchEnableTackback.setChecked(!binding.switchEnableTackback.isChecked()));
    }

    private void playButton() {
        binding.btnPlay.setOnClickListener((v) -> {
            Intent intent = new Intent(this, AndroidLauncher.class);
            ChessMode chessMode = ChessMode.AI;
            chessMode.setDifficulty(Difficulty.HARD);
            intent.putExtra(Constants.BUNDLE_MODE, chessMode.toString());
            int positionTime = Optional.ofNullable(viewModel.getPosition().getValue()).orElse(0);
            TimeType type = switch (positionTime) {
                case 1 -> TimeType.ONE_MINUTE;
                case 2 -> TimeType.THREE_MINUTE;
                case 3 -> TimeType.FIVE_MINUTE;
                case 4 -> TimeType.TWO_MINUTE_PLUS_ONE;
                case 5 -> TimeType.THREE_MINUTE_PLUS_TWO;
                case 6 -> TimeType.FIVE_MINUTE_PLUS_FIVE;
                case 7 -> TimeType.TEN_MINUTE;
                case 8 -> TimeType.FIFTEEN_MINUTE_PLUS_TEN;
                case 9 -> TimeType.THIRTY_MINUTE;
                default -> TimeType.NO_TIME;
            };
            intent.putExtra(Constants.BUNDLE_TIME, type.toString());
            int positionColor = Optional.ofNullable(viewModel.getPositionColor().getValue()).orElse(1);
            PieceColor pieceColor = switch (positionColor) {
                case 1 -> PieceColor.WHITE;
                case 2 -> {
                    Random random = new Random();
                    int color = random.nextInt(2) + 1;
                    if(color == 1) {
                        yield PieceColor.WHITE;
                    }else {
                        yield PieceColor.BLACK;
                    }
                }
                default -> PieceColor.BLACK;
            };
            int level = Optional.ofNullable(viewModel.getLevel().getValue()).orElse(1);
            intent.putExtra(Constants.BUNDLE_PLAYER1_COLOR, pieceColor.toString());
            intent.putExtra(Constants.BUNDLE_PLAYER1_NAME, UserState.getInstance().getName());
            intent.putExtra(Constants.BUNDLE_PLAYER2_NAME, String.format(getResources().getString(R.string.stockfish_level_text), level));
            intent.putExtra(Constants.BUNDLE_AI_LEVEL, level);
            intent.putExtra(Constants.BUNDLE_ENABLE_SUGGEST, binding.switchEnableSuggest.isChecked());
            intent.putExtra(Constants.BUNDLE_ENABLE_TAKEBACK, binding.switchEnableTackback.isChecked());
            startActivity(intent);
            finish();
        });
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
        utils.setIntValue(Constants.DATASTORE_POSITION_AI, position);
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
        button.setBackground(AppCompatResources.getDrawable(this ,R.drawable.transparent_button));
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
