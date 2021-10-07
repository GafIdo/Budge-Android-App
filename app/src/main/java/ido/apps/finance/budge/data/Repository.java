package ido.apps.finance.budge.data;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface Repository{

    public void updateBudget(Budget budget);
    public void deleteBudget(long budgetId);
    public LiveData<List<Budget>> getBudgets();
}

