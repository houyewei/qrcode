package com.houyewei.cameraxapp;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;


public class MainActivity extends AppCompatActivity {

    public BarcodeViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewModel = new ViewModelProvider(this).get(BarcodeViewModel.class);
        viewModel.getBarcode().observe(this, item -> {
            // Perform an action with the latest item data
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}