package com.example.kowherbz.utility;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    private Toast toast;
    private Context context;

    public ToastUtils(Context context) {
        this.context = context;
    }

    public void cancelToast(){
        if(toast != null){
            toast.cancel();
        }
    }

    public void displayOnToastLong(String text){
        cancelToast();
        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public void displayOnToastShort(String text){
        cancelToast();
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
