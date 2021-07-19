package com.example.kowherbz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.example.kowherbz.R;
import com.example.kowherbz.adapter.TabViewPagerAdapter;
import com.example.kowherbz.fragment.PaboritongHalamanFragment;
import com.example.kowherbz.fragment.PaboritongHydroterapiFragment;
import com.example.kowherbz.fragment.PaboritongSakitFragment;
import com.example.kowherbz.utility.AnimationUtility;
import com.example.kowherbz.utility.ClassPackageMaker;
import com.example.kowherbz.utility.DoubleClickHandler;
import com.example.kowherbz.utility.FavoritesUtils;
import com.example.kowherbz.utility.GuiColorManager;
import com.example.kowherbz.utility.ParentFragmentUtils;
import com.example.kowherbz.utility.StringIDs;
import com.example.kowherbz.utility.SystemUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaboritoActivity extends AppCompatActivity {
    private ViewPager mainViewPager;
    private TabViewPagerAdapter viewPagerAdapter;
    private RelativeLayout tab_parent_layout;
    private FrameLayout empty_indicator;

    //private FrameLayout searchButton;

    private TextView tab_title;
    private FrameLayout backButton;
    private FrameLayout prev, next;
    private FrameLayout clearSearch;
    private RelativeLayout searchBarParent;
    private AutoCompleteTextView searchBar;

    private final DoubleClickHandler backHandler = new DoubleClickHandler();

    private int viewPagerPosition = 0;

    private List<Pair<String, Fragment>> availableFragmentList;
    private List<Pair<String, String>> availableTabs;

    private final String[] tabTiles = {"SAKIT", "HYDROTERAPI", "HALAMANG GAMOT"};
    private final int[] activeTabsIndicator = {0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paborito);

        initializeViews();
        setUpViewPager();
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
        backHandler.startIfSatisfied(() -> {
            startActivity(new Intent(PaboritoActivity.this, HomePageActivity.class));
            overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
            finish();
        });
    }

    private void initializeViews() {
        getViewPagerPosition();

        searchBarParent = findViewById(R.id.paborito_search_parent);
        tab_parent_layout = findViewById(R.id.header_parent_layout_paborito);
        empty_indicator = findViewById(R.id.empty_indicator_paborito);

        mainViewPager = findViewById(R.id.viewpager_paborito);
        backButton = findViewById(R.id.btn_back_paborito);
        prev = findViewById(R.id.prev_button_paborito);
        next = findViewById(R.id.next_button_paborito);
        //searchButton = findViewById(R.id.btnSearchActivate);
        searchBar = findViewById(R.id.edt_search_bar_sakit_at_lunas);
        clearSearch = findViewById(R.id.search_clear_paborito);
        tab_title = findViewById(R.id.tab_title_paborito);

        searchBar.clearFocus();
        SystemUtility.disablesSoftKeyboardPopping(this);
    }

    private void getViewPagerPosition(){
        if(ClassPackageMaker.isPackageAvailable(R.string.left_position_serial_name, this)){
            viewPagerPosition = (int) getIntent().getSerializableExtra(
                    StringIDs.getStringValue(this, R.string.left_position_serial_name));
        }
    }

    private void setUpViewPager() {
        viewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());

        availableTabs = new ArrayList<>();
        availableFragmentList = new ArrayList<>();

        //checking available tabs and building up contents
        if(FavoritesUtils.Illness.isEmpty() && FavoritesUtils.Hydrotherapy.isEmpty() && FavoritesUtils.Plants.isEmpty()){
            searchBarParent.setVisibility(View.GONE);
            tab_parent_layout.setVisibility(View.GONE);
            empty_indicator.setVisibility(View.VISIBLE);
            mainViewPager.setVisibility(View.GONE);
        } else {
            if(!FavoritesUtils.Illness.isEmpty()){
                activeTabsIndicator[0] = 1;
                availableTabs.add(Pair.create(tabTiles[0], StringIDs.getStringValue(this, R.string.search_hint_pangalan_ng_sakit)));
                availableFragmentList.add(Pair.create("Paboritong_Sakit", new PaboritongSakitFragment(this)));
            }
            if(!FavoritesUtils.Hydrotherapy.isEmpty()){
                activeTabsIndicator[1] = 1;
                availableTabs.add(Pair.create(tabTiles[1], StringIDs.getStringValue(this, R.string.search_hint_hydroterapi)));
                availableFragmentList.add(Pair.create("Paboritong_Hydroterapi", new PaboritongHydroterapiFragment(this)));
            }
            if(!FavoritesUtils.Plants.isEmpty()){
                activeTabsIndicator[2] = 1;
                availableTabs.add(Pair.create(tabTiles[2], StringIDs.getStringValue(this, R.string.search_hint_pangalan_ng_halaman)));
                availableFragmentList.add(Pair.create("Paboritong_Halaman", new PaboritongHalamanFragment(this)));
            }
        }

        viewPagerAdapter.setFragmentList(availableFragmentList);
        mainViewPager.setAdapter(viewPagerAdapter);

        updateTabContents();
        updateButtonIndicatorsByPosition();

        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                searchBar.clearFocus();
                SystemUtility.forceKeyboardToStateHidden(PaboritoActivity.this);
            }

            @Override
            public void onPageSelected(int position) {
                //perform search filter if the user swipe the viewpager and the search bar has contents
                Filter filter;
                if((filter = getActiveFragmentFilter()) != null){
                    filter.filter(searchBar.getText().toString());
                }
                updateTabs();
                updateButtonIndicatorsByPosition();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(viewPagerPosition == 1 && activeTabsIndicator[0] == 0){
            mainViewPager.setCurrentItem(0);
        } else {
            mainViewPager.setCurrentItem(viewPagerPosition);
        }
    }

    public void clearSearchBarFocus(){
        this.searchBar.clearFocus();
    }

    private void updateTabs(){
        int position = mainViewPager.getCurrentItem();
        AnimationUtility.yoyoAnimate(Techniques.FadeOut, 350, tab_title);
        tab_title.setVisibility(View.INVISIBLE);
        updateTabContents();
        AnimationUtility.yoyoAnimate(Techniques.FadeIn, 350, tab_title);
        tab_title.setVisibility(View.VISIBLE);
    }

    private void updateTabContents(){
        if(!availableTabs.isEmpty()){
            tab_title.setText(availableTabs.get(mainViewPager.getCurrentItem()).first);
            searchBar.setHint(availableTabs.get(mainViewPager.getCurrentItem()).second);
        }
    }

    private void updateButtonIndicatorsByPosition(){
        int size = availableFragmentList.size();
        int position = mainViewPager.getCurrentItem();

        if(size == 1){
            changePrevEnable(false);
            changeNextEnable(false);
        } else if(size == 2){
            if(position == 0){
                changePrevEnable(false);
                changeNextEnable(true);
            } else {
                changePrevEnable(true);
                changeNextEnable(false);
            }
        } else {
            if(position == 0){
                changePrevEnable(false);
                changeNextEnable(true);
            } else if(position == size - 1){
                changePrevEnable(true);
                changeNextEnable(false);
            } else {
                changePrevEnable(true);
                changeNextEnable(true);
            }
        }
    }

    private void changePrevEnable(boolean mode) {
        if (mode) {
            prev.setVisibility(View.VISIBLE);
            prev.setEnabled(true);
        } else {
            prev.setVisibility(View.INVISIBLE);
            prev.setEnabled(false);
        }
    }

    private void changeNextEnable(boolean mode) {
        if (mode) {
            next.setVisibility(View.VISIBLE);
            next.setEnabled(true);
        } else {
            next.setVisibility(View.INVISIBLE);
            next.setEnabled(false);
        }
    }

    private void startActivity() {
        backButton.setOnClickListener(v -> onBackPressed());

        DoubleClickHandler clearSearchHandler = new DoubleClickHandler();
        clearSearch.setOnClickListener(v -> clearSearchHandler.startIfSatisfied(() -> {
            searchBar.getText().clear();
        }));

        prev.setOnClickListener(v -> setViewPagerItem(mainViewPager.getCurrentItem() - 1));
        next.setOnClickListener(v -> setViewPagerItem(mainViewPager.getCurrentItem() + 1));

       /* DoubleClickHandler searchButtonHandler = new DoubleClickHandler();
        searchButton.setOnClickListener(v -> searchButtonHandler.toggleExecutor(() -> {
            searchBarParent.setVisibility(View.VISIBLE); //show search bar
        },  () -> {
            searchBarParent.setVisibility(View.GONE); //hide search bar
            searchBar.getText().clear();
            searchBar.clearFocus();
            SystemUtility.forceKeyboardToStateHidden(this);
        }));*/

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Filter filter = getActiveFragmentFilter();
                if(filter != null) filter.filter(s);
                //dismiss the active fragment snackbar to explicitly remove the selected item for removal
                dismissFragmentSnackBar();
            }
        });
    }

    private Filter getActiveFragmentFilter(){
        String activeTab = availableTabs.get(mainViewPager.getCurrentItem()).first;

        if(activeTab.equals(tabTiles[0])){
            PaboritongSakitFragment fragment1 = (PaboritongSakitFragment) getActiveFragment();
            if(fragment1 != null) return fragment1.getFavoritesListAdapter().getFilter();
        } else if(activeTab.equals(tabTiles[1])){
            PaboritongHydroterapiFragment fragment1 = (PaboritongHydroterapiFragment) getActiveFragment();
            if(fragment1 != null) return fragment1.getFavoritesListAdapter().getFilter();
        }
        PaboritongHalamanFragment fragment1 = (PaboritongHalamanFragment) getActiveFragment();
        if(fragment1 != null) return fragment1.getFavoritePlantsAdapter().getFilter();
        return null;
    }

    private void dismissFragmentSnackBar(){
        String activeTab = availableTabs.get(mainViewPager.getCurrentItem()).first;

        if(activeTab.equals(tabTiles[0])){
            PaboritongSakitFragment fragment1 = (PaboritongSakitFragment) getActiveFragment();
            if(fragment1 != null) fragment1.getSnacksUtils().dismissSnackBar();
        } else if(activeTab.equals(tabTiles[1])){
            PaboritongHydroterapiFragment fragment1 = (PaboritongHydroterapiFragment) getActiveFragment();
            if(fragment1 != null) fragment1.getSnacksUtils().dismissSnackBar();
        } else {
            PaboritongHalamanFragment fragment1 = (PaboritongHalamanFragment) getActiveFragment();
            if(fragment1 != null)  fragment1.getSnacksUtils().dismissSnackBar();
        }
    }

    private Fragment getActiveFragment(){
        return ParentFragmentUtils.getParentFragmentInstance(this, R.id.viewpager_paborito, mainViewPager);
    }

    private void setViewPagerItem(int page) {
        if(page >= 0 && page <= 2) mainViewPager.setCurrentItem(page);
        else mainViewPager.setCurrentItem(mainViewPager.getCurrentItem());
    }
}