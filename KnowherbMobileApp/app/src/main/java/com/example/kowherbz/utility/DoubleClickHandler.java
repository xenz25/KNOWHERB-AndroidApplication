package com.example.kowherbz.utility;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.widget.Toast;

import com.example.kowherbz.activity.HomePageActivity;

public class DoubleClickHandler {

    private Toast toast;
    private long previousClick = 0;
    private final long delayBetweenClicks = 1000;
    private int toggleCount = 0;

    public void startIfSatisfied(Runnable runnable){
        if(SystemClock.elapsedRealtime() - this.previousClick >= delayBetweenClicks){
            this.previousClick = SystemClock.elapsedRealtime();
            runnable.run();
        }
    }

    public void doubleClickToExit(Activity activity){
        cancelToast();
        if(this.previousClick + (delayBetweenClicks*2) > System.currentTimeMillis()){
            cancelToast();
            SystemUtility.finishAndRemoveTask(activity);
            SystemUtility.exit();
        } else {
            toast = Toast.makeText(activity, "Press back again to exit", Toast.LENGTH_SHORT);
            toast.show();
        }
        this.previousClick = System.currentTimeMillis();
    }

    private void cancelToast(){
        if(this.toast != null)
            this.toast.cancel();
    }

    public void setToggleCount(int toggleCount) {
        this.toggleCount = toggleCount;
    }

    public int getToggleCount() {
        return toggleCount;
    }

    public void toggleExecutor(Runnable t1_run, Runnable t2_run){
        if(this.toggleCount%2 == 0){
            t1_run.run();
            this.toggleCount+=1;
        } else if(this.toggleCount%2 == 1){
            t2_run.run();
            this.toggleCount = 0;
        }
    }
}
