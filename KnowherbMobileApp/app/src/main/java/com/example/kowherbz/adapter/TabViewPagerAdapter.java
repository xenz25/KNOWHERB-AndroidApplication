package com.example.kowherbz.adapter;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This Adapter is use for tab layout and view pager
 */
public class TabViewPagerAdapter extends FragmentPagerAdapter {
    List<Pair<String, Fragment>> fragmentList;

    public TabViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void setFragmentList(List<Pair<String, Fragment>> fragmentList) {
        this.fragmentList = fragmentList;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        return this.fragmentList.get(position).second;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.fragmentList.get(position).first;
    }
}
