package com.huy.game.android.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileActivityViewModel extends ViewModel {

    private MutableLiveData<Uri> _imageUri = new MutableLiveData<>(null);

    public LiveData<Uri> getImageUri() {
        return _imageUri;
    }

    public void set_imageUri(Uri imageUri) {
        _imageUri.setValue(imageUri);
    }
}
