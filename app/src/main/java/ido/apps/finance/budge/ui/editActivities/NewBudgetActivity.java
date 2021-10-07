package ido.apps.finance.budge.ui.editActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.time.Duration;

import ido.apps.finance.budge.R;
import ido.apps.finance.budge.data.Budget;
import ido.apps.finance.budge.databinding.ActivityNewBudgetBinding;

public class NewBudgetActivity extends AppCompatActivity {

    private ActivityNewBudgetBinding binding;
    public final static String BUDGET_ID_KEY = "budget id key";
    private long budgetId = Long.MIN_VALUE;
    private Budget budget;
    private NewBudgetViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewBudgetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(NewBudgetViewModel.class);
        Intent intent = getIntent();
        if (intent.hasExtra(BUDGET_ID_KEY)) {
            budgetId = intent.getLongExtra(BUDGET_ID_KEY, Long.MIN_VALUE);
        }


        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_new_budget);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        setupClickListener();
        setupObserver();

    }

    private void setupObserver() {
        viewModel.getBudget(budgetId).observe(this, budget -> {
            if (budgetId != Long.MIN_VALUE && budget != null) {
                binding.editTextBudgetName.setText(budget.getSubject());
                binding.editTextMonthlyBudget.setText(String.valueOf(budget.getBudgetSize()));
            }
        });

    }

    private void setupClickListener() {
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pops up an alert before the delete action
                Snackbar snackbar = Snackbar.make(binding.getRoot(), "You are about to delete a budget, ARE YOU SURE?", Snackbar.LENGTH_LONG);
                snackbar.setAction("I'm Sure", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewModel.deleteBudget(budgetId);
                                snackbar.dismiss();
                            }
                        });
                snackbar.show();

            }
        });
        binding.saveButton.setOnClickListener(v -> {
            String budgetName = binding.editTextBudgetName.getText().toString();
            double monthlyBudget = Float.parseFloat(binding.editTextMonthlyBudget.getText().toString());
            if (budgetId != Long.MIN_VALUE) {
                Budget budget = viewModel.getBudget(budgetId).getValue();
                budget.setBudgetSize(monthlyBudget);
                budget.setSubject(budgetName);
            }
            else {
                budget = Budget.createBudget(budgetName, monthlyBudget);
                budgetId = budget.getId();
            }
            viewModel.saveOrUpdateBudget(budget);
        });
    }
}