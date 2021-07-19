package com.example.kowherbz.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kowherbz.R;
import com.example.kowherbz.activity.InstructionViewActivity;
import com.example.kowherbz.activity.SakitAtLunasActivity;
import com.example.kowherbz.adapter.SakitAtLunasListAdapter;
import com.example.kowherbz.holder.ParentContentHolder;
import com.example.kowherbz.holder.SakitAtLunasItemHolder;
import com.example.kowherbz.utility.AnimationUtility;
import com.example.kowherbz.utility.ClassPackageMaker;
import com.example.kowherbz.utility.OnItemClickFragmentRecyclerView;
import com.example.kowherbz.utility.StringIDs;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;
import java.util.Objects;

/**
 * Fragment for Sakit At Lunas List
 */
public class SakitAtLunasFragment1 extends OnItemClickFragmentRecyclerView {
    private List<ParentContentHolder> parentContentHoldersList;
    private final SakitAtLunasListAdapter sakitAtLunasListAdapter = new SakitAtLunasListAdapter();
    private LinearLayoutManager linearLayoutManager;

    public LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    public SakitAtLunasFragment1() {
        // Required empty public constructor
    }

    public static SakitAtLunasFragment1 newInstance() {
        return new SakitAtLunasFragment1();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        linearLayoutManager = new LinearLayoutManager(requireContext());
        View v = inflater.inflate(R.layout.fragment_sakit_at_lunas1, container, false);
        SetUpRecyclerView(v);
        return v;
    }

    private void SetUpRecyclerView(View v) {
        parentContentHoldersList = ClassPackageMaker.getSakitAtLunasList();
        RecyclerView thisFragmentRecyclerView = v.findViewById(R.id.sakit_at_lunas_fragment_1_recycler_view);
        sakitAtLunasListAdapter.setItemHolderList(parentContentHoldersList);
        thisFragmentRecyclerView.setHasFixedSize(true);
        thisFragmentRecyclerView.setLayoutManager(linearLayoutManager);
        thisFragmentRecyclerView.setHasFixedSize(true);
        thisFragmentRecyclerView.setAdapter(sakitAtLunasListAdapter);

        sakitAtLunasListAdapter.setOnItemClickListenerRelative(this);
    }

    @Override
    public void OnItemClick(int position, ShapeableImageView imageView, TextView title, TextView description) {
        //perform click and shared animation
        Intent intent = new Intent(getActivity(), InstructionViewActivity.class);
        intent.putExtra(StringIDs.getStringValue(requireActivity(), R.string.sakit_at_lunas_serial_name),
                parentContentHoldersList.get(position).getLower_case_name()); //place the object in serializable

        startActivity(intent, AnimationUtility.sharedAnimationBuilder(
                getActivity(),
                Pair.create(imageView, StringIDs.getStringValue(requireActivity(), R.string.sakit_at_lunas_image_tn)),
                Pair.create(title, StringIDs.getStringValue(requireActivity(), R.string.sakit_at_lunas_title_tn)),
                Pair.create(description, StringIDs.getStringValue(requireActivity(), R.string.sakit_at_lunas_description_tn))
        ));

        //accessing view from parent layout
        SakitAtLunasActivity.clearSearchBarFocus(requireActivity().findViewById(R.id.sakit_at_lunas_parent_layout)
                .findViewById(R.id.edt_search_bar_sakit_at_lunas));
    }
}