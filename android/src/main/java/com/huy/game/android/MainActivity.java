package com.huy.game.android;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huy.game.R;
import com.huy.game.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.huy.game.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
