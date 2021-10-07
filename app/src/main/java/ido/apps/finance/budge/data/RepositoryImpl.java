package ido.apps.finance.budge.data;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import ido.apps.finance.budge.util.AppExecutors;

public class RepositoryImpl implements Repository {

    private static RepositoryImpl sInstance;
    private BudgeDao budgetDao;
    private LiveData liveData;


    private RepositoryImpl(Context application) {
        budgetDao = Database.getInstance(application).dao();
        liveData = budgetDao.getAllBudgets();
    }

    @Override
    public void updateBudget(Budget budget) {
        AppExecutors.getInstance().diskIO().execute(() -> budgetDao.updateBudget(budget));
    }

    @Override
    public void deleteBudget(long budgetId) {
        AppExecutors.getInstance().diskIO().execute(() -> budgetDao.deleteBudget(budgetId));
    }

    @Override
    public LiveData<List<Budget>> getBudgets() {
        return liveData;
    }

    /**
     * Creating an instance only once in an app lifetime. performed in a sync way.
     *
     * @param application = the App reference.
     * @return The repository singleton instance.
     */
    public static RepositoryImpl getInstance(Context application) {
        if (sInstance == null) {
            synchronized (RepositoryImpl.class) {
                if (sInstance == null)
                    sInstance = new RepositoryImpl(application);
            }
        }
        return sInstance;
    }
}
