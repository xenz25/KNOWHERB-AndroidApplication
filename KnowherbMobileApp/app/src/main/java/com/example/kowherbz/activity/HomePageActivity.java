package com.example.kowherbz.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.kowherbz.R;
import com.example.kowherbz.utility.AnimationUtility;
import com.example.kowherbz.utility.CircularOutlineProvider;
import com.example.kowherbz.utility.ClassPackageMaker;
import com.example.kowherbz.utility.DoubleClickHandler;
import com.example.kowherbz.utility.GuiColorManager;
import com.example.kowherbz.utility.RawResourceIDs;
import com.example.kowherbz.utility.SharedPreferenceUtility;
import com.example.kowherbz.utility.SystemUtility;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    //Text Views
    private TextView tv_sakit_at_lunas_pref, tv_sakit_at_lunas_body;
    private TextView tv_mga_halaman_pref, tv_mga_halaman_body;
    private TextView tv_paboritong_halaman_pref, tv_paboritong_halaman_body;
    private TextView tv_naglinang_pref, tv_naglinang_body;

    //Button - LinearLayout
    private LinearLayout btn_sakit_at_lunas;
    private LinearLayout btn_mga_halamang_gamot;
    private LinearLayout btn_pabortiong_halaman;
    private LinearLayout btn_naglinang;

    //view elements list
    private List<View> homePageButtonTexts = new ArrayList<>();
    private List<View> homePageButtons = new ArrayList<>();

    private DoubleClickHandler backClickHandler = new DoubleClickHandler();



    private HomePagePreference homePagePreference;

    //shared preference
    public static class HomePagePreference {
        public static final String HOME_PAGE_PREFERENCE = "home_page_preference";
        public static final String LIST_INIT_STATUS = "isListInitialized";
        public static final String ANIMATED_BUTTON_STATUS = "isAnimatedBefore";
        private boolean isListInitialized;
        private boolean isAnimatedBefore;

        public HomePagePreference(boolean isListInitialized, boolean isAnimatedBefore) {
            this.isListInitialized = isListInitialized;
            this.isAnimatedBefore = isAnimatedBefore;
        }

        //HOME PAGE PREFERENCE
        public static void generateHomePagePreference(Context context, HomePagePreference homePagePreference){
            SharedPreferences.Editor prefEditor = SharedPreferenceUtility.getSharedPref(context, HOME_PAGE_PREFERENCE).edit();
            prefEditor.putBoolean(ANIMATED_BUTTON_STATUS, homePagePreference.isAnimatedBefore());
            prefEditor.putBoolean(LIST_INIT_STATUS, homePagePreference.isListInitialized());
            prefEditor.apply();
        }

        //HOME PAGE PREFERENCE
        public static HomePagePreference getHomePagePreference(Context context){
            SharedPreferences sharedPreferences = SharedPreferenceUtility.getSharedPref(context, HOME_PAGE_PREFERENCE);
            return new HomePagePreference(
                    sharedPreferences.getBoolean(LIST_INIT_STATUS, false),
                    sharedPreferences.getBoolean(ANIMATED_BUTTON_STATUS, false)
            );
        }

        public boolean isListInitialized() {
            return isListInitialized;
        }

        public boolean isAnimatedBefore() {
            return isAnimatedBefore;
        }

        public void setListInitialized(boolean listInitialized) {
            isListInitialized = listInitialized;
        }

        public void setAnimatedBefore(boolean animatedBefore) {
            isAnimatedBefore = animatedBefore;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        homePagePreference = HomePagePreference.getHomePagePreference(this);

        initializeView();
        startActivity();
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
        GuiColorManager.setNotificationBarColor(this, R.color.activity_notification_bar);
    }

    @Override
    public void onBackPressed() {
        backClickHandler.doubleClickToExit(this);
    }

    private void initializeView() {
        //4 Home Page Button Texts
        tv_sakit_at_lunas_pref = findViewById(R.id.sakit_at_lunas_prefix);
        tv_sakit_at_lunas_body = findViewById(R.id.sakit_at_lunas_body);

        tv_mga_halaman_pref = findViewById(R.id.mga_halamang_gamot_prefix);
        tv_mga_halaman_body = findViewById(R.id.mga_halamang_gamot_body);

        tv_paboritong_halaman_pref = findViewById(R.id.paboritong_halaman_prefix);
        tv_paboritong_halaman_body = findViewById(R.id.paboritong_halaman_body);

        tv_naglinang_pref = findViewById(R.id.naglinang_prefix);
        tv_naglinang_body = findViewById(R.id.naglinang_body);

        //add all button text views to List<Views>
        homePageButtonTexts = ClassPackageMaker.compileToListOfViews(
                tv_sakit_at_lunas_pref, tv_sakit_at_lunas_body, tv_mga_halaman_pref,
                tv_mga_halaman_body, tv_paboritong_halaman_pref, tv_paboritong_halaman_body,
                tv_naglinang_pref, tv_naglinang_body);

        //make all button text invisible
        if(!homePagePreference.isAnimatedBefore()){
            AnimationUtility.changeBatchViewVisibility(homePageButtonTexts, false);
        }

        //home page buttons
        btn_sakit_at_lunas = findViewById(R.id.button_home_page_sakit_at_lunas);
        btn_mga_halamang_gamot = findViewById(R.id.button_home_page_halamang_gamot);
        btn_pabortiong_halaman = findViewById(R.id.button_home_page_paboritong_halaman);
        btn_naglinang = findViewById(R.id.button_home_page_naglinang);

        //add all button views to List<Views>
        homePageButtons = ClassPackageMaker.compileToListOfViews(
                btn_sakit_at_lunas, btn_mga_halamang_gamot, btn_pabortiong_halaman,
                btn_naglinang);

        //change all button state to false
        if(!homePagePreference.isAnimatedBefore()){
            AnimationUtility.changeBatchViewStateEnabled(homePageButtons, false);
        }
    }

    private void startActivity() {
        final long animTextDelay = 300;
        long animDivisionDelay = 500;
        final long delayFactorIncrement = 300;

        Iterator<View> buttonTextsIterator = homePageButtonTexts.iterator();

        if(!homePagePreference.isAnimatedBefore()){
            homePagePreference.setAnimatedBefore(true);

            //cascading fading effect
            while (buttonTextsIterator.hasNext()) {
                View tv1 = buttonTextsIterator.next();
                View tv2 = buttonTextsIterator.next();
                AnimationUtility.passOnHandlerDelay(() -> {
                    AnimationUtility.yoyoAnimate(Techniques.FadeIn, animTextDelay, tv1, tv2);
                    AnimationUtility.changeBatchViewVisibility(true, tv1, tv2);
                }, (animDivisionDelay += delayFactorIncrement));
            }
            //enable all buttons
            AnimationUtility.passOnHandlerDelay(() -> AnimationUtility.changeBatchViewStateEnabled(
                    homePageButtons, true), (animDivisionDelay + delayFactorIncrement));
        }

        //generate preference
        HomePagePreference.generateHomePagePreference(this, homePagePreference);

        //sakit at lunas btn click
        DoubleClickHandler doubleClickHandler = new DoubleClickHandler();
        btn_sakit_at_lunas.setOnClickListener(v -> doubleClickHandler.startIfSatisfied(() -> {
            startActivity(new Intent(HomePageActivity.this, SakitAtLunasActivity.class));
            overridePendingTransition(AnimationUtility.fade_in_anim_intent, AnimationUtility.fade_out_anim_intent);
            finish();
        }));

        //mga halamang gamot btn click
        DoubleClickHandler doubleClickHandler1 = new DoubleClickHandler();
        btn_mga_halamang_gamot.setOnClickListener(v -> doubleClickHandler1.startIfSatisfied(() -> {
            startActivity(new Intent(HomePageActivity.this, MgaHalamangGamotActivity.class));
            overridePendingTransition(AnimationUtility.fade_in_anim_intent, AnimationUtility.fade_out_anim_intent);
            finish();
        }));

        //paborito
        DoubleClickHandler favoriteHandler = new DoubleClickHandler();
        btn_pabortiong_halaman.setOnClickListener(v -> favoriteHandler.startIfSatisfied(() -> {
            startActivity(new Intent(HomePageActivity.this, PaboritoActivity.class));
            overridePendingTransition(AnimationUtility.fade_in_anim_intent, AnimationUtility.fade_out_anim_intent);
            finish();
        }));

        //naglinang
        DoubleClickHandler naglinangHandler = new DoubleClickHandler();
        btn_naglinang.setOnClickListener(v -> naglinangHandler.startIfSatisfied(() -> {
            startActivity(new Intent(HomePageActivity.this, NaglinangActivity.class));
            overridePendingTransition(AnimationUtility.fade_in_anim_intent, AnimationUtility.fade_out_anim_intent);
            finish();
        }));

    }

    private void writeToJson(String fileName, HomePagePreference homePagePreference){
        String fileNames = getFilesDir() + "/" + fileName;
        Gson gson = new Gson();
        String json = gson.toJson(homePagePreference);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileNames));
            bufferedWriter.write(json);
            bufferedWriter.close();
        } catch (Exception ignored){}
    }
}