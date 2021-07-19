package com.example.kowherbz.utility;

import android.content.Context;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class SnacksUtils {
    public interface SnackOnClickListener {
        void onSnackActionClick(int position);
        void onSnackDismiss(int position);
    }

    private Snackbar snackbar;
    private SnackOnClickListener snackOnClickListener;
    private final View parentView;
    public static final String UNDO = "IBALIK";
    private final Context context;

    private boolean isClicked = false;

    private int position;

    public void setPosition(int position) {
        this.position = position;
    }

    public SnacksUtils(Context context, SnackOnClickListener snackOnClickListener, View parentView) {
        this.context = context;
        this.snackOnClickListener = snackOnClickListener;
        this.parentView = parentView;
        preInitSnackBar();
        startSnackListener();
    }

    public void setSnackOnClickListener(SnackOnClickListener snackOnClickListener) {
        this.snackOnClickListener = snackOnClickListener;
    }

    public void showSnackBarShortNoAction(String text){
        dismissSnackBar();
        snackbar = Snackbar.make(parentView, text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void showSnackBarLongNoAction(String text){
        snackbar = Snackbar.make(parentView, text, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showSnackBarIndefiniteNoAction(String text){
        snackbar = Snackbar.make(parentView, text, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showSnackBarActionable(String prompt_text, String action_text, int action_color){
        snackbar = Snackbar.make(parentView, prompt_text, Snackbar.LENGTH_SHORT)
        .setAction(action_text, v -> {
            isClicked = true;
            this.snackOnClickListener.onSnackActionClick(this.position);
        })
        .setActionTextColor(ContextCompat.getColor(context, action_color));
        snackbar.show();
        startSnackListener();
    }

    public boolean isClicked() {
        return isClicked;
    }

    private void preInitSnackBar(){
        snackbar = Snackbar.make(parentView, "", Snackbar.LENGTH_SHORT);
    }

    public void dismissSnackBar(){
        if(snackbar!=null){
            snackbar.dismiss();
        }
    }

    private void startSnackListener(){
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if(!isClicked){
                    snackOnClickListener.onSnackDismiss(position);
                }
                isClicked = false;
            }
        });
    }
}
