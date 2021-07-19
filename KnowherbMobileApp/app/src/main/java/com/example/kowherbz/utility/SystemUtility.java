package com.example.kowherbz.utility;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class SystemUtility {

    public static final float STANDARD_FONT_SCALE = 1.0f;

    public static void exit(){
        System.exit(0);
    }

    public static void finish(Activity activity){
        activity.finish();
    }

    public static void finishAffinity(Activity activity){
        activity.finishAffinity();
    }

    public static void finishAndRemoveTask(Activity activity){
        activity.finishAndRemoveTask();
    }

    public static void disablesSoftKeyboardPopping(Activity activity){
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void forceKeyboardToStateHidden(Activity activity){
        //https://gist.github.com/lopspower/6e20680305ddfcb11e1e
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
