package com.example.kowherbz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kowherbz.R;
import com.example.kowherbz.holder.ParentContentHolder;
import com.example.kowherbz.utility.DoubleClickHandler;
import com.example.kowherbz.utility.FavoritesUtils;
import com.example.kowherbz.utility.ListContentListener;
import com.example.kowherbz.utility.OnItemClickListenerRelative;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<ParentContentHolder> contentHolderList;
    private TreeMap<String, ParentContentHolder> referenceList;
    private OnItemClickListenerRelative onItemClickListenerRelative;
    private ListContentListener listContentListener; //use to notify if the list is empty in the fragment
    private int mode;

    public void initList() {
        referenceList = new TreeMap<>();
        if(mode == 0) referenceList = new TreeMap<>(FavoritesUtils.Illness.getFavIllnessList());
        else referenceList = new TreeMap<>(FavoritesUtils.Hydrotherapy.getFavHydrotherapyList());
        this.contentHolderList = new ArrayList<>(referenceList.values());
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public List<ParentContentHolder> getContentHolderList() {
        return contentHolderList;
    }

    public void setOnItemClickListenerRelative(OnItemClickListenerRelative onItemClickListenerRelative) {
        this.onItemClickListenerRelative = onItemClickListenerRelative;
    }

    public void removeFromReferenceList(String key){
        referenceList.remove(key);
    }

    public void addToReferenceList(ParentContentHolder item){
        referenceList.put(item.getLower_case_name(), item);
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
            List<ParentContentHolder> filteredList = new ArrayList<>();

            for(ParentContentHolder item : referenceList.values()) {
                if (item.getLower_case_name().contains(constraint.toString().toLowerCase())) {
                    filteredList.add(item);
                }
            }

            if(filteredList.isEmpty()) filteredList.addAll(referenceList.values());
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contentHolderList = (List<ParentContentHolder>) results.values;
            notifyFragmentToUpdate();
            notifyDataSetChanged();
        }
    };

    public void notifyFragmentToUpdate(){
        if(listContentListener != null) listContentListener.checkListNow(contentHolderList.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;

        public ViewHolder(@NonNull @NotNull View itemView, OnItemClickListenerRelative onItemClickListenerRelative) {
            super(itemView);
            title = itemView.findViewById(R.id.title_input);
            description = itemView.findViewById(R.id.description_input);
            FrameLayout remove_button = itemView.findViewById(R.id.btn_remove_item);
            RelativeLayout clickablePanel = itemView.findViewById(R.id.clickable_panel);

            DoubleClickHandler doubleClickHandler = new DoubleClickHandler();
            remove_button.setOnClickListener(v -> doubleClickHandler.startIfSatisfied(() -> {
                if(onItemClickListenerRelative != null){
                    int position = getAbsoluteAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        onItemClickListenerRelative.OnDeleteClick(position);
                    }
                }
            }));

            DoubleClickHandler panelClick = new DoubleClickHandler();
            clickablePanel.setOnClickListener(v -> panelClick.startIfSatisfied(() -> {
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
    public FavoritesListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.paborito_item_container_layout, parent, false);
        return new ViewHolder(v, this.onItemClickListenerRelative);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FavoritesListAdapter.ViewHolder holder, int position) {
        ParentContentHolder current = contentHolderList.get(position);
        holder.title.setText(current.getTitleHolder().getTitleOfSickness());
        String description = current.getTitleHolder().getDefinitionOfSickness();
        if (description.length() > 72) {
            description = description.substring(0, 72) + context.getString(R.string.ellipsis);
        }
        holder.description.setText(description);
    }

    @Override
    public int getItemCount() {
        return contentHolderList.size();
    }
}
