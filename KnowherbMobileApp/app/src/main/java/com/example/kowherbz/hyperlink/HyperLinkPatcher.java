package com.example.kowherbz.hyperlink;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Pair;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kowherbz.R;
import com.example.kowherbz.activity.HydroterapiInstructionViewActivity;
import com.example.kowherbz.activity.InstructionViewActivity;
import com.example.kowherbz.adapter.PlantListAdapter;
import com.example.kowherbz.holder.ContentHolder;
import com.example.kowherbz.holder.HydroterapiItemHolder;
import com.example.kowherbz.holder.ParentContentHolder;
import com.example.kowherbz.holder.PlantContentHolder;
import com.example.kowherbz.utility.ClassPackageMaker;
import com.example.kowherbz.utility.DoubleClickHandler;
import com.example.kowherbz.utility.FavoritesUtils;
import com.example.kowherbz.utility.GuiColorManager;
import com.example.kowherbz.utility.JsonGrabber;
import com.example.kowherbz.utility.RawResourceIDs;
import com.example.kowherbz.utility.StringIDs;
import com.example.kowherbz.utility.ToastUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HyperLinkPatcher {

    public static SpannableString patchHyperHeaderLink(Context context, String header, TextView atTextView){
        SpannableString spannableString = new SpannableString(header);
        ClickableSpan ck = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(context, header, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                Typeface bold_italic = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD_ITALIC);
                ds.setTypeface(bold_italic);
                ds.setColor(ContextCompat.getColor(context, R.color.app_name_know_color));
                ds.setUnderlineText(false);
                atTextView.invalidate();
            }
        };
        spannableString.setSpan(ck, 0, header.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString patchHyperLink(Context context, String parentText, List<Pair<String, Integer>> hyperLinkInstruction, TextView atTextView, int color_on_draw_state){
        SpannableString main = new SpannableString(parentText);

        for (Pair<String, Integer> hyperWord : hyperLinkInstruction) {
            //search the word to sampleText and matcher will automatically handle the start and end index
            //we can also get the substring
            Pattern pattern = Pattern.compile("(" + hyperWord.first + ")");
            Matcher matcher = pattern.matcher(parentText);
            while(matcher.find()) {
                HyperLinkWord2 hpw_2 = new HyperLinkWord2(matcher.start(), matcher.end(), hyperWord);
                main = new SpannableString(setAnOnClickListener(context, atTextView, main,
                        hpw_2.getStartIndex(), hpw_2.getEndIndex(), hpw_2.getSearchPair(), color_on_draw_state));
            }
        }
        return main;
    }

    private static SpannableString setAnOnClickListener(Context context, TextView tv, SpannableString parentString, int start, int end, Pair<String, Integer> searchPair, int color_on_draw_state) {
        SpannableString spannable = new SpannableString(parentString);
        ClickableSpan ck = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                //TODO inside this on click is a function that searches the given sub string
                // if for hydroterapi or for plant then it will auto matically direct user
                // to specific activity
                String name_lower_case;
                Intent active_intent = null;

                if (searchPair.second == ContentHolder.HYDROTERAPI) {
                    active_intent = new Intent(context, HydroterapiInstructionViewActivity.class);

                    name_lower_case = ClassPackageMaker.getLowerCaseNameHydroterapi(context, searchPair.first);
                    active_intent.putExtra(StringIDs.getStringValue(context, R.string.hydroterapi_serial_name),
                            name_lower_case);

                } else if(searchPair.second == ContentHolder.SAKIT){
                    active_intent = new Intent(context, InstructionViewActivity.class);

                    name_lower_case = ClassPackageMaker.getLowerCaseNameSakitAtLunas(context, searchPair.first);
                    active_intent.putExtra(StringIDs.getStringValue(context, R.string.sakit_at_lunas_serial_name),
                            name_lower_case);
                } else if(searchPair.second == ContentHolder.HALAMAN){
                    ToastUtils mainTaost = new ToastUtils(context);
                    Dialog plantDialog = new Dialog(context);

                    plantDialog.setContentView(R.layout.plant_dialog_layout);
                    plantDialog.setCancelable(false);
                    plantDialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;

                    //init buttons
                    FrameLayout close_dialog = plantDialog.getWindow().findViewById(R.id.close_dialog_plant_dialog);
                    FrameLayout fav_button = plantDialog.getWindow().findViewById(R.id.btn_fav_plant_dialog);



                    TextView title = plantDialog.getWindow().findViewById(R.id.tv_title_mga_plant_dialog);
                    RecyclerView mainRecyclerView = plantDialog.getWindow().findViewById(R.id.recyclerView_common_names_plant_dialog);
                    ShapeableImageView imageContainer = plantDialog.getWindow().findViewById(R.id.plant_dialog_image_container);

                    //close button handling
                    DoubleClickHandler closeHandler = new DoubleClickHandler();
                    close_dialog.setOnClickListener(v -> closeHandler.startIfSatisfied(() -> {
                        mainTaost.cancelToast();
                        plantDialog.dismiss();
                    }));

                    //getting data of plant
                    PlantContentHolder plant = ClassPackageMaker.getHalamanList().get(ClassPackageMaker.getIndexByTitlePlant(searchPair.first));
                    title.setText(plant.getPlant_name());

                    changeFavButtonBackground(context, fav_button, plant);

                    DoubleClickHandler favClickHandler = new DoubleClickHandler();
                    fav_button.setOnClickListener(v -> favClickHandler.startIfSatisfied(() -> {
                        FavoritesUtils.Plants.toggleFavState(context, plant, mainTaost, true);
                        changeFavButtonBackground(context, fav_button, plant);
                    }));

                    Glide.with(context).load(ClassPackageMaker.getImageResourceID(context,
                            ClassPackageMaker.getLowerCaseNameHalaman(context, plant.getPlant_name()))).into(imageContainer);

                    PlantListAdapter.CommonNamesAdapter commonNamesAdapter = new PlantListAdapter.CommonNamesAdapter();
                    commonNamesAdapter.setContentList(plant.getCommon_names());

                    mainRecyclerView.setHasFixedSize(true);
                    mainRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    mainRecyclerView.setAdapter(commonNamesAdapter);

                    plantDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    plantDialog.show();
                }

                ToastUtils t = new ToastUtils(context);

                if(active_intent != null){
                    context.startActivity(active_intent);
                    Activity activity = (Activity) context;
                    activity.overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
                    t.cancelToast();
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                Typeface bold_italic = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD_ITALIC);
                ds.setTypeface(bold_italic);
                ds.setColor(ContextCompat.getColor(context, color_on_draw_state));
                ds.setUnderlineText(false);
                tv.invalidate();
            }
        };
        spannable.setSpan(ck, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static void changeFavButtonBackground(Context context, View fav_button, PlantContentHolder plant){
        GuiColorManager.changeBackgroundDrawable(context, fav_button, R.drawable.ic_favorites_leaf_unselected);
        if(FavoritesUtils.Plants.isPlantFavorite(context, plant.getLower_case_name())){
            GuiColorManager.changeBackgroundDrawable(context, fav_button, R.drawable.ic_favorites_leaf_selected);
        }
    }

}