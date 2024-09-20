package com.huy.game.android.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlayFragmentViewModel extends ViewModel {

    private MutableLiveData<String> _selectedTime = new MutableLiveData<>();
    private MutableLiveData<String> _selectedIcon = new MutableLiveData<>();
    public LiveData<String> getSelectedTime() {
        return _selectedTime;
    }

    public LiveData<String> getSelectedIcon() {
        return _selectedIcon;
    }

    public void setSelectedTime(String time) {
        _selectedTime.setValue(time);
    }

    public void setSelectedIcon(String icon) { _selectedIcon.setValue(icon);}
}
