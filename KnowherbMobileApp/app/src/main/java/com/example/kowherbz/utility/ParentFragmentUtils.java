package com.example.kowherbz.utility;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.kowherbz.R;

public class ParentFragmentUtils {

    public static Fragment getParentFragmentInstance(Activity activity, int viewpagerResource, ViewPager activityVP){
        String TAG = "android:switcher:"
                + viewpagerResource + ":"
                + activityVP.getCurrentItem();
        return ((FragmentActivity)activity).getSupportFragmentManager().findFragmentByTag(TAG);
    }
}
