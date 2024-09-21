package com.huy.game.android.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlayWithAISetupViewModel extends ViewModel {

    private MutableLiveData<Boolean> _showButtons = new MutableLiveData<>(false);
    private MutableLiveData<Integer> _position = new MutableLiveData<>();
    private MutableLiveData<Integer> _positonColor = new MutableLiveData<>(1);

    public LiveData<Boolean> showButtons() {
        return _showButtons;
    }

    public LiveData<Integer> getPosition() {
        return _position;
    }

    public LiveData<Integer> getPositionColor() {
        return _positonColor;
    }

    public void setShowButtons(boolean show) {
        _showButtons.setValue(show);
    }

    public void setPosition(int position) {
        _position.setValue(position);
    }

    public void setPositionColor(int positionColor) {
        _positonColor.setValue(positionColor);
    }
}
