package com.example.myfirstapp.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.myfirstapp.DateConverter;
import com.example.myfirstapp.model.Income;
import com.example.myfirstapp.dao.IncomeDao;
import com.example.myfirstapp.model.Stopwatch;
import com.example.myfirstapp.dao.StopwatchDao;
import com.example.myfirstapp.model.Tag;
import com.example.myfirstapp.dao.TagDao;

import java.util.Date;

@Database(entities = {Income.class, Stopwatch.class, Tag.class}, version = 10)
@TypeConverters({DateConverter.class})
public abstract class PiggybankDatabase extends RoomDatabase {

    private static PiggybankDatabase instance;

    public abstract IncomeDao incomeDao();
    public abstract StopwatchDao stopwatchDao();
    public abstract TagDao tagDao();

    public static synchronized PiggybankDatabase getInstance(Context context)      // synchronized - only 1 thread at the time can access it
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),       // We have to use a builder here because the class is abstract
                    PiggybankDatabase.class, "piggybank_database")
                    .addCallback(roomCallback)
                    .build();

            Log.e("Database: " ,"Populating DB");
        }
        else
            Log.e("Database: " ,"Already populated!");
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateIncomeAsyncTask(instance).execute();
            new PopulateStopwatchAsyncTask(instance).execute();
            new PopulateTagAsyncTask(instance).execute();
            Log.e("Database: " ,"Room callback");
        }
    };

    private static class PopulateIncomeAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private IncomeDao incomeDao;

        private PopulateIncomeAsyncTask(PiggybankDatabase db)
        {
            incomeDao = db.incomeDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("Database: " ,"Populating Incomes...");
            incomeDao.insert(new Income(1500,  new Date()));
            incomeDao.insert(new Income(500, new Date()));
            incomeDao.insert(new Income(354, new Date()));
            Log.e("Database: " ,"Done populating Incomes!");
            return null;
        }
    }

    private static class PopulateStopwatchAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private StopwatchDao stopwatchDao;

        private PopulateStopwatchAsyncTask(PiggybankDatabase db)
        {
            stopwatchDao = db.stopwatchDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("Database: " ,"Populating Stopwatch...");
            stopwatchDao.insert(new Stopwatch(15000,  (float)5.876, new Date()));
            stopwatchDao.insert(new Stopwatch(27000,  (float)12.567, new Date()));
            stopwatchDao.insert(new Stopwatch(8561,  (float)2.856, new Date()));
            Log.e("Database: " ,"Done populating Stopwatch!");
            return null;
        }
    }

    private static class PopulateTagAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private TagDao tagDao;

        private PopulateTagAsyncTask(PiggybankDatabase db)
        {
            tagDao = db.tagDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("Database: " ,"Populating Tags...");
            tagDao.insert(new Tag("Eating", false,  new Date(), new Date()));
            tagDao.insert(new Tag("Sleeping", false,  new Date(), new Date()));
            tagDao.insert(new Tag("Workout", false,  new Date(), new Date()));
            tagDao.insert(new Tag("Toilet", false,  new Date(), new Date()));
            tagDao.insert(new Tag("Commute", false,  new Date(), new Date()));
            tagDao.insert(new Tag("Beer", false,  new Date(), new Date()));
            Log.e("Database: " ,"Done populating Tags!");
            return null;
        }
    }
}
