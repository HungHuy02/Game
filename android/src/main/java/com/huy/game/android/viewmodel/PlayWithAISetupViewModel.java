package com.huy.game.android.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlayWithAISetupViewModel extends ViewModel {

    private MutableLiveData<Boolean> _showButtons = new MutableLiveData<>(false);
    private MutableLiveData<Integer> _position = new MutableLiveData<>();
    private MutableLiveData<Integer> _positionColor = new MutableLiveData<>(1);
    private MutableLiveData<Integer> _level = new MutableLiveData<>();

    public LiveData<Boolean> showButtons() {
        return _showButtons;
    }

    public LiveData<Integer> getPosition() {
        return _position;
    }

    public LiveData<Integer> getPositionColor() {
        return _positionColor;
    }

    public LiveData<Integer> getLevel() {return _level;}

    public void setShowButtons(boolean show) {
        _showButtons.setValue(show);
    }

    public void setPosition(int position) {
        _position.setValue(position);
    }

    public void setPositionColor(int positionColor) {
        _positionColor.setValue(positionColor);
    }

    public void setLevel(int level) {
        _level.setValue(level);
    }
}
