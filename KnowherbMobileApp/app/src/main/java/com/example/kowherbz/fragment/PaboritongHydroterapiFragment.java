package com.example.kowherbz.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.kowherbz.R;
import com.example.kowherbz.activity.HydroterapiInstructionViewActivity;
import com.example.kowherbz.activity.PaboritoActivity;
import com.example.kowherbz.adapter.FavoritesListAdapter;
import com.example.kowherbz.holder.ParentContentHolder;
import com.example.kowherbz.utility.FavoritesUtils;
import com.example.kowherbz.utility.ListContentListener;
import com.example.kowherbz.utility.OnItemClickFragmentRecyclerView;
import com.example.kowherbz.utility.SnacksUtils;
import com.example.kowherbz.utility.StringIDs;
import com.example.kowherbz.utility.ThreadExecutorUtils;


public class PaboritongHydroterapiFragment extends OnItemClickFragmentRecyclerView implements SnacksUtils.SnackOnClickListener, ListContentListener {
   // private List<ParentContentHolder> contentHoldersList = new ArrayList<>();
    private FavoritesListAdapter favoritesListAdapter;
    private LinearLayoutManager layoutManager;
    private SnacksUtils snacksUtils;
    private ParentContentHolder itemBackUp;
    private ThreadExecutorUtils threadExecutorUtils;
    private FrameLayout emptyIndicator;

    private PaboritoActivity parentInstance;

    public PaboritongHydroterapiFragment() {
        // Required empty public constructor
    }

    public PaboritongHydroterapiFragment(PaboritoActivity parentInstance) {
        this.parentInstance = parentInstance;
    }

    public static PaboritongHydroterapiFragment newInstance() {
        return new PaboritongHydroterapiFragment();
    }

    public FavoritesListAdapter getFavoritesListAdapter() {
        return favoritesListAdapter;
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
        threadExecutorUtils = new ThreadExecutorUtils("HYDRO_FAV_FRAGMENT");
        View v = inflater.inflate(R.layout.fragment_paboritong_hydroterapi, container, false);

        layoutManager = new LinearLayoutManager(requireContext());
        setUpRecyclerView(v);

        snacksUtils = new SnacksUtils(requireContext(), this, v.findViewById(R.id.parent_layout_fragmenthydro));

        emptyIndicator = v.findViewById(R.id.empty_indicator_fragment);
        favoritesListAdapter.notifyFragmentToUpdate();
        return v;
    }

    private void setUpRecyclerView(View v) {
        favoritesListAdapter = new FavoritesListAdapter();

        favoritesListAdapter.setMode(1);
        favoritesListAdapter.initList();
        favoritesListAdapter.setOnItemClickListenerRelative(this);
        favoritesListAdapter.setListContentListener(this);

        RecyclerView recyclerView = v.findViewById(R.id.paborito_recycler_view_fragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(favoritesListAdapter);
        favoritesListAdapter.notifyDataSetChanged();
    }

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public void OnItemClick(int position) {
        parentInstance.clearSearchBarFocus();
        //perform click and shared animation
        Intent intent = new Intent(getActivity(), HydroterapiInstructionViewActivity.class);
        intent.putExtra(StringIDs.getStringValue(requireActivity(), R.string.hydroterapi_serial_name),
                favoritesListAdapter.getContentHolderList().get(position).getLower_case_name()); //place the object in serializable
        intent.putExtra(StringIDs.getStringValue(requireActivity(), R.string.is_from_fav_hydroterapi_serial),
                true);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
        requireActivity().finish();
    }

    @Override
    public void OnDeleteClick(int position) {
        parentInstance.clearSearchBarFocus();
        itemBackUp = favoritesListAdapter.getContentHolderList().get(position);
        String key = itemBackUp.getLower_case_name();

        threadExecutorUtils.addJob(key,() -> FavoritesUtils.Hydrotherapy.removeToFavorites(requireContext(), key));

        favoritesListAdapter.getContentHolderList().remove(position);
        favoritesListAdapter.removeFromReferenceList(key); //explicitly update reference list for searching
        snacksUtils.setPosition(position);

        favoritesListAdapter.notifyItemRemoved(position);

        snacksUtils.showSnackBarActionable(
                FavoritesUtils.ITEM_REMOVED_TO_FAVORITES,
                SnacksUtils.UNDO, R.color.highlight_color);

        favoritesListAdapter.notifyFragmentToUpdate();
    }

    @Override
    public void onSnackActionClick(int position) {
        favoritesListAdapter.getContentHolderList().add(position, itemBackUp);
        favoritesListAdapter.addToReferenceList(itemBackUp); //explicitly update reference list for searching
        favoritesListAdapter.notifyItemInserted(position);
        FavoritesUtils.Hydrotherapy.addToFavorites(requireContext(), itemBackUp);
        favoritesListAdapter.notifyFragmentToUpdate();
    }

    @Override
    public void onSnackDismiss(int position) {
        threadExecutorUtils.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        snacksUtils.dismissSnackBar();
    }

    @Override
    public void checkListNow(int size) {
        if(size == 0){
            emptyIndicator.setVisibility(View.VISIBLE);
        } else {
            emptyIndicator.setVisibility(View.GONE);
        }
    }
}