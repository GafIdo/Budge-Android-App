package ido.apps.finance.budge.ui.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ido.apps.finance.budge.R;
import ido.apps.finance.budge.data.Budget;
import ido.apps.finance.budge.data.RepositoryImpl;
import ido.apps.finance.budge.data.Transaction;
import ido.apps.finance.budge.databinding.ActivityBudgetBinding;
import ido.apps.finance.budge.ui.editActivities.NewBudgetActivity;
import ido.apps.finance.budge.ui.editActivities.TransactionActivity;

public class BudgetActivity extends AppCompatActivity {

    public final static String BUDGET_ID_KEY = "budget id key";
    public final static String TRANSACTION_ID_KEY = "transaction id key";
    private long budgetId = Long.MIN_VALUE;
    private Button editButton;
    private ActivityBudgetBinding binding;
    private BudgetRecycleViewAdapter adapter;
    private BudgetViewModel viewModel;

    //TODO: add a newTransactionButton
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBudgetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent.hasExtra(BUDGET_ID_KEY)) {
            budgetId = intent.getLongExtra(BUDGET_ID_KEY, Long.MIN_VALUE);
        }

        viewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        setupViews();
        setOnClickListeners();
        observerViewModel(budgetId);

    }

    private void setupViews() {
        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_budget);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);

        // recycleView handling:
        RecyclerView recyclerView = findViewById(R.id.budget_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BudgetRecycleViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void observerViewModel(long budgetId) {
        viewModel.getBudget(budgetId).observe(this, budget -> {
            updateBudgetData(budget);
            adapter.setTransactions(budget.getTransactions());
        });
    }

    private void updateBudgetData(Budget budget) {
        binding.budgetLeftTextView.setText(String.valueOf(budget.getLeftBudget()));
        binding.subjectTextView.setText(String.valueOf(budget.getSubject()));
        binding.progressBar.setMax((int)budget.getBudgetSize());
        binding.progressBar.setProgress((int)budget.getLeftBudget());
        binding.budgetSizeTextView.setText(String.valueOf(budget.getBudgetSize()));
    }

    private void setOnClickListeners() {
        //Click the edit button -> open the right Budget's editActivity
        editButton = findViewById(R.id.edit_budget_button);
        editButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(v.getContext(), NewBudgetActivity.class);
            editIntent.putExtra(BudgetActivity.BUDGET_ID_KEY, budgetId);
            v.getContext().startActivity(editIntent);
        });

        //click the add button -> open a transaction activity for this budget
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TransactionActivity.class);
                intent.putExtra(BUDGET_ID_KEY, budgetId);
                v.getContext().startActivity(intent);
            }
        });
    }
}
