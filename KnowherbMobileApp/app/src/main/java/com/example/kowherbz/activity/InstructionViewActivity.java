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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kowherbz.R;
import com.example.kowherbz.adapter.InstructionAdapter;
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

import static com.example.kowherbz.adapter.InstructionAdapter.SAKIT_STATE_NUMBER;

public class InstructionViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView description_container, title;
    private ShapeableImageView imageView;
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
    private boolean isFromIllnessFav = false;
    private int lastPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_view);

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
                if(isFromIllnessFav){
                    Intent intent = new Intent(InstructionViewActivity.this, PaboritoActivity.class);
                    intent.putExtra(StringIDs.getStringValue(this, R.string.left_position_serial_name), 0);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
                    finish();
                } else {
                    InstructionViewActivity.super.onBackPressed();
                }
            }
        });
    }

    private void initializeViews() {
        GuiColorManager.setNotificationBarColor(this, R.color.app_name_know_color);
        motionLayout = findViewById(R.id.motion_layout);
        recyclerView = findViewById(R.id.instruction_recycler_view);

        button_back = findViewById(R.id.btn_back_instruction_activity);
        button_favorites = findViewById(R.id.btn_favorites_instruction_activity);

        title = findViewById(R.id.tv_instruction_title);
        description_container = findViewById(R.id.tv_instruction_description);
        imageView = findViewById(R.id.instruction_activity_image_container);
    }

    private void grabRequiredData() {
        if (ClassPackageMaker.isPackageAvailable(R.string.sakit_at_lunas_serial_name, this)) {
            lower_case = (String) getIntent().getSerializableExtra(
                    StringIDs.getStringValue(this, R.string.sakit_at_lunas_serial_name));

            parentContentHolder = JsonGrabber.grabJsonDataFromRaw(
                    this, RawResourceIDs.findInMapSakitAtLunas(lower_case)
            );
            parentContentHolder.setLower_case_name(lower_case);
        }

        if(ClassPackageMaker.isPackageAvailable(R.string.is_from_fav_illnes_serial, this)){
            isFromIllnessFav = (boolean) getIntent().getSerializableExtra(
                    StringIDs.getStringValue(this, R.string.is_from_fav_illnes_serial)
            );
        }
    }

    private void setUpHeaders() {
        Glide.with(this).load(
                ClassPackageMaker.getImageResourceID(
                        this, parentContentHolder.getLower_case_name())).into(imageView);
        title.setText(parentContentHolder.getTitleHolder().getTitleOfSickness());
        description_container.setText(parentContentHolder.getTitleHolder().getDefinitionOfSickness());

        isFavorite = FavoritesUtils.Illness.isIllnessFavorite(this, parentContentHolder.getLower_case_name());
        updateFavoriteIndicator();
    }

    private void updateFavoriteIndicator(){
        int colorID = R.color.favorite_leaf_selected2;
        if(!isFavorite){
            colorID = R.color.favorite_leaf_unselected;
        }
        GuiColorManager.changeTint(button_favorites, colorID);
    }

    private void setUpRecyclerView() {
        InstructionAdapter instructionAdapter = new InstructionAdapter(parentContentHolder);
        instructionAdapter.setHyperLinkColorStates(Pair.create(SAKIT_STATE_NUMBER,
                new int[]{R.color.highlight_color, R.color.hyper_link_color}));
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
                FavoritesUtils.Illness.addToFavorites(InstructionViewActivity.this, parentContentHolder);
                toastUtils.displayOnToastShort(FavoritesUtils.ITEM_ADDED_TO_FAVORITES);
            } else {
                FavoritesUtils.Illness.removeToFavorites(InstructionViewActivity.this, lower_case);
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
                    GuiColorManager.setNotificationBarColor(InstructionViewActivity.this, R.color.secondaryDarkColor);
                } else {
                    GuiColorManager.setNotificationBarColor(InstructionViewActivity.this, R.color.app_name_know_color);
                }
                button_back.setAlpha((float) 1.0 - progress);
                imageView.setAlpha((float) 1.0 - progress);
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