package ido.apps.finance.budge.ui.budget;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import ido.apps.finance.budge.data.Budget;
import ido.apps.finance.budge.data.RepositoryImpl;
import ido.apps.finance.budge.data.Transaction;

public class BudgetViewModel extends AndroidViewModel {

    private RepositoryImpl repository;

    public BudgetViewModel(@NonNull Application application) {
        super(application);

        repository = RepositoryImpl.getInstance(application);
    }


    LiveData<Budget> getBudget(long budgetID) {

        return Transformations.map(repository.getBudgets(), budgetList-> budgetList.stream()
                .filter(budget -> budget.getId() == budgetID)
                .findAny().orElse(null));
    }
}