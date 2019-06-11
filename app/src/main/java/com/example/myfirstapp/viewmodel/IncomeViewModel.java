package com.example.myfirstapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.myfirstapp.database.PiggybankRepository;
import com.example.myfirstapp.model.Income;

import java.util.List;

public class IncomeViewModel extends AndroidViewModel {

    private PiggybankRepository repository;
    private LiveData<List<Income>> allIncomes;
    private LiveData<Double> incomesSum;
    private Float suma;

    public IncomeViewModel(@NonNull Application application) {
        super(application);
        repository = new PiggybankRepository(application);
        allIncomes = repository.getAllIncomes();
        incomesSum = repository.getIncomesSum();
    }

    public void insert(Income income)
    {
        repository.insertIncome(income);
    }

    public void update(Income income)
    {
        repository.updateIncome(income);
    }

    public void delete(Income income)
    {
        repository.deleteIncome(income);
    }

    public void deleteAllIncomes()
    {
        repository.deleteAllIncomes();
    }

    public LiveData<List<Income>> getAllIncomes()
    {
        return allIncomes;
    }

    public LiveData<Double> getIncomesSum() {
        return incomesSum;
    }

}
