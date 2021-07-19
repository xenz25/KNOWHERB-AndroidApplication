package com.example.kowherbz.utility;

import android.widget.TextView;

import com.example.kowherbz.adapter.FavoritesListAdapter;
import com.example.kowherbz.holder.PlantContentHolder;
import com.google.android.material.imageview.ShapeableImageView;

public interface OnItemClickListenerRelative {
    void OnItemClick(int position);

    void OnItemClick(int position, TextView title, TextView description);

    void OnItemClick(int position, ShapeableImageView imageView, TextView title, TextView description);

    void OnDeleteClick(int position);

}
