package com.example.kowherbz.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kowherbz.activity.HomePageActivity;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceUtility {

    public static SharedPreferences getSharedPref(Context context, String name) {
        return context.getSharedPreferences(name, MODE_PRIVATE);
    }

}
