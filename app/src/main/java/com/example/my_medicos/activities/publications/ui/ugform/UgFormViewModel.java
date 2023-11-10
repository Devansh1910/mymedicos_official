package com.example.my_medicos.activities.publications.ui.ugform;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UgFormViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public UgFormViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}