package com.houyewei.cameraxapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetailViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<BarcodeModel> barcodeModelLiveData = new MutableLiveData<>();

    public LiveData<BarcodeModel> getUser() {
        return barcodeModelLiveData;
    }

    public DetailViewModel() {
        // trigger detail load.
    }

    void doAction() {
        // depending on the action, do necessary business logic calls and update the
        // userLiveData.
    }
}