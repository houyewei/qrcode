package com.houyewei.cameraxapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StartFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView textView;
    private BarcodeViewModel viewModel;

    private Button buttonView;
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.start_layout, container, false);
        recyclerView = root.findViewById(R.id.recycler_dashboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController =  NavHostFragment.findNavController(StartFragment.this);
                Log.d("TAG", "id" + navController.getCurrentDestination().getId());
                navController.navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        buttonView = view.findViewById(R.id.copy);

        buttonView.setOnClickListener(view1 -> {
            ClipboardManager clipboard = (ClipboardManager)
                    getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("text", "hello, world");

            clipboard.setPrimaryClip(clip);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity)getActivity();
        viewModel = activity.viewModel;

        viewModel.getBarcode().observe(getViewLifecycleOwner(), barcode -> {
            // Update the list UI
            textView.setText(barcode.getDisplayValue());
        });
    }
}
