package com.example.kowherbz.utility;

import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.kowherbz.adapter.FavoritesListAdapter;
import com.example.kowherbz.holder.PlantContentHolder;
import com.google.android.material.imageview.ShapeableImageView;

/**
 * use this class inside fragments that has recycler view to implement clicks this is a more effective way
 * so that other implemented method of interface OnItemClickListenerRelative become optional.
 */

public abstract class OnItemClickFragmentRecyclerView extends Fragment implements OnItemClickListenerRelative {

    @Override
    public void OnItemClick(int position) {

    }

    @Override
    public void OnItemClick(int position, TextView title, TextView description) {

    }

    @Override
    public void OnItemClick(int position, ShapeableImageView imageView, TextView title, TextView description) {

    }

    @Override
    public void OnDeleteClick(int position) {

    }
}
