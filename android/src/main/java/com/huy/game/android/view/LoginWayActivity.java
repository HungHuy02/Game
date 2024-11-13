package com.huy.game.android.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.huy.game.R;
import com.huy.game.android.base.BaseActivity;
import com.huy.game.android.firebase.FirebaseGoogleAuth;
import com.huy.game.android.globalstate.UserState;
import com.huy.game.android.models.User;
import com.huy.game.android.models.request.TokenRequest;
import com.huy.game.android.models.response.LoginResponse;
import com.huy.game.android.utils.Constants;
import com.huy.game.android.utils.StorageUtils;
import com.huy.game.android.viewmodel.apiservice.AuthServiceViewModel;
import com.huy.game.databinding.ActivityLoginWayBinding;

import java.util.Objects;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginWayActivity extends BaseActivity {

    private ActivityLoginWayBinding binding;
    private AuthServiceViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginWayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupViewModel();
        loginButton();
        emailButton();
        googleButton();
        guestButton();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AuthServiceViewModel.class);
    }

    private void loginButton() {
        binding.loginBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void emailButton() {
        binding.emailBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void googleButton() {
        binding.googleBtn.setOnClickListener((v) -> {
            CredentialManager credentialManager = CredentialManager.create(this);

            GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(this.getString(R.string.web_client_id))
                .build();

            GetCredentialRequest getCredentialRequest = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

            credentialManager.getCredentialAsync( LoginWayActivity.this, getCredentialRequest, new CancellationSignal(),Executors.newSingleThreadExecutor(), new CredentialManagerCallback<>() {

                @Override
                public void onResult(GetCredentialResponse result) {
                    try {
                        GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.getCredential().getData());
                        String googleIdToken = googleIdTokenCredential.getIdToken();
                        AuthCredential firebaseCredential = FirebaseGoogleAuth.verifyGoogleId(LoginWayActivity.this, googleIdToken);
                        signUpWithCredential(LoginWayActivity.this, firebaseCredential);
                    } catch (Exception e) {
                        Toast.makeText(LoginWayActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(@NonNull GetCredentialException e) {
                    Toast.makeText(LoginWayActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void  signUpWithCredential(Context context, AuthCredential credential) {
        if (credential != null) {
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(signInTask -> {
                    if(signInTask.isSuccessful()) {
                        Objects.requireNonNull(signInTask.getResult().getUser()).getIdToken(true).addOnCompleteListener(idTokenTask -> {
                                if(idTokenTask.isSuccessful()) {
                                    loginGoogle(new TokenRequest(idTokenTask.getResult().getToken()));
                                }
                        });
                    }else {
                        Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                    }
                }
            );
        }
    }

    private void loginGoogle(TokenRequest token) {
        viewModel.loginGoogle(token, new Callback<>() {
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    StorageUtils storageUtils = StorageUtils.getInstance(LoginWayActivity.this);
                    assert loginResponse != null;
                    storageUtils.setStringValue(Constants.DATASTORE_ACCESS_TOKEN, loginResponse.getAccessToken());
                    storageUtils.setStringValue(Constants.DATASTORE_REFRESH_TOKEN, loginResponse.getRefreshToken());
                    User user = loginResponse.getUser();
                    UserState.getInstance().setGuest(false);
                    UserState.getInstance().setData(user.getName(), user.getEmail(), user.getImageUrl(), user.getElo());
                    Toast.makeText(LoginWayActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    toMainActivity();
                } else if (response.code() == 401) {
                    if (response.body() != null && response.body().getMessage() != null) {
                        Toast.makeText(LoginWayActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginWayActivity.this, "Đăng nhập thất bại. Kiểm tra lại thông tin.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginWayActivity.this, "Lỗi không xác định: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable throwable) {
                Log.e("error", throwable.toString());
            }
        });
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void guestButton() {
        binding.guestBtn.setOnClickListener((v) -> {
            UserState.getInstance().setGuestAccount();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}
