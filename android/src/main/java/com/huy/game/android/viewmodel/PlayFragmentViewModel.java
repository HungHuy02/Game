package com.huy.game.android.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlayFragmentViewModel extends ViewModel {

    private MutableLiveData<String> selectedTime = new MutableLiveData<>();

    public LiveData<String> getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String time) {
        selectedTime.setValue(time);
    }
}
