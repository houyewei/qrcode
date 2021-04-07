package com.houyewei.cameraxapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TabsFragment extends Fragment {

    TabsFragmentAdapter tabsFragmentAdapter;

    ViewPager2 viewPager;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tabs, container, false);
//        recyclerView = root.findViewById(R.id.recycler_dashboard);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabsFragmentAdapter = new TabsFragmentAdapter(this);
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(tabsFragmentAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("History");
                    tab.setIcon(R.drawable.ic_baseline_format_list_bulleted_24);
                    break;
                case 1:
                    tab.setText("Scan");
                    tab.setIcon(R.drawable.ic_baseline_camera_alt_24);
                    break;
                case 2:
                    tab.setText("Setting");
                    tab.setIcon(R.drawable.ic_baseline_settings_24);
            }
        }).attach();
//
    }


}

