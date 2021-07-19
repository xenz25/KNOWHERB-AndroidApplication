package com.example.kowherbz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Pair;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.kowherbz.R;
import com.example.kowherbz.holder.PlantContentHolder;
import com.example.kowherbz.utility.AnimationUtility;
import com.example.kowherbz.utility.ClassPackageMaker;
import com.example.kowherbz.utility.FavoritesUtils;
import com.example.kowherbz.utility.GuiColorManager;
import com.example.kowherbz.utility.RawResourceIDs;
import com.example.kowherbz.utility.SystemUtility;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class SplashScreenActivity extends AppCompatActivity {
    //views
    private GifDrawable logoGifDrawable;
    private GifImageView logoGifContainer;
    private TextView tv_know, tv_herb;

    //parent layout
    private RelativeLayout splash_screen_parent_layout;

    //handler thread
    private final HandlerThread handlerThread = new HandlerThread("SplashScreenHandler");
    private Handler thisActivityHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        handlerThread.start();
        thisActivityHandler = new Handler(handlerThread.getLooper());

        initializeLists();
        initializeViews();
        startActivities();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = SystemUtility.STANDARD_FONT_SCALE;
        applyOverrideConfiguration(override);
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println(getExternalCacheDir());
        GuiColorManager.setNotificationBarColor(this, R.color.top_bar_splash);
    }

    @Override public void onBackPressed() {

    }

    public void initializeLists() {
        thisActivityHandler.post(() -> {
            Context context = SplashScreenActivity.this;

            //List and IDs Initializer
            RawResourceIDs.init(context);
            ClassPackageMaker.grabAllRequiredList(context);

            //Home Page Preferences
            HomePageActivity.HomePagePreference.generateHomePagePreference(context,
                    new HomePageActivity.HomePagePreference(true, false));

            //Favorites Preferences
            FavoritesUtils.Illness.initPreference(context);
            FavoritesUtils.Hydrotherapy.initPreference(context);
            FavoritesUtils.Plants.initPreference(context);

            //Plant and Illness List DATA
            PlantContentHolder.generatePlantNameDictionary(context);
            MgaHalamangGamotActivity.createPreference(context);

        });
    }

    private void initializeViews(){
        splash_screen_parent_layout = findViewById(R.id.splash_screen_parent);
        try{
            logoGifDrawable = new GifDrawable(getResources(), R.drawable.knowherb_anim_white);
        } catch (IOException ignored) {
        }
        logoGifContainer = findViewById(R.id.logoAnimView);
        logoGifContainer.setImageDrawable(logoGifDrawable);
        tv_know = findViewById(R.id.splash_screen_know);
        tv_herb = findViewById(R.id.splash_screen_herb);
    }

    private void startActivities(){
       final long delayLogoAnim = 2000;
       final long elementFadeDelay = 300;

        //start logo animation
        AnimationUtility.passOnHandlerDelay(() -> {
            logoGifDrawable.stop(); //stop gif play
            AnimationUtility.yoyoAnimate(Techniques.FadeIn, elementFadeDelay, tv_know, tv_herb);
            AnimationUtility.changeBatchViewVisibility(true, tv_know, tv_herb);
        }, delayLogoAnim);

        //start second activity with shared animation
        AnimationUtility.passOnHandlerDelay(() -> {
            Intent intent_home_page = new Intent(SplashScreenActivity.this, HomePageActivity.class);

            //get shared element bundles
            startActivity(intent_home_page, AnimationUtility.sharedAnimationBuilder(SplashScreenActivity.this,
                   Pair.create(tv_know, getResources().getString(R.string.know_shared_transition_name)),
                    Pair.create(tv_herb, getResources().getString(R.string.herb_shared_transition_name))));

            AnimationUtility.overridePendingTransition(SplashScreenActivity.this,
                    AnimationUtility.fade_in_anim_intent, AnimationUtility.fade_out_anim_intent);

        }, delayLogoAnim+1000);

        AnimationUtility.passOnHandlerDelay(SplashScreenActivity.this::finish, (delayLogoAnim+2000));
    }
}