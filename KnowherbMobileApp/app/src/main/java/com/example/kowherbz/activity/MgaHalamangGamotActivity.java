package com.example.kowherbz.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kowherbz.R;
import com.example.kowherbz.adapter.PlantListAdapter;
import com.example.kowherbz.holder.PlantContentHolder;
import com.example.kowherbz.utility.AnimationUtility;
import com.example.kowherbz.utility.ClassPackageMaker;
import com.example.kowherbz.utility.DoubleClickHandler;
import com.example.kowherbz.utility.FavoritesUtils;
import com.example.kowherbz.utility.GuiColorManager;
import com.example.kowherbz.utility.OnItemClickFragmentRecyclerView;
import com.example.kowherbz.utility.OnItemClickListenerRelative;
import com.example.kowherbz.utility.SharedPreferenceUtility;
import com.example.kowherbz.utility.StringIDs;
import com.example.kowherbz.utility.SystemUtility;
import com.example.kowherbz.utility.ToastUtils;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MgaHalamangGamotActivity extends AppCompatActivity implements PlantListAdapter.OnFavoriteClickListener {
    private ViewPager2 mainViewPager;
    private AutoCompleteTextView search_bar;
    private FrameLayout btnSearchClear;
    private FrameLayout btnBack;
    private FrameLayout btnChangeSwipeOrientation;
    private DoubleClickHandler backHandler = new DoubleClickHandler();
    private LinearLayout top_layout;

    private ToastUtils mainToast = new ToastUtils(this);
    private Animation rotate;

    private int lastPosition = 0;
    private boolean isClearEnabled = false;

    private static final String MGA_HALAMANG_GAMOT_PREF_NAME = "mga_halamang_gamot_preference";
    private static final String ORIENTATION_PREF_NAME = "orientation";

    private PlantListAdapter plantListAdapter;
    private List<PlantContentHolder> contentHolderList;

    private int swipeOrientation;

    public static void createPreference(Context context){
        SharedPreferences preferences = SharedPreferenceUtility.getSharedPref(
                context, MGA_HALAMANG_GAMOT_PREF_NAME);
        if(!preferences.contains(ORIENTATION_PREF_NAME)){
            SharedPreferences.Editor prefEdit = preferences.edit();
            prefEdit.putInt(ORIENTATION_PREF_NAME, 0);
            prefEdit.apply();
        }

    }

    private void updateOrientationPref(Context context, int mode){
        SharedPreferences preferences = SharedPreferenceUtility.getSharedPref(
                context, MGA_HALAMANG_GAMOT_PREF_NAME);
        SharedPreferences.Editor prefEdit = preferences.edit();
        prefEdit.putInt(ORIENTATION_PREF_NAME, mode);
        prefEdit.apply();
    }

    private int getOrientation(Context context){
        SharedPreferences preferences = SharedPreferenceUtility.getSharedPref(
                context, MGA_HALAMANG_GAMOT_PREF_NAME);
        return preferences.getInt(ORIENTATION_PREF_NAME, 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mga_halamang_gamot);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        swipeOrientation = getOrientation(this);
        initializeViews();
        SetDefaultsColor();
        if((swipeOrientation = getOrientation(this)) == 0){
            setUpViewPagerMode1();
        } else if((swipeOrientation = getOrientation(this)) == 1){
            setUpViewPagerMode2();
        }

        btnSwipeBackgroundUpdate();

        setupSearchBar();
        startButtons();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = SystemUtility.STANDARD_FONT_SCALE;
        applyOverrideConfiguration(override);
    }

    private void SetDefaultsColor() {
        GuiColorManager.setNotificationBarColor(this,
                R.color.activity_notification_bar);
        btnBack.setBackground(ContextCompat.getDrawable(
                this, R.drawable.ic_instruction_back_button
        ));
        GuiColorManager.changeTint(top_layout, R.color.recycler_view_card_background_color2);
        GuiColorManager.changeTint(btnSearchClear, R.color.recycler_view_card_background_color2);
        search_bar.setTextColor(getColor(R.color.app_name_know_color));
        search_bar.setHint(R.string.search_hint_pangalan_ng_halaman);
    }

    private void btnSwipeBackgroundUpdate(){
        AnimationUtility.fadeOutThenHide(btnChangeSwipeOrientation, 800);
        if(swipeOrientation % 2 == 0){
            btnChangeSwipeOrientation.setBackground(ContextCompat.getDrawable(
                    MgaHalamangGamotActivity.this, R.drawable.ic_direction));
        } else if(swipeOrientation % 2 == 1){
            btnChangeSwipeOrientation.setBackground(ContextCompat.getDrawable(
                    MgaHalamangGamotActivity.this, R.drawable.ic_direction2));
        }
        AnimationUtility.fadeInThenShow(btnChangeSwipeOrientation, 800);
    }

    @Override
    public void onBackPressed() {
        backHandler.startIfSatisfied(() -> {
            mainToast.cancelToast();
            startActivity(new Intent(MgaHalamangGamotActivity.this, HomePageActivity.class));
            overridePendingTransition(AnimationUtility.fade_in_anim_intent, AnimationUtility.fade_out_anim_intent);
            finish();
        });
    }

    private void initializeViews() {
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        mainViewPager = findViewById(R.id.mga_halamang_gamot_viewpager);
        search_bar = findViewById(R.id.edt_search_bar_mga_halamang_gamot);
        btnSearchClear = findViewById(R.id.search_clear_mga_halamang_gamot);
        btnBack = findViewById(R.id.btn_back_halamang_gamot);
        btnChangeSwipeOrientation = findViewById(R.id.btn_change_swipe_direction_mga_halamang_gamot);
        top_layout = findViewById(R.id.mga_halamang_gamot_top_layout);
    }

    private void initializeSearchAdapter() {
        search_bar.getText().clear();
        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(
                this,
                R.layout.search_suggest_layout, R.id.text_view_search_layout,
                ClassPackageMaker.getHalamanListTitles());
        search_bar.setAdapter(searchAdapter);
    }

    private void setupSearchBar() {
        initializeSearchAdapter();
        search_bar.setOnItemClickListener((parent, view, position, id) -> {
            int destinationPos = ClassPackageMaker.getHalamanListTitles()
                    .indexOf(parent.getAdapter().getItem(position));
            if (destinationPos == mainViewPager.getCurrentItem()) {
                resetSearchBar();
            } else {
                mainViewPager.setCurrentItem(destinationPos);
            }
        });
    }

    private void resetSearchBar() {
        SystemUtility.forceKeyboardToStateHidden(MgaHalamangGamotActivity.this);
        search_bar.clearFocus();
        search_bar.getText().clear();
    }

    private void clearSearchBar() {
        search_bar.getText().clear();
    }

    private void setUpViewPagerMode1() {
        contentHolderList = new ArrayList<>(ClassPackageMaker.getHalamanList());
        mainViewPager.invalidate();

        plantListAdapter = new PlantListAdapter();
        plantListAdapter.setContentHolderList(contentHolderList);
        plantListAdapter.setOnFavoriteClickListener(this);

        mainViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mainViewPager.setAdapter(plantListAdapter);
        mainViewPager.setOffscreenPageLimit(1);
        mainViewPager.setClipToPadding(false);
        mainViewPager.setClipChildren(false);
        mainViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        mainViewPager.setPageTransformer(getPageTransformer(0));
        mainViewPager.setCurrentItem(lastPosition);
        setupViewPagerListener();
    }

    private void setUpViewPagerMode2() {
        contentHolderList = new ArrayList<>(ClassPackageMaker.getHalamanList());

        mainViewPager.invalidate();

        plantListAdapter = new PlantListAdapter();
        plantListAdapter.setContentHolderList(contentHolderList);
        plantListAdapter.setOnFavoriteClickListener(this);

        mainViewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        mainViewPager.setAdapter(plantListAdapter);
        mainViewPager.setOffscreenPageLimit(1);
        mainViewPager.setClipToPadding(false);
        mainViewPager.setClipChildren(false);
        mainViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        mainViewPager.setPageTransformer(getPageTransformer(1));
        mainViewPager.setCurrentItem(lastPosition);
        setupViewPagerListener();
    }

    private CompositePageTransformer getPageTransformer(int orientation){
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            if(orientation == 0)
                page.setScaleY(0.85f + r * 0.15f);
            else if(orientation == 1)
                page.setScaleX(0.85f + r * 0.20f);
        });
        return compositePageTransformer;
    }

    private void setupViewPagerListener() {
        mainViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                mainToast.cancelToast();
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (isClearEnabled) {
                    resetSearchBar();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                isClearEnabled = state == ViewPager2.SCROLL_STATE_SETTLING;
            }
        });
    }

    private void startButtons() {
        DoubleClickHandler doubleClickHandler = new DoubleClickHandler();
        btnSearchClear.setOnClickListener(v -> doubleClickHandler.startIfSatisfied(this::clearSearchBar));

        btnBack.setOnClickListener(v -> onBackPressed());

        DoubleClickHandler swipeClickHandler = new DoubleClickHandler();
        swipeClickHandler.setToggleCount(swipeOrientation);

        btnChangeSwipeOrientation.setOnClickListener(v -> swipeClickHandler.startIfSatisfied(() -> {
            lastPosition = mainViewPager.getCurrentItem();
            swipeClickHandler.toggleExecutor(() -> {
                //vertical swipe
                mainToast.displayOnToastShort("Swipe was changed to TOP|DOWN direction");
                updateOrientationPref(MgaHalamangGamotActivity.this, 1);
                setUpViewPagerMode2();
            }, () -> {
                //horizontal swipe
                updateOrientationPref(MgaHalamangGamotActivity.this, 0);
                mainToast.displayOnToastShort("Swipe was changed to LEFT|RIGHT direction");
                setUpViewPagerMode1();
            });
            swipeOrientation = swipeClickHandler.getToggleCount();
            btnSwipeBackgroundUpdate();
        }));

    }

    @Override
    public void OnFavClick(int position) {
        FavoritesUtils.Plants.toggleFavState(this, contentHolderList.get(position), mainToast, true);
        plantListAdapter.notifyItemChanged(position);
    }
}