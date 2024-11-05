package com.huy.game.android.view;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.huy.game.R;
import com.huy.game.android.base.BaseActivity;
import com.huy.game.android.utils.NetworkWatcher;
import com.huy.game.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private NetworkWatcher networkWatcher;
    private int bottomId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkNetwork();
        handleBottomNavigation();
    }



    private void checkNetwork() {
         networkWatcher = new NetworkWatcher(this);
         networkWatcher.observe(this, on -> {
            if (on) {
                binding.bottomNavigation.setAlpha(1f);
            }else {
                binding.bottomNavigation.setAlpha(0.4f);
            }
        });
    }

    private void handleBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId != bottomId) {
                if (itemId == R.id.item_1) {
                    getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, new PlayFragment())
                        .commit();
                    bottomId = itemId;
                    return true;
                } else if (itemId == R.id.item_2) {
                    if (Boolean.TRUE.equals(networkWatcher.getValue())) {
                        getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new RankFragment())
                            .commit();
                        bottomId = itemId;
                        return true;
                    }
                    return false;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        });
        binding.bottomNavigation.setSelectedItemId(R.id.item_1);
        bottomId = R.id.item_1;
    }
}
