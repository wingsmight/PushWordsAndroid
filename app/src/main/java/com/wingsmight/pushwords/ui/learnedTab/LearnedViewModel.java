package com.wingsmight.pushwords.ui.learnedTab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LearnedViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LearnedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}