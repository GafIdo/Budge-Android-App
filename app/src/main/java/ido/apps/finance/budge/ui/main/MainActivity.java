package ido.apps.finance.budge.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ido.apps.finance.budge.R;
import ido.apps.finance.budge.data.Budget;
import ido.apps.finance.budge.databinding.ActivityMainBinding;
import ido.apps.finance.budge.ui.budget.BudgetActivity;
import ido.apps.finance.budge.ui.budget.BudgetViewModel;
import ido.apps.finance.budge.ui.editActivities.NewBudgetActivity;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private MainRecyclerViewAdapter adapter;
    private FloatingActionButton fab;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

//        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), NewBudgetActivity.class);
//                v.getContext().startActivity(intent);
//            }
//        });

        fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewBudgetActivity.class);
                v.getContext().startActivity(intent);
            }
        });


        // recyclerView handling:
        setupRecyclerView();
        observerViewModel();
    }

    private void observerViewModel() {
        viewModel.getBudgets().observe(this, budgets -> adapter.setRecyclerBudgetsList(budgets));
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
    //TODO add a floating newBudgetButton(-> activity_new_budget)

}