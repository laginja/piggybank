package com.example.myfirstapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.IncomeCalculator;
import com.example.myfirstapp.IncomeStopwatch;
import com.example.myfirstapp.R;
import com.example.myfirstapp.adapter.ViewPagerAdapter;
import com.example.myfirstapp.fragment.FragmentIncome;
import com.example.myfirstapp.fragment.FragmentStopwatch;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String INCOME = "income";
    public static final int ADD_INCOME_REQUEST_MAIN = 3;

    private TabLayout tabLayout;
    private TextView incomeTextView;
    private ViewPager viewPager;

    private FloatingActionButton addIncomeButton;
    private FloatingActionButton addStopwatchButton;

    private IncomeCalculator incomeCalculator;
    private IncomeStopwatch incomeStopwatch;

    private DecimalFormat counterDF = new DecimalFormat("###,###.00");

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private int delay = 100; // 100 ms
    Handler counterHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MSG", "OnCreate called");

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        incomeTextView = (TextView) findViewById(R.id.incomeTextView);
        incomeTextView.setAllCaps(false);
        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Adding Fragments
        adapter.AddFragment(new FragmentIncome(), "Income");
        adapter.AddFragment(new FragmentStopwatch(), "Stopwatch");

        // Adapter Setup
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                FragmentStopwatch fragmentStopwatch = (FragmentStopwatch) getSupportFragmentManager().getFragments().get(1);

                switch (position) {
                    case 0:
                        Log.e("MainActivity", "Income");

                        incomeStopwatch.onResumeStopwatch();
                        fragmentStopwatch.stop();
                        break;
                    case 1:
                        Log.e("MainActivity", "Stopwatch");

                        incomeStopwatch.onResumeStopwatch();

                        fragmentStopwatch.start();

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2)         // meaning that SCROLL_STATE_SETTLING is true. Indicates that the pager is in the process of settling to a final position.
                {
                    incomeStopwatch.onPauseStopwatch();
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);

        addIncomeButton = (FloatingActionButton) findViewById(R.id.button_add_income);
        addIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddNewIncome();
            }
        });

        addStopwatchButton = (FloatingActionButton) findViewById(R.id.button_add_stopwatch);
        addStopwatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddNewStopwatch();
            }
        });
    }


    @Override
    protected void onStart() {
        Log.d("MSG", "onStart called");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("MSG", "Activity OnResume called");
        super.onResume();

        prefs = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = prefs.edit();

        incomeCalculator = new IncomeCalculator(this);
        incomeStopwatch = IncomeStopwatch.getInstance(prefs);

        // re-setup calendar when ever we resume the app. This is because (in theory) someone could have the app opened for the
        // whole month and if the date passes into the next month all the calculations will be incorrect
        incomeCalculator.setupCalendar();

        // Start a new runnable thread which increments the counter every 100ms and updates the main thread
        counterRunnable.run();

    }

    private Runnable counterRunnable = new Runnable() {
        @Override
        public void run() {
            String currency = prefs.getString("Currency", "$");
            String incomeString = counterDF.format(incomeCalculator.getCurrentIncome());

            SpannableString currencySpan = new SpannableString(currency);
            currencySpan.setSpan(new RelativeSizeSpan(0.5f), 0,currency.length(), 0); // set size
            currencySpan.setSpan(new ForegroundColorSpan(Color.WHITE), 0, currency.length(), 0);// set color

            String stringFinal = currencySpan + " " + incomeString;
            incomeTextView.setText(stringFinal);
            counterHandler.postDelayed(this, delay);
        }
    };

    // ako imamo ovo mi se brzina ne updateanja UI ne poduplava
    @Override
    protected void onPause() {
        Log.d("MSG", "Activity OnPause called");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("MSG", "onStop called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("MSG", "onDestroy called");
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_INCOME_REQUEST_MAIN && resultCode == RESULT_OK){
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            this.finish();

            Toast.makeText(this, "Income saved", Toast.LENGTH_SHORT).show();
        }
    }


    private void openProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void openIncomeManager() {
        Intent intent = new Intent(this, IncomeManagerActivity.class);
        startActivity(intent);
    }

    private void openAddNewIncome() {
        Intent intent = new Intent(this, AddEditIncomeActivity.class);
        startActivityForResult(intent, ADD_INCOME_REQUEST_MAIN);
    }

    private void openAddNewStopwatch() {
        Intent intent = new Intent(this, AddStopwatchActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences:
                openProfileActivity();
                return true;
            case R.id.incomes:
                openIncomeManager();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
