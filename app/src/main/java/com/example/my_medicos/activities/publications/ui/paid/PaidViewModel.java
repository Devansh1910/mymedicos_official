package com.example.my_medicos.activities.publications.ui.paid;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PaidViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PaidViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}