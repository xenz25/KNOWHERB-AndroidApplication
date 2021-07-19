package com.example.kowherbz.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kowherbz.R;
import com.example.kowherbz.holder.PlantContentHolder;
import com.example.kowherbz.utility.ClassPackageMaker;
import com.example.kowherbz.utility.DoubleClickHandler;
import com.example.kowherbz.utility.FavoritesUtils;
import com.example.kowherbz.utility.GuiColorManager;
import com.google.android.material.imageview.ShapeableImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class PlantListAdapter extends RecyclerView.Adapter<PlantListAdapter.ViewHolder> {
    private List<PlantContentHolder> contentHolderList;
    private Context context;
    private OnFavoriteClickListener onFavoriteClickListener;

    public interface OnFavoriteClickListener {
        void OnFavClick(int position);
    }

    public void setContentHolderList(List<PlantContentHolder> contentHolderList) {
        this.contentHolderList = contentHolderList;
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener onFavoriteClickListener) {
        this.onFavoriteClickListener = onFavoriteClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView imageContainer;
        private final TextView title;
        private final RecyclerView recyclerView;
        private final FrameLayout btnFavorite;

        public ViewHolder(@NonNull @NotNull View itemView, OnFavoriteClickListener onFavoriteClickListener) {
            super(itemView);
            imageContainer = itemView.findViewById(R.id.mga_halamang_gamot_image_container);
            title = itemView.findViewById(R.id.tv_title_mga_halamang_gamot);
            recyclerView = itemView.findViewById(R.id.recyclerView_common_names);
            btnFavorite = itemView.findViewById(R.id.btn_fav_mga_halamang_gamot);

            DoubleClickHandler favClickHandler = new DoubleClickHandler();
            btnFavorite.setOnClickListener(v -> favClickHandler.startIfSatisfied(() -> {
                if(onFavoriteClickListener!=null){
                    int position = getAbsoluteAdapterPosition();
                    onFavoriteClickListener.OnFavClick(position);
                }
            }));
        }
    }

    @Override
    public PlantListAdapter.@NotNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mga_halamang_gamot_layout, parent, false);
        context = parent.getContext();
        return new PlantListAdapter.ViewHolder(v, this.onFavoriteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantListAdapter.ViewHolder holder, int position) {
        PlantContentHolder current = contentHolderList.get(position);
        Glide.with(context)
                .load(ClassPackageMaker.getImageResourceID(context, current.getLower_case_name()))
                .into(holder.imageContainer);
        holder.title.setText(current.getPlant_name());
        setUpRecyclerView(holder.recyclerView, current);

        GuiColorManager.changeBackgroundDrawable(context, holder.btnFavorite, R.drawable.ic_favorites_leaf_unselected);
        if(FavoritesUtils.Plants.isPlantFavorite(context, current.getLower_case_name())){
            GuiColorManager.changeBackgroundDrawable(context, holder.btnFavorite, R.drawable.ic_favorites_leaf_selected);
        }
    }

    private void setUpRecyclerView(RecyclerView recyclerView, PlantContentHolder contentHolder){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        CommonNamesAdapter commonNamesAdapter = new CommonNamesAdapter();
        commonNamesAdapter.setContentList(contentHolder.getCommon_names());
        recyclerView.setAdapter(commonNamesAdapter);
    }

    /**
     * adapter for common names
     */
    public static class CommonNamesAdapter extends RecyclerView.Adapter<CommonNamesAdapter.ViewHolder> {
        List<Pair<String, String>> contentList;

        public void setContentList(List<Pair<String, String>> contentList) {
            this.contentList = contentList;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView title;
            private final TextView description;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.common_name_title);
                description = itemView.findViewById(R.id.common_name_description);
            }
        }

        @NonNull
        @NotNull
        @Override
        public CommonNamesAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_names_layout, parent, false);
            return new CommonNamesAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull CommonNamesAdapter.ViewHolder holder, int position) {
            Pair<String, String> current = contentList.get(position);
            String first = current.first + ":";
            holder.title.setText(first);
            holder.description.setText(current.second);
        }

        @Override
        public int getItemCount() {
            return contentList.size();
        }
    }



    @Override
    public int getItemCount() {
        return contentHolderList.size();
    }
}
