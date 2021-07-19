package com.example.kowherbz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kowherbz.R;
import com.example.kowherbz.holder.HydroterapiItemHolder;
import com.example.kowherbz.holder.ParentContentHolder;
import com.example.kowherbz.utility.AnimationUtility;
import com.example.kowherbz.utility.DoubleClickHandler;
import com.example.kowherbz.utility.OnItemClickListenerRelative;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HydroterapiListAdapter extends RecyclerView.Adapter<HydroterapiListAdapter.ViewHolder> {
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
        private final TextView titleContainer;
        private final TextView descriptionContainer;

        public ViewHolder(@NonNull @NotNull View itemView, OnItemClickListenerRelative onItemClickListenerRelative) {
            super(itemView);
            RelativeLayout card_parent = itemView.findViewById(R.id.hydroterapi_card_parent);
            titleContainer = itemView.findViewById(R.id.hydroterapi_title_container);
            descriptionContainer = itemView.findViewById(R.id.hydroterapi_description_container);

            //item click
            DoubleClickHandler doubleClickHandler2 = new DoubleClickHandler();
            card_parent.setOnClickListener(v -> doubleClickHandler2.startIfSatisfied(() -> {
                if (onItemClickListenerRelative != null) {
                    int pos = getAbsoluteAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        onItemClickListenerRelative.OnItemClick(
                                pos, titleContainer, descriptionContainer);
                    }
                }
            }));

        }
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hydroterapi_card_layout, parent, false);
        return new ViewHolder(v, this.onItemClickListenerRelative);
    }

    @Override
    public void onBindViewHolder(@NonNull HydroterapiListAdapter.ViewHolder holder, int position) {
        ParentContentHolder current = itemHolderList.get(position);

        //animation
        recyclerViewUpDownAnimation.startAnimation(holder.itemView, holder.getAbsoluteAdapterPosition());

        holder.titleContainer.setText(current.getTitleHolder().getTitleOfSickness());

        String description = current.getTitleHolder().getDefinitionOfSickness();
        if (description.length() > 99) {
            description = description.substring(0, 99) + context.getString(R.string.ellipsis);
        }
        holder.descriptionContainer.setText(description);
    }

    @Override
    public int getItemCount() {
        return itemHolderList.size();
    }


}
