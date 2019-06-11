package com.example.myfirstapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myfirstapp.R;
import com.example.myfirstapp.model.Tag;

public class TagsStatisticsAdapter extends ListAdapter<Tag, RecyclerView.ViewHolder> {

    public static final String SHARED_PREFS = "sharedPrefs";

    private OnItemClickListener listener;
    private SharedPreferences prefs;
    private String tagName;
    private Context context;
    private Tag currentTag;

    public boolean isActive;

    public TagsStatisticsAdapter(Context context) {
        super(DIFF_CALLBACK);

        this.context = context;
        prefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
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

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && prefs.getBoolean("bAnyTagActive", false))
            return 0;
        else
            return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView;
        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.tag_item_linear_first, viewGroup, false);
                return new TagHolderFirst(itemView);

            case 1:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.tag_item_linear, viewGroup, false);
                return new TagHolderOther(itemView);

            default:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.tag_item_linear, viewGroup, false);
                return new TagHolderOther(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()) {
            case 0:
                TagHolderFirst tagHolderFirst = (TagHolderFirst)viewHolder;

                currentTag = getItem(i);

                tagName = currentTag.getName();

                tagHolderFirst.textViewName.setText(tagName);

                if (currentTag.getIsActive()) {
                    tagHolderFirst.textViewName.setTextColor(Color.parseColor("#009933"));
                } else
                    tagHolderFirst.textViewName.setTextColor(Color.parseColor("#F9F9F9"));
                break;

            case 1:
                TagHolderOther tagHolderOther = (TagHolderOther)viewHolder;

                currentTag = getItem(i);

                tagName = currentTag.getName();

                tagHolderOther.textViewName.setText(tagName);

                if (currentTag.getIsActive()) {
                    tagHolderOther.textViewName.setTextColor(Color.parseColor("#009933"));
                } else
                    tagHolderOther.textViewName.setTextColor(Color.parseColor("#F9F9F9"));
                break;
        }
    }

    /*@Override
    public void onBindViewHolder(@NonNull TagHolder tagHolder, int i) {
        Tag currentTag = getItem(i);

        tagName = currentTag.getName();

        tagHolder.textViewName.setText(tagName);

        if (currentTag.getIsActive()) {
            tagHolder.textViewName.setTextColor(Color.parseColor("#009933"));
        } else
            tagHolder.textViewName.setTextColor(Color.parseColor("#F9F9F9"));
    }*/

    public Tag getTagAt(int position)
    {
        return getItem(position);
    }

    class TagHolderFirst extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private RelativeLayout layout;
        private ViewGroup.LayoutParams params;

        public TagHolderFirst(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textTagName);
            layout = itemView.findViewById(R.id.tagRelativeLayout);
            params = layout.getLayoutParams();

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

    class TagHolderOther extends RecyclerView.ViewHolder {
        private TextView textViewName;

        public TagHolderOther(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textTagName);

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
