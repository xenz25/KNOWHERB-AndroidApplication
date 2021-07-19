package com.example.kowherbz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Pair;
import android.widget.FrameLayout;

import com.example.kowherbz.R;
import com.example.kowherbz.adapter.TabViewPagerAdapter;
import com.example.kowherbz.fragment.GinamitSaAppFragment;
import com.example.kowherbz.fragment.GumawaFragment;
import com.example.kowherbz.fragment.MayAkdaFragment;
import com.example.kowherbz.utility.DoubleClickHandler;
import com.example.kowherbz.utility.GuiColorManager;
import com.example.kowherbz.utility.SystemUtility;

import java.util.ArrayList;
import java.util.List;

public class NaglinangActivity extends AppCompatActivity {
    private ViewPager mainViewPager;
    private TabViewPagerAdapter viewPagerAdapter;
    private List<Pair<String, Fragment>> availableFragmentList;

    private DoubleClickHandler backBtnHandler = new DoubleClickHandler();
    private FrameLayout backButton;


    @Override
    protected void onStart() {
        super.onStart();
        GuiColorManager.setNotificationBarColor(this, R.color.primaryColor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naglinang);

        initializeViews();
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
    public void onBackPressed() {
        backBtnHandler.startIfSatisfied(() -> {
            startActivity(new Intent(NaglinangActivity.this, HomePageActivity.class));
            overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
            finish();
        });

    }

    private void initializeViews() {
        mainViewPager= findViewById(R.id.naglinang_view_pager);
        availableFragmentList = new ArrayList<>();
        availableFragmentList.add(Pair.create("Pinagbatayan", new MayAkdaFragment()));
        availableFragmentList.add(Pair.create("Naglinang", new GumawaFragment()));
        availableFragmentList.add(Pair.create("API", new GinamitSaAppFragment()));

        viewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.setFragmentList(availableFragmentList);

        mainViewPager.setAdapter(viewPagerAdapter);

        backButton = findViewById(R.id.naglinang_btn_back);
    }

    private void startActivity(){
        backButton.setOnClickListener(v -> this.onBackPressed());
    }

}