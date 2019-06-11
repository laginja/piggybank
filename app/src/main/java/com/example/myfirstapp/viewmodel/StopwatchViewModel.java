package com.example.myfirstapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.myfirstapp.database.PiggybankRepository;
import com.example.myfirstapp.model.Stopwatch;

import java.util.List;

public class StopwatchViewModel extends AndroidViewModel {

    private PiggybankRepository repository;
    private LiveData<List<Stopwatch>> allStopwatches;

    public StopwatchViewModel(@NonNull Application application) {
        super(application);

        repository = new PiggybankRepository(application);
        allStopwatches = repository.getAllStopwatches();
    }

    public void insert(Stopwatch stopwatch)
    {
        repository.insertStopwatch(stopwatch);
    }

    public void delete(Stopwatch stopwatch)
    {
        repository.deleteStopwatch(stopwatch);
    }

    public void deleteAllStopwatches()
    {
        repository.deleteAllStopwatches();
    }

    public LiveData<List<Stopwatch>> getAllStopwatches()
    {
        return allStopwatches;
    }
}
