package com.example.myfirstapp.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.myfirstapp.model.Income;
import com.example.myfirstapp.dao.IncomeDao;
import com.example.myfirstapp.model.Stopwatch;
import com.example.myfirstapp.dao.StopwatchDao;
import com.example.myfirstapp.model.Tag;
import com.example.myfirstapp.dao.TagDao;

import java.util.List;

public class PiggybankRepository {

    private IncomeDao incomeDao;
    private StopwatchDao stopwatchDao;
    private TagDao tagDao;

    private LiveData<List<Income>> allIncomes;
    private LiveData<List<Stopwatch>> allStopwatches;
    private LiveData<List<Tag>> allTagsByDateAdded;
    private LiveData<List<Tag>> allTagsByTimeClicked;
    private LiveData<Double> incomesSum;

    public PiggybankRepository(Application application)
    {
        PiggybankDatabase database = PiggybankDatabase.getInstance(application);
        incomeDao = database.incomeDao();
        stopwatchDao = database.stopwatchDao();
        tagDao = database.tagDao();

        allIncomes = incomeDao.getAllIncomes();
        allStopwatches = stopwatchDao.getAllStopwatches();
        allTagsByDateAdded = tagDao.getAllTagsByDateAdded();
        allTagsByTimeClicked = tagDao.getAllTagsByTimeClicked();
        incomesSum = incomeDao.getIncomesSum();
    }

    /* INCOME */

    public void insertIncome(Income income)
    {
        new InsertIncomeAsyncTask(incomeDao).execute(income);
    }

    public void updateIncome(Income income)
    {
        new UpdateIncomeAsyncTask(incomeDao).execute(income);
    }

    public void deleteIncome(Income income)
    {
        new DeleteIncomeAsyncTask(incomeDao).execute(income);
    }

    public void deleteAllIncomes()
    {
        new DeleteAllIncomesAsyncTask(incomeDao).execute();
    }

    public LiveData<List<Income>> getAllIncomes()
    {
        return allIncomes;
    }

    public LiveData<Double> getIncomesSum()
    {
        return incomesSum;
    }


    /* STOPWATCH */

    public void insertStopwatch(Stopwatch stopwatch)
    {
        new InsertStopwatchAsyncTask(stopwatchDao).execute(stopwatch);
    }

    public void deleteStopwatch(Stopwatch stopwatch)
    {
        new DeleteStopwatchAsyncTask(stopwatchDao).execute(stopwatch);
    }

    public void deleteAllStopwatches()
    {
        new DeleteAllStopwatchesAsyncTask(stopwatchDao).execute();
    }

    public LiveData<List<Stopwatch>> getAllStopwatches()
    {
        return allStopwatches;
    }


    /* TAG */

    public void insertTag(Tag tag)
    {
        new InsertTagAsyncTask(tagDao).execute(tag);
    }

    public void updateTag(Tag tag)
    {
        new UpdateTagAsyncTask(tagDao).execute(tag);
    }

    public void deleteTag(Tag tag)
    {
        new DeleteTagAsyncTask(tagDao).execute(tag);
    }

    public LiveData<List<Tag>> getAllTagsByDateAdded()
    {
        return allTagsByDateAdded;
    }

    public LiveData<List<Tag>> getAllTagsByTimeClicked()
    {
        return allTagsByTimeClicked;
    }

    /*public Float getIncomesSumFloat()
    {
        try {
            return new GetIncomesSumFloatAsyncTask(incomeDao).execute().get();      // .get() tries to find the return value from doInBackground() or onPostExecute()
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*// Needs to be static so it doesn't have the reference to the repository itself
    private static class GetIncomesSumFloatAsyncTask extends AsyncTask<Void, Void, Float>
    {
        private IncomeDao incomeDao;

        private GetIncomesSumFloatAsyncTask(IncomeDao incomeDao)
        {
            this.incomeDao = incomeDao;
        }

        @Override
        protected Float doInBackground(Void... voids) {
            Float result = incomeDao.getIncomesSumFloat();
            return result;
        }
    }*/

    // Needs to be static so it doesn't have the reference to the repository itself
    private static class InsertIncomeAsyncTask extends AsyncTask<Income, Void, Void>
    {
        private IncomeDao incomeDao;

        private InsertIncomeAsyncTask(IncomeDao incomeDao)
        {
            this.incomeDao = incomeDao;
        }

        @Override
        protected Void doInBackground(Income... incomes) {
            incomeDao.insert(incomes[0]);
            return null;
        }
    }

    // Needs to be static so it doesn't have the reference to the repository itself
    private static class UpdateIncomeAsyncTask extends AsyncTask<Income, Void, Void>
    {
        private IncomeDao incomeDao;

        private UpdateIncomeAsyncTask(IncomeDao incomeDao)
        {
            this.incomeDao = incomeDao;
        }

        @Override
        protected Void doInBackground(Income... incomes) {
            incomeDao.update(incomes[0]);
            return null;
        }
    }

    // Needs to be static so it doesn't have the reference to the repository itself
    private static class DeleteIncomeAsyncTask extends AsyncTask<Income, Void, Void>
    {
        private IncomeDao incomeDao;

        private DeleteIncomeAsyncTask(IncomeDao incomeDao)
        {
            this.incomeDao = incomeDao;
        }

        @Override
        protected Void doInBackground(Income... incomes) {
            incomeDao.delete(incomes[0]);
            return null;
        }
    }

    // Needs to be static so it doesn't have the reference to the repository itself
    private static class DeleteAllIncomesAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private IncomeDao incomeDao;

        private DeleteAllIncomesAsyncTask(IncomeDao incomeDao)
        {
            this.incomeDao = incomeDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            incomeDao.deleteAllIncomes();
            return null;
        }
    }



    // Needs to be static so it doesn't have the reference to the repository itself
    private static class InsertStopwatchAsyncTask extends AsyncTask<Stopwatch, Void, Void>
    {
        private StopwatchDao stopwatchDao;

        private InsertStopwatchAsyncTask(StopwatchDao stopwatchDao)
        {
            this.stopwatchDao = stopwatchDao;
        }

        @Override
        protected Void doInBackground(Stopwatch... stopwatches) {
            stopwatchDao.insert(stopwatches[0]);
            return null;
        }
    }

    // Needs to be static so it doesn't have the reference to the repository itself
    private static class DeleteStopwatchAsyncTask extends AsyncTask<Stopwatch, Void, Void>
    {
        private StopwatchDao stopwatchDao;

        private DeleteStopwatchAsyncTask(StopwatchDao stopwatchDao)
        {
            this.stopwatchDao = stopwatchDao;
        }

        @Override
        protected Void doInBackground(Stopwatch... stopwatches) {
            stopwatchDao.delete(stopwatches[0]);
            return null;
        }
    }

    // Needs to be static so it doesn't have the reference to the repository itself
    private static class DeleteAllStopwatchesAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private StopwatchDao stopwatchDao;

        private DeleteAllStopwatchesAsyncTask(StopwatchDao stopwatchDao)
        {
            this.stopwatchDao = stopwatchDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            stopwatchDao.deleteAllStopwatches();
            return null;
        }
    }



    // Needs to be static so it doesn't have the reference to the repository itself
    private static class InsertTagAsyncTask extends AsyncTask<Tag, Void, Void>
    {
        private TagDao tagDao;

        private InsertTagAsyncTask(TagDao tagDao)
        {
            this.tagDao = tagDao;
        }

        @Override
        protected Void doInBackground(Tag... tags) {
            tagDao.insert(tags[0]);
            return null;
        }
    }

    // Needs to be static so it doesn't have the reference to the repository itself
    private static class UpdateTagAsyncTask extends AsyncTask<Tag, Void, Void>
    {
        private TagDao tagDao;

        private UpdateTagAsyncTask(TagDao tagDao)
        {
            this.tagDao = tagDao;
        }

        @Override
        protected Void doInBackground(Tag... tags) {
            tagDao.update(tags[0]);
            return null;
        }
    }

    // Needs to be static so it doesn't have the reference to the repository itself
    private static class DeleteTagAsyncTask extends AsyncTask<Tag, Void, Void>
    {
        private TagDao tagDao;

        private DeleteTagAsyncTask(TagDao tagDao)
        {
            this.tagDao = tagDao;
        }

        @Override
        protected Void doInBackground(Tag... tags) {
            tagDao.delete(tags[0]);
            return null;
        }
    }

}
