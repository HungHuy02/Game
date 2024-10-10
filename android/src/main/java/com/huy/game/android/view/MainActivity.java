package com.huy.game.android.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.huy.game.R;
import com.huy.game.android.base.BaseActivity;
import com.huy.game.android.globalstate.UserState;
import com.huy.game.android.models.User;
import com.huy.game.android.viewmodel.apiservice.UserServiceViewModel;
import com.huy.game.databinding.ActivityMainBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private UserServiceViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        handleBottomNavigation();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(UserServiceViewModel.class);
        viewModel.getCurrentUser(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    UserState.getInstance().setData(response.body().getName(), response.body().getEmail(), response.body().getImageUrl());
                }else if(response.code() != 401){
                    Toast.makeText(MainActivity.this, "Lỗi không xác định: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable throwable) {
                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.item_1) {
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, new PlayFragment())
                    .commit();
                return true;
            } else if (itemId == R.id.item_2) {
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, new RankFragment())
                    .commit();
                return true;
            } else {
                return false;
            }
        });

        binding.bottomNavigation.setSelectedItemId(R.id.item_1);
    }
}
