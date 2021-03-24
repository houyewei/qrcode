package com.houyewei.cameraxapp;

import android.app.Application;

import androidx.lifecycle.LiveData;


import java.util.List;

public class AppRepository {
    private BarcodeDao mBarcodeDAO;

    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mBarcodeDAO = db.barcodeDAO();
    }


    public void insert(BarcodeModel barcodeModel) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mBarcodeDAO.insert(barcodeModel);
        });
    }

    public void update(BarcodeModel barcodeModel) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mBarcodeDAO.update(barcodeModel);
        });
    }

    public LiveData<List<BarcodeModel>> getAll() {
        LiveData<List<BarcodeModel>> barcodes = mBarcodeDAO.getAll();
        return barcodes;
    }
}
