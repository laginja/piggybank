package com.example.myfirstapp.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myfirstapp.R;
import com.example.myfirstapp.model.Tag;

public class TagsAdapter extends ListAdapter<Tag, TagsAdapter.TagHolder> {

    private OnItemClickListener listener;

    private String tagName;

    public TagsAdapter() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<Tag> DIFF_CALLBACK = new DiffUtil.ItemCallback<Tag>() {
        @Override
        public boolean areItemsTheSame(@NonNull Tag oldTag, @NonNull Tag newTag) {
            return oldTag.getId() == newTag.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Tag oldTag, @NonNull Tag newTag) {
            return oldTag.getIsActive() == newTag.getIsActive();
        }
    };

    @NonNull
    @Override
    public TagHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tag_item, viewGroup, false);

        return new TagHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TagHolder tagHolder, int i) {
        Tag currentTag = getItem(i);

        tagName = currentTag.getName();

        tagHolder.textViewName.setText(tagName);

        if (currentTag.getIsActive())
            tagHolder.textViewName.setTextColor(Color.parseColor("#009933"));
        else
            tagHolder.textViewName.setTextColor(Color.parseColor("#F9F9F9"));
    }

    public Tag getTagAt(int position)
    {
        return getItem(position);
    }

    class TagHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;

        public TagHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    // for onClick listener
    public interface OnItemClickListener
    {
        void onItemClick(Tag tag);
    }
}