package com.houyewei.cameraxapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface BarcodeDao {
    @Insert
    void insert(BarcodeModel barcodeModel);

    @Update(entity = BarcodeModel.class)
    void update(BarcodeModel barcodeModel);

    @Query("select * from barcode_table")
    LiveData<List<BarcodeModel>> getAll();
}
