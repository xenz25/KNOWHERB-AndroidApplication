package com.example.kowherbz.utility;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

public class CircularOutlineProvider extends ViewOutlineProvider {
    @Override
    public void getOutline(View view, Outline outline) {
        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), (view.getWidth() / 2F));
    }
}