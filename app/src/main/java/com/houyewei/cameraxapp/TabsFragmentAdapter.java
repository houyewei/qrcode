package com.houyewei.cameraxapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabsFragmentAdapter extends FragmentStateAdapter {

    public TabsFragmentAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            Fragment list = new ListFragment();
            return list;
        }
        if (position == 1) {
            Fragment start = new StartFragment();
            return start;
        }
        Fragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putInt(SettingFragment.ARG_OBJECT, position + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
