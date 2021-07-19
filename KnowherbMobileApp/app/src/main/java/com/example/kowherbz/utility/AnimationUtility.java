package com.example.kowherbz.utility;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.kowherbz.R;

import java.util.List;

public class AnimationUtility {
    private static boolean activityTransEnabled = false; //change to true to enable activity transitions override
    public static final int fade_in_anim_intent = R.anim.fade_in_anim;
    public static final int fade_out_anim_intent = R.anim.fade_out_anim;

    /**
     * Returns the fadeInShort animation file in anim resource
     * @param context activity context
     * @return the fadeInShort animation file in anim resource as Animation type
     */
    public static Animation getFadeInShort(Context context){
        return AnimationUtils.loadAnimation(context, R.anim.fade_in_short);
    }

    /**
     * Pass a runnable to a handler easily
     * @param runnable the runnable to execute
     * @param delayMillis delay before execution
     */
    public static void passOnHandlerDelay(Runnable runnable, long delayMillis){
        new Handler().postDelayed(runnable, delayMillis);
    }

    /**
     * change view state clickable and focusable based on isEnabled value
     * @param views List of views to process
     * @param isEnabled value of clickable and focusable
     */
    public static void changeBatchViewStateEnabled(List<View> views, boolean isEnabled){
        for(View item : views){
            item.setClickable(isEnabled);
            item.setFocusable(isEnabled);
        }
    }

    /**
     * change view state clickable and focusable based on isEnabled value and perform a click action after
     * @param views List of views to process
     * @param isEnabled value of clickable and focusable
     */
    public static void changeBatchViewStateEnabledPerformClick(List<View> views, boolean isEnabled){
        for(View item : views){
            item.setClickable(isEnabled);
            item.setFocusable(isEnabled);
            item.performClick();
        }
    }

    /**
     * change all views state of visibility based on stat value
     * @param views List of views to process
     * @param state state of visibility true - VISIBLE, false - INVISIBLE
     */
    public static void changeBatchViewVisibility(List<View> views, boolean state){
        for(View item : views){
            if(!state){
                item.setVisibility(View.INVISIBLE);
            } else {
                item.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * change all views state of visibility based on stat value
     * @param views Varang of views to process
     * @param state state of visibility true - VISIBLE, false - INVISIBLE
     */
    public static void changeBatchViewVisibility(boolean state, View... views){
        for(View item : views){
            if(!state){
                item.setVisibility(View.INVISIBLE);
            } else {
                item.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Overrides existing activity transition animation
     * @param activity the activity
     * @param enterAnimId id of enter animation file from resource
     * @param exitAnimID id of exit animation file from resource
     */
    public static void overridePendingTransition(Activity activity, int enterAnimId, int exitAnimID){
        if(activityTransEnabled){
            activity.overridePendingTransition(enterAnimId, exitAnimID);
        }
    }

    /**
     * Pack the shared elements into bundle for shared animation
     * @param activity the activity
     * @param pairs pairs of shared element
     * @return Bundle of shared elements
     */
    @SafeVarargs
    public static Bundle sharedAnimationBuilder(Activity activity, Pair<View, String>... pairs){
        return ActivityOptions.makeSceneTransitionAnimation(activity, pairs).toBundle();
    }

    /**
     * Easily animate views via YOYO API
     * @param techniques YOYO technique
     * @param duration length of animation
     * @param views All views to be animate
     */
    public static void yoyoAnimate(Techniques techniques, long duration, View... views){
        for(View item : views){
            YoYo.with(techniques).duration(duration).playOn(item);
        }
    }

    //use for up down animation in recycler view
    public static class RecyclerViewUpDownAnimation {
        private int lastPosition = 0;

        //place this method inside OnBindViewHolder
        public void startAnimation(View layout, int lastAbsolutePosition){
            //animations
            if (lastAbsolutePosition > this.lastPosition) {
                layout.startAnimation(AnimationUtils.loadAnimation(layout.getContext(), R.anim.down_to_up_anim));
            } else {
                layout.startAnimation(AnimationUtils.loadAnimation(layout.getContext(), R.anim.up_to_down_anim));
            }
            this.lastPosition = lastAbsolutePosition;
        }
    }

    public static RecyclerView.SmoothScroller getSmoothScroller(Context context, int position){
        RecyclerView.SmoothScroller smoothScroller =  new
                LinearSmoothScroller(context) {
                    @Override
                    protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };
        smoothScroller.setTargetPosition(position);
        return smoothScroller;
    }

    public static void fadeOutThenHide(View v, long duration){
        AnimationUtility.yoyoAnimate(Techniques.FadeOut, duration, v);
        v.setVisibility(View.GONE);
    }

    public static void fadeInThenShow(View v, long duration){
        AnimationUtility.yoyoAnimate(Techniques.FadeIn, duration, v);
        v.setVisibility(View.VISIBLE);
    }
}
