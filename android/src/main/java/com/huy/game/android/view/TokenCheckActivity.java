package com.huy.game.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.huy.game.android.base.BaseActivity;
import com.huy.game.android.globalstate.UserState;
import com.huy.game.android.models.User;
import com.huy.game.android.utils.Constants;
import com.huy.game.android.utils.NetworkWatcher;
import com.huy.game.android.utils.StorageUtils;
import com.huy.game.android.viewmodel.apiservice.UserServiceViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenCheckActivity extends BaseActivity {

    private UserServiceViewModel viewModel;
    private NetworkWatcher networkWatcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
        checkGuestAccount();
        checkNetwork();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(UserServiceViewModel.class);
    }

    private void checkGuestAccount() {
        if(StorageUtils.getInstance(this).getStringValue(Constants.DATASTORE_ACCESS_TOKEN).equals("null")) {
            UserState.getInstance().setGuestAccount();
            toMainActivity();
        }
    }

    private void checkNetwork() {
        networkWatcher = new NetworkWatcher(this);
        networkWatcher.observe(this, on -> {
            if (on) {
                if (UserState.getInstance().getEmail() == null && !UserState.getInstance().isGuest()) {
                    getUser();
                }
            }else {
                toMainActivity();
            }
        });
    }

    private void getUser() {
        viewModel.getCurrentUser(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if(response.isSuccessful()) {
                    User user = response.body();
                    assert user != null;
                    UserState.getInstance().setName(user.getName());
                    UserState.getInstance().setEmail(user.getEmail());
                    if (user.getImageUrl() != null) {
                        UserState.getInstance().setImageUrl(user.getImageUrl());
                    }
                    UserState.getInstance().setElo(user.getElo());
                    toMainActivity();
                }else if(response.code() != 401){
                    Toast.makeText(TokenCheckActivity.this, "Lỗi không xác định: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable throwable) {
                Toast.makeText(TokenCheckActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                UserState.getInstance().setGuestAccount();
                toMainActivity();
            }
        });
    }

    private void toMainActivity() {
        Intent intent = new Intent(TokenCheckActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
