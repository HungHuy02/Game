package com.huy.game.android.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlayFragmentViewModel extends ViewModel {

    private MutableLiveData<String> selectedTime = new MutableLiveData<>();
    private MutableLiveData<String> selectedIcon = new MutableLiveData<>();
    public LiveData<String> getSelectedTime() {
        return selectedTime;
    }

    public LiveData<String> getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedTime(String time) {
        selectedTime.setValue(time);
    }

    public void setSelectedIcon(String icon) { selectedIcon.setValue(icon);}
}
