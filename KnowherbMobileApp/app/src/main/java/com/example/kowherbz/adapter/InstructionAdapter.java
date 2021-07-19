package com.example.kowherbz.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kowherbz.R;
import com.example.kowherbz.holder.ContentHolder;
import com.example.kowherbz.holder.ParentContentHolder;
import com.example.kowherbz.hyperlink.HyperLinkPatcher;

import org.jetbrains.annotations.NotNull;
import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder> {
    private final List<ContentHolder> contentHolders;
    private Context context;
    public static final int SAKIT_STATE_NUMBER = 0;
    public static final int HYDROTHERAPY_STATE_NUMBER = 1;
    private Pair<Integer, int[]> hyperLinkColorStates; //int[]{} 0 - Highlight Color | 1 - hyperlink color

    public void setHyperLinkColorStates(Pair<Integer, int[]> hyperLinkColorStates) {
        this.hyperLinkColorStates = hyperLinkColorStates;
    }

    public static class InstructionViewHolder extends RecyclerView.ViewHolder {
        TextView header;
        TextView content;

        public InstructionViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.instruction_header);
            content = itemView.findViewById(R.id.instruction_content);
        }
    }

    public InstructionAdapter(ParentContentHolder parentContentHolder) {
        this.contentHolders = parentContentHolder.getContentHolderList();
    }

    @Override
    public @NotNull InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_layout, parent, false);
        context = parent.getContext();
        return new InstructionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionAdapter.InstructionViewHolder holder, int position) {
        ContentHolder current = contentHolders.get(position);

        //change header color
        if(hyperLinkColorStates.first == SAKIT_STATE_NUMBER){
            holder.header.setTextColor(ContextCompat.getColor(context, R.color.instruction_header_color));
        } else if(hyperLinkColorStates.first == HYDROTHERAPY_STATE_NUMBER){
            holder.header.setTextColor(ContextCompat.getColor(context, R.color.hydroterapi_primaryDarkColor));
        }


        if (!current.isHyperHeaderEmpty()) {
            holder.header.setText(HyperLinkPatcher.patchHyperLink(
                    context, current.getHeader(), current.getHyperWordsHeader(), holder.header, hyperLinkColorStates.second[1]));
            holder.header.setHighlightColor(ContextCompat.getColor(context, hyperLinkColorStates.second[0]));
            holder.header.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            holder.header.setText(current.getHeader());
        }

        if (!current.isHyperWordContentEmpty()) {
            //applying hyperlinks and click instruction if instruction is not empty
            SpannableString spannableString = HyperLinkPatcher.patchHyperLink(context,
                    current.getContent(),
                    current.getHyperWordsInstruction(),
                    holder.content,
                    hyperLinkColorStates.second[1]);
            holder.content.setText(spannableString);
            holder.content.setHighlightColor(ContextCompat.getColor(context, hyperLinkColorStates.second[0]));
            holder.content.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            //if no hyperlinks found
            if (current.getContent().equals("")) {
                holder.content.setVisibility(View.GONE);
            } else {
                holder.content.setText(current.getContent());
            }
        }

    }

    @Override
    public int getItemCount() {
        return contentHolders.size();
    }
}

