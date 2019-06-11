package com.example.myfirstapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myfirstapp.model.Income;
import com.example.myfirstapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IncomeAdapter extends ListAdapter<Income, IncomeAdapter.IncomeHolder> {

    private OnItemClickListener listener;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
    private float incomeValue;
    private Date incomeDate;

    public IncomeAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Income> DIFF_CALLBACK = new DiffUtil.ItemCallback<Income>() {
        @Override
        public boolean areItemsTheSame(@NonNull Income oldItem, @NonNull Income newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Income oldItem, @NonNull Income newItem) {
            return oldItem.getValue() == newItem.getValue() && oldItem.getDate().equals(newItem.getDate());
        }
    };

    @NonNull
    @Override
    public IncomeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.income_item, viewGroup, false);

        return new IncomeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeHolder incomeHolder, int i) {
        Income currentIncome = getItem(i);

        incomeValue = currentIncome.getValue();
        incomeDate = currentIncome.getDate();
        long incomeDateInMillis = incomeDate.getTime();

        incomeHolder.textViewValue.setText(String.valueOf(incomeValue));
        incomeHolder.textViewDate.setText(DateUtils.getRelativeTimeSpanString(incomeDateInMillis, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NUMERIC_DATE));
    }


    public Income getIncomeAt(int position)
    {
        return getItem(position);
    }

    class IncomeHolder extends RecyclerView.ViewHolder {
        private TextView textViewValue;
        private TextView textViewDate;

        public IncomeHolder(@NonNull View itemView) {
            super(itemView);

            textViewValue = itemView.findViewById(R.id.text_view_value);
            textViewDate = itemView.findViewById(R.id.text_view_date);

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
        void onItemClick(Income income);
    }
}
