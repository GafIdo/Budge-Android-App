package ido.apps.finance.budge.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface  BudgeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateBudget(Budget budget);
    //TODO run not on UI thread

    @Query("SELECT * FROM budgets ORDER BY id ")
    LiveData<List<Budget>> getAllBudgets();

    @Query("DELETE FROM budgets WHERE id = :budgetID ")
    public void deleteBudget(long budgetID);

}
