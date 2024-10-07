package com.huy.game.android.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingActivityViewModel extends ViewModel {

    private MutableLiveData<Boolean> _mute = new MutableLiveData<>();

    public LiveData<Boolean> getMute() {
        return _mute;
    }

    public void setMute(boolean mute) {
        _mute.setValue(mute);
    }
}
