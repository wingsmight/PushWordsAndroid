package com.example.pushwords.ui.dictionaryTab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DictionaryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DictionaryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}