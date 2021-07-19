package com.example.kowherbz.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kowherbz.R;
import com.example.kowherbz.holder.ParentContentHolder;
import com.example.kowherbz.holder.SakitAtLunasItemHolder;
import com.example.kowherbz.utility.AnimationUtility;
import com.example.kowherbz.utility.ClassPackageMaker;
import com.example.kowherbz.utility.DoubleClickHandler;
import com.example.kowherbz.utility.OnItemClickListenerRelative;
import com.google.android.material.imageview.ShapeableImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SakitAtLunasListAdapter extends RecyclerView.Adapter<SakitAtLunasListAdapter.ViewHolder> {
    private Context context;
    private List<ParentContentHolder> itemHolderList;
    private OnItemClickListenerRelative onItemClickListenerRelative;
    private AnimationUtility.RecyclerViewUpDownAnimation recyclerViewUpDownAnimation = new AnimationUtility.RecyclerViewUpDownAnimation();

    public void setOnItemClickListenerRelative(OnItemClickListenerRelative onItemClickListenerRelative) {
        this.onItemClickListenerRelative = onItemClickListenerRelative;
    }

    public void setItemHolderList(List<ParentContentHolder> itemHolderList) {
        this.itemHolderList = itemHolderList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView itemImageContainer;
        private final TextView itemHeader;
        private final TextView itemDescription;

        public ViewHolder(@NonNull @NotNull View itemView, OnItemClickListenerRelative onItemClickListenerRelative) {
            super(itemView);
            itemImageContainer = itemView.findViewById(R.id.imageView_sakit_lunas);
            itemHeader = itemView.findViewById(R.id.tv_sakit_title);
            itemDescription = itemView.findViewById(R.id.tv_sakit_at_lunas_description);
            RelativeLayout card_parent_layout = itemView.findViewById(R.id.card_parent_layout);

            //item click
            DoubleClickHandler doubleClickHandler2 = new DoubleClickHandler();
            card_parent_layout.setOnClickListener(v -> doubleClickHandler2.startIfSatisfied(() -> {
                if (onItemClickListenerRelative != null) {
                    int pos = getAbsoluteAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        onItemClickListenerRelative.OnItemClick(pos,
                                itemImageContainer, itemHeader, itemDescription);
                    }
                }
            }));
        }
    }

    @NotNull
    @Override
    public SakitAtLunasListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sakit_at_lunas_card_layout, parent, false);
        return new ViewHolder(v, this.onItemClickListenerRelative);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SakitAtLunasListAdapter.ViewHolder holder, int position) {
        ParentContentHolder current = itemHolderList.get(position);

        //animations
        recyclerViewUpDownAnimation.startAnimation(holder.itemView, holder.getAbsoluteAdapterPosition());

        //using glide to improve image loading
        Glide.with(context).load(ClassPackageMaker.getImageResourceID(context, current.getLower_case_name())).into(holder.itemImageContainer);
        holder.itemHeader.setText(current.getTitleHolder().getTitleOfSickness());
        String description = current.getTitleHolder().getDefinitionOfSickness();
        if (description.length() > 72) {
            description = description.substring(0, 72) + context.getString(R.string.ellipsis);
        }
        holder.itemDescription.setText(description);
    }

    @Override
    public int getItemCount() {
        return itemHolderList.size();
    }
}
