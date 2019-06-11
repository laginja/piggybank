package com.example.myfirstapp.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myfirstapp.model.Income;
import com.example.myfirstapp.adapter.IncomeAdapter;
import com.example.myfirstapp.viewmodel.IncomeViewModel;
import com.example.myfirstapp.R;

import java.util.List;

public class IncomeManagerActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String INCOME = "income";

    public static final int ADD_INCOME_REQUEST = 1;
    public static final int EDIT_INCOME_REQUEST = 2;
    private IncomeViewModel incomeViewModel;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private float sumIncomes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomes);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Income manager");

        FloatingActionButton buttonAddIncome = findViewById(R.id.button_add_income);
        buttonAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IncomeManagerActivity.this, AddEditIncomeActivity.class);
                startActivityForResult(intent, ADD_INCOME_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.reycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final IncomeAdapter adapter = new IncomeAdapter();
        recyclerView.setAdapter(adapter);

        incomeViewModel = ViewModelProviders.of(this).get(IncomeViewModel.class);
        incomeViewModel.getAllIncomes().observe(this, new Observer<List<Income>>() {
            @Override
            public void onChanged(@Nullable List<Income> incomes) {
                // update RecyclerView
                adapter.submitList(incomes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {   // Directions in which we swipe
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Income income = adapter.getIncomeAt(viewHolder.getAdapterPosition());
                incomeViewModel.delete(income);

                sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                editor = sharedPreferences.edit();

                float sumIncomes = sharedPreferences.getFloat(INCOME, 0);
                float newSumIncomes = sumIncomes - income.getValue();

                editor.putFloat(INCOME, newSumIncomes);
                editor.apply();

                Toast.makeText(IncomeManagerActivity.this, "Income deleted: " + income.getValue(), Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new IncomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Income income) {
                Intent intent = new Intent(IncomeManagerActivity.this, AddEditIncomeActivity.class);
                intent.putExtra(AddEditIncomeActivity.EXTRA_ID, income.getId());
                intent.putExtra(AddEditIncomeActivity.EXTRA_VALUE, income.getValue());
                intent.putExtra(AddEditIncomeActivity.EXTRA_DATE, income.getDate().getTime());
                startActivityForResult(intent, EDIT_INCOME_REQUEST);
            }
        });

    }

    private void clearSharedPrefs() {
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putFloat(INCOME, 0);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_INCOME_REQUEST && resultCode == RESULT_OK)
        {
            Toast.makeText(this, "Income saved", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_INCOME_REQUEST && resultCode == RESULT_OK)
        {
            Toast.makeText(IncomeManagerActivity.this, "Income updated", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(IncomeManagerActivity.this, "Income not updated", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.income_manager_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                return true;
            case R.id.delete_all_incomes:
                incomeViewModel.deleteAllIncomes();
                clearSharedPrefs();
                Toast.makeText(IncomeManagerActivity.this, "All Incomes Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
