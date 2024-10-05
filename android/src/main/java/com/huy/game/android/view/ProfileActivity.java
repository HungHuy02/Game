package com.huy.game.android.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.huy.game.R;
import com.huy.game.android.base.BaseActivity;
import com.huy.game.android.globalstate.UserState;
import com.huy.game.android.models.User;
import com.huy.game.android.models.response.ScalarBooleanResponse;
import com.huy.game.android.network.cloudinary.Cloudinary;
import com.huy.game.android.viewmodel.ProfileActivityViewModel;
import com.huy.game.android.viewmodel.apiservice.UserServiceViewModel;
import com.huy.game.databinding.ActivityProfileBinding;

import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {

    private ActivityProfileBinding binding;
    private ProfileActivityViewModel profileActivityViewModel;
    private UserServiceViewModel viewModel;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupViewModel();
        setupLauncher();
        backButton();
        changeImage();
        setName();
        updateButton();
        deleteButton();
    }

    private void setupViewModel() {
        profileActivityViewModel = new ViewModelProvider(this).get(ProfileActivityViewModel.class);
        profileActivityViewModel.getImageUri().observe(this, uri -> {
            if(uri == null) {
                binding.image.setImageResource(R.drawable.resource_default);
            }else {
                binding.image.setImageURI(uri);
            }
        });
        viewModel = new ViewModelProvider(this).get(UserServiceViewModel.class);
    }

    private void setupLauncher() {
        launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        profileActivityViewModel.set_imageUri(data.getData());
                    }
                }
            }
        );
    }

    private void backButton() {
        binding.backBtn.setOnClickListener((v) -> finish());
    }

    private void changeImage() {
        binding.imageChange.setOnClickListener((v) -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            launcher.launch(Intent.createChooser(intent, "Select picture"));
        });
    }

    private void setName() {
        binding.nameTf.setText(UserState.getInstance().getName());
    }

    private void updateButton() {
        binding.btnUpdate.setOnClickListener((v) -> {
            User user = new User();
            boolean updateName = false;
            String name = Objects.requireNonNull(binding.nameTf.getText()).toString();
            if(!name.equals(UserState.getInstance().getName())) {
                user.setName(name);
                UserState.getInstance().setName(name);
                updateName = true;
            }
            if(profileActivityViewModel.getImageUri().getValue() != null) {
                Cloudinary.getInstance().init(ProfileActivity.this);
                Cloudinary.getInstance().uploadImage(profileActivityViewModel.getImageUri().getValue() ,new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String secureUrl = (String) resultData.get("secure_url");
                        assert secureUrl != null;
                        updateUser(user);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(ProfileActivity.this, error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                });
            }else {
                if(updateName) {
                    updateUser(user);
                }
            }
        });
    }

    private void updateUser(User user) {
        viewModel.updateUser(user, new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ScalarBooleanResponse> call, @NonNull Response<ScalarBooleanResponse> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Cập nhạt thành công", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ProfileActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ScalarBooleanResponse> call, @NonNull Throwable throwable) {
                Toast.makeText(ProfileActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void deleteButton() {
        binding.btnDelete.setOnClickListener((v) -> viewModel.deleteUser(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ScalarBooleanResponse> call, @NonNull Response<ScalarBooleanResponse> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Tài khoản xóa thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileActivity.this, LoginWayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(ProfileActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ScalarBooleanResponse> call, @NonNull Throwable throwable) {
                Toast.makeText(ProfileActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
