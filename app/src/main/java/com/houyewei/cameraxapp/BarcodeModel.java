package com.houyewei.cameraxapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "barcode_table")
public class BarcodeModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private String barcode;

    public BarcodeModel() {
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
