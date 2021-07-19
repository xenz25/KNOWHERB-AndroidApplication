package com.example.kowherbz.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kowherbz.R;
import com.example.kowherbz.activity.HydroterapiInstructionViewActivity;
import com.example.kowherbz.activity.InstructionViewActivity;
import com.example.kowherbz.activity.SakitAtLunasActivity;
import com.example.kowherbz.adapter.HydroterapiListAdapter;
import com.example.kowherbz.holder.HydroterapiItemHolder;
import com.example.kowherbz.holder.ParentContentHolder;
import com.example.kowherbz.utility.AnimationUtility;
import com.example.kowherbz.utility.ClassPackageMaker;
import com.example.kowherbz.utility.OnItemClickFragmentRecyclerView;
import com.example.kowherbz.utility.StringIDs;

import java.util.List;

/**
 * Fragment for Hydroterapi List
 */
public class SakitAtLunasFragment2 extends OnItemClickFragmentRecyclerView {
    private List<ParentContentHolder> parentContentHolderList;
    private final HydroterapiListAdapter hydroterapiListAdapter = new HydroterapiListAdapter();
    private LinearLayoutManager layoutManager;

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    public SakitAtLunasFragment2() {
        // Required empty public constructor
    }

    public static SakitAtLunasFragment2 newInstance() {
        return new SakitAtLunasFragment2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutManager = new LinearLayoutManager(requireContext());
        View v = inflater.inflate(R.layout.fragment_sakit_at_lunas2, container, false);
        setUpRecyclerView(v);
        return v;
    }

    private void setUpRecyclerView(View v) {
        parentContentHolderList = ClassPackageMaker.getHydroterapiList();
        RecyclerView recyclerView = v.findViewById(R.id.sakit_at_lunas_fragment_2_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        hydroterapiListAdapter.setItemHolderList(parentContentHolderList);
        recyclerView.setAdapter(hydroterapiListAdapter);

        hydroterapiListAdapter.setOnItemClickListenerRelative(this);
    }

    @Override
    public void OnItemClick(int position, TextView title, TextView description) {
        //perform click and shared animation
        Intent intent = new Intent(getActivity(), HydroterapiInstructionViewActivity.class);
        intent.putExtra(StringIDs.getStringValue(requireActivity(), R.string.hydroterapi_serial_name),
                parentContentHolderList.get(position).getLower_case_name()); //place the object in serializable

        startActivity(intent, AnimationUtility.sharedAnimationBuilder(
                getActivity(),
                Pair.create(title, StringIDs.getStringValue(requireActivity(), R.string.hydroterapi_title_tn)),
                Pair.create(description, StringIDs.getStringValue(requireActivity(), R.string.hydroterapi_description_tn))
        ));

        SakitAtLunasActivity.clearSearchBarFocus(requireActivity().findViewById(R.id.sakit_at_lunas_parent_layout)
                .findViewById(R.id.edt_search_bar_sakit_at_lunas));
    }
}