package com.example.kowherbz.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kowherbz.R;
import com.example.kowherbz.activity.PaboritoActivity;
import com.example.kowherbz.adapter.FavoritePlantsAdapter;
import com.example.kowherbz.adapter.PlantListAdapter;
import com.example.kowherbz.holder.PlantContentHolder;
import com.example.kowherbz.hyperlink.HyperLinkPatcher;
import com.example.kowherbz.utility.ClassPackageMaker;
import com.example.kowherbz.utility.DoubleClickHandler;
import com.example.kowherbz.utility.FavoritesUtils;
import com.example.kowherbz.utility.ListContentListener;
import com.example.kowherbz.utility.OnItemClickFragmentRecyclerView;
import com.example.kowherbz.utility.SnacksUtils;
import com.example.kowherbz.utility.SystemUtility;
import com.example.kowherbz.utility.ThreadExecutorUtils;
import com.example.kowherbz.utility.ToastUtils;
import com.google.android.material.imageview.ShapeableImageView;

public class PaboritongHalamanFragment extends OnItemClickFragmentRecyclerView implements SnacksUtils.SnackOnClickListener, ListContentListener {
    private LinearLayoutManager layoutManager;
    private FrameLayout emptyIndicator;

    private ToastUtils mainToast;
    private SnacksUtils snacksUtils;

    private PlantContentHolder itemBackUp;
    private FavoritePlantsAdapter favoritePlantsAdapter;
    private ThreadExecutorUtils threadExecutorUtils;

    private PaboritoActivity parentInstance;

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    public PaboritongHalamanFragment() {
        // Required empty public constructor
    }

    public PaboritongHalamanFragment(PaboritoActivity parentInstance) {
        this.parentInstance = parentInstance;
    }

    public FavoritePlantsAdapter getFavoritePlantsAdapter() {
        return favoritePlantsAdapter;
    }

    public static PaboritongHalamanFragment newInstance() {
        return new PaboritongHalamanFragment();
    }

    public SnacksUtils getSnacksUtils() {
        return snacksUtils;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutManager = new LinearLayoutManager(requireContext());
        View v = inflater.inflate(R.layout.fragment_paboritong_halaman, container, false);
        setUpRecyclerView(v);

        snacksUtils = new SnacksUtils(requireContext(), this, v);
        emptyIndicator = v.findViewById(R.id.empty_indicator_fragment);
        favoritePlantsAdapter.notifyFragmentToUpdates();

        mainToast = new ToastUtils(requireContext());

        threadExecutorUtils = new ThreadExecutorUtils("HALAMAN_FAV_FRAGMENT");
        return v;
    }

    private void setUpRecyclerView(View v){
        favoritePlantsAdapter = new FavoritePlantsAdapter();
        favoritePlantsAdapter.initList();
        favoritePlantsAdapter.setOnItemClickListenerRelative(this);
        favoritePlantsAdapter.setListContentListener(this);

        RecyclerView recyclerView = v.findViewById(R.id.paboritong_halaman_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(favoritePlantsAdapter);
    }

    @Override
    public void checkListNow(int size) {
        if(size == 0){
            emptyIndicator.setVisibility(View.VISIBLE);
        } else {
            emptyIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnItemClick(int position) {
        SystemUtility.forceKeyboardToStateHidden(parentInstance);
        parentInstance.clearSearchBarFocus();
        snacksUtils.dismissSnackBar();
        //showing plant dialog
        Context context = requireContext();
        PlantContentHolder plant = favoritePlantsAdapter.getContentHolderList().get(position);

        Dialog plantDialog = new Dialog(context);
        plantDialog.setContentView(R.layout.plant_dialog_layout);
        plantDialog.setCancelable(false);
        plantDialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;

        //init buttons
        FrameLayout close_dialog = plantDialog.getWindow().findViewById(R.id.close_dialog_plant_dialog);
        FrameLayout fav_button = plantDialog.getWindow().findViewById(R.id.btn_fav_plant_dialog);

        HyperLinkPatcher.changeFavButtonBackground(requireContext(), fav_button, plant);

        DoubleClickHandler favHandler = new DoubleClickHandler();
        fav_button.setOnClickListener(v -> favHandler.startIfSatisfied(() -> {
            boolean isAdded = FavoritesUtils.Plants.toggleFavState(
                    requireContext(), plant, mainToast, false);
            HyperLinkPatcher.changeFavButtonBackground(requireContext(), fav_button, plant);
            if(!isAdded){
                this.OnDeleteClick(position);
                plantDialog.dismiss();
            }
        }));

        TextView title = plantDialog.getWindow().findViewById(R.id.tv_title_mga_plant_dialog);
        RecyclerView mainRecyclerView = plantDialog.getWindow().findViewById(R.id.recyclerView_common_names_plant_dialog);
        ShapeableImageView imageContainer = plantDialog.getWindow().findViewById(R.id.plant_dialog_image_container);

        //close button handling
        DoubleClickHandler closeHandler = new DoubleClickHandler();
        close_dialog.setOnClickListener(v -> closeHandler.startIfSatisfied(plantDialog::dismiss));

        //getting data of plant
        title.setText(plant.getPlant_name());

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

    @Override
    public void OnDeleteClick(int position) {
        parentInstance.clearSearchBarFocus();
        itemBackUp = favoritePlantsAdapter.getContentHolderList().get(position);
        String key = itemBackUp.getLower_case_name();

        threadExecutorUtils.addJob(key, () -> FavoritesUtils.Plants.removeToFavorites(requireContext(), key));

        favoritePlantsAdapter.getContentHolderList().remove(position);
        favoritePlantsAdapter.removeToReferenceList(key);
        favoritePlantsAdapter.notifyItemRemoved(position);

        snacksUtils.setPosition(position);
        snacksUtils.showSnackBarActionable(
                FavoritesUtils.ITEM_REMOVED_TO_FAVORITES,
                SnacksUtils.UNDO, R.color.highlight_color);

        favoritePlantsAdapter.notifyFragmentToUpdates();
    }

    @Override
    public void onSnackActionClick(int position) {
        favoritePlantsAdapter.getContentHolderList().add(position, itemBackUp);
        favoritePlantsAdapter.addToReferenceList(itemBackUp);
        favoritePlantsAdapter.notifyItemInserted(position);
        FavoritesUtils.Plants.addToFavorites(requireContext(), itemBackUp);

        favoritePlantsAdapter.notifyFragmentToUpdates();
    }

    @Override
    public void onSnackDismiss(int position) {
        threadExecutorUtils.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        snacksUtils.dismissSnackBar();
        mainToast.cancelToast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainToast.cancelToast();
    }

}