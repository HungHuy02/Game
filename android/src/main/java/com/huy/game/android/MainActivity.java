package com.huy.game.android;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huy.game.R;
import com.huy.game.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PlayFragment playFragment = new PlayFragment();
    private RankFragment rankFragment = new RankFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.item_1) {
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, playFragment)
                    .commit();
                return true;
            } else if (itemId == R.id.item_2) {
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, rankFragment)
                    .commit();
                return true;
            } else {
                return false;
            }
        });

        binding.bottomNavigation.setSelectedItemId(R.id.item_1);
    }
}
