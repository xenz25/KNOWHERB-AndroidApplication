package com.example.kowherbz.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.kowherbz.R;

public class GuiColorManager {

    public static void setNotificationBarColor(Activity activity, int color_id){
        activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, color_id));
    }

    public static void changeTint(View view, int color_id){
        Drawable fav_tint_selected = view.getBackground();
        fav_tint_selected = DrawableCompat.wrap(fav_tint_selected);
        DrawableCompat.setTint(fav_tint_selected, ContextCompat.getColor(view.getContext(), color_id));
    }

    public static void changeBackgroundDrawable(Context context, View view, int drawable_id){
        view.setBackground(ContextCompat.getDrawable(context, drawable_id));
    }
}
