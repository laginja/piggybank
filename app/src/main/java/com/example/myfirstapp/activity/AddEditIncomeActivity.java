package com.example.myfirstapp.activity;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.fragment.DatePickerFragment;
import com.example.myfirstapp.model.Income;
import com.example.myfirstapp.viewmodel.IncomeViewModel;
import com.example.myfirstapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEditIncomeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String INCOME = "income";
    public static final String EXTRA_ID = "com.example.myfirstapp.EXTRA_ID";
    public static final String EXTRA_VALUE = "com.example.myfirstapp.EXTRA_VALUE";
    public static final String EXTRA_DATE = "com.example.myfirstapp.EXTRA_DATE";

    private EditText editTextValue;
    private TextView textViewDate;

    private String incomeDateString;
    private Date incomeDate;
    private Date oldDate;

    private IncomeViewModel incomeViewModel;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private float sumIncomes;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Income");

        editTextValue = findViewById(R.id.edit_text_value);
        textViewDate = findViewById(R.id.edit_text_date);
        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        incomeViewModel = ViewModelProviders.of(this).get(IncomeViewModel.class);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID))
        {

            editTextValue.setText(String.valueOf(intent.getFloatExtra(EXTRA_VALUE, 0f)));

            oldDate = new Date();
            oldDate.setTime(intent.getLongExtra(EXTRA_DATE, -1));       // setTime() sets Date from Long
            incomeDate = oldDate;                                                   // current income Date is old Date

            String oldDateString = dateFormat.format(oldDate);
            textViewDate.setText(oldDateString);
        }
        else
        {

        }
    }

    private void saveIncome() {
        String valueString = editTextValue.getText().toString();
        String date = textViewDate.getText().toString();

        if(valueString.trim().isEmpty() || date.trim().isEmpty())
        {
            Toast.makeText(this, "Please insert Value and Date", Toast.LENGTH_LONG).show();
            return;
        }

        float value = Float.valueOf(valueString);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);

        // Update Income
        if (id != -1)
        {
            updateIncomeDB(id, value);
            setResult(RESULT_OK, null);
            finish();
        }

        // Insert Income
        else
        {
            insertIncomeDB(value);
            setResult(RESULT_OK, null);
            finish();
        }

    }

    private void insertIncomeDB(float value)
    {
        Income income = new Income(value, incomeDate);
        incomeViewModel.insert(income);

        // get sum of all incomes
        incomeViewModel.getIncomesSum().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double incomesSum) {
                // Don't calculate sum if there are no incomes in DB
                if (incomesSum != null) {
                    sumIncomes = incomesSum.floatValue();

                    sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    editor = sharedPreferences.edit();

                    // store new sum in shared prefs
                    editor.putFloat(INCOME, sumIncomes);
                    editor.apply();
                }
            }
        });
    }

    private void updateIncomeDB(int incomeId, float value)
    {
        Income income = new Income(value, incomeDate);
        income.setId(incomeId);
        incomeViewModel.update(income);

        // get sum of all incomes
        incomeViewModel.getIncomesSum().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double incomesSum) {
                // Don't calculate sum if there are no incomes in DB
                if (incomesSum != null) {
                    sumIncomes = incomesSum.floatValue();

                    sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    editor = sharedPreferences.edit();

                    editor.putFloat(INCOME, sumIncomes);
                    editor.apply();
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        incomeDate = c.getTime();

        incomeDateString = dateFormat.format(c.getTime());
        textViewDate.setText(incomeDateString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_income_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_income:
                saveIncome();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
