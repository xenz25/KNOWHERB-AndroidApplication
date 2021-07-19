package com.example.kowherbz.adapter;

import android.util.Printer;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kowherbz.R;
import com.example.kowherbz.holder.ParentContentHolder;
import com.example.kowherbz.holder.PlantContentHolder;
import com.example.kowherbz.utility.DoubleClickHandler;
import com.example.kowherbz.utility.FavoritesUtils;
import com.example.kowherbz.utility.ListContentListener;
import com.example.kowherbz.utility.OnItemClickFragmentRecyclerView;
import com.example.kowherbz.utility.OnItemClickListenerRelative;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

public class FavoritePlantsAdapter extends RecyclerView.Adapter<FavoritePlantsAdapter.ViewHolder> implements Filterable {
    private List<PlantContentHolder> contentHolderList;
    private TreeMap<String, PlantContentHolder> referenceList;
    private OnItemClickListenerRelative onItemClickListenerRelative;
    private ListContentListener listContentListener;

    public List<PlantContentHolder> getContentHolderList() {
        return contentHolderList;
    }

    public void setOnItemClickListenerRelative(OnItemClickListenerRelative onItemClickListenerRelative) {
        this.onItemClickListenerRelative = onItemClickListenerRelative;
    }

    public void initList() {
        referenceList = new TreeMap<>(FavoritesUtils.Plants.getFavPlantList());
        this.contentHolderList = new ArrayList<>(referenceList.values());
    }

    public void removeToReferenceList(String key){
        this.referenceList.remove(key);
    }

    public void addToReferenceList(PlantContentHolder plant){
        this.referenceList.put(plant.getLower_case_name(), plant);
    }

    public void setListContentListener(ListContentListener listContentListener) {
        this.listContentListener = listContentListener;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PlantContentHolder> filteredList = new ArrayList<>();

            for(PlantContentHolder plant : referenceList.values()) {
                if (plant.getLower_case_name().contains(constraint.toString().toLowerCase())) {
                    filteredList.add(plant);
                }
            }

            if(filteredList.isEmpty()) filteredList.addAll(referenceList.values());
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contentHolderList = (List<PlantContentHolder>) results.values;
            notifyFragmentToUpdates();
            notifyDataSetChanged();
        }
    };

    public void notifyFragmentToUpdates(){
        if(listContentListener != null) listContentListener.checkListNow(contentHolderList.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;

        public ViewHolder(@NonNull @NotNull View itemView, OnItemClickListenerRelative onItemClickListenerRelative) {
            super(itemView);
            RelativeLayout clickable_panel = itemView.findViewById(R.id.clickable_panel);

            title = itemView.findViewById(R.id.title_input);
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            title.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.app_name_herb_color));

            TextView description = itemView.findViewById(R.id.description_input);
            description.setVisibility(View.GONE);

            FrameLayout remove_to_fav = itemView.findViewById(R.id.btn_remove_item);

            DoubleClickHandler removeClickHandler = new DoubleClickHandler();
            remove_to_fav.setOnClickListener(v -> removeClickHandler.startIfSatisfied(() -> {
                if(onItemClickListenerRelative != null){
                    int position = getAbsoluteAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        onItemClickListenerRelative.OnDeleteClick(position);
                    }
                }
            }));

            DoubleClickHandler clickHandler = new DoubleClickHandler();
            clickable_panel.setOnClickListener(v -> clickHandler.startIfSatisfied(() -> {
                if(onItemClickListenerRelative != null){
                    int position = getAbsoluteAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        onItemClickListenerRelative.OnItemClick(position);
                    }
                }
            }));
        }
    }

    @NonNull
    @NotNull
    @Override
    public FavoritePlantsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.paborito_item_container_layout, parent, false);
        return new ViewHolder(v, this.onItemClickListenerRelative);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FavoritePlantsAdapter.ViewHolder holder, int position) {
        PlantContentHolder current = contentHolderList.get(position);
        holder.title.setText(current.getPlant_name());
    }

    @Override
    public int getItemCount() {
        return contentHolderList.size();
    }
}
