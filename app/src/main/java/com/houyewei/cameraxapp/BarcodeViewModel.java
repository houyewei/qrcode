package com.houyewei.cameraxapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.mlkit.vision.barcode.Barcode;

import java.util.List;

public class BarcodeViewModel extends AndroidViewModel {
    private final MutableLiveData<Barcode> barcode = new MutableLiveData<Barcode>();
    private AppRepository mRepository;
    public MutableLiveData<BarcodeModel> mBarcode;


    public BarcodeViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AppRepository(application);
        mBarcode = new MutableLiveData<BarcodeModel>();

    }

    public void setBarcode(Barcode code) {
        barcode.setValue(code);
    }
    public LiveData<Barcode> getBarcode() {
        return barcode;
    }

    public LiveData<List<BarcodeModel>> getAll() {
        return mRepository.getAll();
    }

    public void update(BarcodeModel barcodeModel) {
        mRepository.update(barcodeModel);
    }

    public void insert(BarcodeModel barcodeModel) {
        mRepository.insert(barcodeModel);
    }
}
