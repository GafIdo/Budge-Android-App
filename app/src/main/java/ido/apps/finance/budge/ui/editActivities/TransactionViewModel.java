package ido.apps.finance.budge.ui.editActivities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.Date;
import java.util.List;

import ido.apps.finance.budge.data.Budget;
import ido.apps.finance.budge.data.RepositoryImpl;
import ido.apps.finance.budge.data.Transaction;

public class TransactionViewModel extends AndroidViewModel {

    RepositoryImpl repository;
    Long transactionId;
    Long budgetId;
    LiveData<List<Budget>> budgetListLiveData;
    LiveData<Budget> budgetLiveData;
    LiveData<Transaction> transactionLiveData;

    public TransactionViewModel(@NonNull Application application) {
        super(application);

        repository = RepositoryImpl.getInstance(application);
    }

    void setIDs(long budgetId, long transactionId) {
        this.budgetId = budgetId;
        this.transactionId = transactionId;
        initiateBudgetAndTransactionLiveDatas(budgetId, transactionId);
    }

    private void initiateBudgetAndTransactionLiveDatas(long budgetId, long transactionId) {
        budgetLiveData = Transformations.map(budgetListLiveData, budgetList -> budgetList.stream()
                .filter(budget -> budget.getId() == budgetId)
                .findAny().orElse(null));
        transactionLiveData = Transformations.map(budgetLiveData, budget -> budget.getTransactions()
                .stream().filter(transaction -> transaction.getId() == transactionId)
                .findAny().orElse(null));
    }

    LiveData<Transaction> getTransaction() {
        return transactionLiveData;
    }

    LiveData<Budget> getBudget() {
       return budgetLiveData;
    }

    LiveData<List<Budget>> getBudgetsList() {
        return repository.getBudgets();
    }

    private void saveBudget(Budget budget) {
        repository.updateBudget(budget);
    }

    void deleteTransaction() {
        if (budgetLiveData.getValue() != null && transactionLiveData.getValue() != null) {
            Budget budget = budgetLiveData.getValue();
            budget.getTransactions().remove(transactionLiveData.getValue());
            saveBudget(budget);

        }
    }

    void saveTransaction(String payee, double payment, Date date, int transactionSign) {
        Transaction newTransaction = new Transaction(payee, payment, date, transactionSign);
        Budget budget = budgetLiveData.getValue();
        //in case of update an existing transaction the method will replace and not only add the transaction
        if (transactionId != Long.MIN_VALUE) {
            newTransaction.setId(transactionId);
            budget.getTransactions().remove(transactionLiveData.getValue());
        }
        budget.getTransactions().add(newTransaction);
        saveBudget(budget);
    }
}

