package com.huy.game.android.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TwoPersonsPlaySetupViewModel extends ViewModel {

    private MutableLiveData<Boolean> _showButtons = new MutableLiveData<>(false);
    private MutableLiveData<Integer> _position = new MutableLiveData<>();

    public LiveData<Boolean> showButtons() {
        return _showButtons;
    }

    public LiveData<Integer> getPosition() {
        return _position;
    }

    public void setShowButtons(boolean show) {
        _showButtons.setValue(show);
    }

    public void setPosition(int position) {
        _position.setValue(position);
    }
}
