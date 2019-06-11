package com.example.myfirstapp.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfirstapp.IncomeCalculator;
import com.example.myfirstapp.IncomeStopwatch;
import com.example.myfirstapp.R;
import com.example.myfirstapp.adapter.TagsStatisticsAdapter;
import com.example.myfirstapp.model.Tag;
import com.example.myfirstapp.viewmodel.TagViewModel;

import java.text.DecimalFormat;
import java.util.List;

public class FragmentStopwatch extends Fragment {

    View view;

    public static final String SHARED_PREFS = "sharedPrefs";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    /*private StopwatchViewModel stopwatchViewModel;*/
    private TagViewModel tagViewModel;

    private long Time, StartTime, PauseTime, CurrentTime, OnStopTime, OnStartTime = 0L;

    private Handler handler;

    private boolean bIsRunning;

    private IncomeCalculator incomeCalculator;
    private IncomeStopwatch incomeStopwatch;

    private DecimalFormat counterDF = new DecimalFormat("0.0000");

    private float income = 0f;

    public String string;

    public FragmentStopwatch() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.stopwatch_fragment, container, false);

        handler = new Handler();

        RecyclerView recyclerView = view.findViewById(R.id.reycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        final TagsStatisticsAdapter tagsStatisticsAdapter = new TagsStatisticsAdapter(getContext());
        recyclerView.setAdapter(tagsStatisticsAdapter);

        tagViewModel = ViewModelProviders.of(this).get(TagViewModel.class);
        tagViewModel.getAllTagsByTimeClicked().observe(this, new Observer<List<Tag>>() {
            @Override
            public void onChanged(@Nullable List<Tag> tags) {
                tagsStatisticsAdapter.submitList(tags);
            }
        });

        string = "BLaaaaaaaaaaaaaaaa";

        /*RecyclerView recyclerView = view.findViewById(R.id.reycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        final StopwatchAdapter adapter = new StopwatchAdapter();
        recyclerView.setAdapter(adapter);

        stopwatchViewModel = ViewModelProviders.of(this).get(StopwatchViewModel.class);
        stopwatchViewModel.getAllStopwatches().observe(this, new Observer<List<Stopwatch>>() {
            @Override
            public void onChanged(@Nullable List<Stopwatch> stopwatches) {
                adapter.submitList(stopwatches);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                stopwatchViewModel.delete(adapter.getStopwatchAt(viewHolder.getAdapterPosition()));

                Toast.makeText(getActivity(), "Stopwatch deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);*/

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    // These two methods are called no matter how we leave the activity (if the system destroys it, if we leave over the back button or even if we close the app)
    @Override
    public void onPause() {
        super.onPause();
        Log.e("Stopwatch", "onPause called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Stopwatch", "onResume called");

        prefs = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = prefs.edit();

        incomeCalculator = new IncomeCalculator(getContext());
        incomeStopwatch = IncomeStopwatch.getInstance(prefs);

    }



    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.e("Stopwatch", "Fragment Stopwatch visible");
            handler.postDelayed(runnable2, 100);
        }
        else {
            if (handler != null)
                Log.e("Stopwatch", "Fragment Stopwatch handler removed");
                handler.removeCallbacks(runnable2);
        }
    }*/

    public void start()
    {
        Log.e("Stopwatch", "Stopwatch started");
        handler.postDelayed(runnable2, 100);
    }

    public void stop()
    {
        Log.e("Stopwatch", "Stopwatch stopped");
        handler.removeCallbacks(runnable2);
    }

    private void startStopwatch()
    {
        //StartTime = SystemClock.uptimeMillis();
        //handler.postDelayed(runnable, 0);
        handler.postDelayed(runnable2, 100);

        bIsRunning = true;
    }

    private void pauseStopwatch()
    {
        //PauseTime += Time;
        //handler.removeCallbacks(runnable);
        handler.removeCallbacks(runnable2);

        bIsRunning = false;
    }

    /*public Runnable runnable = new Runnable() {

        public void run() {

            updateStopwatchText();
            handler.postDelayed(this, 0);
        }

    };

    private void updateStopwatchText()
    {
        Time = SystemClock.uptimeMillis() - StartTime;
        CurrentTime = PauseTime + Time;

        stopwatchTextView.setText(String.format("%02d:%02d,%02d",
                TimeUnit.MILLISECONDS.toMinutes(CurrentTime),
                TimeUnit.MILLISECONDS.toSeconds(CurrentTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(CurrentTime)),
                ((TimeUnit.MILLISECONDS.toMillis(CurrentTime) - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(CurrentTime))) / 10) % 100

        ));
    }*/

    public Runnable runnable2 = new Runnable() {

        public void run() {

            updateIncomeText();
            handler.postDelayed(this, 100);
        }

    };

    private void updateIncomeText()
    {
        income = incomeCalculator.getIncrementMS() * incomeStopwatch.CurrentTime;
        //incomeTextView.setText(counterDF.format(income) + " " + prefs.getString("Currency", "USD"));

        Log.e("Income321321", "Income: " + income);
    }

}
