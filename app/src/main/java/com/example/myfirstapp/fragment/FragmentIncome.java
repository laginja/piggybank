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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.myfirstapp.IncomeCalculator;
import com.example.myfirstapp.IncomeStopwatch;
import com.example.myfirstapp.R;
import com.example.myfirstapp.adapter.TagsAdapter;
import com.example.myfirstapp.model.Tag;
import com.example.myfirstapp.viewmodel.TagViewModel;

import java.util.Date;
import java.util.List;

public class FragmentIncome extends Fragment{

    View view;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String INCOME = "income";

    /*private TextView monthTextView;
    private TextView weekTextView;
    private TextView dayTextView;
    private TextView hourTextView;
    private TextView minuteTextView;

    private float incomeLong;
    private DecimalFormat incomesDF = new DecimalFormat("###,##0.00");

    Calendar c = Calendar.getInstance();   // this takes current date

    // Only need the number of days in the current month
    private int numDaysInCurrentMonth;*/

    GridLayout mainGridLayout;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private long Time, StartTime, PauseTime, CurrentTime, OnStopTime, OnStartTime = 0L;

    private Handler handler;

    private boolean bIsRunning;

    private float income = 0f;

    private IncomeCalculator incomeCalculator;

    private TagViewModel tagViewModel;

    private IncomeStopwatch incomeStopwatch;

    public FragmentIncome() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.income_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handler = new Handler();

        RecyclerView tagsRecyclerView = view.findViewById(R.id.tagsReyclerView);
        int numberOfColumns = 2;
        tagsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        tagsRecyclerView.setHasFixedSize(true);

        final TagsAdapter tagsAdapter = new TagsAdapter();
        tagsRecyclerView.setAdapter(tagsAdapter);

        tagViewModel = ViewModelProviders.of(this).get(TagViewModel.class);
        tagViewModel.getAllTagsByDateAdded().observe(this, new Observer<List<Tag>>() {
            @Override
            public void onChanged(@Nullable List<Tag> tags) {
                // update RecyclerView
                tagsAdapter.submitList(tags);
            }
        });

        tagsAdapter.setOnItemClickListener(new TagsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Tag tag) {
                Tag currentTag = tag;

                int currentTagId = currentTag.getId();
                String currentTagName = currentTag.getName();
                Date currentTagDateAdded = currentTag.getDateAdded();
                Date currentTagTimeClicked = currentTag.getTimeClicked();

                if (!currentTag.getIsActive() && !prefs.getBoolean("bAnyTagActive", false))
                {
                    incomeStopwatch.startStopwatch();
                    Toast.makeText(getContext(), "Tag activated!", Toast.LENGTH_SHORT).show();
                    Tag updatedTag = new Tag(currentTagName, true, currentTagDateAdded, new Date());
                    updatedTag.setId(currentTagId);
                    tagViewModel.update(updatedTag);
                    editor.putBoolean("bAnyTagActive", true);
                }
                else if(currentTag.getIsActive() && prefs.getBoolean("bAnyTagActive", false))
                {
                    incomeStopwatch.pauseStopwatch();
                    Toast.makeText(getContext(), "Tag DE-activated!", Toast.LENGTH_SHORT).show();
                    Tag updatedTag = new Tag(currentTagName, false, currentTagDateAdded, currentTagTimeClicked);
                    updatedTag.setId(currentTagId);
                    tagViewModel.update(updatedTag);
                    editor.putBoolean("bAnyTagActive", false);
                }

                editor.apply();
            }
        });
    }

    @Override
    public void onResume() {
        Log.e("Income", "Fragment OnResume called");

        prefs = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = prefs.edit();

        incomeCalculator = new IncomeCalculator(getContext());
        incomeStopwatch = IncomeStopwatch.getInstance(prefs);

        incomeStopwatch.onResumeStopwatch();

        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("Income", "Fragment OnPause called");

        incomeStopwatch.onPauseStopwatch();
        super.onPause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.e("Income", "Fragment Income visible");
        }
        else {
        }
    }


    private void loadData() {
       /* incomeLong = prefs.getFloat(INCOME, 0);*/
    }

    private void setViews() {
        /*monthTextView.setText(incomesDF.format(getPerMonth()) + prefs.getString("Currency", "USD"));
        weekTextView.setText(incomesDF.format(getPerWeek()) + prefs.getString("Currency", "USD"));
        dayTextView.setText(incomesDF.format(getPerDay()) + prefs.getString("Currency", "USD"));
        hourTextView.setText(incomesDF.format(getPerHour()) + prefs.getString("Currency", "USD"));
        minuteTextView.setText(incomesDF.format(getPerMinute()) + prefs.getString("Currency", "USD"));*/
    }

   /* private float getPerMonth()
    {
        return incomeLong;
    }

    private float getPerWeek()
    {
        return getPerDay() * 7;
    }

    private float getPerDay()
    {
        return (float) incomeLong / numDaysInCurrentMonth;
    }

    private float getPerHour()
    {
        return getPerDay() / 24;
    }

    private float getPerMinute()
    {
        return getPerHour() / 60;
    }*/
}
