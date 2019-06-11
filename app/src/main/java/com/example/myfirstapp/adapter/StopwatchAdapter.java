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

import com.example.myfirstapp.R;
import com.example.myfirstapp.model.Stopwatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StopwatchAdapter extends ListAdapter<Stopwatch, StopwatchAdapter.StopwatchHolder> {

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

    private long StopwatchTime;
    private Date StopwatchDate;
    private float StopwatchIncome;

    public StopwatchAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Stopwatch> DIFF_CALLBACK = new DiffUtil.ItemCallback<Stopwatch>() {
        @Override
        public boolean areItemsTheSame(@NonNull Stopwatch oldItem, @NonNull Stopwatch newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Stopwatch oldItem, @NonNull Stopwatch newItem) {
            return false;
        }
    };

    @NonNull
    @Override
    public StopwatchHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.stopwatch_item, viewGroup, false);

        return new StopwatchHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StopwatchHolder stopwatchHolder, int i) {
        Stopwatch currentStopwatch = getItem(i);

        StopwatchIncome = currentStopwatch.getIncomeValue();
        StopwatchTime = currentStopwatch.getStopwatchValue();
        StopwatchDate = currentStopwatch.getDate();

        stopwatchHolder.incomeValue.setText(String.valueOf(StopwatchIncome));
        stopwatchHolder.stopwatchValue.setText(String.format(String.format("%02d:%02d,%02d",
                TimeUnit.MILLISECONDS.toMinutes(StopwatchTime),
                TimeUnit.MILLISECONDS.toSeconds(StopwatchTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(StopwatchTime)),
                ((TimeUnit.MILLISECONDS.toMillis(StopwatchTime) - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(StopwatchTime))) / 10) % 100)));

        long stopwatchDateInMillis = StopwatchDate.getTime();
        stopwatchHolder.date.setText(DateUtils.getRelativeTimeSpanString(stopwatchDateInMillis));

    }


    public Stopwatch getStopwatchAt(int position)
    {
        return getItem(position);
    }

    class StopwatchHolder extends RecyclerView.ViewHolder {
        private TextView incomeValue;
        private TextView stopwatchValue;
        private TextView date;

        public StopwatchHolder(@NonNull View itemView) {
            super(itemView);

            incomeValue = itemView.findViewById(R.id.text_view_incomeValue);
            stopwatchValue = itemView.findViewById(R.id.text_view_stopwatchValue);
            date = itemView.findViewById(R.id.text_view_date);

        }
    }

}
