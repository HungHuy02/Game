package com.huy.game.android.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlayFragmentViewModel extends ViewModel {

    private MutableLiveData<String> _selectedTime = new MutableLiveData<>();
    private MutableLiveData<Integer> _selectedIcon = new MutableLiveData<>();
    private MutableLiveData<Integer> _position = new MutableLiveData<>(7);

    public LiveData<String> getSelectedTime() {
        return _selectedTime;
    }

    public LiveData<Integer> getSelectedIcon() {
        return _selectedIcon;
    }

    public LiveData<Integer> getPosition() {
        return _position;
    }

    public void setSelectedTime(String time) {
        _selectedTime.setValue(time);
    }

    public void setSelectedIcon(int icon) { _selectedIcon.setValue(icon);}

    public void setPosition(int position) {
        _position.setValue(position);
    }
}
