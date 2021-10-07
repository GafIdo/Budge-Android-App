package ido.apps.finance.budge.ui.editActivities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import ido.apps.finance.budge.data.Budget;
import ido.apps.finance.budge.data.Repository;
import ido.apps.finance.budge.data.RepositoryImpl;

public class NewBudgetViewModel extends AndroidViewModel {

    private RepositoryImpl repository;
    private Budget budget;

    public NewBudgetViewModel(@NonNull Application application) {
        super(application);

        repository = RepositoryImpl.getInstance(application);
    }

    LiveData<Budget> getBudget(long budgetID) {
        return Transformations.map(repository.getBudgets(), budgetList -> budgetList.stream()
                .filter(budget -> budget.getId() == budgetID)
                .findAny().orElse(null));
    }

    void deleteBudget(long budgetID) {
        repository.deleteBudget(budgetID);
    }

    public void saveOrUpdateBudget(Budget budget) {
        repository.updateBudget(budget);
    }
}
