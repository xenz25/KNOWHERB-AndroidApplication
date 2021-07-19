package com.example.kowherbz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.kowherbz.R;
import com.example.kowherbz.adapter.TabViewPagerAdapter;
import com.example.kowherbz.fragment.SakitAtLunasFragment1;
import com.example.kowherbz.fragment.SakitAtLunasFragment2;
import com.example.kowherbz.utility.AnimationUtility;
import com.example.kowherbz.utility.ClassPackageMaker;
import com.example.kowherbz.utility.DoubleClickHandler;
import com.example.kowherbz.utility.GuiColorManager;
import com.example.kowherbz.utility.ParentFragmentUtils;
import com.example.kowherbz.utility.RawResourceIDs;
import com.example.kowherbz.utility.SystemUtility;
import com.example.kowherbz.utility.ToastUtils;
import com.google.android.material.internal.ToolbarUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SakitAtLunasActivity extends AppCompatActivity {
    private TabLayout tab_sakitAtLunas;
    private ViewPager viewPager_sakitAtLunas;
    private TabViewPagerAdapter tabViewPagerAdapter;
    private FrameLayout btn_back_sakit_at_lunas;
    private LinearLayout sakit_at_lunas_top_layout;

    private FrameLayout search_clear;
    private AutoCompleteTextView searchBar;
    private ArrayAdapter<String> searchAdapter;

    private final DoubleClickHandler backClickHandler = new DoubleClickHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sakit_at_lunas);

        initializeViews();
        SetDefaultsColor();
        setUpSearchBar();
        setUpTabs();
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
        SystemUtility.disablesSoftKeyboardPopping(this);
    }

    @Override
    public void onBackPressed() {
        backClickHandler.startIfSatisfied(() -> {
            startActivity(new Intent(SakitAtLunasActivity.this, HomePageActivity.class));
            overridePendingTransition(AnimationUtility.fade_in_anim_intent, AnimationUtility.fade_out_anim_intent);
            finish();
        });
    }

    private void initializeViews() {
        GuiColorManager.setNotificationBarColor(this, R.color.activity_notification_bar);
        tab_sakitAtLunas = findViewById(R.id.tabLayout_sakit_At_luans);
        viewPager_sakitAtLunas = findViewById(R.id.vp_sakit_at_lunas);
        btn_back_sakit_at_lunas = findViewById(R.id.btn_back_sakit_at_lunas);
        sakit_at_lunas_top_layout = findViewById(R.id.sakit_at_lunas_top_layout);

        search_clear = findViewById(R.id.search_clear_sakit_at_lunas);
        searchBar = findViewById(R.id.edt_search_bar_sakit_at_lunas);
        searchBar.clearFocus();
        searchBar.getText().clear();
    }

    private void initializeSearchAdapter() {
        searchBar.getText().clear();
        searchAdapter = new ArrayAdapter<>(
                this,
                R.layout.search_suggest_layout, R.id.text_view_search_layout,
                viewPager_sakitAtLunas.getCurrentItem() == 0 ? new ArrayList<>(ClassPackageMaker.getSakitAtLunasListTitles()) :
                        new ArrayList<>(ClassPackageMaker.getHydroterapiListTitles())
        );
        searchBar.setAdapter(searchAdapter);
    }

    private Fragment getActiveFragment(){
        return ParentFragmentUtils.getParentFragmentInstance(this, R.id.vp_sakit_at_lunas, viewPager_sakitAtLunas);
    }

    private void setUpSearchBar() {
        initializeSearchAdapter();
        searchBar.setOnItemClickListener((parent, view, position, id) -> {
            //smooth scroll on search
            if (viewPager_sakitAtLunas.getCurrentItem() == 0) {
                SakitAtLunasFragment1 fragment1 = (SakitAtLunasFragment1) getActiveFragment();

                fragment1.getLinearLayoutManager().startSmoothScroll(
                        AnimationUtility.getSmoothScroller(this, ClassPackageMaker
                                .getSakitAtLunasListTitles()
                                .indexOf(parent.getAdapter().getItem(position))));

            } else if (viewPager_sakitAtLunas.getCurrentItem() == 1) {
                SakitAtLunasFragment2 fragment2 = (SakitAtLunasFragment2) getActiveFragment();

                fragment2.getLayoutManager().startSmoothScroll(
                        AnimationUtility.getSmoothScroller(this, ClassPackageMaker
                                .getHydroterapiListTitles()
                                .indexOf(parent.getAdapter().getItem(position))));
            }
        });
    }

    private void setUpTabs() {
        List<Pair<String, Fragment>> fragmentList = new ArrayList<>();
        fragmentList.add(Pair.create("Sakit", new SakitAtLunasFragment1()));
        fragmentList.add(Pair.create("Hydroterapi", new SakitAtLunasFragment2()));

        tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        tabViewPagerAdapter.setFragmentList(fragmentList);

        viewPager_sakitAtLunas.setAdapter(tabViewPagerAdapter);

        tab_sakitAtLunas.addTab(tab_sakitAtLunas.newTab());
        tab_sakitAtLunas.addTab(tab_sakitAtLunas.newTab());

        tab_sakitAtLunas.setupWithViewPager(viewPager_sakitAtLunas);
    }

    private void SetDefaultsColor() {
        GuiColorManager.setNotificationBarColor(SakitAtLunasActivity.this,
                R.color.activity_notification_bar);
        btn_back_sakit_at_lunas.setBackground(ContextCompat.getDrawable(
                SakitAtLunasActivity.this, R.drawable.ic_instruction_back_button
        ));
        GuiColorManager.changeTint(sakit_at_lunas_top_layout, R.color.recycler_view_card_background_color2);
        tab_sakitAtLunas.setSelectedTabIndicatorColor(getColor(R.color.recycler_view_card_background_color2));
        GuiColorManager.changeTint(search_clear, R.color.recycler_view_card_background_color2);
        searchBar.setTextColor(getColor(R.color.app_name_know_color));
        searchBar.setHint(R.string.search_hint_pangalan_ng_sakit);
    }

    private void startActivity() {
        btn_back_sakit_at_lunas.setOnClickListener(v -> onBackPressed());

        DoubleClickHandler searchClearHandler = new DoubleClickHandler();
        search_clear.setOnClickListener(v -> searchClearHandler.startIfSatisfied(() -> searchBar.getText().clear()));

        viewPager_sakitAtLunas.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initializeSearchAdapter();
                if (position == 0) {
                    SetDefaultsColor();
                } else if (position == 1) {
                    GuiColorManager.setNotificationBarColor(SakitAtLunasActivity.this,
                            R.color.hydroterapi_primaryDarkColor);
                    btn_back_sakit_at_lunas.setBackground(ContextCompat.getDrawable(
                            SakitAtLunasActivity.this, R.drawable.ic_instruction_back_button_hydroterapi
                    ));
                    GuiColorManager.changeTint(btn_back_sakit_at_lunas, R.color.hydroterapi_secondaryColor);
                    GuiColorManager.changeTint(sakit_at_lunas_top_layout, R.color.hydroterapi_primaryColor);
                    tab_sakitAtLunas.setSelectedTabIndicatorColor(getColor(R.color.hydroterapi_secondaryColor));
                    GuiColorManager.changeTint(search_clear, R.color.hydroterapi_primaryDarkColor);
                    searchBar.setTextColor(getColor(R.color.hydroterapi_primaryDarkColor));
                    searchBar.setHint(R.string.search_hint_hydroterapi);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static void clearSearchBarFocus(AutoCompleteTextView autoCompleteTextView){
        autoCompleteTextView.getText().clear();
        autoCompleteTextView.clearFocus();
    }

}