package com.example.kowherbz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kowherbz.R;
import com.example.kowherbz.adapter.InstructionAdapter;
import com.example.kowherbz.holder.HydroterapiItemHolder;
import com.example.kowherbz.holder.ParentContentHolder;
import com.example.kowherbz.holder.SakitAtLunasItemHolder;
import com.example.kowherbz.utility.ClassPackageMaker;
import com.example.kowherbz.utility.DoubleClickHandler;
import com.example.kowherbz.utility.FavoritesUtils;
import com.example.kowherbz.utility.GuiColorManager;
import com.example.kowherbz.utility.JsonGrabber;
import com.example.kowherbz.utility.RawResourceIDs;
import com.example.kowherbz.utility.StringIDs;
import com.example.kowherbz.utility.SystemUtility;
import com.example.kowherbz.utility.ToastUtils;
import com.google.android.material.imageview.ShapeableImageView;

import static com.example.kowherbz.adapter.InstructionAdapter.HYDROTHERAPY_STATE_NUMBER;

public class HydroterapiInstructionViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView description_container, title;
    private ParentContentHolder parentContentHolder;

    //buttons
    private FrameLayout button_back;
    private FrameLayout button_favorites;


    private boolean isOnCompleteProgress = false;

    private final DoubleClickHandler backClickHandler = new DoubleClickHandler();

    private MotionLayout motionLayout;

    private boolean isFavorite = false;

    private final ToastUtils toastUtils = new ToastUtils(this);

    private String lower_case;
    private boolean isFromFavHydro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hydroterapi_instruction_view);

        initializeViews();
        grabRequiredData();
        setUpHeaders();
        setUpRecyclerView();
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
        backClickHandler.startIfSatisfied(() -> {
            if (isOnCompleteProgress) {
                motionLayout.transitionToState(motionLayout.getStartState(), 500);
            } else {
                toastUtils.cancelToast();
                if(isFromFavHydro){
                    Intent intent = new Intent(HydroterapiInstructionViewActivity.this, PaboritoActivity.class);
                    intent.putExtra(StringIDs.getStringValue(
                            this, R.string.left_position_serial_name), 1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
                    finish();
                } else {
                    HydroterapiInstructionViewActivity.super.onBackPressed();
                }
            }
        });
    }

    private void initializeViews() {
        GuiColorManager.setNotificationBarColor(this, R.color.hydroterapi_primaryDarkColor);
        motionLayout = findViewById(R.id.hydroterapi_motion_layout);
        recyclerView = findViewById(R.id.hydroterapi_instruction_recycler_view);

        button_back = findViewById(R.id.btn_back_hydroterapi_instruction_activity);
        GuiColorManager.changeTint(button_back, R.color.hydroterapi_secondaryDarkColor);
        button_favorites = findViewById(R.id.btn_favorites_hydroterapi_instruction_activity);

        title = findViewById(R.id.tv_hydroterapi_instruction_title);
        description_container = findViewById(R.id.tv_hydroterapi_instruction_description);
    }

    private void grabRequiredData() {
        if (ClassPackageMaker.isPackageAvailable(R.string.hydroterapi_serial_name, this)) {
            lower_case  = (String) getIntent().getSerializableExtra(
                    StringIDs.getStringValue(this, R.string.hydroterapi_serial_name));
            parentContentHolder = JsonGrabber.grabJsonDataFromRaw(
                    this, RawResourceIDs.findInMapHydrotearapi(lower_case)
            );
            parentContentHolder.setLower_case_name(lower_case);
        }

        if(ClassPackageMaker.isPackageAvailable(R.string.is_from_fav_hydroterapi_serial, this)){
            isFromFavHydro = (boolean) getIntent().getSerializableExtra(
                    StringIDs.getStringValue(this, R.string.is_from_fav_hydroterapi_serial));
        }
    }

    private void setUpHeaders() {
        title.setText(parentContentHolder.getTitleHolder().getTitleOfSickness());
        description_container.setText(parentContentHolder.getTitleHolder().getDefinitionOfSickness());

        isFavorite = FavoritesUtils.Hydrotherapy.isHydrotherapyFavorite(this, parentContentHolder.getLower_case_name());
        updateFavoriteIndicator();
    }

    private void updateFavoriteIndicator(){
        Drawable fav_tint_selected = button_favorites.getBackground();
        fav_tint_selected = DrawableCompat.wrap(fav_tint_selected);
        int colorID = R.color.favorite_water_selected2;
        if(!isFavorite){
            colorID = R.color.favorite_water_unselected;
        }
        DrawableCompat.setTint(fav_tint_selected, ContextCompat.getColor(this, colorID));
        button_favorites.setBackground(fav_tint_selected);
    }

    private void setUpRecyclerView() {
        InstructionAdapter instructionAdapter = new InstructionAdapter(parentContentHolder);
        instructionAdapter.setHyperLinkColorStates(Pair.create(HYDROTHERAPY_STATE_NUMBER,
                new int[]{R.color.highlight_color_hydroterapi, R.color.hyper_link_color_hydroterapi}));
        recyclerView.setAdapter(instructionAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void startActivity() {
        button_back.setOnClickListener(v -> onBackPressed());

        DoubleClickHandler favClickHandler = new DoubleClickHandler();
        button_favorites.setOnClickListener(v -> favClickHandler.startIfSatisfied(() -> {
            isFavorite = !isFavorite;
            if(isFavorite){
                FavoritesUtils.Hydrotherapy.addToFavorites(HydroterapiInstructionViewActivity.this, parentContentHolder);
                toastUtils.displayOnToastShort(FavoritesUtils.ITEM_ADDED_TO_FAVORITES);
            } else {
                FavoritesUtils.Hydrotherapy.removeToFavorites(HydroterapiInstructionViewActivity.this, lower_case);
                toastUtils.displayOnToastShort(FavoritesUtils.ITEM_REMOVED_TO_FAVORITES);
            }
            updateFavoriteIndicator();
        }));

        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
                isOnCompleteProgress = progress >= 0.8;
                if (isOnCompleteProgress) {
                    GuiColorManager.setNotificationBarColor(HydroterapiInstructionViewActivity.this, R.color.hydroterapi_secondaryDarkColor);
                } else {
                    GuiColorManager.setNotificationBarColor(HydroterapiInstructionViewActivity.this, R.color.hydroterapi_primaryDarkColor);
                }
                title.setAlpha((float) 1.0 - progress);
                button_back.setAlpha((float) 1.0 - progress);
                button_favorites.setAlpha((float) 1.0 - progress);
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {

            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }
        });
    }
}